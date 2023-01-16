package com.ww.template.utils;

import cn.hutool.core.util.BooleanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class CacheClient {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public boolean tryLock(String key) {
        Boolean flag = redisTemplate.opsForValue().setIfAbsent(key, "1", 10, TimeUnit.SECONDS);
        return BooleanUtil.isTrue(flag);
    }

    public void unlock(String key) {
        redisTemplate.delete(key);
    }
}
