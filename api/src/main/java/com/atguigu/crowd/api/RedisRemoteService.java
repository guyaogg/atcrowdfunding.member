package com.atguigu.crowd.api;

import com.atguigu.crowd.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.TimeUnit;

/**
 * @author guyao
 */
@FeignClient("crowd-redis")
public interface RedisRemoteService {
    /**
     * 获取key过期时间
     * @param key
     * @return
     */
    @RequestMapping("/get/redis/key/expire/remote")
    ResultEntity<Long> getRedisKeyExpireRemote(@RequestParam("key") String key);
    /**
     * 不带超时时间的kv存储
     * @param key
     * @param value
     * @return
     */
    @RequestMapping("/set/redis/key/value/remote")
    ResultEntity<String> setRedisKeyValueRemote(@RequestParam("key") String key,
                                                @RequestParam("value")String value);

    /**
     * 带超时时间的kv存储
     * @param key
     * @param value
     * @param time
     * @param timeUnit
     * @return
     */
    @RequestMapping("/set/redis/key/value/remote/with/timeout")
    ResultEntity<String> setRedisKeyValueRemoteWithTimeout(@RequestParam("key") String key,
                                                           @RequestParam("value")String value,
                                                           @RequestParam("time")long time,
                                                           @RequestParam("timeUnit") TimeUnit timeUnit
                                                           );

    /**
     * 根据可以获取String值
     * @param key
     * @return
     */
    @RequestMapping("/get/redis/string/value/by/key")
    ResultEntity<String> getRedisStringValueByKey(@RequestParam("key") String key);

    /**
     * 通过key删除存储值
     * @param key
     * @return
     */
    @RequestMapping("/remove/redis/key/remote")
    ResultEntity<String> removeRedisKeyRemote(@RequestParam("key") String key);
}
