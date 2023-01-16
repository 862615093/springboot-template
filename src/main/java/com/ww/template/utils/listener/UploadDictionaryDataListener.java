package com.ww.template.utils.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson.JSON;
import com.ww.template.entity.UploadDictionaryData;
import com.ww.template.utils.UploadDictionaryUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class UploadDictionaryDataListener implements ReadListener<UploadDictionaryData> {

    private static final int BATCH_COUNT = 10;

    private List<UploadDictionaryData> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);

    private final UploadDictionaryUtil uploadDictionaryUtil;

    public UploadDictionaryDataListener(UploadDictionaryUtil uploadDictionaryUtil) {
        this.uploadDictionaryUtil = uploadDictionaryUtil;
    }


    @Override
    public void invoke(UploadDictionaryData data, AnalysisContext context) {
        log.info("解析到一条数据:{}", JSON.toJSONString(data));
        cachedDataList.add(data);
        if (cachedDataList.size() >= BATCH_COUNT) {
            saveData();
            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
        log.info("所有数据解析完成！");
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        log.info("{}条数据，开始存储数据库！", cachedDataList.size());
        uploadDictionaryUtil.save(cachedDataList);
        log.info("存储数据库成功！");
    }
}
