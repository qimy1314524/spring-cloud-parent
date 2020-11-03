package com.atguigu.spring.cloud.config;

import java.util.Objects;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheManager.RedisCacheManagerBuilder;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

/**
 * @author tey
 * @version V1.0
 * @date 2020/8/14- 10:33
 * @desc
 **/
@Configuration
public class RedisConfig {

  @Bean
  public RedisTemplate<Object,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
    RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory);
    redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
    return redisTemplate;
  }

  @Bean
  public CacheManager cacheManager(RedisTemplate<Object,Object> redisTemplate){
    RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
        .serializeKeysWith(SerializationPair.fromSerializer(redisTemplate.getStringSerializer()))
        .serializeValuesWith(SerializationPair.fromSerializer(redisTemplate.getValueSerializer()))
        .disableCachingNullValues();

    RedisCacheManager cacheManager = RedisCacheManagerBuilder
        .fromConnectionFactory(Objects.requireNonNull(redisTemplate.getConnectionFactory()))
        .cacheDefaults(cacheConfiguration)
        .transactionAware()
        .build();
    return cacheManager;
  }

}
