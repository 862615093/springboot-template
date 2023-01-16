package com.ww.template.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ApiModel(value = "TrainLearnVo对象出参", description = "培训学习列表对象")
public class TrainLearnVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("培训学习ID")
    private String id;

    @ApiModelProperty("培训学习执法类型")
    private String trainingLearningType;

    @ApiModelProperty("培训学习执法文件名")
    private String fileName;

    @ApiModelProperty("培训学习执法文件上传人")
    private String uploadUserName;

    @ApiModelProperty("培训学习执法文件总浏览数")
    private String totalLearnCount;

    @ApiModelProperty("培训学习执法文件总下载数")
    private String totalDownloadCount;

    @ApiModelProperty("培训学习执法文件总时长")
    private long totalLearnTime;
}
