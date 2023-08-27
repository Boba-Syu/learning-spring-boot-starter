package cn.bobayu.hystrix.valve;

import cn.bobayu.hystrix.annotation.DoHystrix;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

public interface IValveService {
    Object access(ProceedingJoinPoint jp, Method method, DoHystrix doHystrix, Object[] args);

    Object run();

    Object getFallback();
}
