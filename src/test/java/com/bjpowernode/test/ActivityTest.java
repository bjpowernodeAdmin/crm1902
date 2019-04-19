package com.bjpowernode.test;

import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;
import org.junit.Assert;
import org.junit.Test;

/**
 * author : 动力节点
 * 2019/4/15 0015
 */
public class ActivityTest {

    @Test
    public void testSave(){

        Activity a = new Activity();
        a.setId(UUIDUtil.getUUID());
        a.setName("百度推广宣传");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = as.save(a);

        System.out.println(flag);

        Assert.assertEquals(true, flag);

    }

    @Test
    public void testUpdate(){

        Activity a = new Activity();
        a.setId("06d09c5beed14f29b4e174d48881d3bd12312123123");
        a.setName("开宣传推广会123");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        boolean flag = as.update(a);

        System.out.println(flag);

        //System.out.println(flag);

        Assert.assertEquals(true, flag);

    }


}
