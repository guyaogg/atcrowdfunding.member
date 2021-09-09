package com.atguigu.crowd;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * @author guyao
 */
@SpringBootTest
public class RedisTest{
    Logger logger = LoggerFactory.getLogger(RedisTest.class);
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Test
    public void test(){
        ValueOperations<String, String> stringStringValueOperations = redisTemplate.opsForValue();
        stringStringValueOperations.set("apple","yellow");
        logger.info(stringStringValueOperations.get("apple"));
    }
    @Test
    public void testEX(){
        Long expire = redisTemplate.getExpire("dd");
        logger.info(String.valueOf(expire));
    }
}
