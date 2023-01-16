package com.ww.template.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class UploadDictionaryData {

    @ApiModelProperty("1级指标项名称")
    private String indexName1;

    @ApiModelProperty("2级指标项名称")
    private String indexName2;

    @ApiModelProperty("3级指标项名称")
    private String indexName3;

    @ApiModelProperty("指标项方式：系统自动、人工添加")
    private String acquireWay;

    @ApiModelProperty("分数")
    private Integer indexValue;

    @ApiModelProperty("指标项描述")
    private String indexDec;
}
