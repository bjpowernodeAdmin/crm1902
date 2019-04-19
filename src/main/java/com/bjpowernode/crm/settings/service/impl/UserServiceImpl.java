package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.exception.LoginException;
import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author : 动力节点
 * 2019/4/9 0009
 */
public class UserServiceImpl implements UserService {

    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);


    public User login(String loginAct, String loginPwd, String ip) throws LoginException {

        /*

            根据账号密码查询user对象
                    select * from tbl_user where loginAct=#{loginAct} and loginPwd=#{loginPwd}

            判断user对象，如果user对象为null，说明账号密码错误，为控制器抛出自定义异常，异常信息 账号密码错误

            如果user对象不为null
                从user对象中取 expireTime，lockState，allowIps

                验证expireTime，如果失效时间小于当前系统时间，为控制器抛出自定义异常，异常信息 账号已失效

                验证lockState，如果lockState的状态码为0，表示账号锁定了，为控制器抛出自定义异常，异常信息 账号已锁定

                验证ip地址，ip地址如果没有包含在allowIps当中，表示ip地址是无效的，为控制器抛出自定义异常，异常信息 ip地址受限

            如果验证以上信息，没有抛出任何的异常，将user返回到控制器


         */

        Map<String,String> map = new HashMap<String,String>();

        map.put("loginAct", loginAct);
        map.put("loginPwd", loginPwd);

        User user = userDao.login(map);

        if(user==null){

            throw new LoginException("账号密码错误");

        }

        //如果程序顺利的执行到了该行，证明账号密码正确
        //开始验证其他信息

        // 验证expireTime，如果失效时间小于当前系统时间，为控制器抛出自定义异常，异常信息 账号已失效
        String expireTime = user.getExpireTime();
        String currentTime = DateTimeUtil.getSysTime();
        if(expireTime.compareTo(currentTime)<0){

            throw new LoginException("账号已失效");

        }

        // 验证lockState，如果lockState的状态码为0，表示账号锁定了，为控制器抛出自定义异常，异常信息 账号已锁定
        String lockState = user.getLockState();
        if("0".equals(lockState)){

            throw new LoginException("账号已锁定");

        }


        // 验证ip地址，ip地址如果没有包含在allowIps当中，表示ip地址是无效的，为控制器抛出自定义异常，异常信息 ip地址受限
        String allowIps = user.getAllowIps();
        if(!allowIps.contains(ip)){

            throw new LoginException("ip地址受限");

        }


        return user;

    }

    public List<User> getUserList() {

        List<User> uList = userDao.getUserList();

        return uList;
    }
}

























































