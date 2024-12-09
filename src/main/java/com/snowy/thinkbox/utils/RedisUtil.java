package com.snowy.thinkbox.utils;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    private static final Logger LOG = LoggerFactory.getLogger(RedisUtil.class);

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * true：不存在，放一个KEY
     * false：已存在
     * @param key
     * @param second
     * @return
     */
    public boolean validateRepeat(String key, long second) {
        if (redisTemplate.hasKey(key)) {
            LOG.info("key已存在：{}", key);
            return false;
        } else {
            LOG.info("key不存在，放入：{}，过期 {} 秒", key, second);
            redisTemplate.opsForValue().set(key, key, second, TimeUnit.SECONDS);
            return true;
        }
    }
}

//完善版
//@Component
//public class RedisUtil {
//
//    private static final Logger LOG = LoggerFactory.getLogger(RedisUtil.class);
//    private static final String PREFIX = "repeat_check:"; // 键名前缀
//
//    @Resource
//    private RedisTemplate<String, String> redisTemplate;
//
//    /**
//     * 校验是否重复
//     * true：不存在，放入一个KEY
//     * false：已存在
//     *
//     * @param key Redis键名
//     * @param second 过期时间，单位：秒
//     * @return 是否是重复
//     */
//    public boolean validateRepeat(String key, long second) {
//        if (key == null || key.isEmpty()) {
//            LOG.warn("Redis key 不能为空");
//            return false;
//        }
//
//        String redisKey = PREFIX + key; // 添加前缀
//        Boolean isSet = redisTemplate.opsForValue().setIfAbsent(redisKey, key, second, TimeUnit.SECONDS);
//
//        if (Boolean.TRUE.equals(isSet)) {
//            LOG.info("key不存在，放入：{}，过期 {} 秒", redisKey, second);
//            return true;
//        } else {
//            LOG.info("key已存在：{}", redisKey);
//            return false;
//        }
//    }
//}

