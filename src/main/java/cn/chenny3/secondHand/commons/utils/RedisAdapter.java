package cn.chenny3.secondHand.commons.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisAdapter {
    @Autowired
    private StringRedisTemplate redisTemplate;

    public Long sadd(String key,String value){
        return redisTemplate.opsForSet().add(key,value);
    }

}
