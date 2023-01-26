package com.ww.template.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: jianye6
 * @Description:
 * @Version: 1.0.0
 * @Date: 2023/01/13 17:22
 */
@Slf4j
public class CommonUtil {

    /**
     * EXCEL导出条数限制
     */
    public final static int MAX_ROW_LENGTH = 50000;

    /**
     * 单个excel导出数量
     */
    public final static int ONE_PAGE_ROW_LENGTH = 50000;

    /**
     * XLS文件格式
     */
    public static final String EXCEL_PATH_NAME = "/导出excel压缩包";

    /**
     * 文件长度
     */
    private final static int FILE_LEN = 50;

    /**
     * rate
     */
    public static final Integer FILE_MERGE_HTTP_RATE = 8000;

    /**
     * XLS文件格式
     */
    private static final String STR_XLS = ".xls";

    /**
     * ZIP文件格式
     */
    private static final String STR_ZIP = ".zip";

    /**
     * cpu数
     */
    private static int cpu = Runtime.getRuntime().availableProcessors();
    /**
     * 线程池
     */
    private static final ExecutorService executorService = ThreadUtil.newExecutor(2 * cpu, 2 * cpu, Integer.MAX_VALUE);

    /**
     * silk 语音头文件
     */
    private static final String WECHAR_FILE_CHARS = "!SILK_V3";

    // 字符串
    public final static String str = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * 通过路径获取文件名
     *
     * @param path 文件路径或url
     * @return java.lang.String
     */
//    public static String getFileNameByPath(String path) {
//        if (HttpUtil.isHttp(path)) {
//            String url = URLUtil.decode(path);
//            String name = Arrays.stream(StrUtil.split(FileUtil.getName(url), "&"))
//                    .filter(e -> StrUtil.isNotBlank(FileUtil.extName(e))).findFirst().orElse(IdUtil.simpleUUID() + ".wav");
//            // 替换非法字符
//            return name.replaceAll("[^\\w.-]", "");
//
//        }
//        String fileName = FileUtil.getName(path);
//        return StrUtil.isBlank(FileUtil.extName(fileName)) ? fileName.concat(".wav") : fileName;
//    }

    /**
     * 将文件流写入response
     *
     * @param path     文件路径或url
     * @param response response
     */
    public static void writeToResponse(String path, HttpServletResponse response) {
        File pathFile = new File(path);
        ServletOutputStream sos = null;
        try {
            String strFileName = new String(pathFile.getName().getBytes("GB18030"), "ISO8859_1");
            if (path.contains(STR_XLS)) {
                strFileName= strFileName.concat(STR_XLS);
            } else {
                strFileName= strFileName.concat(STR_ZIP);
            }
            // 设置response的Header
            response.addHeader("Content-Disposition",
                    "attachment; filename=\"" + strFileName + "\"");
            response.addHeader("Content-Length", "" + pathFile.length());
            sos = response.getOutputStream();
            sos.write(FileUtil.readBytes(pathFile));
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        } finally {
            try {
                if(sos!=null){
                    sos.close();
                }
            } catch (IOException e) {
                log.error(e.getMessage(),e);
            }
            FileUtil.del(pathFile);
            FileUtil.del(pathFile.getParentFile());
        }
    }


    public static void exportExcel(HttpServletResponse response, String[] headers, String fileName, List<String[]> data) {
        //设置最大导出行数
        int rowLength = data.size();
        if (rowLength > MAX_ROW_LENGTH) {
            rowLength = MAX_ROW_LENGTH;
        }
        exportExcel(response, headers, fileName, data, rowLength);
    }

    public static void exportExcelNotLimit(HttpServletResponse response, String[] headers, String fileName, List<String[]> data) {
        //设置最大导出行数
        int rowLength = data.size();
        exportExcel(response, headers, fileName, data, rowLength);
    }


    /**
     * 导出多个sheet Excel表格供客户端下载
     *
     * @param headers  表头
     */
    public static void setWorkbook(HSSFWorkbook workbook, String[] headers, List<String[]> data, int sheetNumber) {
        getSheets(workbook,headers, data, data.size(), sheetNumber);
    }


    /**
     * 导出多个sheet Excel表格供客户端下载
     *
     * @param response HttpServletResponse
     */
    public static void exportMultipleSheetExcel(HttpServletResponse response, HSSFWorkbook workbook, String fileName,
                                                int rowLength) {

        //设置response并获得网络输出流
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=\"" + formatFileName(fileName) + "\"");
        ServletOutputStream servletout = null;
        try {
            servletout = response.getOutputStream();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        //写入操作，到输出流out
        try {
            workbook.write(servletout);
            servletout.close();
        } catch (IOException ex) {
            ex.getStackTrace();
        }
    }

    private static HSSFWorkbook getSheets(HSSFWorkbook workbook,String[] headers, List<String[]> dataset, int rowLength, int sheetNumber) {
        // 声明一个工作薄
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet(String.format("%s%s","sheet",sheetNumber));
        // 设置表格默认列宽度为30个字节
        sheet.setDefaultColumnWidth(30);
        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();

        //设置表格数据居中
        //style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setAlignment(HorizontalAlignment.CENTER);

        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.toHSSFColor(new HSSFColor()).getIndex());
        font.setFontHeightInPoints((short) 12);
        //生成一个文本格式
        HSSFDataFormat format = workbook.createDataFormat();
        // 把字体应用到当前的样式
        style.setFont(font);
        style.setDataFormat(format.getFormat("@"));
        // 往Excel写标题行
        HSSFRow row = sheet.createRow(0);
        //循环取出dataSet的数据填入表格
        for (int num = 0; num < rowLength; num++) {
            row = sheet.createRow(num);
            for (int i = 0; i < headers.length; i++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellStyle(style);
                HSSFRichTextString text = new HSSFRichTextString(dataset.get(num)[i]);
                cell.setCellValue(text);
            }

        }
        return workbook;
    }


    /**
     * 导出Excel表格供客户端下载
     *
     * @param response HttpServletResponse
     * @param headers  表头
     * @param dataset  数据集合，没个元素为一行，是数组形式
     */
    public static void exportExcel(HttpServletResponse response, String[] headers, String fileName,
                                   List<String[]> dataset, int rowLength) {

        //设置response并获得网络输出流
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=\"" + formatFileName(fileName) + "\"");
        ServletOutputStream servletout = null;
        try {
            servletout = response.getOutputStream();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet("sheet1");
        // 设置表格默认列宽度为30个字节
        sheet.setDefaultColumnWidth(30);
        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();

        //设置表格数据居中
        //style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setAlignment(HorizontalAlignment.CENTER);

        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.toHSSFColor(new HSSFColor()).getIndex());
        font.setFontHeightInPoints((short) 12);
        //生成一个文本格式
        HSSFDataFormat format = workbook.createDataFormat();
        // 把字体应用到当前的样式
        style.setFont(font);
        style.setDataFormat(format.getFormat("@"));
        // 往Excel写标题行
        HSSFRow row = sheet.createRow(0);
        //循环取出dataSet的数据填入表格
        for (int num = 0; num < rowLength; num++) {
            row = sheet.createRow(num);
            for (int i = 0; i < headers.length; i++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellStyle(style);
                HSSFRichTextString text = new HSSFRichTextString(dataset.get(num)[i]);
                cell.setCellValue(text);
            }
        }
        //写入操作，到输出流out
        try {
            workbook.write(servletout);
            servletout.close();
        } catch (IOException ex) {
            log.error(ex.getMessage(),ex);
        }finally {
            try {
                workbook.close();
                servletout.close();
            } catch (IOException e) {
                log.error(e.getMessage(),e);
            }
        }
    }

    /**
     * 导出Excel表格供客户端下载
     *
     * @param headers 表头
     * @param dataset 数据集合，没个元素为一行，是数组形式
     */
    public static String exportExcel(String[] headers, String tmpPath,
                                     List<String[]> dataset, int rowlength) {
        File file = new File(tmpPath);
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(),e);
        }
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet("sheet1");
        // 设置表格默认列宽度为30个字节
        sheet.setDefaultColumnWidth(30);
        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();

        //设置表格数据居中
        //style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        style.setAlignment(HorizontalAlignment.CENTER);

        // 生成一个字体
        HSSFFont font = workbook.createFont();
        font.setColor(HSSFColor.toHSSFColor(new HSSFColor()).getIndex());
        font.setFontHeightInPoints((short) 12);
        //生成一个文本格式
        HSSFDataFormat format = workbook.createDataFormat();
        // 把字体应用到当前的样式
        style.setFont(font);
        style.setDataFormat(format.getFormat("@"));
        // 往Excel写标题行
        HSSFRow row = sheet.createRow(0);
        //循环取出dataSet的数据填入表格
        for (int num = 0; num < rowlength; num++) {
            row = sheet.createRow(num);
            for (int i = 0; i < headers.length; i++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellStyle(style);
                HSSFRichTextString text = new HSSFRichTextString(dataset.get(num)[i]);
                cell.setCellValue(text);
            }
        }
        //写入操作，到输出流out
        try {
            workbook.write(output);
            if(output!=null){
                output.close();
            }
        } catch (IOException ex) {
            log.error(ex.getMessage(),ex);
        }finally {
            try{
                workbook.close();
            }catch (IOException e){
                log.error(e.getMessage(),e);
            }
        }
        return tmpPath;
    }


    /**
     * 格式化文件名.
     *
     * @param fileName 文件名
     * @return String 格式化的文件名
     */
    public final static String formatFileName(String fileName) {
        String buildFileName = fileName;

        if (fileName == null) {
            buildFileName = "Excel";
        } else if (fileName.length() > FILE_LEN) {
            buildFileName = fileName.substring(0, FILE_LEN - 1) + "...";
        }
        Date dt = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String strFileName = buildFileName + "-" + sdf.format(dt) + STR_XLS;
        try {
            strFileName = new String(strFileName.getBytes("GB18030"), "ISO8859_1");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        return strFileName;
    }

    /**
     * 获取filemanager下载url
     *
     * @param id       唯一标识
     * @param type     类型
     * @param download filemanager下载url
     * @return java.lang.String
     * @author yongzhang20
     * @date 11:13 2021/4/26
     */
    public static String getDownloadUrl(String id, String type, String download) {
        return StrUtil.format("{}?id={}&storeType={}", download, id, type);
    }

    /**
     * 计算分数
     *
     * @param number 小数
     * @return java.lang.String
     * @author yongzhang20
     * @date 14:14 2021/4/26
     */
    public static float caculatorScore(float number) {
        return NumberUtil.round(NumberUtil.div(NumberUtil.add(number, 1), 2) * 1000, 2).floatValue();
    }

    /**
     * 多线程删除文件
     *
     * @param paths 文件目录集合
     * @author yongzhang20
     * @date 18:33 2021/4/16
     */
    public static void del(List<String> paths) {
        for (String path : paths) {
            CommonUtil.del(path);
        }
    }

    /**
     * 多线程删除文件
     *
     * @param files 文件对象集合
     * @author yongzhang20
     * @date 18:33 2021/4/16
     */
    public static void delFiles(List<File> files) {
        for (File file : files) {
            CommonUtil.del(file);
        }
    }

    /**
     * 多线程删除文件
     *
     * @param path 文件目录
     * @author yongzhang20
     * @date 18:33 2021/4/16
     */
    public static void del(String path) {
        executorService.execute(() -> {
            FileUtil.del(path);
        });
    }

    /**
     * 多线程删除文件
     *
     * @param file 文件对象
     * @author yongzhang20
     * @date 18:33 2021/4/16
     */
    public static void del(File file) {
        executorService.execute(() -> {
            FileUtil.del(file);
        });
    }

    /**
     * 从尾部替换0字符串
     *
     * @param str 字符串
     * @return java.lang.String
     * @author yongzhang20
     * @date 19:25 2021/7/7
     */
    public static String replaceZeroSuffix(String str) {
        if (StrUtil.isBlank(str)) {
            return StrUtil.EMPTY;
        }
        // 判断是否是重庆市公安局，是的话，返回前两位。
        if ("500000000000".equals(str)) {
            return "50";
        }
        //去掉多余的0
        String s = str.replaceAll("0+?$", "");
        if (s.length() % 2 != 0) {
            return s + "0";
        } else {
            return s;
        }
//        return str.replaceAll("0+?$", "");
    }

    /**
     * 根据文件地址将文件转为字节数组
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    private static byte[] readFileBytes(String filePath) throws IOException {
        if (org.springframework.util.StringUtils.isEmpty(filePath)) {
            throw new NullPointerException("无效的文件路径");
        }
        File file = new File(filePath);
        ByteArrayOutputStream bos =null;
        BufferedInputStream in = null;
        FileInputStream fileInputStream=null;
        try{
            bos = new ByteArrayOutputStream((int) file.length());
            fileInputStream=new FileInputStream(file);
            in = new BufferedInputStream(fileInputStream);
            int bufSize = 1024;
            byte[] buffer = new byte[bufSize];
            int len = 0;
            while (-1 != (len = in.read(buffer, 0, bufSize))) {
                bos.write(buffer, 0, len);
            }
            return bos.toByteArray();
        }catch (IOException e) {
            log.error(e.getMessage(), e);
        }finally {

            if(bos!=null){
                try{
                    bos.close();
                }catch (IOException e){
                    log.error(e.getMessage(),e);
                }
            }
            if (fileInputStream!=null){
                try{
                    fileInputStream.close();
                }catch (IOException e){
                    log.error(e.getMessage(),e);
                }
            }
            if (in!=null) {
                try{
                    in.close();
                }catch (IOException e){
                    log.error(e.getMessage(),e);
                }
            }

        }
        return null;
    }

    public static String getMacInfo(HttpServletRequest request) {
        //获取ip地址
        String macInfo = null;
        try {
            String ip = request.getRemoteAddr();
            macInfo = getTestMac(ip);
        } catch (Exception e) {
            log.error("获取mac地址失败");
            return null;
        }
        return macInfo;
    }

    // 从类unix机器上获取mac地址
    public static String getMac(String ip) throws IOException {
        String mac = StringUtils.EMPTY;
        if (ip != null) {
            try {
                Process process = Runtime.getRuntime().exec("arp -A " + ip);
                InputStreamReader ir = new InputStreamReader(process.getInputStream());
                LineNumberReader input = new LineNumberReader(ir);
                String line;
                StringBuffer s = new StringBuffer();
                while ((line = input.readLine()) != null) {
                    s.append(line);
                }
                mac = s.toString();
                if (StringUtils.isNotBlank(mac)) {
                    mac = mac.substring(mac.indexOf(":") - 2, mac.lastIndexOf(":") + 3);
                }
                return mac;
            } catch (Exception e) {
                log.error(e.getMessage(),e);
            }
        }
        return mac;
    }

    // 从windows机器上获取mac地址
    public static String getMacInWindows(final String ip) {
        String result = "";
        String[] cmd = {"cmd", "/c", "ping " + ip};
        String[] another = {"cmd", "/c", "ipconfig -all"};
        // 获取执行命令后的result
        String cmdResult = callCmd(cmd, another);
        // 从上一步的结果中获取mac地址
        result = filterMacAddress(ip, cmdResult, "-");
        return result;
    }

    // 命令执行
    public static String callCmd(String[] cmd, String[] another) {
        String result = "";
        String line = "";
        try {
            Runtime rt = Runtime.getRuntime();
            // 执行第一个命令
            Process proc = rt.exec(cmd);
            proc.waitFor();
            // 执行第二个命令
            proc = rt.exec(another);
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader(is);
            while ((line = br.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return result;
    }

    // 获取mac地址
    public static String filterMacAddress(final String ip, final String sourceString, final String macSeparator) {
        String result = "";
        String regExp = "((([0-9,A-F,a-f]{1,2}" + macSeparator + "){1,5})[0-9,A-F,a-f]{1,2})";
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(sourceString);
        while (matcher.find()) {
            result = matcher.group(1);
            // 因计算机多网卡问题，截取紧靠IP后的第一个mac地址
            int num = sourceString.indexOf(ip) - sourceString.indexOf(": " + result + " ");
            if (num > 0 && num < 300) {
                break;
            }
        }
        return result;
    }

    public static String getTestMac(String ip) throws UnknownHostException, SocketException {
        String macAddress = "";
        String line;
        // 如果为127.0.0.1,则获取本地MAC地址
        if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip) || "localhost".equals(ip) || ip.equals(getLocalIp())) {    //注意getLocalIp().equals(ip)，由于else中的方法获取不到本机ip对应的的mac，所以放到这里，调用的getLocalIp()
            InetAddress inetAddress = InetAddress.getLocalHost();
            byte[] mac = NetworkInterface.getByInetAddress(inetAddress)
                    .getHardwareAddress();
            // 下面代码是把mac地址拼装成String
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                if (i != 0) {
                    sb.append("-");
                }
                // mac[i] & 0xFF 是为了把byte转化为正整数
                String s = Integer.toHexString(mac[i] & 0xFF);
                sb.append(s.length() == 1 ? 0 + s : s);
            }
            // 把字符串所有小写字母改为大写成为正规的mac地址并返回
            macAddress = sb.toString().trim().toUpperCase();
            return macAddress;
        } else {    // 获取非本地IP的MAC地址

            //由于我测试的时候，使用"nbtstat -A " + ip获取不到一些Mac，
            //所以换成"arp -A " + ip
            try {
                Process p = Runtime.getRuntime().exec("arp -A " + ip);
                InputStreamReader isr = new InputStreamReader(p.getInputStream());
                BufferedReader br = new BufferedReader(isr);
                while ((line = br.readLine()) != null) {
                    if (line != null) {
                        int index = line.indexOf(ip);
                        if (index != -1) {
                            macAddress = line.substring(index + ip.length() + 10, index + ip.length() + 27).trim().toUpperCase();
                        }
                    }
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace(System.out);
            }
            return macAddress;
        }
    }

    public static String getLocalIp() {
        try {
            Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip;
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
                Enumeration addresses = netInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    ip = (InetAddress) addresses.nextElement();
                    if (ip != null && ip instanceof Inet4Address && !"127.0.0.1".equals(ip.getHostAddress())) {
                        return ip.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 支持特殊字符
     * @param str
     * @return
     */
    public static String escapeStr(String str){
        //！@#￥%……&
        if(StrUtil.isNotEmpty(str)){
            str = str.replaceAll("\\\\","\\\\\\\\");
            str = str.replaceAll("_","\\\\_");
            str = str.replaceAll("%","\\\\%");
            str = str.replaceAll("!","\\\\!");
            str = str.replaceAll("@","\\\\@");
            str = str.replaceAll("#","\\\\#");
            str = str.replaceAll("￥","\\\\￥");
            str = str.replaceAll("……","\\\\……");
            str = str.replaceAll("&","\\\\&");
        }
        return str;
    }

    public static String escapeStrContainSpace(String str){
        //！@#￥%……&
        if(StrUtil.isNotEmpty(str)){
            str=str.trim();
            str = str.replaceAll("\\\\","\\\\\\\\");
            str = str.replaceAll("_","\\\\_");
            str = str.replaceAll("%","\\\\%");
            str = str.replaceAll("!","\\\\!");
            str = str.replaceAll("@","\\\\@");
            str = str.replaceAll("#","\\\\#");
            str = str.replaceAll("￥","\\\\￥");
            str = str.replaceAll("……","\\\\……");
            str = str.replaceAll("&","\\\\&");

        }
        return str;
    }

}
