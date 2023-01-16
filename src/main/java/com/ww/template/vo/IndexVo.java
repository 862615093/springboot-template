package com.ww.template.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@ApiModel(value = "指标项对象", description = "指标项对象")
public class IndexVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("模块")
    private String model;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("指标项编码")
    private String indexCode;

    @ApiModelProperty("指标项名称")
    private String indexName;

    @ApiModelProperty("扣分")
    private Integer indexValue;

    @ApiModelProperty("指标项方式：系统自动、人工添加")
    private String acquireWay;

    @ApiModelProperty("父级指标项编码")
    private String parentCode;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;
}
