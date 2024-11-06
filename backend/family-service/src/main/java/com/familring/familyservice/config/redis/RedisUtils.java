package com.familring.familyservice.config.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisUtils {
    private final RedisTemplate<String, String> redisTemplate;

    public void insertList(String key, String value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    public void getListSize(String key) {
        redisTemplate.opsForList().size(key);
    }

    public Long insertSet(String key, String value) {
        redisTemplate.opsForSet().add(key, value);
        return redisTemplate.opsForSet().size(key);
    }

    public Long getSetSize(String key) {
        Long size = redisTemplate.opsForSet().size(key);
        if (size == null || size == 0) {
            redisTemplate.delete(key);
            return 0L;
        }
        return size;
    }

    public Boolean containSet(String key, String value) {
        return redisTemplate.opsForSet().isMember(key,value);
    }

    public Long deleteSet(String key, String value) {
        redisTemplate.opsForSet().remove(key, value);
        Long size = redisTemplate.opsForSet().size(key);
        if (size == null || size == 0) {
            redisTemplate.delete(key);
            return 0L;
        }
        return size;
    }

    public void insertString(String key, String value) {
        redisTemplate.delete(key);
        redisTemplate.opsForValue().append(key, value);
    }

    public String getString(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteString(String key) {
        redisTemplate.delete(key);
    }
}
