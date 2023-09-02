package cn.bobasyu.orm.spring.test;

import cn.bobasyu.orm.spring.test.entity.User;
import cn.bobasyu.orm.spring.test.mapper.IUserMapper;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class ApiTest {

    @Test
    public void test_ClassPathXmlApplicationContext() {
        BeanFactory beanFactory = new ClassPathXmlApplicationContext("spring.xml");
        IUserMapper userMapper = beanFactory.getBean("IUserMapper", IUserMapper.class);
        User user = userMapper.queryUserById(1L);
        System.out.println("测试结果：" + JSON.toJSONString(user));
    }
}
