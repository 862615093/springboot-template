package com.ww.template.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ww.template.base.BaseResult;
import com.ww.template.base.PageParam;
import com.ww.template.base.PageResult;
import com.ww.template.entity.ZfdaDictionary;
import com.ww.template.mapper.ZfdaDictionaryMapper;
import com.ww.template.service.IZfdaDictionaryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ww.template.vo.DictionaryTreeVo;
import com.ww.template.vo.IndexVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 字典项表 服务实现类
 * </p>
 *
 * @author iflytek
 * @since 2023-01-12
 */
@Service
@Slf4j
public class ZfdaDictionaryServiceImpl extends ServiceImpl<ZfdaDictionaryMapper, ZfdaDictionary> implements IZfdaDictionaryService {

    @Autowired
    private ZfdaDictionaryMapper zfdaDictionaryMapper;

    @Override
    public List<DictionaryTreeVo> getTreeDictByParentCode(String parentCode) {
        return zfdaDictionaryMapper.selectTreeDictByParentCode(parentCode);
    }

    @Override
    public DictionaryTreeVo getNodeDictByIndexCode(String indexCode) {
        DictionaryTreeVo dictionaryTreeVo = zfdaDictionaryMapper.selectNodeDictByIndexCode(indexCode);
        if (ObjectUtil.isNull(dictionaryTreeVo)) {
            log.error("indexCode={},节点不存在", indexCode);
            return null;
        }
        return getNodeDict(dictionaryTreeVo);
    }

    @Override
    public PageResult<IndexVo> queryIndexList(String indexName, PageParam<IndexVo> page) {
        return zfdaDictionaryMapper.queryIndexList(indexName, page.toPage());
    }

    @Override
    public BaseResult<Boolean> updateIndex(String indexCode, int score) {
        if (this.lambdaUpdate().eq(ZfdaDictionary::getIndexCode, indexCode).set(ZfdaDictionary::getIndexValue, score).update()) {
            return BaseResult.success(true);
        }
        return BaseResult.fail();
    }

    @Override
    public BaseResult<Boolean> deleteIndex(String indexCode) {
        if (this.lambdaUpdate().eq(ZfdaDictionary::getIndexCode, indexCode).set(ZfdaDictionary::getValid, false).update()) {
            return BaseResult.success(true);
        }
        return BaseResult.fail();
    }

    private DictionaryTreeVo getNodeDict(DictionaryTreeVo dictionaryTreeVo) {
        if (dictionaryTreeVo.getParentCode().equals("0")) {
            return dictionaryTreeVo;
        }
        DictionaryTreeVo nodeDict = zfdaDictionaryMapper.selectNodeDictByParentCode(dictionaryTreeVo.getParentCode());
        if (ObjectUtil.isNull(nodeDict)) {
            log.error("indexCode={},节点异常", dictionaryTreeVo.getIndexCode());
            return null;
        }
        ArrayList<DictionaryTreeVo> nodeDictTree = new ArrayList<>();
        nodeDictTree.add(dictionaryTreeVo);
        nodeDict.setChildren(nodeDictTree);
        return getNodeDict(nodeDict);
    }
}
