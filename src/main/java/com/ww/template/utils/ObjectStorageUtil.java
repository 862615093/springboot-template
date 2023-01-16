package com.ww.template.utils;

import com.ww.template.config.fdfs.ObjectStorage;
import org.springframework.lang.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 对象存储工具类
 *
 * @author zhiyang7
 */
public class ObjectStorageUtil {
    private static ObjectStorage objectStorage;

    public static void setObjectStorage(ObjectStorage objectStorage) {
        ObjectStorageUtil.objectStorage = objectStorage;
    }

    /**
     * 获取存储桶名称
     *
     * @return 名称
     */
    public static String getBucketName() {
        return objectStorage.getBucketName();
    }

    /**
     * 存放对象
     *
     * @param objectName 对象名称
     * @param content    文件内容
     * @return 上传后文件名
     */
    @Nullable
    public static String putObject(String objectName, byte[] content) throws IOException {
        return objectStorage.putObject(objectName, content);
    }

    /**
     * 存放对象
     *
     * @param file 文件
     * @return 上传后文件名
     */
    @Nullable
    public static String putObject(String objectName, File file) throws IOException {
        return objectStorage.putObject(objectName, file);
    }

    /**
     * 存放对象
     *
     * @param objectName 对象名称
     * @param is         文件流
     * @return 上传后文件名
     */
    @Nullable
    public static String putObject(String objectName, InputStream is) throws IOException {
        return objectStorage.putObject(objectName, is);
    }

    /**
     * 下载对象
     *
     * @param objectName 对象名称
     * @return 字节数组
     * @throws IOException 异常
     */
    @Nullable
    public static byte[] getObject(String objectName) throws IOException {
        return objectStorage.getObject(objectName);
    }

    @Nullable
    public static File getObjectToFile(String objectName) throws IOException {
        return objectStorage.getObjectToFile(objectName);
    }

    /**
     * 删除对象
     *
     * @param objectName 对象名称
     * @return 是否删除成功
     */
    public static boolean deleteObject(String objectName) throws IOException {
        return objectStorage.deleteObject(objectName);
    }

}