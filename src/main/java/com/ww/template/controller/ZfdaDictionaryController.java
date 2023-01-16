package com.ww.template.controller;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcel;
import com.ww.template.base.BaseResult;
import com.ww.template.base.PageParam;
import com.ww.template.base.PageResult;
import com.ww.template.entity.UploadDictionaryData;
import com.ww.template.service.IZfdaDictionaryService;
import com.ww.template.utils.UploadDictionaryUtil;
import com.ww.template.utils.listener.UploadDictionaryDataListener;
import com.ww.template.vo.DictionaryTreeVo;
import com.ww.template.vo.IndexVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 字典项表 前端控制器
 * </p>
 *
 * @author iflytek
 * @since 2023-01-12
 */
@Slf4j
@RestController
@Api(tags = {"字典项"})
@RequestMapping("/dictionary")
public class ZfdaDictionaryController {

    @Autowired
    private IZfdaDictionaryService zfdaDictionaryService;

    @Autowired
    private UploadDictionaryUtil uploadDictionaryUtil;

    @PostMapping(value = "/import")
    @ApiOperation("字典数据初始化导入")
    public BaseResult<String> importQuestion(MultipartFile file) throws IOException {
        EasyExcel.read(file.getInputStream(), UploadDictionaryData.class, new UploadDictionaryDataListener(uploadDictionaryUtil)).sheet().doRead();
        return BaseResult.success("success");
    }

    @GetMapping("/getTreeDictByParentCode")
    @ApiOperation("树状字典列表")
    public BaseResult<List<DictionaryTreeVo>> getTreeDictByParentCode(@RequestParam("parentCode") String parentCode) {
        return BaseResult.success(zfdaDictionaryService.getTreeDictByParentCode(parentCode));
    }

    @GetMapping("/queryNode")
    @ApiOperation(value = "获取节点全路径")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", dataType = "String", required = true, value = "叶子节点indexCode", name = "indexCode")
    })
    public BaseResult<DictionaryTreeVo> queryTreeById(@RequestParam(value = "indexCode") String indexCode) {
        DictionaryTreeVo dictionaryTreeVo = zfdaDictionaryService.getNodeDictByIndexCode(indexCode);
        if (ObjectUtil.isNull(dictionaryTreeVo)) {
            return BaseResult.fail("未找到指定节点");
        }
        return BaseResult.success(dictionaryTreeVo);
    }

    @ApiOperation(value = "指标项列表")
    @GetMapping("/queryIndexList")
    public BaseResult<PageResult<IndexVo>> queryIndexList(@RequestParam(value = "indexName", required = false) String indexName, PageParam<IndexVo> page) {
        return BaseResult.success(zfdaDictionaryService.queryIndexList(indexName, page));
    }

    @ApiOperation(value = "指标项修改")
    @PutMapping("/updateIndex")
    public BaseResult<Boolean> updateIndex(@RequestParam(value = "indexCode") String indexCode, @RequestParam(value = "score") int score) {
        return zfdaDictionaryService.updateIndex(indexCode, score);
    }

    @ApiOperation(value = "指标项修改")
    @DeleteMapping("/deleteIndex")
    public BaseResult<Boolean> deleteIndex(@RequestParam(value = "indexCode") String indexCode) {
        return zfdaDictionaryService.deleteIndex(indexCode);
    }

}
