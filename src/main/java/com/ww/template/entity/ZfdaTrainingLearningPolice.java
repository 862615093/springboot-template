package com.ww.template.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 培训学习民警表
 * </p>
 *
 * @author iflytek
 * @since 2023-01-14
 */
@Builder
@Getter
@Setter
@TableName("zfda_training_learning_police")
@ApiModel(value = "ZfdaTrainingLearningPolice对象", description = "培训学习民警表")
public class ZfdaTrainingLearningPolice implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
      private String id;

    @ApiModelProperty("培训学习主键id")
    private String trainingLearningId;

    @ApiModelProperty("民警id")
    private String policeId;

    @ApiModelProperty("民警")
    private String policeName;

    @ApiModelProperty("民警单位id")
    private String orgId;

    @ApiModelProperty("民警单位")
    private String orgName;

    @ApiModelProperty("民警分局ID")
    private String subofficeId;

    @ApiModelProperty("民警总学习时长(单位:秒)")
    private Long totalLearnTime;

    @ApiModelProperty("民警总学习次数")
    private Long totalLearnCount;

    @ApiModelProperty("民警总下载次数")
    private Long totalDownloadCount;

    @ApiModelProperty("最后一次学习时间")
    private LocalDateTime lastLearnTime;

    @ApiModelProperty("最后一次下载时间")
    private LocalDateTime lastDownloadingTime;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("是否生效 1:生效 0:不生效")
    private Boolean valid;


}
