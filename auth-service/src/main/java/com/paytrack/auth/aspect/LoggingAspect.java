package com.paytrack.auth.aspect;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log =
            LoggerFactory.getLogger(
                    LoggingAspect.class
            );

    // Apply to ALL layers
    @Pointcut(
            "execution(* com.paytrack..controller..*(..)) || " +
            "execution(* com.paytrack..service..*(..)) || " +
            "execution(* com.paytrack..repository..*(..))"
    )
    public void appLayer() {

        // Pointcut method intentionally empty
    }

    /* ================= BEFORE ================= */

    @Before("appLayer()")
    public void logBefore(
            JoinPoint joinPoint) {

        if (log.isInfoEnabled()) {

            log.info(
                    " ENTER: {}.{}() args={}",
                    joinPoint.getTarget()
                            .getClass()
                            .getSimpleName(),

                    joinPoint.getSignature()
                            .getName(),

                    Arrays.toString(
                            joinPoint.getArgs()
                    )
            );
        }
    }

    /* ================= AFTER RETURN ================= */

    @AfterReturning(
            pointcut = "appLayer()",
            returning = "result"
    )
    public void logAfter(
            JoinPoint joinPoint,
            Object result) {

        if (log.isInfoEnabled()) {

            log.info(
                    " EXIT: {}.{}() result={}",
                    joinPoint.getTarget()
                            .getClass()
                            .getSimpleName(),

                    joinPoint.getSignature()
                            .getName(),

                    result
            );
        }
    }

    /* ================= EXCEPTION ================= */

    @AfterThrowing(
            pointcut = "appLayer()",
            throwing = "ex"
    )
    public void logException(
            JoinPoint joinPoint,
            Throwable ex) {

        if (log.isErrorEnabled()) {

            log.error(
                    " ERROR: {}.{}() message={}",
                    joinPoint.getTarget()
                            .getClass()
                            .getSimpleName(),

                    joinPoint.getSignature()
                            .getName(),

                    ex.getMessage()
            );
        }
    }
}