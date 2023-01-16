package com.ww.template.aspect;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ww.template.base.BaseResult;
import com.ww.template.entity.ZfdaTrainingLearning;
import com.ww.template.entity.ZfdaTrainingLearningPolice;
import com.ww.template.service.IZfdaTrainingLearningPoliceService;
import com.ww.template.service.IZfdaTrainingLearningService;
import com.ww.template.utils.CacheClient;
import com.ww.template.utils.ThreadPoolUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;

import static com.ww.template.enums.OperateTypeEnum.TOTAL_DOWNLOAD_COUNT;


/**
 * 培训学习业务
 *
 * @author weiwang127
 */
@Slf4j
@Order(1)
@Aspect
@Component
public class OperateTrainLearnService implements OperateTrainLearnAspect {

    @Resource
    private ThreadPoolUtils threadPoolUtils;

    @Autowired
    private CacheClient cacheClient;

    @Autowired
    private IZfdaTrainingLearningService trainLearnService;

    @Autowired
    private IZfdaTrainingLearningPoliceService trainLearnPoliceService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object operateTrainLearnLogic(ProceedingJoinPoint joinPoint, OperateTrainLearn ol) {
        if (ol.operateType().equals(TOTAL_DOWNLOAD_COUNT)) {
            //1.获取请求体参数
            Object[] args = joinPoint.getArgs();
            String argsStr = JSONUtil.toJsonStr(Arrays.asList(args));
            log.info("切入点接口入参【{}】", argsStr);
            String trainLearnId = (String) args[1];
            String policeId = (String) args[2];
            ZfdaTrainingLearning zfdaTrainLearn = trainLearnService.getById(trainLearnId);
            if (ObjectUtil.isNull(zfdaTrainLearn)) {
                throw new RuntimeException("文件不存在");
            }
            //2.获取返回参数
            String key = "totalDownLoadCount:" + trainLearnId;
            try {
                Object result = joinPoint.proceed();
                //3.执行逻辑业务，增加课程下载总次数，修改民警下载次数和最后一次下载时间
                //TODO AOP 加锁
                //3.1.增加课程下载总次数(防止多民警下载同一课程并发修改总下载次数，加锁)
//                ZfdaTrainingLearning curTrainLearn = trainLearnService.getById(trainLearnId);
//                trainLearnService.lambdaUpdate().eq(ZfdaTrainingLearning::getId, trainLearnId)
//                        .set(ZfdaTrainingLearning::getTotalDownloadCount, (curTrainLearn.getTotalDownloadCount() + 1))
//                        .update();
//                int i = 1 / 0;
//                //2.2.修改民警下载次数和最后一次下载时间
//                ZfdaTrainingLearningPolice trainLearnPolice = trainLearnPoliceService.getOne(new LambdaQueryWrapper<ZfdaTrainingLearningPolice>()
//                        .eq(ZfdaTrainingLearningPolice::getTrainingLearningId, trainLearnId)
//                        .eq(ZfdaTrainingLearningPolice::getPoliceId, policeId)
//                        .eq(ZfdaTrainingLearningPolice::getValid, 1));
//                trainLearnPoliceService.update(trainLearnPolice, new LambdaUpdateWrapper<ZfdaTrainingLearningPolice>()
//                        .set(ZfdaTrainingLearningPolice::getTotalDownloadCount, trainLearnPolice.getTotalDownloadCount() + 1)
//                        .set(ZfdaTrainingLearningPolice::getLastDownloadingTime, LocalDateTime.now()));
//                boolean lock = cacheClient.tryLock(key);
//                if (lock) {
//                    //3.1.增加课程下载总次数(防止多民警下载同一课程并发修改总下载次数，加锁)
//                    ZfdaTrainingLearning curTrainLearn = trainLearnService.getById(trainLearnId);
//                    trainLearnService.lambdaUpdate().eq(ZfdaTrainingLearning::getId, trainLearnId)
//                            .set(ZfdaTrainingLearning::getTotalDownloadCount, (curTrainLearn.getTotalDownloadCount() + 1))
//                            .update();
//                    int i = 1 / 0;
//                    //2.2.修改民警下载次数和最后一次下载时间
//                    ZfdaTrainingLearningPolice trainLearnPolice = trainLearnPoliceService.getOne(new LambdaQueryWrapper<ZfdaTrainingLearningPolice>()
//                                    .eq(ZfdaTrainingLearningPolice::getTrainingLearningId, trainLearnId)
//                                    .eq(ZfdaTrainingLearningPolice::getPoliceId, policeId)
//                                    .eq(ZfdaTrainingLearningPolice::getValid, 1));
//                    trainLearnPoliceService.update(trainLearnPolice, new LambdaUpdateWrapper<ZfdaTrainingLearningPolice>()
//                            .set(ZfdaTrainingLearningPolice::getTotalDownloadCount, trainLearnPolice.getTotalDownloadCount() + 1)
//                            .set(ZfdaTrainingLearningPolice::getLastDownloadingTime, LocalDateTime.now()));
//                }
                return result;
            } catch (Throwable e) {
                log.error("方法异常", e);
                throw new RuntimeException(e);
            } finally {
                cacheClient.unlock(key);
            }
        }
        return BaseResult.fail("非法环绕通知");
    }
}