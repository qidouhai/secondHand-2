package cn.chenny3.secondHand.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtils {
    @Autowired
    private StringRedisTemplate redisTemplate;

    private SetOperations<String, String> getOpsForSet() {
        return redisTemplate.opsForSet();
    }

    private ValueOperations<String, String> getOpsForValue() {
        return redisTemplate.opsForValue();
    }

    private ListOperations<String, String> getOpsForList() {
        return redisTemplate.opsForList();
    }

    private ZSetOperations<String, String> getOpsForZSet() {
        return redisTemplate.opsForZSet();
    }

    public Long sadd(String key, String value) {
        return getOpsForSet().add(key, value);
    }

    public Long srem(String key, String value) {
        return getOpsForSet().remove(key, value);
    }

    public Boolean sIsMember(String key, String value) {
        return getOpsForSet().isMember(key, value);
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

    public void set(String key, String value, Long timeOut) {
        getOpsForValue().set(key, value, timeOut, TimeUnit.SECONDS);
    }

    public void set(String key, String value) {
        getOpsForValue().set(key, value);
    }

    public String get(String key) {
        return getOpsForValue().get(key);
    }

    public void del(String key) {
        redisTemplate.delete(key);
    }

    public Set<String> scard(String key) {
        return getOpsForSet().members(key);
    }

    public Long lpush(String key, String value) {
        return getOpsForList().leftPush(key, value);
    }

    public Long rpush(String key, String value) {
        return getOpsForList().rightPush(key, value);
    }

    public Boolean zadd(String key, String value, double score) {
        return getOpsForZSet().add(key, value, score);
    }

    public Set<String> zrange(String key, int start, int end) {
        return getOpsForZSet().range(key, start, end);
    }

    public Set<String> zrevrange(String key, int start, int end) {
        return getOpsForZSet().reverseRange(key, start, end);
    }

    public Long zrem(String key, String... member) {
        return getOpsForZSet().remove(key, member);
    }

    public Long zrank(String key, String memeber) {
        return getOpsForZSet().rank(key, memeber);
    }
}
