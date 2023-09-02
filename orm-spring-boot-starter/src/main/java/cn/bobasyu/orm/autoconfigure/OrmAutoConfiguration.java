package cn.bobasyu.orm.autoconfigure;

import cn.bobasyu.orm.SqlSessionFactory;
import cn.bobasyu.orm.SqlSessionFactoryBuilder;
import cn.bobasyu.orm.spring.MapperFactoryBean;
import cn.bobasyu.orm.spring.MapperScannerConfigurer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
@ConditionalOnClass({SqlSessionFactory.class})
@EnableConfigurationProperties(OrmProperties.class)
public class OrmAutoConfiguration implements InitializingBean {

    @Bean
    @ConditionalOnMissingBean
    public SqlSessionFactory sqlSessionFactory(Connection connection, OrmProperties ormProperties) throws Exception {
        return new SqlSessionFactoryBuilder().build(connection, ormProperties.getMapperLocations());
    }

    @Bean
    @ConditionalOnMissingBean
    public Connection connection(OrmProperties ormProperties) {
        try {
            Class.forName(ormProperties.getDriver());
            return DriverManager.getConnection(ormProperties.getUrl(), ormProperties.getUsername(), ormProperties.getPassword());
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 在Bean实例化之前（注册Bean信息时）通过Environment读取yml中的信息，用于扫描mapper接口
     */
    public static class AutoConfiguredMapperScannerRegistrar implements EnvironmentAware, ImportBeanDefinitionRegistrar {
        private String basePackage;

        @Override
        public void setEnvironment(Environment environment) {
            this.basePackage = environment.getProperty("orm.datasource.base-mapper-package");

        }

        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(MapperScannerConfigurer.class);
            builder.addPropertyValue("basePackage", basePackage);
            registry.registerBeanDefinition(MapperScannerConfigurer.class.getName(), builder.getBeanDefinition());
        }
    }

    /**
     * 把用于扫描加载配置和初始化的类启动起来，即加载AutoConfiguredMapperScannerRegistrar类
     */
    @org.springframework.context.annotation.Configuration
    @Import(AutoConfiguredMapperScannerRegistrar.class)
    @ConditionalOnMissingBean({MapperFactoryBean.class, MapperScannerConfigurer.class})
    public static class MapperScannerRegistrarNotFoundConfiguration implements InitializingBean {

        @Override
        public void afterPropertiesSet() {
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }
}
