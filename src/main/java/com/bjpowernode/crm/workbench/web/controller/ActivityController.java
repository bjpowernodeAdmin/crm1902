package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.*;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到市场活动模块控制器");

        String path = request.getServletPath();

        if("/workbench/activity/getUserList.do".equals(path)){

            getUserList(request,response);

        }else if("/workbench/activity/save.do".equals(path)){

            save(request,response);

        }else if("/workbench/activity/pageList.do".equals(path)){

            pageList(request,response);

        }else if("/workbench/activity/delete.do".equals(path)){

            delete(request,response);

        }else if("/workbench/activity/getUserListAndActivity.do".equals(path)){

            getUserListAndActivity(request,response);

        }else if("/workbench/activity/update.do".equals(path)){

            update(request,response);

        }else if("/workbench/activity/detail.do".equals(path)){

            detail(request,response);

        }else if("/workbench/activity/getRemarkListByAid.do".equals(path)){

            getRemarkListByAid(request,response);

        }else if("/workbench/activity/deleteRemark.do".equals(path)){

            deleteRemark(request,response);

        }else if("/workbench/activity/saveRemark.do".equals(path)){

            saveRemark(request,response);

        }else if("/workbench/activity/updateRemark.do".equals(path)){

            updateRemark(request,response);

        }

    }

    private void updateRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("修改市场活动备注的操作");

        String id = request.getParameter("id");
        String noteContent = request.getParameter("noteContent");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "1";

        ActivityRemark ar = new ActivityRemark();
        ar.setId(id);
        ar.setNoteContent(noteContent);
        ar.setEditBy(editBy);
        ar.setEditTime(editTime);
        ar.setEditFlag(editFlag);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.updateRemark(ar);

        //需要为前端提供flag，实体类ar
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("success", flag);
        map.put("ar", ar);

        PrintJson.printJsonObj(response,map);

    }

    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("添加市场活动备注的操作");

        String noteContent = request.getParameter("noteContent");
        String activityId = request.getParameter("activityId");
        String id = UUIDUtil.getUUID();
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag = "0";

        ActivityRemark ar = new ActivityRemark();
        ar.setId(id);
        ar.setActivityId(activityId);
        ar.setNoteContent(noteContent);
        ar.setCreateBy(createBy);
        ar.setCreateTime(createTime);
        ar.setEditFlag(editFlag);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.saveRemark(ar);

        //需要为前端提供flag，实体类ar
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("success", flag);
        map.put("ar", ar);

        PrintJson.printJsonObj(response,map);


    }

    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("删除市场活动备注的操作");

        String id = request.getParameter("id");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.deleteRemark(id);

        PrintJson.printJsonFlag(response, flag);

    }

    private void getRemarkListByAid(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("根据市场活动id，取得备注信息列表");

        String aid = request.getParameter("aid");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<ActivityRemark> arList = as.getRemarkListByAid(aid);

        PrintJson.printJsonObj(response, arList);


    }

    private void detail(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

        System.out.println("跳转到详细信息页的操作");

        String id = request.getParameter("id");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        Activity a = as.detail(id);

        request.setAttribute("a", a);
        request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request, response);

    }

    private void update(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行修改操作");

        String id = request.getParameter("id");
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        //修改时间：当前的系统时间
        String editTime = DateTimeUtil.getSysTime();
        //修改人：当前登录用户
        String editBy = ((User)request.getSession().getAttribute("user")).getName();

        Activity a = new Activity();
        a.setId(id);
        a.setOwner(owner);
        a.setName(name);
        a.setStartDate(startDate);
        a.setEndDate(endDate);
        a.setCost(cost);
        a.setDescription(description);
        a.setEditBy(editBy);
        a.setEditTime(editTime);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.update(a);

        PrintJson.printJsonFlag(response, flag);



    }

    private void getUserListAndActivity(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("查询 用户列表+市场活动单条信息 的操作");

        String id = request.getParameter("id");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        /*

            返回
                uList
                a

                map

         */
        Map<String,Object> map = as.getUserListAndActivity(id);

        PrintJson.printJsonObj(response,map);

    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行删除操作");

        String ids[] = request.getParameterValues("id");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.delete(ids);

        PrintJson.printJsonFlag(response, flag);

    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到查询市场活动信息列表 （结合条件查询+分页查询） 的操作");

        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String pageNoStr = request.getParameter("pageNo");
        String pageSizeStr = request.getParameter("pageSize");

        int pageNo = Integer.valueOf(pageNoStr);
        int pageSize = Integer.valueOf(pageSizeStr);

        //计算出略过的记录数
        int skipCount = (pageNo-1)*pageSize;

        //将sql语句用到的6个参数保存到map中
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        /*

            调用业务层方法：

                参数：
                    2个分页参数
                    4个条件参数

                返回值：
                    total
                    dataList

                    业务层需要同时为控制器返回total和dataList，两个值没办法同时返回
                    随意在业务层需要将这两个值处理为一个值，进行返回
                    如果处理成为一个值

                    方式1：
                        将total和dataList保存到map中，返回map

                    方式2：
                        创建一个vo类，vo类中包含total属性和dataList属性，将total和dataList封装到vo对象中，返回vo

                    如果返回值，复用率高，我们创建一个vo类，使用方便
                    如果返回值，就这一个需求使用到，没有必要创建一个vo类（开销太大），临时使用map即可

                    对于我们现在的这个需求，将来的复用率肯定高（因为不仅仅只是市场活动模块要执行分页查询操作，所有模块都需要分页查询操作）
                    所以我们选择使用vo

                    结论：返回vo对象（就相当于返回了total和dataList）


         */
        PaginationVO<Activity> vo = as.pageList(map);

        PrintJson.printJsonObj(response, vo);

    }

    private void save(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到市场活动的添加操作");

        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String name = request.getParameter("name");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String cost = request.getParameter("cost");
        String description = request.getParameter("description");
        //创建时间：当前的系统时间
        String createTime = DateTimeUtil.getSysTime();
        //创建人：当前登录用户
        String createBy = ((User)request.getSession().getAttribute("user")).getName();

        Activity a = new Activity();
        a.setId(id);
        a.setOwner(owner);
        a.setName(name);
        a.setStartDate(startDate);
        a.setEndDate(endDate);
        a.setCost(cost);
        a.setDescription(description);
        a.setCreateBy(createBy);
        a.setCreateTime(createTime);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        boolean flag = as.save(a);

        PrintJson.printJsonFlag(response, flag);


    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到查询用户信息列表的操作");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> uList =  us.getUserList();

        //将uList解析为json串[{用户1},{用户2},{用户3}...]
        PrintJson.printJsonObj(response, uList);

    }


}















































