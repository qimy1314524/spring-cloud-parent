/*
package com.atguigu.spring.cloud.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

*/
/**
 * redis集群必须redis 3.0以上版本的支持 redis集群
 *//*

//@Configuration
public class JedisClusterConfig {


  private static final Logger LOGGER = LoggerFactory.getLogger(JedisClusterConfig.class);

  @Autowired
  private RedisProperties redisProperties;


  */
/**
   * Spring Data Redis 1.7 支持redis集群 jedis集群配置
   *//*

  @Bean
  @Primary
  public RedisConnectionFactory connectionFactory() {
    RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(redisProperties.getCluster().getNodes());
    redisClusterConfiguration.setPassword(redisProperties.getPassword());
    redisClusterConfiguration.setMaxRedirects(redisProperties.getCluster().getMaxRedirects());
    RedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory(redisClusterConfiguration);
    return redisConnectionFactory;
  }


}*/
