package com.atguigu.crowd.handler;

import com.atguigu.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @author guyao
 */
@RestController
public class RedisHandler {
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 获取key的超时时间
     *
     * @param key key值
     * @return
     */
    @RequestMapping("/get/redis/key/expire/remote")
    ResultEntity<Long> getRedisKeyExpireRemote(@RequestParam("key") String key) {
        Long expire = null;
        try {
            expire = redisTemplate.getExpire(key);
            return ResultEntity.successWithData(expire);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }

    }

    /**
     * 不带超时时间的kv存储
     *
     * @param key
     * @param value
     * @return
     */
    @RequestMapping("/set/redis/key/value/remote")
    ResultEntity<String> setRedisKeyValueRemote(@RequestParam("key") String key,
                                                @RequestParam("value") String value) {

        try {
            ValueOperations<String, String> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    }

    /**
     * 带超时时间的kv存储
     *
     * @param key
     * @param value
     * @param time
     * @param timeUnit
     * @return
     */
    @RequestMapping("/set/redis/key/value/remote/with/timeout")
    ResultEntity<String> setRedisKeyValueRemoteWithTimeout(@RequestParam("key") String key,
                                                           @RequestParam("value") String value,
                                                           @RequestParam("time") long time,
                                                           @RequestParam("timeUnit") TimeUnit timeUnit
    ) {

        try {
            ValueOperations<String, String> operations = redisTemplate.opsForValue();
            operations.set(key, value, time, timeUnit);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    }

    /**
     * 根据可以获取String值
     *
     * @param key
     * @return
     */
    @RequestMapping("/get/redis/string/value/by/key")
    ResultEntity<String> getRedisStringValueByKey(@RequestParam("key") String key) {
        try {
            ValueOperations<String, String> operations = redisTemplate.opsForValue();
            String value = operations.get(key);
            return ResultEntity.successWithData(value);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }

    }

    /**
     * 通过key删除存储值
     *
     * @param key
     * @return
     */
    @RequestMapping("/remove/redis/key/remote")
    ResultEntity<String> removeRedisKeyRemote(@RequestParam("key") String key) {

        try {
            redisTemplate.delete(key);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.fail(e.getMessage());
        }
    }
}
