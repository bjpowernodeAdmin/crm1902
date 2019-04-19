package com.bjpowernode.crm.settings.dao;

import com.bjpowernode.crm.settings.domain.User;

import java.util.List;
import java.util.Map;

/**
 * author : 动力节点
 * 2019/4/9 0009
 */
public interface UserDao {


    User login(Map<String, String> map);

    List<User> getUserList();
}
