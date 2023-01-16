package com.ww.template.mapper;

import com.ww.template.base.PageResult;
import com.ww.template.entity.ZfdaDictionary;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ww.template.vo.DictionaryTreeVo;
import com.ww.template.vo.IndexVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 字典项表 Mapper 接口
 * </p>
 *
 * @author iflytek
 * @since 2023-01-12
 */
@Mapper
public interface ZfdaDictionaryMapper extends BaseMapper<ZfdaDictionary> {

    List<DictionaryTreeVo> selectTreeDictByParentCode(@Param("parentCode") String parentCode);

    DictionaryTreeVo selectNodeDictByParentCode(@Param("parentCode") String parentCode);

    DictionaryTreeVo selectNodeDictByIndexCode(@Param("indexCode") String indexCode);

    PageResult<IndexVo> queryIndexList(@Param("indexName") String indexName, PageResult<IndexVo> toPage);
}
