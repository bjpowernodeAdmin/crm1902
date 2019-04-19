package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;

import java.util.List;
import java.util.Map;

/**
 * author : 动力节点
 * 2019/4/16 0016
 */
public interface TranService {
    boolean save(Tran t, String customerName);

    Tran detail(String id);

    List<TranHistory> getHistroyListByTranId(String tranId);

    boolean changeStage(Tran t);

    Map<String, Object> getCharts();
}
