package com.ww.template.utils;

import lombok.extern.slf4j.Slf4j;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Slf4j
public class FileUtil {

    //根据文件路径删除文件
    public static void deleteFile(String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public static void deleteFile(String... path) {
        try {
            for (int i = 0; i < path.length; i++) {
                File file = new File(path[i]);
                if (file.exists()) {
                    file.delete();
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    public static void byteArrayConvertToFile(byte[] bfile, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            file = new File(fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            log.error("", e);
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    log.error("", e1);
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    log.error("", e1);
                }
            }
        }
    }

    public static void get(String path, String filePath) {
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(path);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(10000);
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf8");
            urlConnection.connect();
            File file = new File(filePath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }

            try (InputStream inputStream = urlConnection.getInputStream();
                 BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                 FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                 BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream)) {

                byte[] buf = new byte[4096];
                int length = bufferedInputStream.read(buf);
                while (-1 != length) {
                    bufferedOutputStream.write(buf, 0, length);
                    length = bufferedInputStream.read(buf);
                }
            }
        } catch (Exception e) {
            log.error("", e);
        } finally {
            if (null != urlConnection) {
                urlConnection.disconnect();
            }
        }
    }

    /**
     * file转byte数组
     *
     * @param tradeFile
     * @return
     */
    public static byte[] file2byte(File tradeFile) {
        byte[] buffer = null;
        try (FileInputStream fis = new FileInputStream(tradeFile)) {
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                byte[] b = new byte[1024];
                int n;
                while ((n = fis.read(b)) != -1) {
                    bos.write(b, 0, n);
                }
                buffer = bos.toByteArray();
            }
        } catch (IOException e) {
            log.error("", e);
        }
        return buffer;
    }

}
