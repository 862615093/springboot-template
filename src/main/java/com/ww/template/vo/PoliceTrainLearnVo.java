package com.ww.template.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value = "TrainLearnPoliceVo对象出参", description = "民警培训学习列表对象")
public class PoliceTrainLearnVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("民警培训学习ID")
    private String id;

    @ApiModelProperty("单位")
    private String orgName;

    @ApiModelProperty("民警")
    private String policeName;

    @ApiModelProperty("培训学习执法文件总浏览数")
    private String totalLearnCount;

    @ApiModelProperty("培训学习执法文件总下载数")
    private String totalDownloadCount;

    @ApiModelProperty("培训学习执法文件总时长")
    private long totalLearnTime;
}
