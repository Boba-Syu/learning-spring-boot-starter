package cn.bobasyu.redis.config;

import cn.bobasyu.redis.annotation.XRedis;
import cn.bobasyu.redis.reflect.XRedisFactoryBean;
import cn.bobasyu.redis.util.SimpleMetadataReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.beans.Introspector;
import java.util.List;

@Configuration
@EnableConfigurationProperties(XRedisProperties.class)
public class XRedisRegisterAutoConfiguration implements InitializingBean {

    private Logger logger = LoggerFactory.getLogger(XRedisRegisterAutoConfiguration.class);

    @Autowired
    private XRedisProperties xRedisProperties;

    @Bean
    @ConditionalOnMissingBean
    public Jedis jedis() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(5);
        jedisPoolConfig.setTestOnBorrow(false);
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, xRedisProperties.getHost(), xRedisProperties.getPort());
        return jedisPool.getResource();
    }


    public static class XRedisRegister implements BeanFactoryAware, ImportBeanDefinitionRegistrar {
        private BeanFactory beanFactory;

        @Override
        public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            this.beanFactory = beanFactory;
        }

        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
            try {
                if (!AutoConfigurationPackages.has(this.beanFactory)) {
                    return;
                }

                List<String> packages = AutoConfigurationPackages.get(this.beanFactory);
                String basePackage = StringUtils.collectionToCommaDelimitedString(packages);

                String packageSearchPath = "classpath*:" + basePackage.replace('.', '/') + "/**/*.class";

                ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
                Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);

                for (Resource resource : resources) {
                    MetadataReader metadataReader = new SimpleMetadataReader(resource, ClassUtils.getDefaultClassLoader());
                    // 判断是否有注解
                    XRedis xRedis = Class.forName(metadataReader.getClassMetadata().getClassName()).getAnnotation(XRedis.class);
                    if (xRedis == null) continue;

                    ScannedGenericBeanDefinition beanDefinition = new ScannedGenericBeanDefinition(metadataReader);
                    String beanName = Introspector.decapitalize(ClassUtils.getShortName(beanDefinition.getBeanClassName()));

                    beanDefinition.setResource(resource);
                    beanDefinition.setSource(resource);
                    beanDefinition.getConstructorArgumentValues().addGenericArgumentValue(beanDefinition.getBeanClassName());
                    beanDefinition.setBeanClass(XRedisFactoryBean.class);

                    BeanDefinitionHolder beanDefinitionHolder = new BeanDefinitionHolder(beanDefinition, beanName);
                    registry.registerBeanDefinition(beanName, beanDefinitionHolder.getBeanDefinition());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Configuration
    @Import(XRedisRegister.class)
    public static class MapperScannerRegistrarNotFoundConfiguration implements InitializingBean {

        @Override
        public void afterPropertiesSet() {
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
