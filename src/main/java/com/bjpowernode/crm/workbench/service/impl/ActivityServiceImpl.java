package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.dao.ActivityDao;
import com.bjpowernode.crm.workbench.dao.ActivityRemarkDao;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author : 动力节点
 * 2019/4/11 0011
 */
public class ActivityServiceImpl implements ActivityService {

    private ActivityDao activityDao = SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    private UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    public boolean save(Activity a) {

        boolean flag = true;

        int count = activityDao.save(a);

        if(count!=1){

            flag = false;

        }

        return flag;

    }

    public PaginationVO<Activity> pageList(Map<String, Object> map) {

        //取得total
        int total = activityDao.getTotalByCondition(map);


        //取得dataList
        List<Activity> dataList = activityDao.getActivityListByCondition(map);


        //创建一个vo对象，封装total和dataList
        PaginationVO<Activity> vo = new PaginationVO<Activity>();
        vo.setTotal(total);
        vo.setDataList(dataList);


        //返回vo
        return vo;
    }

    public boolean delete(String[] ids) {

        boolean flag = true;

        //查询市场活动关联的备注的数量
        int count1 = activityRemarkDao.getCountByAids(ids);

        //删除市场活动关联的备注（得到一个删除的数量）
        int count2 = activityRemarkDao.deleteByAids(ids);

        //需要删除的数量和实际删除的数量，进行比对，如果数量一致，说明备注删除成功
        if(count1!=count2){
            flag = false;
        }

        //删除市场活动
        int count3 = activityDao.delete(ids);
        if(count3!=ids.length){
            flag = false;
        }


        return flag;

    }

    public Map<String, Object> getUserListAndActivity(String id) {

        //取uList
        List<User> uList = userDao.getUserList();

        //根据id取a对象
        Activity a = activityDao.getById(id);

        //将uList和a保存到map中
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("uList", uList);
        map.put("a", a);

        //返回map
        return map;
    }

    public boolean update(Activity a) {
        boolean flag = true;

        int count = activityDao.update(a);

        if(count!=1){

            flag = false;

        }

        return flag;
    }

    public Activity detail(String id) {

        Activity a = activityDao.detail(id);

        return a;

    }

    public List<ActivityRemark> getRemarkListByAid(String aid) {

        List<ActivityRemark> arList = activityRemarkDao.getRemarkListByAid(aid);

        return arList;

    }

    public boolean deleteRemark(String id) {

        boolean flag = true;

        int count = activityRemarkDao.deleteRemark(id);

        if(count!=1){
            flag = false;
        }

        return flag;

    }

    public boolean saveRemark(ActivityRemark ar) {

        boolean flag = true;

        int count = activityRemarkDao.saveRemark(ar);

        if(count!=1){
            flag = false;
        }

        return flag;
    }

    public boolean updateRemark(ActivityRemark ar) {
        boolean flag = true;

        int count = activityRemarkDao.updateRemark(ar);

        if(count!=1){
            flag = false;
        }

        return flag;
    }

    public List<Activity> getActivityListByCid(String clueId) {

        List<Activity> aList = activityDao.getActivityListByCid(clueId);

        return aList;
    }

    public List<Activity> getActivityListByNameAndNotByClueId(Map<String, String> map) {

        List<Activity> aList = activityDao.getActivityListByNameAndNotByClueId(map);

        return aList;
    }

    public List<Activity> getActivityListByName(String aname) {

        List<Activity> aList = activityDao.getActivityListByName(aname);

        return aList;
    }
}

























































