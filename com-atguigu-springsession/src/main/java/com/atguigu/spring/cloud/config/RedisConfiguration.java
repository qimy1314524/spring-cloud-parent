package com.atguigu.spring.cloud.config;

//import io.lettuce.core.ReadFrom;
//import io.lettuce.core.cluster.ClusterClientOptions;
//import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisClusterConfiguration;
//import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//import java.time.Duration;
 
/**
 * @author pgig
 * @date 2019/3/21
 */
/*
@Configuration
public class RedisConfiguration {


    @Autowired
    private RedisProperties redisProperties;

    @Value("${redis.maxRedirects:10}")
    private int maxRedirects;

    @Value("${redis.refreshTime:5}")
    private int refreshTime;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {

        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(
            redisProperties.getCluster().getNodes());
        redisClusterConfiguration.setPassword("619619Tey");

        redisClusterConfiguration.setMaxRedirects(maxRedirects);

        //支持自适应集群拓扑刷新和静态刷新源
        ClusterTopologyRefreshOptions clusterTopologyRefreshOptions = ClusterTopologyRefreshOptions
            .builder()
            .enablePeriodicRefresh()
            .enableAllAdaptiveRefreshTriggers()
            .refreshPeriod(Duration.ofSeconds(refreshTime))
            .build();

        ClusterClientOptions clusterClientOptions = ClusterClientOptions.builder()
            .topologyRefreshOptions(clusterTopologyRefreshOptions).build();

        //从优先，读写分离，读从可能存在不一致，最终一致性CP
        LettuceClientConfiguration lettuceClientConfiguration = LettuceClientConfiguration.builder()
            .readFrom(ReadFrom.SLAVE_PREFERRED)
            .clientOptions(clusterClientOptions).build();

        return new LettuceConnectionFactory(redisClusterConfiguration, lettuceClientConfiguration);
    }


    @Bean
    public RedisTemplate<Object, Object> redisTemplate(
        LettuceConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
*/

