package com.smart.aiplatformauth.utils;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Redis缓存处理工具类
 * @Auther chengjz
 */
@Component
public class RedisUtil {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 判断key是否存在
     *
     * @param key
     * @return
     */
    public boolean existKey(String key) {
        return redisTemplate.hasKey(key);
    }


    /**
     * 向key对应的map中添加缓存对象
     *
     * @param key   cache对象key
     * @param field map对应的key
     * @param obj   对象
     */
    public <T> void addMap(String key, String field, T obj) {
        redisTemplate.opsForHash().put(key, field, obj);
    }

    /**
     * 判断field是否存在
     *
     * @param key
     * @param field
     * @return
     */
    public boolean existMapField(String key, String field) {
        if (key != null && key.trim().length() != 0 && field != null && field.trim().length() != 0) {
            return redisTemplate.opsForHash().hasKey(key, field);
        }
        return false;
    }

    /**
     * 获取map缓存中的某个对象
     *
     * @param key
     * @param field
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getMapField(String key, String field, Class<T> clazz) {
        return (T) redisTemplate.boundHashOps(key).get(field);
    }

    /**
     * 获取map缓存中的所有对象key的集合
     * @param key
     * @return
     */
    public Set<String> getMapKeySet(String key) {
        if (key != null && key.trim().length() != 0) {
            //return redisTemplate.opsForHash().keys(key);
            return redisTemplate.boundHashOps(key).keys();
        }
        return null;
    }

    /**
     * 清理redis所有key
     */
    public void initRedis() {
        Set<String> keys = redisTemplate.keys("*");
        redisTemplate.delete(keys);
    }

    /**
     * 清理redis某个key
     * @param key
     */
    public void deleteRedisKey(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 清理redis某些keys
     * @param keys
     */
    public void deleteRedisKey(Set<String> keys) {
        redisTemplate.delete(keys);
    }

    /**
     * 向key对应的map中移出某个缓存对象
     * @param key   cache对象key
     * @param field map对应的key
     */
    public <T> void deleteMap(String key, String field) {
        redisTemplate.opsForHash().delete(key, field);
    }


}