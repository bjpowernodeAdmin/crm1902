package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueService;
import com.bjpowernode.crm.workbench.service.CustomerService;
import com.bjpowernode.crm.workbench.service.TranService;
import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;
import com.bjpowernode.crm.workbench.service.impl.ClueServiceImpl;
import com.bjpowernode.crm.workbench.service.impl.CustomerServiceImpl;
import com.bjpowernode.crm.workbench.service.impl.TranServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranController extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("进入到交易模块控制器");

        String path = request.getServletPath();

        if ("/workbench/transaction/add.do".equals(path)) {

            add(request, response);

        } else if ("/workbench/transaction/getCustomerNameListByName.do".equals(path)) {

            getCustomerNameListByName(request, response);

        } else if ("/workbench/transaction/save.do".equals(path)) {

            save(request, response);

        } else if ("/workbench/transaction/detail.do".equals(path)) {

            detail(request, response);

        } else if ("/workbench/transaction/getHistroyListByTranId.do".equals(path)) {

            getHistroyListByTranId(request, response);

        } else if ("/workbench/transaction/changeStage.do".equals(path)) {

            changeStage(request, response);

        } else if ("/workbench/transaction/getCharts.do".equals(path)) {

            getCharts(request, response);

        }

    }

    private void getCharts(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("取得交易阶段统计图所需数据");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        /*

            {"total":100,"dataList":[{value: 数量, name: '阶段名称'},{value: 数量, name: '阶段名称'},{value: 数量, name: '阶段名称'}]}

         */
        Map<String,Object> map = ts.getCharts();

        PrintJson.printJsonObj(response, map);

    }

    private void changeStage(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("进入到 改变阶段 的操作");

        String id = request.getParameter("id");
        String stage = request.getParameter("stage");
        String money = request.getParameter("money");
        String expectedDate = request.getParameter("expectedDate");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();

        Tran t = new Tran();
        t.setId(id);
        t.setStage(stage);
        t.setMoney(money);
        t.setExpectedDate(expectedDate);
        t.setEditBy(editBy);
        t.setEditTime(editTime);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag = ts.changeStage(t);

        //处理可能性
        Map<String,String> pMap = (Map<String,String>)this.getServletContext().getAttribute("pMap");
        String possibility = pMap.get(stage);
        t.setPossibility(possibility);

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("success", flag);
        map.put("t", t);

        PrintJson.printJsonObj(response, map);

    }

    private void getHistroyListByTranId(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("根据交易id取得交易历史列表的操作");

        String tranId = request.getParameter("tranId");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        Map<String,String> pMap = (Map<String,String>)this.getServletContext().getAttribute("pMap");

        List<TranHistory> thList = ts.getHistroyListByTranId(tranId);

        //遍历每一个交易历史
        for(TranHistory th:thList){

            //取得每一个交易历史中的阶段
            String stage = th.getStage();

            //通过阶段取得可能性
            String possibility = pMap.get(stage);

            //将可能性赋值到每一个交易历史中
            th.setPossibility(possibility);

        }


        PrintJson.printJsonObj(response, thList);


    }

    private void detail(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

        System.out.println("进入到跳转到交易详细信息页的操作");

        String id = request.getParameter("id");

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        Tran t = ts.detail(id);

        //处理可能性
        //需要阶段，以及阶段和可能性之间的对应关系
        String stage = t.getStage();
        Map<String,String> pMap = (Map<String,String>)this.getServletContext().getAttribute("pMap");
        String possibility = pMap.get(stage);
        t.setPossibility(possibility);

        request.setAttribute("t", t);
        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request, response);

    }

    private void save(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

        System.out.println("执行交易添加操作");

        String id = UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String money = request.getParameter("money");
        String name = request.getParameter("name");
        String expectedDate = request.getParameter("expectedDate");

        //我们现在从表单中只能拿到customerName，一会在业务层处理customerId的问题
        String customerName = request.getParameter("customerName");

        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String activityId = request.getParameter("activityId");
        String contactsId = request.getParameter("contactsId");
        String createTime = DateTimeUtil.getSysTime();
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");

        Tran t = new Tran();
        t.setId(id);
        t.setOwner(owner);
        t.setMoney(money);
        t.setName(name);
        t.setExpectedDate(expectedDate);
        t.setStage(stage);
        t.setType(type);
        t.setSource(source);
        t.setActivityId(activityId);
        t.setContactsId(contactsId);
        t.setCreateTime(createTime);
        t.setCreateBy(createBy);
        t.setDescription(description);
        t.setContactSummary(contactSummary);
        t.setNextContactTime(nextContactTime);

        TranService ts = (TranService) ServiceFactory.getService(new TranServiceImpl());

        boolean flag = ts.save(t,customerName);

        if(flag){

            response.sendRedirect(request.getContextPath() + "/workbench/transaction/index.jsp");

        }


    }

    private void getCustomerNameListByName(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("根据客户名称查询名称列表（供自动补全插件用的数据）");

        String name = request.getParameter("name");

        CustomerService cs = (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());

        List<String> sList = cs.getCustomerNameListByName(name);

        PrintJson.printJsonObj(response, sList);



    }

    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {

        System.out.println("跳转到交易添加页");

        //走后台的目的是为了取得用户列表
        UserService us = (UserService) ServiceFactory.getService(new UserServiceImpl());

        List<User> uList = us.getUserList();

        request.setAttribute("uList", uList);
        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request, response);

    }


}





















