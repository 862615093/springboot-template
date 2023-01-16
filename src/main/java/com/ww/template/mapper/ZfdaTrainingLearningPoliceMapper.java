package com.ww.template.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ww.template.base.PageResult;
import com.ww.template.dto.param.PoliceTrainLearnDetailParam;
import com.ww.template.dto.param.PoliceTrainLearnQueryParam;
import com.ww.template.dto.param.TrainLearnDetailParam;
import com.ww.template.entity.ZfdaTrainingLearningPolice;
import com.ww.template.vo.PoliceTrainLearnDetailVo;
import com.ww.template.vo.PoliceTrainLearnVo;
import com.ww.template.vo.TrainLearnDetailVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 培训学习民警表 Mapper 接口
 * </p>
 *
 * @author iflytek
 * 2@since 2023-01-14
 */
@Mapper
public interface ZfdaTrainingLearningPoliceMapper extends BaseMapper<ZfdaTrainingLearningPolice> {

    PageResult<TrainLearnDetailVo> mergeTrainLearn(@Param("param") TrainLearnDetailParam param,
                                                   @Param(Constants.WRAPPER) LambdaQueryWrapper<ZfdaTrainingLearningPolice> ew,
                                                   PageResult<TrainLearnDetailVo> toPage);

    PageResult<PoliceTrainLearnVo> policeTrainLearnList(@Param("param") PoliceTrainLearnQueryParam param,
                                                        @Param(Constants.WRAPPER) LambdaQueryWrapper<ZfdaTrainingLearningPolice> ew,
                                                        PageResult<PoliceTrainLearnVo> toPage);

    PageResult<PoliceTrainLearnDetailVo> mergePoliceTrainLearn(@Param("param") PoliceTrainLearnDetailParam param,
                                                               @Param(Constants.WRAPPER) LambdaQueryWrapper<ZfdaTrainingLearningPolice> ew,
                                                               PageResult<PoliceTrainLearnDetailVo> toPage);
}
