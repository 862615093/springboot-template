package com.ww.template.config;

import com.ww.template.utils.ThreadPoolUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 常用配置项
 *
 * @author weiwang127
 */
@Configuration
public class CommonConfig {

    @Bean
    public ThreadPoolUtils threadPoolUtils(){
        return new ThreadPoolUtils(ThreadPoolUtils.ThreadEnum.FixedThread);
    }
}