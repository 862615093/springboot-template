package com.ww.template.service;

import com.ww.template.base.BaseResult;
import com.ww.template.base.PageParam;
import com.ww.template.base.PageResult;
import com.ww.template.dto.param.*;
import com.ww.template.entity.ZfdaTrainingLearning;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.template.vo.PoliceTrainLearnDetailVo;
import com.ww.template.vo.TrainLearnDetailVo;
import com.ww.template.vo.PoliceTrainLearnVo;
import com.ww.template.vo.TrainLearnVo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 培训学习执法表 服务类
 * </p>
 *
 * @author iflytek
 * @since 2023-01-13
 */
public interface IZfdaTrainingLearningService extends IService<ZfdaTrainingLearning> {

    BaseResult<String> saveTrainLearn(String trainingLearningType, String problemCode, String uploadUserName, String uploadUserId, MultipartFile[] files);

    PageResult<TrainLearnVo> trainLearnList(TrainLearnQueryParam param, PageParam<TrainLearnVo> page);

    BaseResult<Boolean> updateTrainLearn(String id, String fileName);

    BaseResult<Boolean> deleteTrainLearn(String id);

    BaseResult<TrainLearnVo> viewTrainLearn(String id, String viewPoliceId, String viewPoliceOrgId, String viewPoliceSubstationId);

    BaseResult<Boolean> viewTrainLearnTick(TrainLearnViewTickParam param);

    void downloadFile(HttpServletResponse response, String id, String policeId);

    PageResult<TrainLearnDetailVo> mergeTrainLearn(TrainLearnDetailParam param, PageParam<TrainLearnDetailVo> page);

    PageResult<PoliceTrainLearnVo> policeTrainLearnList(PoliceTrainLearnQueryParam param, PageParam<PoliceTrainLearnVo> page);

    PageResult<PoliceTrainLearnDetailVo> mergePoliceTrainLearn(PoliceTrainLearnDetailParam param, PageParam<PoliceTrainLearnDetailVo> page);

}
