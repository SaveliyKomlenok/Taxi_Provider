package com.software.modsen.rideservice.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogAspect {
    @Pointcut("within(com.software.modsen.rideservice.service.impl.*ServiceImpl)")
    public void isServiceLayer() {
    }

    @Pointcut("isServiceLayer() && @annotation(com.software.modsen.rideservice.annotation.LogExecutionTime)")
    public void hasLogExecutionTime() {
    }

    @Before(value = "hasLogExecutionTime()" +
                    "&& args(passengerId)" +
                    "&& target(service)", argNames = "joinPoint,passengerId,service")
    public void addLogging(JoinPoint joinPoint,
                           Object passengerId,
                           Object service) {
        log.info("Before invoke method in class {}, with passenger id {} with logs", service, passengerId);
    }

    @AfterReturning(value = "hasLogExecutionTime()" +
                            "&& target(service)",
            returning = "result", argNames = "result,service")
    public void addLoggingAfterReturning(Object result, Object service) {
        log.info("Result of invoked method in class {}. Result - {}", service, result);
    }

    @Around(value = "hasLogExecutionTime()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;
        log.info(joinPoint.getSignature() + " execute for " + executionTime + " ms");
        return proceed;
    }
}
