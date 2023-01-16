package com.ww.template.dto.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "TrainLearnQueryParam对象", description = "培训学习查询入参")
public class TrainLearnQueryParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("培训类型（1-法条规范，2-执法视频）")
    private String trainingLearningType;

    @ApiModelProperty("执法问题文件名称")
    private String fileName;

    @ApiModelProperty("登陆人ID")
    private String loginUserId;

    @ApiModelProperty("查询范围: 1-个人, 2-所有")
    private String searchType;

    @ApiModelProperty("总学习时长排序 asc-正序, desc-倒序")
    private String totalLearnTimeOrderBy;

    @ApiModelProperty("总学习次数排序")
    private String totalLearnCountOrderBy;

    @ApiModelProperty("总下载次数排序")
    private String totalDownloadCountOrderBy;

    @ApiModelProperty("培训学习新增的时间排序")
    private String createTimeOrderBy;
}
