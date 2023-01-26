package com.ww.template.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.ww.template.config.CustomCellWriteWeightConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

/**
 * @Author: jianye6
 * @Description:
 * @Version: 1.0.0
 * @Date: 2023/01/13 17:22
 */
@Slf4j
public class EasyExcelUtils {

    /**
     * cpu数
     */
    private static int cpu = Runtime.getRuntime().availableProcessors();
    /**
     * 线程池
     * N  代表的时cpu 核数
     * cpu 密集型 建议 为 N+1
     * IO  密集型 建议为2N
     */
    private static final ExecutorService executorService = ThreadUtil.newExecutor(2 * cpu, 2 * cpu, Integer.MAX_VALUE);

    /**
     * EXCEL导出条数限制
     */
    public final static int MAX_ROW_LENGTH = 50000;
    /**
     * @title exportExcel
     * @description  以流的形式导出到前端
     * @param response:
     * @param fileName:  文件名称
     * @param list:   数据集合
     * @param clazz:  类class
     * @throws
     */
    public static  <T>  void exportExcel(HttpServletResponse response , String fileName, List<T> list, Class<T> clazz) throws IOException {
        try{
            //添加响应头信息
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName1 = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName1);
            int lastIndexOf = fileName.lastIndexOf(".");
            //获取文件的后缀名 .xls
            String suffix = fileName.substring(lastIndexOf);
            String name = fileName.substring(0,lastIndexOf);
            ExcelTypeEnum excelTypeEnum;
            if(suffix.equals(ExcelTypeEnum.XLS.getValue())){
                excelTypeEnum= ExcelTypeEnum.XLS;
            }else{
                excelTypeEnum= ExcelTypeEnum.XLSX;
            }
            EasyExcel.write(response.getOutputStream(),clazz)
                    .excelType(excelTypeEnum)
                    .registerWriteHandler(setHorizontalCellStyleStrategy())
                    .registerWriteHandler(new CustomCellWriteWeightConfig())
                    .sheet(name)
                    .head(clazz)
                    .doWrite(list);
        }catch(Exception e){
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("success",false );
            map.put("code",500 );
            map.put("msg", "下载"+fileName+"文件失败");
            response.getWriter().println(JSONUtil.toJsonStr(map));
        }
    }
    /**
     * @title exportExcel
     * @description 无注解动态表头数据生成Excel
     * @param response:
     * @param fileName:
     * @param list:
     * @param heads:
     * @throws
     */
    public static void exportExcel(HttpServletResponse response , String fileName, List<List<Object>> list, List <List<String>> heads) throws IOException {
        try{
            //添加响应头信息
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName1 = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName1);
            int lastIndexOf = fileName.lastIndexOf(".");
            //获取文件的后缀名 .xls
            String suffix = fileName.substring(lastIndexOf);
            String name = fileName.substring(0,lastIndexOf);
            ExcelTypeEnum excelTypeEnum;
            if(suffix.equals(ExcelTypeEnum.XLS.getValue())){
                excelTypeEnum= ExcelTypeEnum.XLS;
            }else{
                excelTypeEnum= ExcelTypeEnum.XLSX;
            }
            EasyExcel.write(response.getOutputStream())
                    .excelType(excelTypeEnum)
                    .registerWriteHandler(setHorizontalCellStyleStrategy())
                    .registerWriteHandler(new CustomCellWriteWeightConfig())
                    .sheet(name)
                    .head(heads)
                    .doWrite(list);
        }catch(Exception e){
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("success",false );
            map.put("code",500 );
            map.put("msg", "下载"+fileName+"文件失败");
            response.getWriter().println(JSONUtil.toJsonStr(map));
        }
    }
    /**
     * @title exportFileExcel
     * @description  导出到固定的目录下
     * @param filepath:
     * @param fileName:
     * @param list:
     * @param clazz:
     * @return boolean
     * @throws
     */
    public static  <T>  boolean exportFileExcel(String filepath,String fileName, List<T> list, Class<T> clazz) throws IOException {
        int lastIndexOf = fileName.lastIndexOf(".");
        //获取文件的后缀名 .xls
        String suffix = fileName.substring(lastIndexOf);
        String name = fileName.substring(0,lastIndexOf);
        ExcelTypeEnum excelTypeEnum;
        if(suffix.equals(ExcelTypeEnum.XLS.getValue())){
            excelTypeEnum= ExcelTypeEnum.XLS;
        }else{
            excelTypeEnum= ExcelTypeEnum.XLSX;
        }
        String path=filepath+ File.separator+fileName;
        EasyExcel.write(path,clazz)
                .excelType(excelTypeEnum)
                .registerWriteHandler(setHorizontalCellStyleStrategy())
                .registerWriteHandler(new CustomCellWriteWeightConfig())
                .sheet(name)
                .head(clazz)
                .doWrite(list);
        return  true;
    }
    /**
     * @title exportFileExcel
     * @description 无注解动态表头数据生成Excel
     * @param filepath:  生成路径地址
     * @param fileName: 文件名称
     * @param list:  数据集合
     * @param heads:  文件头集合
     * @return boolean
     * @throws
     */
    public static  boolean exportFileExcel(String filepath,String fileName, List<List<Object>> list, List <List<String>> heads) throws IOException {
        int lastIndexOf = fileName.lastIndexOf(".");
        //获取文件的后缀名 .xls
        String suffix = fileName.substring(lastIndexOf);
        String name = fileName.substring(0,lastIndexOf);
        ExcelTypeEnum excelTypeEnum;
        if(suffix.equals(ExcelTypeEnum.XLS.getValue())){
            excelTypeEnum= ExcelTypeEnum.XLS;
        }else{
            excelTypeEnum= ExcelTypeEnum.XLSX;
        }
        String path=filepath+ File.separator+fileName;
        EasyExcel.write(path)
                .excelType(excelTypeEnum)
                .registerWriteHandler(setHorizontalCellStyleStrategy())
                .registerWriteHandler(new CustomCellWriteWeightConfig())
                .sheet(name)
                .head(heads)
                .doWrite(list);
        return  true;
    }
    /**
     * @title setHorizontalCellStyleStrategy
     * @description 设置Excel 头单元格样式
     * @return com.alibaba.excel.write.style.HorizontalCellStyleStrategy
     * @throws
     */
    public static HorizontalCellStyleStrategy setHorizontalCellStyleStrategy(){
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        headWriteCellStyle.setWrapped(true);
        //设置背景颜色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        //设置头字体
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short)13);
        headWriteFont.setBold(true);
        headWriteCellStyle.setWriteFont(headWriteFont);
        //设置头居中
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);

        //内容策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        //设置 水平居中
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
        return horizontalCellStyleStrategy;
    }

    /**
     * @title exportBizExcel
     * @description 针对数据量导出Excel 或者zip 包文件
     * @param response:
     * @param fileName:
     * @param list: 数据集合
     * @param clazz:  导出数据头的实体类
     * @throws
     */
    public static  <T>  void exportBizExcel(HttpServletResponse response,String fileName, List<T> list, Class<T> clazz) throws IOException {
        int total=list.size();
        if(total<=MAX_ROW_LENGTH){
            exportExcel(response,fileName.concat(ExcelTypeEnum.XLS.getValue()),list,clazz);
        }else{
            // 取整
            int roundNum =total/MAX_ROW_LENGTH;
            // 取余
            int supNum= total%MAX_ROW_LENGTH;
            int threadNum =roundNum;
            if(supNum>0){
                threadNum=roundNum+1;
            }
            final CountDownLatch latch = new CountDownLatch(threadNum);
            String newPath = FileUtil.getTmpDirPath().concat(CommonUtil.EXCEL_PATH_NAME).concat(String.valueOf(System.currentTimeMillis()));
            // 创建临时文件夹
            File newFile = FileUtil.mkdir(newPath);
            for (int i = 0; i < threadNum; i++) {
                //计算出每个线程的下载开始位置和结束位置
                int start = i * MAX_ROW_LENGTH ;
                int end = i == threadNum - 1 ? total : (i+1)*MAX_ROW_LENGTH;
                List<T> newList=list.subList(start,end);
                int finalI = i;
                executorService.execute(() -> {
                    try {
                        exportFileExcel(newFile.getAbsolutePath(),fileName.concat("-"+ finalI).concat(ExcelTypeEnum.XLS.getValue()),newList,clazz);
                        latch.countDown();
                    } catch (IOException e) {
                        log.error(e.getMessage());
                    }
                });
            }
            try{
                latch.await();
                CommonUtil.writeToResponse(ZipUtil.zip(newFile).getAbsolutePath(), response);
            }catch (InterruptedException e){
                log.error(e.getMessage());
                Thread.currentThread().interrupt();
            }
        }

    }
    /**
     * @title exportBizExcel
     * @description  动态表头 导出
     * @param response:
     * @param fileName:  文件名称
     * @param list:   数据集合
     * @param heads: 动态表头
     * @throws
     */
    public static  void exportBizExcel(HttpServletResponse response,String fileName, List<List<Object>> list, List <List<String>> heads) throws IOException {
        int total=list.size();
        if(total<=MAX_ROW_LENGTH){
            exportExcel(response,fileName.concat(ExcelTypeEnum.XLS.getValue()),list,heads);
        }else{
            // 取整
            int roundNum =total/MAX_ROW_LENGTH;
            // 取余
            int supNum= total%MAX_ROW_LENGTH;
            int threadNum =roundNum;
            if(supNum>0){
                threadNum=roundNum+1;
            }
            final CountDownLatch latch = new CountDownLatch(threadNum);
            String newPath = FileUtil.getTmpDirPath().concat(CommonUtil.EXCEL_PATH_NAME).concat(String.valueOf(System.currentTimeMillis()));
            // 创建临时文件夹
            File newFile = FileUtil.mkdir(newPath);
            for (int i = 0; i < threadNum; i++) {
                //计算出每个线程的下载开始位置和结束位置
                int start = i * MAX_ROW_LENGTH ;
                int end = i == threadNum - 1 ? total : (i+1)*MAX_ROW_LENGTH;
                List<List<Object>> newList=list.subList(start,end);
                int finalI = i;
                executorService.execute(() -> {
                    try {
                        exportFileExcel(newFile.getAbsolutePath(),fileName.concat("-"+ finalI).concat(ExcelTypeEnum.XLS.getValue()),newList,heads);
                        latch.countDown();
                    } catch (IOException e) {
                        log.error(e.getMessage());
                    }
                });
            }
            try{
                latch.await();
                CommonUtil.writeToResponse(ZipUtil.zip(newFile).getAbsolutePath(), response);
            }catch (InterruptedException e){
                log.error(e.getMessage());
                Thread.currentThread().interrupt();
            }
        }

    }

}
