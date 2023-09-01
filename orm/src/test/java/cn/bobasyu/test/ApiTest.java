package cn.bobasyu.test;

import cn.bobasyu.orm.Resources;
import cn.bobasyu.orm.SqlSession;
import cn.bobasyu.orm.SqlSessionFactoryBuilder;
import cn.bobasyu.test.entity.User;
import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.io.Reader;

public class ApiTest {
    @Test
    public void test_queryUserInfoById() throws Exception {
        String resource = "mybatis-config-datasource.xml";
        try (Reader reader = Resources.getResourceAsReader(resource);
             SqlSession session = new SqlSessionFactoryBuilder().build(reader).openSession()) {
            User user = session.selectOne("cn.bobasyu.test.mapper.IUserMapper.queryUserById", 1L);
            System.out.println(JSON.toJSONString(user));
        }
    }
}
