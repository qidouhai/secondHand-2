package cn.chenny3.secondHand.configuration;

import cn.chenny3.secondHand.common.config.RedisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.JedisPoolConfig;
@Configuration
@EnableCaching
@Import(value = RedisAutoConfiguration.class)
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfiguration {
    @Autowired
    private RedisProperties properties;

    @Bean
    public RedisConnectionFactory jedisConnectionFactory() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        //poolConfig.setMaxTotal(properties.getPool().getMaxActive());
        poolConfig.setMaxTotal(-1);
        poolConfig.setMaxIdle(properties.getPool().getMaxIdle());
       poolConfig.setMaxWaitMillis(properties.getPool().getMaxWait());
        //poolConfig.setMaxWaitMillis(5000);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnCreate(true);
        poolConfig.setTestWhileIdle(true);
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(poolConfig);
        jedisConnectionFactory.setHostName(properties.getHost());
        if (null != properties.getPassword()) {
            jedisConnectionFactory.setPassword(properties.getPassword());
        }
        jedisConnectionFactory.setDatabase(properties.getDatabase());
        jedisConnectionFactory.setPort(properties.getPort());

        //其他配置，可再次扩展
        return jedisConnectionFactory;
    }

    @Bean(name = "stringRedisTemplate")
    public StringRedisTemplate StringRedisTemplate() {
        StringRedisTemplate redisTemplate = new StringRedisTemplate();

        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        redisTemplate.afterPropertiesSet();
        redisTemplate.setEnableTransactionSupport(true);

        return redisTemplate;
    }



}
