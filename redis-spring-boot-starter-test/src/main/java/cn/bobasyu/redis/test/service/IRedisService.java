package cn.bobasyu.redis.test.service;

import cn.bobasyu.redis.annotation.XRedis;

@XRedis
public interface IRedisService {

    String get(String key);

    void set(String key, String val);
}
