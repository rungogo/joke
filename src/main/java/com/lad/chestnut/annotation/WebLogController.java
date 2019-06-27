package com.lad.chestnut.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface WebLogController {
    /**
     * 日志描述信息
     *
     * @return
     */
    String description() default "";
}
