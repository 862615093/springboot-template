package com.ww.template.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 培训学习切面
 *
 * @author weiwang127
 */
@Aspect
@Component
public interface OperateTrainLearnAspect {

    /**
     * 业务接口
     *
     * @return Object
     */
    @Around("@annotation(ol)")
    Object operateTrainLearnLogic(ProceedingJoinPoint joinPoint, OperateTrainLearn ol) ;
}