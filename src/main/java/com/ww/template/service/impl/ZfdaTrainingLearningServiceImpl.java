package com.ww.template.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.template.base.BaseResult;
import com.ww.template.base.PageParam;
import com.ww.template.base.PageResult;
import com.ww.template.dto.param.*;
import com.ww.template.entity.ZfdaTrainingLearning;
import com.ww.template.entity.ZfdaTrainingLearningPolice;
import com.ww.template.mapper.ZfdaTrainingLearningMapper;
import com.ww.template.mapper.ZfdaTrainingLearningPoliceMapper;
import com.ww.template.service.IZfdaTrainingLearningPoliceService;
import com.ww.template.service.IZfdaTrainingLearningService;
import com.ww.template.utils.CacheClient;
import com.ww.template.utils.ObjectStorageUtil;
import com.ww.template.vo.PoliceTrainLearnDetailVo;
import com.ww.template.vo.TrainLearnDetailVo;
import com.ww.template.vo.PoliceTrainLearnVo;
import com.ww.template.vo.TrainLearnVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 培训学习执法表 服务实现类
 * </p>
 *
 * @author iflytek
 * @since 2023-01-13
 */
@Service
@Slf4j
public class ZfdaTrainingLearningServiceImpl extends ServiceImpl<ZfdaTrainingLearningMapper, ZfdaTrainingLearning> implements IZfdaTrainingLearningService {

    @Autowired
    private ZfdaTrainingLearningMapper trainLearnMapper;

    @Autowired
    private IZfdaTrainingLearningPoliceService trainLearnPoliceService;

    @Autowired
    private ZfdaTrainingLearningPoliceMapper trainLearnPoliceMapper;

    @Autowired
    private CacheClient cacheClient;

    @Override
    public PageResult<PoliceTrainLearnDetailVo> mergePoliceTrainLearn(PoliceTrainLearnDetailParam param, PageParam<PoliceTrainLearnDetailVo> page) {
        //1.设置排序
        LambdaQueryWrapper<ZfdaTrainingLearningPolice> queryWrapper = new LambdaQueryWrapper<>();
        switch (param.getSearchType()) {
            case LEARNTIME:
                String totalLearnTimeOrderBy = param.getTotalLearnTimeOrderBy();
                String lastLearnTimeOrderBy = param.getLastLearnTimeOrderBy();
                if (ObjectUtil.isNotNull(totalLearnTimeOrderBy) && ObjectUtil.isNotEmpty(totalLearnTimeOrderBy)) {
                    if (totalLearnTimeOrderBy.equals("asc")) {
                        queryWrapper.orderByAsc(ZfdaTrainingLearningPolice::getTotalLearnTime);
                    }
                    if (totalLearnTimeOrderBy.equals("desc")) {
                        queryWrapper.orderByDesc(ZfdaTrainingLearningPolice::getTotalLearnTime);
                    }
                }
                if (ObjectUtil.isNotNull(lastLearnTimeOrderBy) && ObjectUtil.isNotEmpty(lastLearnTimeOrderBy)) {
                    if (lastLearnTimeOrderBy.equals("asc")) {
                        queryWrapper.orderByAsc(ZfdaTrainingLearningPolice::getLastLearnTime);
                    }
                    if (lastLearnTimeOrderBy.equals("desc")) {
                        queryWrapper.orderByDesc(ZfdaTrainingLearningPolice::getLastLearnTime);
                    }
                }
                break;
            case LEARNCOUNT:
                String lastLearnTimeOrderBy1 = param.getLastLearnTimeOrderBy();
                String totalLearnCountOrderBy = param.getTotalLearnCountOrderBy();
                if (ObjectUtil.isNotNull(lastLearnTimeOrderBy1) && ObjectUtil.isNotEmpty(lastLearnTimeOrderBy1)) {
                    if (lastLearnTimeOrderBy1.equals("asc")) {
                        queryWrapper.orderByAsc(ZfdaTrainingLearningPolice::getTotalLearnTime);
                    }
                    if (lastLearnTimeOrderBy1.equals("desc")) {
                        queryWrapper.orderByDesc(ZfdaTrainingLearningPolice::getTotalLearnTime);
                    }
                }
                if (ObjectUtil.isNotNull(totalLearnCountOrderBy) && ObjectUtil.isNotEmpty(totalLearnCountOrderBy)) {
                    if (totalLearnCountOrderBy.equals("asc")) {
                        queryWrapper.orderByAsc(ZfdaTrainingLearningPolice::getTotalLearnCount);
                    }
                    if (totalLearnCountOrderBy.equals("desc")) {
                        queryWrapper.orderByDesc(ZfdaTrainingLearningPolice::getTotalLearnCount);
                    }
                }
                break;
            case DOWNCOUNT:
                String totalDownloadCountOrderBy = param.getTotalDownloadCountOrderBy();
                String lastDownloadTimeOrderBy = param.getLastDownloadTimeOrderBy();
                if (ObjectUtil.isNotNull(totalDownloadCountOrderBy) && ObjectUtil.isNotEmpty(totalDownloadCountOrderBy)) {
                    if (totalDownloadCountOrderBy.equals("asc")) {
                        queryWrapper.orderByAsc(ZfdaTrainingLearningPolice::getTotalDownloadCount);
                    }
                    if (totalDownloadCountOrderBy.equals("desc")) {
                        queryWrapper.orderByDesc(ZfdaTrainingLearningPolice::getTotalDownloadCount);
                    }
                }
                if (ObjectUtil.isNotNull(lastDownloadTimeOrderBy) && ObjectUtil.isNotEmpty(lastDownloadTimeOrderBy)) {
                    if (lastDownloadTimeOrderBy.equals("asc")) {
                        queryWrapper.orderByAsc(ZfdaTrainingLearningPolice::getLastDownloadingTime);
                    }
                    if (lastDownloadTimeOrderBy.equals("desc")) {
                        queryWrapper.orderByDesc(ZfdaTrainingLearningPolice::getLastDownloadingTime);
                    }
                }
                break;
            default:
        }
        //2.查询
        return trainLearnPoliceMapper.mergePoliceTrainLearn(param, queryWrapper, page.toPage());
    }

    @Override
    public PageResult<PoliceTrainLearnVo> policeTrainLearnList(PoliceTrainLearnQueryParam param, PageParam<PoliceTrainLearnVo> page) {
        LambdaQueryWrapper<ZfdaTrainingLearningPolice> queryWrapper = new LambdaQueryWrapper<>();
        //1.设置排序
        String totalLearnTimeOrderBy = param.getTotalLearnTimeOrderBy();
        String totalLearnCountOrderBy = param.getTotalLearnCountOrderBy();
        String totalDownloadCountOrderBy = param.getTotalDownloadCountOrderBy();
        if (ObjectUtil.isNotNull(totalLearnTimeOrderBy) && ObjectUtil.isNotEmpty(totalLearnTimeOrderBy)) {
            if (totalLearnTimeOrderBy.equals("asc")) {
                queryWrapper.orderByAsc(ZfdaTrainingLearningPolice::getTotalLearnTime);
            }
            if (totalLearnTimeOrderBy.equals("desc")) {
                queryWrapper.orderByDesc(ZfdaTrainingLearningPolice::getTotalLearnTime);
            }
        }
        if (ObjectUtil.isNotNull(totalLearnCountOrderBy) && ObjectUtil.isNotEmpty(totalLearnCountOrderBy)) {
            if (totalLearnCountOrderBy.equals("asc")) {
                queryWrapper.orderByAsc(ZfdaTrainingLearningPolice::getTotalLearnCount);
            }
            if (totalLearnCountOrderBy.equals("desc")) {
                queryWrapper.orderByDesc(ZfdaTrainingLearningPolice::getTotalLearnCount);
            }
        }
        if (ObjectUtil.isNotNull(totalDownloadCountOrderBy) && ObjectUtil.isNotEmpty(totalDownloadCountOrderBy)) {
            if (totalDownloadCountOrderBy.equals("asc")) {
                queryWrapper.orderByAsc(ZfdaTrainingLearningPolice::getTotalDownloadCount);
            }
            if (totalDownloadCountOrderBy.equals("desc")) {
                queryWrapper.orderByDesc(ZfdaTrainingLearningPolice::getTotalDownloadCount);
            }
        }
        return trainLearnPoliceMapper.policeTrainLearnList(param, queryWrapper, page.toPage());
    }

    @Override
    public PageResult<TrainLearnDetailVo> mergeTrainLearn(TrainLearnDetailParam param, PageParam<TrainLearnDetailVo> page) {
        //1.设置排序
        LambdaQueryWrapper<ZfdaTrainingLearningPolice> queryWrapper = new LambdaQueryWrapper<>();
        switch (param.getSearchType()) {
            case LEARNTIME:
                String totalLearnTimeOrderBy = param.getTotalLearnTimeOrderBy();
                String lastLearnTimeOrderBy = param.getLastLearnTimeOrderBy();
                if (ObjectUtil.isNotNull(totalLearnTimeOrderBy) && ObjectUtil.isNotEmpty(totalLearnTimeOrderBy)) {
                    if (totalLearnTimeOrderBy.equals("asc")) {
                        queryWrapper.orderByAsc(ZfdaTrainingLearningPolice::getTotalLearnTime);
                    }
                    if (totalLearnTimeOrderBy.equals("desc")) {
                        queryWrapper.orderByDesc(ZfdaTrainingLearningPolice::getTotalLearnTime);
                    }
                }
                if (ObjectUtil.isNotNull(lastLearnTimeOrderBy) && ObjectUtil.isNotEmpty(lastLearnTimeOrderBy)) {
                    if (lastLearnTimeOrderBy.equals("asc")) {
                        queryWrapper.orderByAsc(ZfdaTrainingLearningPolice::getLastLearnTime);
                    }
                    if (lastLearnTimeOrderBy.equals("desc")) {
                        queryWrapper.orderByDesc(ZfdaTrainingLearningPolice::getLastLearnTime);
                    }
                }
                break;
            case LEARNCOUNT:
                String lastLearnTimeOrderBy1 = param.getLastLearnTimeOrderBy();
                String totalLearnCountOrderBy = param.getTotalLearnCountOrderBy();
                if (ObjectUtil.isNotNull(lastLearnTimeOrderBy1) && ObjectUtil.isNotEmpty(lastLearnTimeOrderBy1)) {
                    if (lastLearnTimeOrderBy1.equals("asc")) {
                        queryWrapper.orderByAsc(ZfdaTrainingLearningPolice::getTotalLearnTime);
                    }
                    if (lastLearnTimeOrderBy1.equals("desc")) {
                        queryWrapper.orderByDesc(ZfdaTrainingLearningPolice::getTotalLearnTime);
                    }
                }
                if (ObjectUtil.isNotNull(totalLearnCountOrderBy) && ObjectUtil.isNotEmpty(totalLearnCountOrderBy)) {
                    if (totalLearnCountOrderBy.equals("asc")) {
                        queryWrapper.orderByAsc(ZfdaTrainingLearningPolice::getTotalLearnCount);
                    }
                    if (totalLearnCountOrderBy.equals("desc")) {
                        queryWrapper.orderByDesc(ZfdaTrainingLearningPolice::getTotalLearnCount);
                    }
                }
                break;
            case DOWNCOUNT:
                String totalDownloadCountOrderBy = param.getTotalDownloadCountOrderBy();
                String lastDownloadTimeOrderBy = param.getLastDownloadTimeOrderBy();
                if (ObjectUtil.isNotNull(totalDownloadCountOrderBy) && ObjectUtil.isNotEmpty(totalDownloadCountOrderBy)) {
                    if (totalDownloadCountOrderBy.equals("asc")) {
                        queryWrapper.orderByAsc(ZfdaTrainingLearningPolice::getTotalDownloadCount);
                    }
                    if (totalDownloadCountOrderBy.equals("desc")) {
                        queryWrapper.orderByDesc(ZfdaTrainingLearningPolice::getTotalDownloadCount);
                    }
                }
                if (ObjectUtil.isNotNull(lastDownloadTimeOrderBy) && ObjectUtil.isNotEmpty(lastDownloadTimeOrderBy)) {
                    if (lastDownloadTimeOrderBy.equals("asc")) {
                        queryWrapper.orderByAsc(ZfdaTrainingLearningPolice::getLastDownloadingTime);
                    }
                    if (lastDownloadTimeOrderBy.equals("desc")) {
                        queryWrapper.orderByDesc(ZfdaTrainingLearningPolice::getLastDownloadingTime);
                    }
                }
                break;
            default:
        }
        //2.查询
        return trainLearnPoliceMapper.mergeTrainLearn(param, queryWrapper, page.toPage());
    }


    //==================================================================================================================

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void downloadFile(HttpServletResponse response, String id, String policeId) {
        try {
            ZfdaTrainingLearning zfdaTrainLearn;
            String key = "totalDownLoadCount:" + id;
            try {
                zfdaTrainLearn = null;
                //1.增加课程下载总次数，修改民警下载次数和最后一次下载时间
                //1.1.增加课程下载总次数(防止多民警下载同一课程并发修改总下载次数，加锁)
                boolean lock = cacheClient.tryLock(key);
                if (lock) {
                    zfdaTrainLearn = trainLearnMapper.selectById(id);
                    this.lambdaUpdate().eq(ZfdaTrainingLearning::getId, id)
                            .set(ZfdaTrainingLearning::getTotalDownloadCount, (zfdaTrainLearn.getTotalDownloadCount() + 1))
                            .update();
                    //1.2.修改民警下载次数和最后一次下载时间
                    ZfdaTrainingLearningPolice trainLearnPolice = trainLearnPoliceService.getOne(new LambdaQueryWrapper<ZfdaTrainingLearningPolice>()
                            .eq(ZfdaTrainingLearningPolice::getTrainingLearningId, id)
                            .eq(ZfdaTrainingLearningPolice::getPoliceId, policeId)
                            .eq(ZfdaTrainingLearningPolice::getValid, 1));
                    trainLearnPoliceService.update(trainLearnPolice, new LambdaUpdateWrapper<ZfdaTrainingLearningPolice>()
                            .set(ZfdaTrainingLearningPolice::getTotalDownloadCount, trainLearnPolice.getTotalDownloadCount() + 1)
                            .set(ZfdaTrainingLearningPolice::getLastDownloadingTime, LocalDateTime.now()));
                } else {
                    //TODO 后期升级Redisson改善重试机制
                }
            } finally {
                cacheClient.unlock(key);
            }
            //2.下载文件
            // 解析文件的 mime 类型
            String mediaTypeStr = URLConnection.getFileNameMap().getContentTypeFor(zfdaTrainLearn.getFileName());
            // 无法判断MIME类型时，作为流类型
            mediaTypeStr = (mediaTypeStr == null) ? MediaType.APPLICATION_OCTET_STREAM_VALUE : mediaTypeStr;
            // 实例化MIME
            MediaType mediaType = MediaType.parseMediaType(mediaTypeStr);
            // 下载之后需要在请求头中放置文件名，该文件名按照ISO_8859_1编码。
            String filenames = new String(zfdaTrainLearn.getFileName().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            response.setHeader("Content-Disposition", "attachment;filename=\"" + filenames + "\"");
            response.setHeader("Content-Type", mediaType.toString());
            File tempFile = ObjectStorageUtil.getObjectToFile(zfdaTrainLearn.getStorageUrl());
            if (tempFile == null) {
                throw new RuntimeException("FDFS下载失败");
            }
            try (
                    FileInputStream fileInputStream = new FileInputStream(tempFile);
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(response.getOutputStream())
            ) {
                FileCopyUtils.copy(bufferedInputStream, bufferedOutputStream);
            } finally {
                tempFile.delete();
            }
        } catch (IOException e) {
            log.error("文件下载失败", e);
            throw new RuntimeException("附件下载失败");
        }
    }


    @Override
    public BaseResult<Boolean> viewTrainLearnTick(TrainLearnViewTickParam param) {
        //1.查询民警最近一次学习该课程时间
        ZfdaTrainingLearningPolice trainLearnPolice = trainLearnPoliceMapper.selectOne(new LambdaQueryWrapper<ZfdaTrainingLearningPolice>()
                .eq(ZfdaTrainingLearningPolice::getPoliceId, param.getViewPoliceId())
                .eq(ZfdaTrainingLearningPolice::getTrainingLearningId, param.getId())
                .eq(ZfdaTrainingLearningPolice::getValid, 1));
        //1.1.判断最近学习的时间加上增加的学习时长是否在指定范围(second对应的秒数内多次调用只计算一次)
        LocalDateTime newDateTime = trainLearnPolice.getLastLearnTime().plusSeconds((long) param.getSecond());
        LocalDateTime now = LocalDateTime.now();
        if (newDateTime.equals(now) || newDateTime.isBefore(now)) {
            //1.2.增加民警学习时长
            trainLearnPoliceService.lambdaUpdate().eq(ZfdaTrainingLearningPolice::getId, trainLearnPolice.getId())
                    .set(ZfdaTrainingLearningPolice::getLastLearnTime, now)
                    .set(ZfdaTrainingLearningPolice::getTotalLearnTime, (trainLearnPolice.getTotalLearnTime() + param.getSecond()))
                    .set(ZfdaTrainingLearningPolice::getUpdateTime, LocalDateTime.now()).update();
            //1.3.增加该课程总的学习时长 (防止多民警学习同一课程并发修改总学习时长，加锁)
            String key = "totalLearnTime:" + param.getId();
            boolean lock = cacheClient.tryLock(key);
            try {
                if (lock) {
                    ZfdaTrainingLearning trainLearn = this.getById(param.getId());
                    this.lambdaUpdate().eq(ZfdaTrainingLearning::getId, trainLearn.getId())
                            .set(ZfdaTrainingLearning::getTotalLearnTime, (trainLearn.getTotalLearnTime() + param.getSecond()))
                            .set(ZfdaTrainingLearning::getUpdateTime, LocalDateTime.now())
                            .update();
                } else {
                    //TODO 后期升级Redisson改善重试机制
                }
            } catch (Exception e) {
                log.error("课程学习总时间异常", e);
                throw new RuntimeException(e);
            } finally {
                cacheClient.unlock(key);
            }
        }
        return BaseResult.success(true);
    }


    @Override
    public BaseResult<TrainLearnVo> viewTrainLearn(String id, String viewPoliceId, String viewPoliceOrgId, String viewPoliceSubstationId) {
        boolean lock = cacheClient.tryLock(id);
        if (lock) {
            try {
                //1.课程浏览次数递增(只要浏览一次某个文件，就算他学习一次)
                ZfdaTrainingLearning trainLearn = trainLearnMapper.selectById(id);
                this.lambdaUpdate().eq(ZfdaTrainingLearning::getId, id)
                        .set(ZfdaTrainingLearning::getTotalLearnCount, (trainLearn.getTotalLearnCount() + 1))
                        .set(ZfdaTrainingLearning::getUpdateTime, LocalDateTime.now()).update();
                //2.保存或修改民警学习记录
                ZfdaTrainingLearningPolice trainLearnPolice = trainLearnPoliceMapper.selectOne(new LambdaQueryWrapper<ZfdaTrainingLearningPolice>()
                        .eq(ZfdaTrainingLearningPolice::getPoliceId, viewPoliceId)
                        .eq(ZfdaTrainingLearningPolice::getTrainingLearningId, id)
                        .eq(ZfdaTrainingLearningPolice::getValid, 1));
                if (ObjectUtil.isNull(trainLearnPolice)) {
                    //2.1.民警第一次学习该课程，则新增
                    ZfdaTrainingLearningPolice build = ZfdaTrainingLearningPolice.builder()
                            .id(UUID.randomUUID().toString(true))
                            .trainingLearningId(id)
                            .policeId(viewPoliceId)
                            .orgId(viewPoliceOrgId)
                            .subofficeId(viewPoliceSubstationId)
                            .totalDownloadCount(0L)
                            .totalLearnTime(0L)
                            .totalLearnCount(1L)
                            .lastLearnTime(LocalDateTime.now())
                            .valid(true)
                            .build();
                    trainLearnPoliceMapper.insert(build);
                } else {
                    //2.2.民警不是第一次学习该课程，则修改
                    trainLearnPoliceService.lambdaUpdate().eq(ZfdaTrainingLearningPolice::getId, trainLearnPolice.getId())
                            .set(ZfdaTrainingLearningPolice::getLastLearnTime, LocalDateTime.now())
                            .set(ZfdaTrainingLearningPolice::getTotalLearnCount, (trainLearnPolice.getTotalLearnCount() + 1))
                            .set(ZfdaTrainingLearningPolice::getUpdateTime, LocalDateTime.now())
                            .update();
                }
                //3.返回课程信息
                TrainLearnVo trainLearnVo = BeanUtil.copyProperties(trainLearn, TrainLearnVo.class);
                return BaseResult.success(trainLearnVo);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                cacheClient.unlock(id);
            }
        } else {
            //TODO 后期升级Redisson改善重试机制
            this.viewTrainLearn(id, viewPoliceId, viewPoliceOrgId, viewPoliceSubstationId);
        }
        return BaseResult.fail();
    }


    //==============================================================================================================================

    @Override
    public BaseResult<Boolean> updateTrainLearn(String id, String fileName) {
        if (this.lambdaUpdate().eq(ZfdaTrainingLearning::getId, id).set(ZfdaTrainingLearning::getFileName, fileName).update()) {
            return BaseResult.success();
        }
        return BaseResult.fail();
    }

    @Override
    public BaseResult<Boolean> deleteTrainLearn(String id) {
        if (this.lambdaUpdate().eq(ZfdaTrainingLearning::getId, id).set(ZfdaTrainingLearning::getValid, false).update()) {
            return BaseResult.success();
        }
        return BaseResult.fail();
    }

    @Override
    public PageResult<TrainLearnVo> trainLearnList(TrainLearnQueryParam param, PageParam<TrainLearnVo> page) {
        LambdaQueryWrapper<ZfdaTrainingLearning> queryWrapper = new LambdaQueryWrapper<>();
        //1.设置排序
        String totalLearnTimeOrderBy = param.getTotalLearnTimeOrderBy();
        String totalLearnCountOrderBy = param.getTotalLearnCountOrderBy();
        String totalDownloadCountOrderBy = param.getTotalDownloadCountOrderBy();
        if (ObjectUtil.isNotNull(totalLearnTimeOrderBy) && ObjectUtil.isNotEmpty(totalLearnTimeOrderBy)) {
            if (totalLearnTimeOrderBy.equals("asc")) {
                queryWrapper.orderByAsc(ZfdaTrainingLearning::getTotalLearnTime);
            }
            if (totalLearnTimeOrderBy.equals("desc")) {
                queryWrapper.orderByDesc(ZfdaTrainingLearning::getTotalLearnTime);
            }
        }
        if (ObjectUtil.isNotNull(totalLearnCountOrderBy) && ObjectUtil.isNotEmpty(totalLearnCountOrderBy)) {
            if (totalLearnCountOrderBy.equals("asc")) {
                queryWrapper.orderByAsc(ZfdaTrainingLearning::getTotalLearnCount);
            }
            if (totalLearnCountOrderBy.equals("desc")) {
                queryWrapper.orderByDesc(ZfdaTrainingLearning::getTotalLearnCount);
            }
        }
        if (ObjectUtil.isNotNull(totalDownloadCountOrderBy) && ObjectUtil.isNotEmpty(totalDownloadCountOrderBy)) {
            if (totalDownloadCountOrderBy.equals("asc")) {
                queryWrapper.orderByAsc(ZfdaTrainingLearning::getTotalDownloadCount);
            }
            if (totalDownloadCountOrderBy.equals("desc")) {
                queryWrapper.orderByDesc(ZfdaTrainingLearning::getTotalDownloadCount);
            }
        }
        //2.查询
        return trainLearnMapper.queryTrainLearnList(param, queryWrapper, page.toPage());
    }

    @Override
    public BaseResult<String> saveTrainLearn(String trainingLearningType, String problemCode, String uploadUserName, String uploadUserId,
                                             MultipartFile[] files) {
        //1.校验文件格式和大小 (PDF文件最大不超过5M,MP4文件最大不超过200M)
        Map<String, Object> map = checkFile(files, trainingLearningType);
        if (!(boolean) map.get("flag")) {
            return BaseResult.fail((String) map.get("msg"));
        }

        //2.保存上传记录
        boolean saveFlag = Arrays.stream(files).anyMatch(file -> {
            ZfdaTrainingLearning.ZfdaTrainingLearningBuilder builder = ZfdaTrainingLearning.builder();
            try {
                String fileName = file.getOriginalFilename();
                String storageUrl = ObjectStorageUtil.putObject(fileName, file.getBytes());
                builder.id(UUID.randomUUID().toString(true))
                        .trainingLearningType(trainingLearningType)
                        .problemCode(problemCode)
                        .uploadUserId(uploadUserId)
                        .uploadUserName(uploadUserName)
                        .storageUrl(storageUrl)
                        .fileName(fileName)
//                        .fileName(fileName.substring(0, fileName.lastIndexOf(".")))
                        .fileType(fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase())
                        .fileSize((int) file.getSize())
                        .totalLearnTime(0L)
                        .totalLearnCount(0L)
                        .totalDownloadCount(0L)
                        .valid(true);
                return trainLearnMapper.insert(builder.build()) < 1;
            } catch (Exception e) {
                log.error("保存上传记录失败");
                throw new RuntimeException(e);
            }
        });
        if (saveFlag) {
            return BaseResult.fail("新增失败");
        }
        return BaseResult.success("新增成功");
    }

    //校验文件
    private Map<String, Object> checkFile(MultipartFile[] files, String trainingLearningType) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("flag", true);
        if (trainingLearningType.equals("1")) {
            //1.校验文件类型
            if (Arrays.stream(files).anyMatch(file -> checkFileTypeIsLimit(file, trainingLearningType))) {
                hashMap.put("flag", false);
                hashMap.put("msg", "上传文件类型不符合规定");
                return hashMap;
            }
            //2.校验文件大小
            if (Arrays.stream(files).anyMatch(file -> checkFileSizeIsLimit(file.getSize(), 5, "M"))) {
                hashMap.put("flag", false);
                hashMap.put("msg", "上传文件大小不符合规定");
                return hashMap;
            }
        }
        if (trainingLearningType.equals("2")) {
            if (Arrays.stream(files).anyMatch(file -> checkFileTypeIsLimit(file, trainingLearningType))) {
                hashMap.put("flag", false);
                hashMap.put("msg", "上传视频类型不符合规定");
                return hashMap;
            }
            if (Arrays.stream(files).anyMatch(file -> checkFileSizeIsLimit(file.getSize(), 200, "M"))) {
                hashMap.put("flag", false);
                hashMap.put("msg", "上传视频大小不符合规定");
                return hashMap;
            }
        }
        return hashMap;
    }


    /**
     * 判断文件大小处于限制内
     *
     * @param fileLen  文件长度
     * @param fileSize 限制大小
     * @param fileUnit 限制的单位（B,K,M,G）
     * @return 结果
     */
    public static boolean checkFileSizeIsLimit(Long fileLen, int fileSize, String fileUnit) {
        if (fileLen == 0L) {
            return true;
        }
        double fileSizeCom = 0;
        if ("B".equalsIgnoreCase(fileUnit)) {
            fileSizeCom = (double) fileLen;
        } else if ("K".equalsIgnoreCase(fileUnit)) {
            fileSizeCom = (double) fileLen / 1024;
        } else if ("M".equalsIgnoreCase(fileUnit)) {
            fileSizeCom = (double) fileLen / (1024 * 1024);
        } else if ("G".equalsIgnoreCase(fileUnit)) {
            fileSizeCom = (double) fileLen / (1024 * 1024 * 1024);
        }
        return fileSizeCom > fileSize;
    }


    /**
     * @param file 文件对象
     * @return 结果
     */
    public static boolean checkFileTypeIsLimit(MultipartFile file, String type) {
        String fileName = file.getOriginalFilename();
        String[] types = new String[0];
        if (type.equals("1")) {
            types = new String[]{
//                    "doc",
//                    "docx",
                    "pdf"
            };
        }
        if (type.equals("2")) {
            types = new String[]{
                    "mp4"
            };
        }
        List<String> typeList = Arrays.asList(types);
        return !typeList.contains(fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase());
    }

}
