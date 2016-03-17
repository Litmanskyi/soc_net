package com.socnet.aspect;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Created by anton on 24.02.16.
 */
@Aspect
@Component
public class LoggerAspect {

    private Logger log = Logger.getLogger(LoggerAspect.class);

    @Pointcut("execution(* com.socnet.service.impl.*.*(..))")
    public void loggingOperation() {
    }

    @Before("loggingOperation()")
    public void beforeAdvice(JoinPoint joinPoint) {
        log.info(".............beforeAdvice.............");

        log.info("Join point kind : " + joinPoint.getKind());
        log.info("Signature declaring type : " + joinPoint.getSignature().getDeclaringTypeName());
        log.info("Signature name : " + joinPoint.getSignature().getName());
        log.info("Arguments : " + Arrays.toString(joinPoint.getArgs()));
        log.info("Target class : " + joinPoint.getTarget().getClass().getName());
        log.info("This class : " + joinPoint.getThis().getClass().getName());

        log.info("****************************************");
    }

    @AfterReturning(pointcut = "loggingOperation()", returning = "result")
    @Order(2)
    public void logAfter(JoinPoint joinPoint, Object result) {
        log.info(".............afterReturning.............");
        log.info("Exiting from Method :" + joinPoint.getSignature().getName());
        log.info("Return value :" + result);
        log.info("****************************************");
    }

    @AfterThrowing(pointcut = "loggingOperation()", throwing = "e")
    @Order(3)
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e) {
        log.info(".............afterThrowing.............");
        log.error("An exception has been thrown in " + joinPoint.getSignature().getName() + "()");
        log.error("Cause :" + e.getMessage());
        log.info("****************************************");
    }

    @Around("loggingOperation()")
    @Order(4)
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info(".............around.............");
        log.info("The method " + joinPoint.getSignature().getName() + "() begins with " + Arrays.toString(joinPoint.getArgs()));
        try {
            Object result = joinPoint.proceed();
            log.info("The method " + joinPoint.getSignature().getName() + "() ends with " + result);
            log.info("****************************************");
            return result;
        } catch (IllegalArgumentException e) {
            log.error("Illegal argument " + Arrays.toString(joinPoint.getArgs()) + " in " + joinPoint.getSignature().getName() + "()");
            log.info("****************************************");
            throw e;
        }
    }
}
