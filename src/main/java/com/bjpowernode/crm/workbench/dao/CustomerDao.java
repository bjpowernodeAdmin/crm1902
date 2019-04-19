package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Customer;

import java.util.List;

public interface CustomerDao {

    Customer getCustomerByName(String customerName);

    int save(Customer cus);

    List<String> getCustomerNameListByName(String name);
}
