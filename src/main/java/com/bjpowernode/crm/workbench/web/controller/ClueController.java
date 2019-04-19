package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueService;
import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;
import com.bjpowernode.crm.workbench.service.impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ClueController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到线索模块控制器");

        String path = request.getServletPath();

        if ("/workbench/clue/getUserList.do".equals(path)) {

            getUserList(request, response);

        } else if ("/workbench/clue/save.do".equals(path)) {

            save(request, response);

        } else if ("/workbench/clue/detail.do".equals(path)) {

            detail(request, response);

        } else if ("/workbench/clue/getActivityListByCid.do".equals(path)) {

            getActivityListByCid(request, response);

        } else if ("/workbench/clue/unbund.do".equals(path)) {

            unbund(request, response);

        } else if ("/workbench/clue/getActivityListByNameAndNotByClueId.do".equals(path)) {

            getActivityListByNameAndNotByClueId(request, response);

        } else if ("/workbench/clue/bund.do".equals(path)) {

            bund(request, response);

        } else if ("/workbench/clue/getActivityListByName.do".equals(path)) {

            getActivityListByName(request, response);

        } else if ("/workbench/clue/convert.do".equals(path)) {

            convert(request, response);

        }

    }

    private void convert(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

        System.out.println("执行线索转换的操作");

        //不论是否创建交易，参数clueId必须传递进来，执行线索转换
        String clueId = request.getParameter("clueId");

        //判断是否需要创建交易
        String flag = request.getParameter("flag");

        Tran t = null;

        String createBy = ((User)request.getSession().getAttribute("user")).getName();

        if("a".equals(flag)){

            //如果创建交易
            //将Tran t对象创建出来进入到if语句块中，表示需要
            t = new Tran();
            //将转换页面交易表单中的参数信息接收进来
            String money = request.getParameter("money");
            String name = request.getParameter("name");
            String expectedDate = request.getParameter("expectedDate");
            String stage = request.getParameter("stage");
            String activityId = request.getParameter("activityId");
            String id = UUIDUtil.getUUID();
            String createTime = DateTimeUtil.getSysTime();


            t.setId(id);
            t.setMoney(money);
            t.setName(name);
            t.setExpectedDate(expectedDate);
            t.setStage(stage);
            t.setActivityId(activityId);
            t.setCreateBy(createBy);
            t.setCreateTime(createTime);

        }

        //执行完以上操作后，如果t==null说明不需要创建交易，如果t!=null，说明需要创建交易

        /*
            调用业务层对象执行线索转换的操作

            需要为业务层convert方法传递哪些参数？
                clueId：做线索转换用
                t：在业务层创建交易

                在业务层执行线索转换的过程当中，需要做大量的添加的操作insert
                只要是添加操作，就离不开填写两个字段信息，createBy，createTime

                其中createTime在业务层是可以随时取得的
                但是createBy在业务层能拿到吗？因为业务层里面没有request对象

                所以，除了为业务层传递clueId和t之外，还需要为业务层传递createBy


         */
        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag1 = cs.convert(clueId,t,createBy);

        if(flag1){

            response.sendRedirect(request.getContextPath() + "/workbench/clue/index.jsp");

        }


    }

    private void getActivityListByName(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("查询市场活动信息列表（根据名称模糊查询）");

        String aname = request.getParameter("aname");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> aList = as.getActivityListByName(aname);

        PrintJson.printJsonObj(response, aList);

    }

    private void bund(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("线索关联市场活动操作");

        String cid = request.getParameter("cid");
        String aids[] = request.getParameterValues("aid");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.bund(cid,aids);

        PrintJson.printJsonFlag(response, flag);

    }

    private void getActivityListByNameAndNotByClueId(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("取得市场活动列表（根据名称模糊查+非关联的clueId的列表）");

        String aname = request.getParameter("aname");
        String clueId = request.getParameter("clueId");

        Map<String,String> map = new HashMap<String,String>();
        map.put("aname", aname);
        map.put("clueId", clueId);

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> aList = as.getActivityListByNameAndNotByClueId(map);

        PrintJson.printJsonObj(response, aList);

    }

    private void unbund(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("解除关联操作");

        String id = request.getParameter("id");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.unbund(id);

        PrintJson.printJsonFlag(response, flag);

    }

    private void getActivityListByCid(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("查询指定线索关联的市场活动列表");

        String clueId = request.getParameter("clueId");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());

        List<Activity> aList = as.getActivityListByCid(clueId);

        PrintJson.printJsonObj(response, aList);

    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {

        System.out.println("跳转到指定线索详细信息页");

        String id = request.getParameter("id");

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        Clue c = cs.detail(id);

        request.setAttribute("c", c);
        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request, response);


    }

    private void save(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("执行线索添加操作");

        String id = UUIDUtil.getUUID();
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        Clue c = new Clue();
        c.setId(id);
        c.setAddress(address);
        c.setAppellation(appellation);
        c.setCompany(company);
        c.setContactSummary(contactSummary);
        c.setCreateBy(createBy);
        c.setCreateTime(createTime);
        c.setDescription(description);
        c.setJob(job);
        c.setEmail(email);
        c.setPhone(phone);
        c.setWebsite(website);
        c.setMphone(mphone);
        c.setState(state);
        c.setSource(source);
        c.setNextContactTime(nextContactTime);
        c.setFullname(fullname);
        c.setOwner(owner);

        ClueService cs = (ClueService) ServiceFactory.getService(new ClueServiceImpl());

        boolean flag = cs.save(c);

        PrintJson.printJsonFlag(response, flag);

    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到取得用户信息列表的操作");

        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> uList = us.getUserList();

        PrintJson.printJsonObj(response, uList);

    }


}





















