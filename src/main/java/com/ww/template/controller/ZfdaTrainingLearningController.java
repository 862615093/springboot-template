package com.ww.template.controller;

import com.ww.template.base.BaseResult;
import com.ww.template.base.PageParam;
import com.ww.template.base.PageResult;
import com.ww.template.dto.param.*;
import com.ww.template.service.IZfdaTrainingLearningService;
import com.ww.template.vo.PoliceTrainLearnDetailVo;
import com.ww.template.vo.TrainLearnDetailVo;
import com.ww.template.vo.PoliceTrainLearnVo;
import com.ww.template.vo.TrainLearnVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 培训学习执法表 前端控制器
 * </p>
 *
 * @author iflytek
 * @since 2023-01-13
 */
@Slf4j
@RestController
@Api(tags = {"培训学习"})
@RequestMapping("/zfdaTrainingLearning/zfFile")
public class ZfdaTrainingLearningController {

    @Autowired
    private IZfdaTrainingLearningService zfdaTrainingLearningService;

    @ApiOperation(value = "新增")
    @PostMapping("/save")
    public BaseResult<String> saveTrainLearn(@RequestParam(value = "trainingLearningType") String trainingLearningType,
                                             @RequestParam(value = "problemCode") String problemCode,
                                             @RequestParam(value = "uploadUserName") String uploadUserName,
                                             @RequestParam(value = "uploadUserId") String uploadUserId,
                                             @RequestParam(value = "files") MultipartFile[] files) {
        return zfdaTrainingLearningService.saveTrainLearn(trainingLearningType, problemCode, uploadUserName, uploadUserId, files);
    }

    @ApiOperation(value = "列表、培训内容维度列表")
    @PostMapping("/list")
    public BaseResult<PageResult<TrainLearnVo>> trainLearnList(@RequestBody TrainLearnQueryParam param, PageParam<TrainLearnVo> page) {
        return BaseResult.success(zfdaTrainingLearningService.trainLearnList(param, page));
    }

    @ApiOperation(value = "下载文件")
    @GetMapping("/downloadFile")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "培训学习ID", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "id", value = "下载文件民警ID", dataType = "string", paramType = "query")
    })
    public void downloadFile(HttpServletResponse response, @RequestParam("id") String id, @RequestParam("policeId") String policeId) {
        zfdaTrainingLearningService.downloadFile(response, id, policeId);
    }

    @ApiOperation(value = "修改")
    @PutMapping("/updateTrainLearn")
    public BaseResult<Boolean> updateTrainLearn(@RequestParam(value = "id") String id,
                                                @RequestParam(value = "fileName") String fileName) {
        return zfdaTrainingLearningService.updateTrainLearn(id, fileName);
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("/deleteTrainLearn")
    public BaseResult<Boolean> deleteTrainLearn(@RequestParam(value = "id") String id) {
        return zfdaTrainingLearningService.deleteTrainLearn(id);
    }

    @ApiOperation(value = "浏览")
    @GetMapping("/view")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "培训学习ID", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "viewPoliceId", value = "浏览文件民警ID", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "viewPoliceOrgId", value = "浏览文件民警所队ID", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "viewPoliceSubstationId", value = "浏览文件民警分局ID", dataType = "string", paramType = "query")
    })
    public BaseResult<TrainLearnVo> viewTrainLearn(@RequestParam(value = "id") String id,
                                                   @RequestParam(value = "viewPoliceId") String viewPoliceId,
                                                   @RequestParam(value = "viewPoliceOrgId") String viewPoliceOrgId,
                                                   @RequestParam(value = "viewPoliceSubstationId") String viewPoliceSubstationId) {
        return zfdaTrainingLearningService.viewTrainLearn(id, viewPoliceId, viewPoliceOrgId, viewPoliceSubstationId);
    }

    @ApiOperation(value = "增加浏览时长")
    @PutMapping("/view/tick")
    public BaseResult<Boolean> viewTrainLearnTick(@RequestBody TrainLearnViewTickParam param) {
        return zfdaTrainingLearningService.viewTrainLearnTick(param);
    }

    @ApiOperation(value = "培训内容维度-总学习时长、总学习次数、总下载次数详情列表")
    @PostMapping("/mergeTrainLearn/list")
    public BaseResult<PageResult<TrainLearnDetailVo>> mergeTrainLearn(@RequestBody TrainLearnDetailParam param, PageParam<TrainLearnDetailVo> page) {
        return BaseResult.success(zfdaTrainingLearningService.mergeTrainLearn(param, page));
    }

    @ApiOperation(value = "民警维度-列表")
    @PostMapping("/police/list")
    public BaseResult<PageResult<PoliceTrainLearnVo>> policeTrainLearnList(@RequestBody PoliceTrainLearnQueryParam param, PageParam<PoliceTrainLearnVo> page) {
        return BaseResult.success(zfdaTrainingLearningService.policeTrainLearnList(param, page));
    }

    @ApiOperation(value = "民警维度-总学习时长、总学习次数、总下载次数详情列表")
    @PostMapping("/mergePoliceTrainLearn/list")
    public BaseResult<PageResult<PoliceTrainLearnDetailVo>> mergePoliceTrainLearn(@RequestBody PoliceTrainLearnDetailParam param, PageParam<PoliceTrainLearnDetailVo> page) {
        return BaseResult.success(zfdaTrainingLearningService.mergePoliceTrainLearn(param, page));
    }
}
