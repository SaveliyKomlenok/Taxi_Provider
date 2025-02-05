package com.software.modsen.driverservice.aop;

import com.software.modsen.driverservice.entity.DriverRating;
import com.software.modsen.driverservice.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class CacheAspect {
    private final RedisService redisService;

    @Pointcut("within(com.software.modsen.driverservice.service.impl.*ServiceImpl)")
    public void isServiceLayer() {
    }

    @Pointcut("isServiceLayer() && @annotation(com.software.modsen.driverservice.annotation.CacheableDriverRating)")
    public void hasCacheableDriverRating() {
    }

    @Around(value = "hasCacheableDriverRating() && args(driverId)", argNames = "joinPoint, driverId")
    public Object cacheDriverRating(ProceedingJoinPoint joinPoint, Long driverId) throws Throwable {
        String cacheKey = "driverRating:" + driverId;

        DriverRating cachedRating = (DriverRating) redisService.getValue(cacheKey);
        if (cachedRating != null) {
            return cachedRating;
        }

        Object result = joinPoint.proceed();

        if (result instanceof DriverRating) {
            redisService.setValue(cacheKey, result, 10);
        }
        return result;
    }
}
