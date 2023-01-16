package com.ww.template.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 字典项表
 * </p>
 *
 * @author iflytek
 * @since 2023-01-12
 */
@Getter
@Setter
@TableName("zfda_dictionary")
@ApiModel(value = "ZfdaDictionary对象", description = "字典项表")
public class ZfdaDictionary implements Serializable {

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

    @ApiModelProperty("创建ID")
    private String createId;

    @ApiModelProperty("更新人ID")
    private String updateId;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("是否生效 1:生效 0:不生效")
    private Boolean valid;


}
