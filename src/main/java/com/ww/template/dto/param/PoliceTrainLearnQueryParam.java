package com.ww.template.dto.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "PoliceTrainLearnQueryParam对象", description = "民警培训学习查询入参")
public class PoliceTrainLearnQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("民警")
    private String policeName;

    @ApiModelProperty("单位")
    private String orgName;

    @ApiModelProperty("总学习时长排序 asc-正序, desc-倒序")
    private String totalLearnTimeOrderBy;

    @ApiModelProperty("总学习次数排序")
    private String totalLearnCountOrderBy;

    @ApiModelProperty("总下载次数排序")
    private String totalDownloadCountOrderBy;
}
