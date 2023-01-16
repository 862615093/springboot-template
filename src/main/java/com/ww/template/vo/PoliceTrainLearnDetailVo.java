package com.ww.template.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
@ApiModel(value = "PoliceTrainLearnDetailVo对象出参", description = "民警培训学习详情列表对象")
public class PoliceTrainLearnDetailVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("民警培训ID")
    private String id;

    @ApiModelProperty("民警")
    private String policeName;

    @ApiModelProperty("培训学习文件")
    private String fileName;

    @ApiModelProperty("培训学习文件类型")
    private String fileType;

    @ApiModelProperty("民警培训学习执法文件总时长")
    private long policeTotalLearnTime;

    @ApiModelProperty("民警培训学习执法文件最近学习一次时间")
    private LocalDateTime policeLastLearnTime;

    @ApiModelProperty("民警培训学习执法文件总浏览数")
    private String policeTotalLearnCount;

    @ApiModelProperty("民警培训学习执法文件总下载数")
    private String policeTotalDownloadCount;

    @ApiModelProperty("民警培训学习执法文件最近一次下载时间")
    private LocalDateTime policeTotalDownloadTime;
}
