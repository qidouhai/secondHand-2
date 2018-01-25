package cn.chenny3.secondHand.commons.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component
public class RedisAdapter {
    @Autowired
    private StringRedisTemplate redisTemplate;

    private SetOperations<String, String> getOpsForSet() {
        return redisTemplate.opsForSet();
    }

    private ValueOperations<String, String> getOpsForValue() {
        return redisTemplate.opsForValue();
    }

    public Long sadd(String key, String value) {
        return getOpsForSet().add(key, value);
    }

    public Long srem(String key,String value){
        return getOpsForSet().remove(key,value);
    }
    public Boolean sIsMember(String key,String value){
        return getOpsForSet().isMember(key,value);
    }
    public Long incr(String key) {
        return getOpsForValue().increment(key, 1);
    }

    public Long incrBy(String key, int delta) {
        return getOpsForValue().increment(key, delta);
    }

    public Long decr(String key) {
        return getOpsForValue().increment(key, -1);
    }

    public Long decrBy(String key, int delta) {
        return getOpsForValue().increment(key, delta);
    }

    public String get(String key) {
        return getOpsForValue().get(key);
    }

    public void del(String key) {
        redisTemplate.delete(key);
    }


}
