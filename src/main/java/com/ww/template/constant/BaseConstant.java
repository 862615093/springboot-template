package com.ww.template.constant;

/**
 * 通用常量
 *
 * @author weiwang127
 */
public class BaseConstant {

    /**
     * 逗号","
     */
    public static final String COMMA = ",";

    /**
     * 横杠"/""
     */
    public static final String BAR = "/";

    /**
     * 等号"="
     */
    public static final String EQUAL = "=";

    /**
     * "&"
     */
    public static final String AND = "&";

    /**
     * 问号"?"
     */
    public static final String QUESTION = "?";

    /**
     * 冒号
     */
    public static final String COLON = ":";

    /**
     * 下划线"_"
     */
    public static final String UNDER_LINE = "_";

    /**
     * 日期年月日格式化
     */
    public static final String NORM_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 空字符串
     */
    public final static String EMPTY_NULL = null;

    /**
     * 初始化线程前缀名
     */
    public static final String CACHE_THREAD_PREFIX_NAME = "thread-call-runner";

    /**
     * 初始化线程存活时间
     */
    public static final Long THREAD_KEEP_ALIVE_TIME = 0L;

    /**
     * 成功返回值
     */
    public static final Integer SUCCESS_CODE = 200;

    /**
     * API网关通过header透传的userId
     */
    public static final String USER_ID_HEADER = "userId";

    /**
     * API网关通过header透传的loginName
     */
    public static final String LOGIN_NAME_HEADER = "loginName";

    /**
     * token令牌
     */
    public static final String TOKEN_HEADER = "x-token";

    /**
     * word 文件
     */
    public static final String DOCX = ".docx";

    /**
     * pdf文件
     */
    public static final String PDF = ".pdf";

    /**
     * png文件
     */
    public static final String PNG = ".png";

    /**
     * data
     */
    public static final String DATA = "data";

    /**
     * 笔录已签字
     */
    public static final String SIGNED = "3";

    /**
     * 笔录未签字
     */
    public static final String NOT_SIGNED = "1";
}