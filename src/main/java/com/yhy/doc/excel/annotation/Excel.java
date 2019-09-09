package com.yhy.doc.excel.annotation;

import java.lang.annotation.*;

/**
 * author : 颜洪毅
 * e-mail : yhyzgn@gmail.com
 * time   : 2019-09-09 14:04
 * version: 1.0.0
 * desc   : 字段注解
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Excel {

    /**
     * 字段名称
     *
     * @return 字段名称
     */
    String value() default "";

    /**
     * 是否可空
     *
     * @return 是否可空
     */
    boolean nullable() default true;

    /**
     * 模糊匹配字段名称模板，如：%名称%
     *
     * @return 模糊匹配模板
     */
    String like() default "";

    /**
     * 是否智能匹配，采用字符串相似度匹配
     *
     * @return 是否只能匹配
     */
    boolean intelligent() default false;

    /**
     * 智能匹配容差，容错率
     * <p>
     * 只有相似度 >= (1 - tolerance) 才能匹配成功
     *
     * @return 智能匹配容错率
     */
    double tolerance() default 0.4;

    /**
     * 是否自动处理换行符
     *
     * @return 是否自动处理换行符
     */
    boolean wrap() default false;
}