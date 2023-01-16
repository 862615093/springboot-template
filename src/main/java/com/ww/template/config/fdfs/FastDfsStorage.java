package com.ww.template.config.fdfs;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileNameUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * @author zhiyang7
 */
@Slf4j
@Getter
public class FastDfsStorage implements ObjectStorage {
    private final TrackerClient trackerClient;
    private final String bucketName;

    public FastDfsStorage(FastDfsConfig config) {
        this.bucketName = config.getBucketName();
        Properties properties = new Properties();
        properties.setProperty("fastdfs.charset", config.getCharset());
        properties.setProperty("fastdfs.connect_timeout_in_seconds", config.getConnectTimeout());
        properties.setProperty("fastdfs.network_timeout_in_seconds", config.getNetworkTimeout());
        properties.setProperty("fastdfs.http_anti_steal_token", config.getHttpAntiStealToken());
        properties.setProperty("fastdfs.http_secret_key", config.getHttpSecretKey());
        properties.setProperty("fastdfs.http_tracker_http_port", config.getHttpTrackerHttpPort());
        properties.setProperty("fastdfs.tracker_servers", config.getTrackerServers());
        if (config.getConnectionPool() != null) {
            properties.setProperty("fastdfs.connection_pool.enabled", config.getConnectionPool().getEnabled());
            properties.setProperty("fastdfs.connection_pool.max_count_per_entry", config.getConnectionPool().getMaxCountPerEntry());
            properties.setProperty("fastdfs.connection_pool.max_idle_time", config.getConnectionPool().getMaxIdleTime());
            properties.setProperty("fastdfs.connection_pool.max_wait_time_in_ms", config.getConnectionPool().getMaxWaitTimeInMs());
        }
        try {
            ClientGlobal.initByProperties(properties);
        } catch (IOException | MyException e) {
            throw new RuntimeException(e);
        }
        trackerClient = new TrackerClient();
    }

    private StorageClient buildStorageClient() throws IOException {
        TrackerServer trackerServer = trackerClient.getTrackerServer();
        return new StorageClient(trackerServer);
    }

    @Override
    public String putObject(String objectName, byte[] content) throws IOException {
        StorageClient storageClient = buildStorageClient();
        try {
            String[] parts = storageClient.upload_file(bucketName, content, FileNameUtil.extName(objectName), null);
            return parts != null ? parts[1] : null;
        } catch (MyException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public String putObject(String objectName, File file) throws IOException {
        return putObject(objectName, Files.readAllBytes(file.toPath()));
    }

    @Override
    public String putObject(String objectName, InputStream is) throws IOException {
        return putObject(objectName, IoUtil.readBytes(is));
    }

    @Override
    public byte[] getObject(String objectName) throws IOException {
        StorageClient storageClient = buildStorageClient();
        try {
            return storageClient.download_file(bucketName, objectName, 0L, 0L);
        } catch (MyException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public File getObjectToFile(String objectName) throws IOException {
        StorageClient storageClient = buildStorageClient();
        try {
            Path tempFile = Files.createTempFile("fastdfs_tmp_", null);
            int success = storageClient.download_file(bucketName, objectName, tempFile.toFile().getAbsolutePath());
            if(success != 0) {
                throw new IOException("Download failed:" + objectName);
            }
            return tempFile.toFile();
        } catch (MyException e) {
            log.error(e.getMessage(), e);
            throw new IOException("Download failed:" + objectName);
        }
    }

    @Override
    public boolean deleteObject(String objectName) throws IOException {
        StorageClient storageClient = buildStorageClient();
        try {
            return storageClient.delete_file(bucketName, objectName) == 0;
        } catch (MyException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }
}