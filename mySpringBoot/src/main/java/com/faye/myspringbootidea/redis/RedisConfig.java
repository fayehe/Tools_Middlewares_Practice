package com.faye.myspringbootidea.redis;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

@Configuration
@EnableCaching //来开启redis缓存。
public class RedisConfig extends CachingConfigurerSupport {
    // 生成Redis的key， 例如：
    // [@Cacheable("testRedisKey") -> testRedisKey::com.faye.myspringbootidea.web.JsonTestController_getCategory]
    @Bean
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                StringBuilder sb = new StringBuilder();
                return sb.append(target.getClass().getName())
                        .append("_")
                        .append(method.getName())
                        .toString();
            }
        };
    }
}
