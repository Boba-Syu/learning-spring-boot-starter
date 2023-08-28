package cn.bobayu.ratelimiter.valve.impl;

import cn.bobayu.ratelimiter.Constants;
import cn.bobayu.ratelimiter.annotation.DoRateLimiter;
import cn.bobayu.ratelimiter.valve.IRateLimiterValveService;
import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

public class RateLimiterValve implements IRateLimiterValveService {
    @Override
    public Object access(ProceedingJoinPoint jp, Method method, DoRateLimiter doRateLimiter, Object[] args) throws Throwable {
        // 判断是否开启
        if (doRateLimiter.permitsPerSecond() == 0) {
            return jp.proceed();
        }

        String className = jp.getTarget().getClass().getName();
        String methodName = method.getName();

        String key = className + "." + methodName;

        if (Constants.rateLimiterMap.get(key) == null) {
            Constants.rateLimiterMap.put(key, RateLimiter.create(doRateLimiter.permitsPerSecond()));
        }

        RateLimiter rateLimiter = Constants.rateLimiterMap.get(key);
        if (rateLimiter.tryAcquire()) {
            return jp.proceed();
        }
        return JSON.parseObject(doRateLimiter.returnJson(), method.getReturnType());
    }
}
