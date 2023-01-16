package com.ww.template.dto.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "TrainLearnViewTickParam对象", description = "培训学习增加时长入参")
public class TrainLearnViewTickParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("培训学习ID")
    private String id;

    @ApiModelProperty("培训学习民警ID")
    private String viewPoliceId;

    @ApiModelProperty("增加的秒数(默认60秒)")
    private Integer second = 60;
}
