package com.ww.template.service;

import com.ww.template.base.BaseResult;
import com.ww.template.base.PageParam;
import com.ww.template.base.PageResult;
import com.ww.template.entity.ZfdaDictionary;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ww.template.vo.DictionaryTreeVo;
import com.ww.template.vo.IndexVo;

import java.util.List;

/**
 * <p>
 * 字典项表 服务类
 * </p>
 *
 * @author iflytek
 * @since 2023-01-12
 */
public interface IZfdaDictionaryService extends IService<ZfdaDictionary> {

    List<DictionaryTreeVo> getTreeDictByParentCode(String parentCode);

    DictionaryTreeVo getNodeDictByIndexCode(String indexCode);

    PageResult<IndexVo> queryIndexList(String indexName, PageParam<IndexVo> page);

    BaseResult<Boolean> updateIndex(String indexCode, int score);

    BaseResult<Boolean> deleteIndex(String indexCode);
}
