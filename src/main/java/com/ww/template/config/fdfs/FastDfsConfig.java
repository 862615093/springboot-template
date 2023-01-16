package com.ww.template.config.fdfs;

import com.ww.template.utils.ObjectStorageUtil;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhiyang7
 */
@Configuration
//@ConditionalOnProperty(prefix = "storage", name = "mode", havingValue = "fastdfs")
//@ConfigurationProperties(prefix = "storage.fastdfs")
@Data
public class FastDfsConfig {

    private String bucketName = "group1";

    private String trackerServers = "172.30.12.166:22122";

    private String connectTimeout = "7";

    private String networkTimeout = "7";

    private String charset = "UTF-8";

    private String httpAntiStealToken = "false";

    private String httpSecretKey = "";

    private String httpTrackerHttpPort = "";

    private ConnectionPool connectionPool;

    @Data
    public static class ConnectionPool {
        private String enabled = "true";
        private String maxCountPerEntry = "500";
        private String maxIdleTime = "3600";
        private String maxWaitTimeInMs = "1000";
    }

    @Bean
    public ObjectStorage objectProvider() {
        FastDfsStorage storage = new FastDfsStorage(this);
        ObjectStorageUtil.setObjectStorage(storage);
        return storage;
    }
}