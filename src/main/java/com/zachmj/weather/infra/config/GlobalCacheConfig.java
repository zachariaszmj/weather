package com.zachmj.weather.infra.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.CacheKeyPrefix;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.*;
import org.springframework.lang.NonNull;

import java.nio.ByteBuffer;
import java.time.Duration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
@EnableCaching
public class GlobalCacheConfig {
    public static final String WEATHER = "weather";
    public static final String LOCATION = "location";

    @Bean
    @Primary
    public CacheManager globalCacheManager(
            RedisConnectionFactory redisConnectionFactory,
            RedisCacheConfiguration defaultCacheConfig,
            Map<String, Duration> expirations
    ) {
        final Map<String, RedisCacheConfiguration> nonDefaultCacheConfig = new HashMap<>();
        for (Map.Entry<String, Duration> configuration : expirations.entrySet()) {
            if ("default".equals(configuration.getKey())) {
                continue;
            }
            final RedisCacheConfiguration nonDefaultRedisConfiguration = defaultCacheConfig
                    .entryTtl(configuration.getValue());
            nonDefaultCacheConfig.put(configuration.getKey(), nonDefaultRedisConfiguration);
        }
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultCacheConfig)
                .withInitialCacheConfigurations(nonDefaultCacheConfig)
                .build();
    }

    @Bean
    @ConfigurationProperties(prefix = "cache.expire")
    Map<String, Duration> cacheExpirations() {
        return new LinkedHashMap<>();
    }

    @Bean
    CacheKeyPrefix redisKeyPrefix() {
        final String gitPrefix = "master";
        return cachePrefix -> gitPrefix + "::" + cachePrefix + "::";
    }

    @Bean
    RedisCacheConfiguration defaultCacheConfig(
            CacheKeyPrefix redisKeyPrefix,
            RedisSerializationContext.SerializationPair<Object> valuesSerialization,
            @Value("${tsm.cache.expire.default:5m}") Duration expireDuration
    ) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .computePrefixWith(redisKeyPrefix)
                .serializeValuesWith(valuesSerialization)
                .entryTtl(expireDuration);
    }

    @Bean
    RedisSerializationContext.SerializationPair<Object> valuesSerialization(GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer) {
        return new SerializerToSerializationPairAdapter<>(genericJackson2JsonRedisSerializer);
    }

    @Bean
    GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }

    @RequiredArgsConstructor
    private static class SerializerToSerializationPairAdapter<T> implements RedisSerializationContext.SerializationPair<T> {
        private final RedisSerializer<T> serializer;

        @Override
        @NonNull
        public RedisElementReader<T> getReader() {
            return byteBuffer -> serializer.deserialize(byteBuffer.array());
        }

        @Override
        @NonNull
        public RedisElementWriter<T> getWriter() {
            return o -> ByteBuffer.wrap(Objects.requireNonNull(serializer.serialize(o)));
        }
    }
}