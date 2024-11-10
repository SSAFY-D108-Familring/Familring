package com.familring.familyservice.config.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class RedisUtil {
    private final RedisTemplate<String, String> redisTemplate;

    public void insertList(String key, String value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    public Long getListSize(String key) {
        return redisTemplate.opsForList().size(key);
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
        return redisTemplate.opsForSet().isMember(key, value);
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

    public void setString(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public String getString(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteString(String key) {
        redisTemplate.delete(key);
    }

    // 숫자 형태의 문자열 값을 증가시키기
    public Long incrementString(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    // 숫자 형태의 문자열 값을 감소시키기
    public Long decrementString(String key) {
        return redisTemplate.opsForValue().decrement(key);
    }

    public Set<String> getSetMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }
}

