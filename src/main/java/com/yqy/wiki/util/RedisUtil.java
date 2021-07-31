package com.yqy.wiki.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author bahsk
 * @createTime 2021-07-29 16:24
 * @description Redis工具类
 */
@Component
public class RedisUtil {
    private static final Logger LOG = LoggerFactory.getLogger(RedisUtil.class);

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * @author: bahsk
     * @date: 2021/7/29 16:25
     * @description: 判断redis中的key是否存在
     * @params:
     * @return: true: 不存在可以放一个key, false:已存在此
     */
    public boolean validateRepeat(String key, long second) {
        if (redisTemplate.hasKey(key)) {
            LOG.info("key已经存在: {}", key);
            return false;
        } else {
            LOG.info("key不存在,放入： {},过期 {} 秒", key, second);
            redisTemplate.opsForValue().set(key, key, second, TimeUnit.SECONDS);
            return true;
        }
    }
}
