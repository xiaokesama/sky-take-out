package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    /**
     * 自动填充切面
     * execution(* com.sky.mapper.*.*(..)) => execution(所有返回值类型 com.sky.mapper.所有类.所有方法(所有参数))
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut() {
    }

    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("执行自动填充...");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoFill annotation = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = annotation.value();

        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) {
            throw new IllegalArgumentException("参数为空异常");
        }
        Object entity = args[0];

        LocalDateTime now = LocalDateTime.now();
        Long userId = BaseContext.getCurrentId();

        Method setCreateTime = null;
        Method setUpdateTime = null;
        Method setCreateUser = null;
        Method setUpdateUser = null;

        try {
            setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
            setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        if (operationType == OperationType.INSERT) {
            try {
                setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            setUpdateTime.invoke(entity, now);
            setUpdateUser.invoke(entity, userId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (operationType == OperationType.INSERT) {
            try {
                setCreateTime.invoke(entity, now);
                setCreateUser.invoke(entity, userId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}