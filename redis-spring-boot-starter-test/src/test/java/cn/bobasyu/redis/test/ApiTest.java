package cn.bobasyu.redis.test;

import cn.bobasyu.redis.test.service.IRedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiTest {

    @Autowired
    private IRedisService redisService;

    @Test
    public void setTest() {
        redisService.set("hello", "world");
    }

    @Test
    public void getTest() {
        String result = redisService.get("hello");
        System.out.println("获取 Redis key：hello 信息：" + result);
    }
}
