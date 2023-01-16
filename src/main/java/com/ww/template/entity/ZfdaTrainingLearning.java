package com.ww.template.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * <p>
 * 培训学习执法表
 * </p>
 *
 * @author iflytek
 * @since 2023-01-13
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("zfda_training_learning")
@ApiModel(value = "ZfdaTrainingLearning对象", description = "培训学习执法表")
public class ZfdaTrainingLearning implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("培训学习类型：1-法条规范，2-执法视频")
    private String trainingLearningType;

    @ApiModelProperty("指标项编码(执法问题编码)")
    private String problemCode;

    @ApiModelProperty("上传人ID")
    private String uploadUserId;

    @ApiModelProperty("上传人姓名")
    private String uploadUserName;

    @ApiModelProperty("文件访问地址")
    private String storageUrl;

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("文件类型")
    private String fileType;

    @ApiModelProperty("文件大小")
    private Integer fileSize;

    @ApiModelProperty("总学习时长(单位:秒)")
    private Long totalLearnTime;

    @ApiModelProperty("总学习次数")
    private Long totalLearnCount;

    @ApiModelProperty("总下载次数")
    private Long totalDownloadCount;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("是否生效 1:生效 0:不生效")
    private Boolean valid;


}
