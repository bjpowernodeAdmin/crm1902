package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.workbench.dao.CustomerDao;
import com.bjpowernode.crm.workbench.service.CustomerService;

import java.util.List;

/**
 * author : 动力节点
 * 2019/4/16 0016
 */
public class CustomerServiceImpl implements CustomerService {

    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    public List<String> getCustomerNameListByName(String name) {

        List<String> sList = customerDao.getCustomerNameListByName(name);

        return sList;
    }
}
