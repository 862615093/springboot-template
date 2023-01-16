package com.ww.template.config.fdfs;

import org.springframework.lang.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author zhiyang7
 */
public interface ObjectStorage {
    /**
     * 获取存储桶名称
     * @return 名称
     */
    String getBucketName();

    /**
     * 存放对象
     * @param objectName    对象名称
     * @param content       文件内容
     * @return 上传后文件名
     */
    @Nullable
    String putObject(String objectName, byte[] content) throws IOException;

    /**
     * 存放对象
     * @param file          文件
     * @return 上传后文件名
     */
    @Nullable
    String putObject(String objectName, File file) throws IOException;
    /**
     * 存放对象
     * @param objectName    对象名称
     * @param is            文件流
     * @return 上传后文件名
     */
    @Nullable
    String putObject(String objectName, InputStream is) throws IOException;

    /**
     * 下载对象
     * @param objectName    对象名称
     * @return 字节数组
     * @throws IOException  异常
     */
    @Nullable
    byte[] getObject(String objectName) throws IOException;

    /**
     * 下载对象
     * @param objectName    对象名称
     * @return 字节数组
     * @throws IOException  异常
     */
    @Nullable
    File getObjectToFile(String objectName) throws IOException;

    /**
     * 删除对象
     * @param objectName    对象名称
     * @return 是否删除成功
     */
    boolean deleteObject(String objectName) throws IOException;
}