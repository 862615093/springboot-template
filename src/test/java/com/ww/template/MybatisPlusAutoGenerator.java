package com.ww.template;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.util.HashMap;

/**
 * @author weiwang127
 */
public class MybatisPlusAutoGenerator {
    public static void main(String[] args) {
        String basePath = "D:\\work-space\\my-project\\springboot-template\\src\\main\\java\\";
        FastAutoGenerator.create(
                "jdbc:mysql://127.0.0.1:3306/zfda?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull",
                "root",
                "123456")
                .globalConfig(builder -> {
                    builder.author("iflytek") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .outputDir(basePath); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.ww.template") // 设置父包名
//                            .moduleName("zljs") // 设置父包模块名
                            .pathInfo(new HashMap<OutputFile, String>() {{
                                put(OutputFile.entity, basePath + "com/ww/template/entity");
                                put(OutputFile.service, basePath + "com/ww/template/service");
                                put(OutputFile.serviceImpl, basePath + "com/ww/template/service/impl");
                                put(OutputFile.mapper, basePath + "com/ww/template/mapper");
                                put(OutputFile.xml, "D:\\work-space\\my-project\\springboot-template\\src\\main\\" + "resources/mapper");
                            }}); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder
                            .addInclude("zfda_training_learning_police") // 设置需要生成的表名
//                            .addTablePrefix("t_")
                            .entityBuilder().enableLombok();
                    ; // 设置过滤表前缀
                })
                .templateEngine(new VelocityTemplateEngine()) // 默认的是Velocity引擎模板
                .execute();
    }
}
