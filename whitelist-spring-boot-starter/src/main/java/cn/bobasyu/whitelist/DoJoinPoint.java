package cn.bobasyu.whitelist;

import cn.bobasyu.whitelist.annotation.DoWhiteList;
import com.alibaba.fastjson.JSON;
import org.apache.commons.beanutils.BeanUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;

@Aspect
@Component
public class DoJoinPoint {
    private Logger logger = LoggerFactory.getLogger(DoJoinPoint.class);

    @Resource
    private String whiteListConfig;

    @Pointcut("@annotation(cn.bobasyu.whitelist.annotation.DoWhiteList)")
    public void aopPoint() {
    }

    @Around("aopPoint()")
    public Object doRouter(ProceedingJoinPoint jp) throws Throwable {
        // 获取内容
        Method method = geMethod(jp);
        DoWhiteList whiteList = method.getAnnotation(DoWhiteList.class);

        // 获取字段值
        String keyValue = getFieldValue(whiteList.key(), jp.getArgs());
        logger.info("middleware whitelist handler method: {} value: {}", method.getName(), keyValue);
        if (null == keyValue || "".equals(keyValue)) {
            return jp.proceed();
        }
        // 白名单过滤
        String[] split = whiteListConfig.split(",");
        for (String str : split) {
            if (keyValue.equals(str)) {
                return jp.proceed();
            }
        }
        // 拦截
        return returnObject(whiteList, method);
    }

    /**
     * 返回对象
     *
     * @param whiteList
     * @param method
     * @return
     */
    private Object returnObject(DoWhiteList whiteList, Method method) throws InstantiationException, IllegalAccessException {
        Class<?> returnType = method.getReturnType();
        String returnJson = whiteList.returnJson();
        if ("".equals(returnJson)) {
            return returnType.newInstance();
        }
        return JSON.parseObject(returnJson, returnType);
    }

    /**
     * 获取属性值
     *
     * @param field
     * @param args
     * @return
     */
    private String getFieldValue(String field, Object[] args) {
        String fieldValue = null;
        for (Object arg : args) {
            try {
                if (fieldValue == null || "".equals(fieldValue)) {
                    fieldValue = BeanUtils.getProperty(arg, field);
                }
            } catch (Exception e) {
                if (args.length == 1) {
                    return args[0].toString();
                }
            }
        }
        return fieldValue;
    }

    private Method geMethod(ProceedingJoinPoint jp) throws NoSuchMethodException {
        Signature sig = jp.getSignature();
        MethodSignature methodSignature = (MethodSignature) sig;
        return jp.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
    }
}