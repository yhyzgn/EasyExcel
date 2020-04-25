package com.yhy.doc.excel.utils;

import com.yhy.doc.excel.annotation.Converter;
import com.yhy.doc.excel.annotation.Filter;
import com.yhy.doc.excel.annotation.Formatter;
import com.yhy.doc.excel.extra.ExcelTitle;
import com.yhy.doc.excel.extra.ReaderConfig;
import com.yhy.doc.excel.extra.Rect;
import com.yhy.doc.excel.internal.ExcelConverter;
import com.yhy.doc.excel.internal.ExcelFilter;
import com.yhy.doc.excel.internal.ExcelFormatter;
import com.yhy.doc.excel.io.ExcelReader;
import com.yhy.doc.excel.io.ExcelWriter;
import com.yhy.doc.excel.offer.DateFormatter;
import com.yhy.doc.excel.offer.LocalDateTimeFormatter;
import com.yhy.doc.excel.offer.SqlDateFormatter;
import com.yhy.doc.excel.offer.TimestampFormatter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2019-09-09 12:14
 * version: 1.0.0
 * desc   :
 */
@Slf4j
public class ExcelUtils {

    private ExcelUtils() {
        throw new UnsupportedOperationException("Utils class can not be instantiate.");
    }

    public static <T> List<T> read(File file, Class<T> clazz) {
        return read(file, null, clazz);
    }

    public static <T> List<T> read(File file, ReaderConfig config, Class<T> clazz) {
        try {
            return read(new FileInputStream(file), config, clazz);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public static <T> List<T> read(InputStream is, Class<T> clazz) {
        return read(is, null, clazz);
    }

    public static <T> List<T> read(InputStream is, ReaderConfig config, Class<T> clazz) {
        ExcelReader<T> reader = null;
        try {
            return new ExcelReader<T>(is, config).read(clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public static <T> List<T> read(ServletRequest request, Class<T> clazz) {
        return read(request, null, clazz);
    }

    public static <T> List<T> read(ServletRequest request, ReaderConfig config, Class<T> clazz) {
        try {
            return read(request.getInputStream(), config, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public static <T> void write(File file, List<T> src, String sheetName) {
        try {
            new ExcelWriter<T>(file).write(sheetName, src);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> void writeX(File file, List<T> src, String sheetName) {
        try {
            new ExcelWriter<T>(file).x().write(sheetName, src);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> void writeBig(File file, List<T> src, String sheetName) {
        try {
            new ExcelWriter<T>(file).x().big().write(sheetName, src);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> void write(OutputStream os, List<T> src, String sheetName) {
        try {
            new ExcelWriter<T>(os).write(sheetName, src);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> void writeX(OutputStream os, List<T> src, String sheetName) {
        try {
            new ExcelWriter<T>(os).x().write(sheetName, src);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> void writeBig(OutputStream os, List<T> src, String sheetName) {
        try {
            new ExcelWriter<T>(os).x().big().write(sheetName, src);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> void write(HttpServletResponse response, List<T> src, String sheetName) {
        write(response, null, src, sheetName);
    }

    public static <T> void writeX(HttpServletResponse response, List<T> src, String sheetName) {
        writeX(response, null, src, sheetName);
    }

    public static <T> void writeBig(HttpServletResponse response, List<T> src, String sheetName) {
        writeBig(response, null, src, sheetName);
    }

    public static <T> void write(HttpServletResponse response, String filename, List<T> src, String sheetName) {
        try {
            new ExcelWriter<T>(response, filename).write(sheetName, src);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> void writeX(HttpServletResponse response, String filename, List<T> src, String sheetName) {
        try {
            new ExcelWriter<T>(response, filename).x().write(sheetName, src);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> void writeBig(HttpServletResponse response, String filename, List<T> src, String sheetName) {
        try {
            new ExcelWriter<T>(response, filename).x().big().write(sheetName, src);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Rect merged(Sheet sheet, int row, int column, int rowStartIndex, int columnStartIndex) {
        int mergedCount = sheet.getNumMergedRegions();
        CellRangeAddress range;
        int firstColumn, lastColumn, firstRow, lastRow;
        for (int i = 0; i < mergedCount; i++) {
            range = sheet.getMergedRegion(i);
            firstRow = range.getFirstRow() - rowStartIndex;
            lastRow = range.getLastRow() - rowStartIndex;
            firstColumn = range.getFirstColumn() - columnStartIndex;
            lastColumn = range.getLastColumn() - columnStartIndex;
            if (row >= firstRow && row <= lastRow && column >= firstColumn && column <= lastColumn) {
                return new Rect(true, firstRow, lastRow, firstColumn, lastColumn);
            }
        }
        return new Rect(false, row, row, column, column);
    }

    public static LocalDateTime convertDate(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public static Date convertDate(LocalDateTime time) {
        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static String formatDate(Date date, String pattern) {
        return date.toInstant().atZone(ZoneId.systemDefault()).format(new DateTimeFormatterBuilder().appendPattern(pattern).toFormatter(Locale.getDefault()));
    }

    public static String formatDate(LocalDateTime time, String pattern) {
        return time.format(new DateTimeFormatterBuilder().appendPattern(pattern).toFormatter(Locale.getDefault()));
    }

    public static <T> T instantiate(Class<T> clazz) {
        try {
            return instantiate(clazz.getConstructor());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T instantiate(Constructor<T> constructor) {
        try {
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Method setter(Field field, Class<?> clazz) throws IntrospectionException {
        PropertyDescriptor descriptor = new PropertyDescriptor(field.getName(), clazz);
        return descriptor.getWriteMethod();
    }

    public static Method getter(Field field, Class<?> clazz) throws IntrospectionException {
        PropertyDescriptor descriptor = new PropertyDescriptor(field.getName(), clazz);
        return descriptor.getReadMethod();
    }

    public static Type getParamType(Class<?> clazz, Class<?> interfaceClazz, int index) {
        Type[] types = clazz.getGenericInterfaces();
        ParameterizedType ptp;
        Type raw;
        for (Type tp : types) {
            if (tp instanceof ParameterizedType) {
                ptp = (ParameterizedType) tp;
                raw = ptp.getRawType();
                if (raw.getTypeName().equalsIgnoreCase(interfaceClazz.getName()) && ptp.getActualTypeArguments().length > index) {
                    // 匹配到对应的接口
                    return ptp.getActualTypeArguments()[index];
                }
            }
        }
        return null;
    }

    public static DateFormatter offeredDateFormatter() {
        return new DateFormatter();
    }

    public static LocalDateTimeFormatter offeredLocalDateTimeFormatter() {
        return new LocalDateTimeFormatter();
    }

    public static SqlDateFormatter offeredSqlDateFormatter() {
        return new SqlDateFormatter();
    }

    public static TimestampFormatter offeredTimestampFormatter() {
        return new TimestampFormatter();
    }

    public static void checkTitle(ExcelTitle title, Field field) {
        // 扫描过滤器
        Filter filter = field.getAnnotation(Filter.class);
        if (null != filter && filter.value() != ExcelFilter.class) {
            title.setFilter(instantiate(filter.value()));
        }
        // 扫描转换器
        Converter converter = field.getAnnotation(Converter.class);
        if (null != converter && converter.value() != ExcelConverter.class) {
            title.setConverter(instantiate(converter.value()));
        }
        // 扫描格式化模式
        Formatter formatter = field.getAnnotation(Formatter.class);
        if (null != formatter && formatter.value() != ExcelFormatter.class) {
            title.setFormatter(instantiate(formatter.value()));
        }
    }
}
