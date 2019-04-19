package com.bjpowernode.crm.settings.dao;

import com.bjpowernode.crm.settings.domain.DicValue;

import java.util.List;

/**
 * author : 动力节点
 * 2019/4/13 0013
 */
public interface DicValueDao {
    List<DicValue> getValueListByCode(String code);
}
