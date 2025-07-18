package com.project.Shopapp.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class RedisConfig {
    @Value("${spring.data.redis.host}") // Read 'spring.data.redis.host' property from application.yml
    private String redisHost;

    @Value("${spring.data.redis.port}")  // Read 'spring.data.redis.port' property from application.yml
    private int redisPort;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory(){
        RedisStandaloneConfiguration configuration =  new RedisStandaloneConfiguration(redisHost,redisPort);
        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(){
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());

        ObjectMapper redisObjectMapper = createRedisObjectMapper();
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(redisObjectMapper,Object.class);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);
        template.afterPropertiesSet();
        return template;
    }

    private ObjectMapper createRedisObjectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDateTime.class,new LocalDateTimeSerializer(DateTimeFormatter.ISO_DATE_TIME));
        module.addDeserializer(LocalDateTime.class,new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME));
        objectMapper.registerModule(module);
        return objectMapper;
    }
}
