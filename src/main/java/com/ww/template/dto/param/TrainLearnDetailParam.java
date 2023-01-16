package com.ww.template.dto.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "TrainLearnDetailParam对象", description = "培训学习详情查询入参")
public class TrainLearnDetailParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("查询类型: LEARNTIME-培训学习总时长详情, LEARNCOUNT-培训学习总学习次数详情, DOWNCOUNT-培训学习总下载次数详情")
    private SearchType searchType;

    @ApiModelProperty("培训学习ID")
    private String id;

    @ApiModelProperty("最近一次学习时间排序 asc-正序, desc-倒序")
    private String lastLearnTimeOrderBy;

    @ApiModelProperty("总学习时长排序")
    private String totalLearnTimeOrderBy;

    @ApiModelProperty("总学习次数排序")
    private String totalLearnCountOrderBy;

    @ApiModelProperty("总下载次数排序")
    private String totalDownloadCountOrderBy;

    @ApiModelProperty("最近一次下载时间排序")
    private String lastDownloadTimeOrderBy;

    @ApiModelProperty("培训学习新增的时间排序")
    private String createTimeOrderBy;

    public enum SearchType {
        /**
         * 培训学习总时长详情
         */
        LEARNTIME,

        /**
         * 培训学习总学习次数详情
         */
        LEARNCOUNT,

        /**
         * 培训学习总下载次数详情
         */
        DOWNCOUNT;
    }
}
