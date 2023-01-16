package com.ww.template.enums;

public enum OperateTypeEnum {

    TOTAL_DOWNLOAD_COUNT("培训学习总下载次数"),

    TOTAL_LEARN_COUNT("培训学习总学习次数");

    String type;
    OperateTypeEnum(String type) {
        this.type = type;
    }

    public String getType(){
        return type;
    }
}