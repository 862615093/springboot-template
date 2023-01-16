package com.ww.template.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.template.base.PageResult;
import com.ww.template.dto.param.TrainLearnQueryParam;
import com.ww.template.entity.ZfdaTrainingLearning;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ww.template.vo.TrainLearnVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 培训学习执法表 Mapper 接口
 * </p>
 *
 * @author iflytek
 * @since 2023-01-13
 */
@Mapper
public interface ZfdaTrainingLearningMapper extends BaseMapper<ZfdaTrainingLearning> {

    PageResult<TrainLearnVo> queryTrainLearnList(@Param("param") TrainLearnQueryParam param,
                                                 @Param(Constants.WRAPPER) LambdaQueryWrapper<ZfdaTrainingLearning> ew,
                                                 PageResult<TrainLearnVo> toPage);

}
