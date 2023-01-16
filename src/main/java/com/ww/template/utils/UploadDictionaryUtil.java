package com.ww.template.utils;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ww.template.entity.UploadDictionaryData;
import com.ww.template.entity.ZfdaDictionary;
import com.ww.template.mapper.ZfdaDictionaryMapper;
import com.ww.template.service.IZfdaDictionaryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@Slf4j
@Component
public class UploadDictionaryUtil {
    //1级指标排序
    private AtomicInteger indexOrder1 = new AtomicInteger(1);

    @Autowired
    private ZfdaDictionaryMapper zfdaDictionaryMapper;

    public void save(List<UploadDictionaryData> cachedDataList) {
        if (!cachedDataList.isEmpty()) {
            cachedDataList.forEach(item -> {
                //1 判断指标项等级
                Long l1 = zfdaDictionaryMapper.selectCount(new LambdaQueryWrapper<ZfdaDictionary>().eq(ZfdaDictionary::getIndexName, item.getIndexName1()));
                if (l1 < 1) {
                    //1.1 创建1级指标项
                    ZfdaDictionary zfdaDictionary = new ZfdaDictionary();
                    zfdaDictionary.setId(UUID.randomUUID().toString(true));
                    zfdaDictionary.setIndexCode(UUID.randomUUID().toString(true));
                    zfdaDictionary.setIndexType("减分");
                    zfdaDictionary.setIndexName(item.getIndexName1());
                    zfdaDictionary.setIndexOrder(indexOrder1.get());
                    zfdaDictionary.setParentCode("0");
                    zfdaDictionary.setValid(true);
                    zfdaDictionaryMapper.insert(zfdaDictionary);
                    indexOrder1.incrementAndGet();
                }
                Long l2 = zfdaDictionaryMapper.selectCount(new LambdaQueryWrapper<ZfdaDictionary>().eq(ZfdaDictionary::getIndexName, item.getIndexName2()));
                if (l2 < 1) {
                    //1.2 创建2级指标项
                    ZfdaDictionary zfdaDictionary = new ZfdaDictionary();
                    zfdaDictionary.setId(UUID.randomUUID().toString(true));
                    zfdaDictionary.setIndexCode(UUID.randomUUID().toString(true));
                    zfdaDictionary.setIndexType("减分");
                    zfdaDictionary.setIndexName(item.getIndexName2());
                    ZfdaDictionary dictionary1 = zfdaDictionaryMapper.selectOne(new LambdaQueryWrapper<ZfdaDictionary>().eq(ZfdaDictionary::getIndexName, item.getIndexName1()));
                    zfdaDictionary.setParentCode(dictionary1.getIndexCode());
                    zfdaDictionary.setValid(true);
                    //2级指标排序
                    Long s2 = zfdaDictionaryMapper.selectCount(new LambdaQueryWrapper<ZfdaDictionary>().eq(ZfdaDictionary::getParentCode, dictionary1.getIndexCode()));
                    if (s2 > 0) {
                        int i = Integer.parseInt(String.valueOf(s2));
                        zfdaDictionary.setIndexOrder(++i);
                    } else {
                        zfdaDictionary.setIndexOrder(1);
                    }
                    zfdaDictionaryMapper.insert(zfdaDictionary);
                }
                //1.3 创建3级指标项
                ZfdaDictionary zfdaDictionary = new ZfdaDictionary();
                zfdaDictionary.setId(UUID.randomUUID().toString(true));
                zfdaDictionary.setIndexCode(UUID.randomUUID().toString(true));
                zfdaDictionary.setIndexType("减分");
                zfdaDictionary.setIndexDec(item.getIndexDec());
                zfdaDictionary.setAcquireWay(item.getAcquireWay());
                ZfdaDictionary dictionary2 = zfdaDictionaryMapper.selectOne(new LambdaQueryWrapper<ZfdaDictionary>().eq(ZfdaDictionary::getIndexName, item.getIndexName2()));
                zfdaDictionary.setParentCode(dictionary2.getIndexCode());
                zfdaDictionary.setIndexName(item.getIndexName3());
                zfdaDictionary.setIndexValue(item.getIndexValue());
                zfdaDictionary.setValid(true);
                //3级指标排序
                Long s3 = zfdaDictionaryMapper.selectCount(new LambdaQueryWrapper<ZfdaDictionary>().eq(ZfdaDictionary::getParentCode, dictionary2.getIndexCode()));
                if (s3 > 0) {
                    int i = Integer.parseInt(String.valueOf(s3));
                    zfdaDictionary.setIndexOrder(++i);
                } else {
                    zfdaDictionary.setIndexOrder(1);
                }
                zfdaDictionaryMapper.insert(zfdaDictionary);
            });
        }
    }
}
