package com.ww.template.dto.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@ApiModel(value = "TrainLearnSaveParam对象", description = "培训学习入参")
public class TrainLearnSaveParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("培训类型（1-法条规范，2-执法视频）")
    private String trainingLearningType;

    @ApiModelProperty("执法问题编码")
    private String problemCode;

    @ApiModelProperty("上传人")
    private String uploadUserName;

    @ApiModelProperty("上传人ID")
    private String uploadUserId;

    @ApiModelProperty("执法文件")
    private MultipartFile[] files;
}
