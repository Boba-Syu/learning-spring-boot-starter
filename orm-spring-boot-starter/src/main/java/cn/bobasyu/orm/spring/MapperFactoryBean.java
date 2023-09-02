package cn.bobasyu.orm.spring;


import cn.bobasyu.orm.SqlSession;
import cn.bobasyu.orm.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;

import javax.annotation.Resource;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class MapperFactoryBean<T> implements FactoryBean<T>, DisposableBean {

    private Logger logger = LoggerFactory.getLogger(MapperFactoryBean.class);

    private Class<T> mapperInterface;

    @Resource
    private SqlSessionFactory sqlSessionFactory;

    private SqlSession session;

    public MapperFactoryBean(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    @Override
    public T getObject() {
        InvocationHandler handler = (proxy, method, args) -> {
            if ("toString".equals(method.getName())) {
                return method.getReturnType().newInstance();
            }
            logger.info("对Mapper接口进行代理：{}", method.getName());
            try {
                this.session = sqlSessionFactory.openSession();

                return this.session.selectOne(mapperInterface.getName() + "." + method.getName(), args[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return method.getReturnType().newInstance();
        };
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{mapperInterface}, handler);
    }

    @Override
    public Class<?> getObjectType() {
        return mapperInterface;
    }

    @Override
    public void destroy() throws Exception {
        session.close();
    }
}
