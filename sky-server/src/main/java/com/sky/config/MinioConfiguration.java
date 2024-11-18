package com.sky.config;

import com.sky.properties.MinionsProperties;
import com.sky.utils.MinionsUtil;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class MinioConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public MinionsUtil minionsUtil(MinionsProperties minionsProperties) {
        log.info("开始创建mino工具类对象：{}", minionsProperties);
        return new MinionsUtil(minionsProperties.getEndpoint(),
                minionsProperties.getAccessKeyId(),
                minionsProperties.getAccessKeySecret(),
                minionsProperties.getBucketName());
    }
}
