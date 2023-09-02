package cn.bobasyu.orm.test;


import cn.bobasyu.orm.test.entity.User;
import cn.bobasyu.orm.test.mapper.IUserMapper;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiTest {

    @Autowired
    private IUserMapper userMapper;

    @Test
    public void queryUserById() {
        User user = userMapper.queryUserById(1L);
        System.out.println("测试结果：" + JSON.toJSONString(user));
    }
}
