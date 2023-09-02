package cn.bobasyu.orm.spring;

import cn.bobasyu.orm.Resources;
import cn.bobasyu.orm.SqlSessionFactory;
import cn.bobasyu.orm.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.io.Reader;

public class SqlSessionFactoryBean implements FactoryBean<SqlSessionFactory>, InitializingBean {
    private String resource;

    private SqlSessionFactory sqlSessionFactory;

    @Override
    public SqlSessionFactory getObject() throws Exception {
        return this.sqlSessionFactory;
    }

    @Override
    public Class<?> getObjectType() {
        return this.sqlSessionFactory.getClass();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try (Reader reader = Resources.getResourceAsReader(resource)) {
            this.sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setResource(String resource) {
        this.resource = resource;
    }
}
