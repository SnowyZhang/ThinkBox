package com.snowy.thinkbox.req;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

public class PageReq {
    @NotNull(message = "当前页数不可为空")
    private int page;
    @NotNull(message = "每页的查询条数不可为空")
    @Max(value = 100, message = "每页的查询条数不可超过100")
    private int size;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "PageReq{" +
                "page=" + page +
                ", size=" + size +
                '}';
    }
}