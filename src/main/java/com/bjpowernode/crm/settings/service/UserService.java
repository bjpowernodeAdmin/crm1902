package com.bjpowernode.crm.settings.service;

import com.bjpowernode.crm.exception.LoginException;
import com.bjpowernode.crm.settings.domain.User;

import java.util.List;

/**
 * author : 动力节点
 * 2019/4/9 0009
 */
public interface UserService {
    User login(String loginAct, String loginPwd, String ip) throws LoginException;

    List<User> getUserList();
}
