package com.ww.template.aspect;

import com.ww.template.enums.OperateTypeEnum;

import java.lang.annotation.*;

import static com.ww.template.enums.OperateTypeEnum.TOTAL_DOWNLOAD_COUNT;

/**
 * 自定义培训学习注解
 *
 * @author weiwang127
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperateTrainLearn {

    /**
     * 操作描述
     */
    String operateDesc() default "";

    /**
     * 操作类型
     */
    OperateTypeEnum operateType() default TOTAL_DOWNLOAD_COUNT;
}

