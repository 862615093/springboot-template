package com.ww.template.base;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author zhiyang7
 */
@Data
@Accessors(chain=true)
@ApiModel("com.ww.template.base.PageParam")
public class PageParam<T> {
    /**
     * 每页显示条数，默认 10
     */
    protected long size = 10;

    /**
     * 当前页
     */
    protected long current = 1;

    public PageResult<T> toPage() {
        PageResult<T> result = new PageResult<>();
        result.setCurrent(current);
        result.setSize(size);
        return result;
    }
}
