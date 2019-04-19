package com.bjpowernode.crm.vo;

import com.bjpowernode.crm.workbench.domain.Activity;

import java.util.List;

/**
 * author : 动力节点
 * 2019/4/12 0012
 */
public class PaginationVO<T> {

    private int total;

    private List<T> dataList;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}
