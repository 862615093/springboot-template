package com.ww.template.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author zhiyang7
 */
@Data
@Accessors(chain=true)
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("com.ww.template.base.PageResult")
public class PageResult<T> implements IPage<T> {
    /**
     * 查询数据列表
     */
    protected List<T> records = Collections.emptyList();

    /**
     * 总数
     */
    protected long total = 0;
    /**
     * 每页显示条数，默认 10
     */
    protected long size = 10;

    /**
     * 当前页
     */
    protected long current = 1;

    /**
     * 排序字段信息
     */
    @Setter
    protected List<OrderItem> orders = new ArrayList<>();

    public <E extends IPage<T>> PageResult(E page) {
        this.current = page.getCurrent();
        this.size = page.getSize();
        this.total = page.getTotal();
        this.records = page.getRecords();
        this.orders = page.orders();
    }

    @Override
    public List<OrderItem> orders() {
        return this.orders;
    }
}
