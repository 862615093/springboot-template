package com.ww.template;

import com.ww.template.utils.ObjectStorageUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@SpringBootTest
public class FdfsDemo {

    @Test
    public void t() throws IOException {
//        String testName = ObjectStorageUtil.putObject("1.jpg", new File("C:\\Users\\weiwang127\\Desktop\\1.jpg"));
//        System.out.println("testName=" + testName);

        System.out.println("========测试下载=========");
//        File objectToFile = ObjectStorageUtil.getObjectToFile("M00/00/00/rB4j3GNJG6iAKEj_AAXnWmzuiZk271.jpg");
//        IoUtil.copy(new FileInputStream(objectToFile),
//                new FileOutputStream("C:\\Users\\weiwang127\\Desktop\\2.jpg"));
        byte[] object = ObjectStorageUtil.getObject("M00/00/00/rB4j3GNJG6iAKEj_AAXnWmzuiZk271.jpg");
        System.out.println("========>" + object.length);
        System.out.println("下载成功。。。");

//        System.out.println("========测试上传=========");
//        String b64str = "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";
//        byte[] bytes = Base64.decodeBase64(b64str);
//        String s = ObjectStorageUtil.putObject("帅.jpg", bytes);
//        System.out.println("上传成功,s--------->" + s);

    }

    @Test
    public void loadFile() {
        RestTemplate restTemplate = new RestTemplate();
        byte[] bytes = restTemplate.getForObject("http://172.30.33.215:8086/file/09ac017d6ab5491d82e116a127a18149", byte[].class);

        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = new File("D://abc.pdf");
        if (!file.getParentFile().exists()) {
            //文件夹不存在 生成
            file.getParentFile().mkdirs();
        }
        try {
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}