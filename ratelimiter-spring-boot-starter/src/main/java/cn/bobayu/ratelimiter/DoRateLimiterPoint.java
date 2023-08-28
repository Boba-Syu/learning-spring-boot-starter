package cn.bobayu.ratelimiter;

import cn.bobayu.ratelimiter.annotation.DoRateLimiter;
import cn.bobayu.ratelimiter.valve.IRateLimiterValveService;
import cn.bobayu.ratelimiter.valve.impl.RateLimiterValve;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class DoRateLimiterPoint {
    @Pointcut("@annotation(cn.bobayu.ratelimiter.annotation.DoRateLimiter)")
    public void aopPoint() {
    }

    @Around("aopPoint() && @annotation(doRateLimiter)")
    public Object doRouter(ProceedingJoinPoint jp, DoRateLimiter doRateLimiter) throws Throwable {
        IRateLimiterValveService valveService = new RateLimiterValve();
        return valveService.access(jp, getMethod(jp), doRateLimiter, jp.getArgs());

    }

    private Method getMethod(ProceedingJoinPoint jp) throws NoSuchMethodException {
        Signature sig = jp.getSignature();
        MethodSignature methodSignature = (MethodSignature) sig;
        return jp.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
    }
}
