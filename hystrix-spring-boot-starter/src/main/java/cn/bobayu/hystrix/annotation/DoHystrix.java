package cn.bobayu.hystrix.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DoHystrix {
    /**
     * 失败结果 JSON
     *
     * @return
     */
    String returnJson() default "";

    /**
     * 超时熔断
     *
     * @return
     */
    int timeoutValue() default 0;
}
