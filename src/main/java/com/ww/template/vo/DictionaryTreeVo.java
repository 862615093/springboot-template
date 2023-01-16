package com.ww.template.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
@ApiModel(value = "树状字典对象", description = "树状字典对象")
public class DictionaryTreeVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("指标项编码")
    private String indexCode;

    @ApiModelProperty("指标项名称")
    private String indexName;

    @ApiModelProperty("分数")
    private Integer indexValue;

    @ApiModelProperty("指标项排序")
    private Integer indexOrder;

    @ApiModelProperty("指标项类型：加分、减分、质效分")
    private String indexType;

    @ApiModelProperty("指标项描述")
    private String indexDec;

    @ApiModelProperty("指标项单位")
    private String indexUnit;

    @ApiModelProperty("指标项方式：系统自动、人工添加")
    private String acquireWay;

    @ApiModelProperty("父级指标项编码")
    private String parentCode;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("是否生效 1:生效 0:不生效")
    private Boolean valid;

    @ApiModelProperty("子字典")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<DictionaryTreeVo> children;
}
