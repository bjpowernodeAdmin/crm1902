package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.ActivityRemark;

import java.util.List;

/**
 * author : 动力节点
 * 2019/4/11 0011
 */
public interface ActivityRemarkDao {


    int getCountByAids(String[] ids);

    int deleteByAids(String[] ids);

    List<ActivityRemark> getRemarkListByAid(String aid);

    int deleteRemark(String id);

    int saveRemark(ActivityRemark ar);

    int updateRemark(ActivityRemark ar);
}
