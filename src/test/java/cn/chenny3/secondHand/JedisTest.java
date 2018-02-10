package cn.chenny3.secondHand;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JedisTest {
    @Autowired
    StringRedisTemplate redisTemplate;

    @Test
    public void set(){
        redisTemplate.opsForValue().set("name","title");
    }

    @Test
    public void brpop(){
        String value= redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                List<byte[]> bytes = connection.bRPop(0, "list".getBytes());
                return new String(bytes.get(1));
            }
        });

        System.out.println(value);
    }


}
