package cn.bobayu.ratelimiter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DoRateLimiter {
    /**
     * 限流许可量
     *
     * @return
     */
    double permitsPerSecond() default 0D;

    /**
     * 失败结果
     *
     * @return
     */
    String returnJson() default "";
}
