package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.CustomerDao;
import com.bjpowernode.crm.workbench.dao.TranDao;
import com.bjpowernode.crm.workbench.dao.TranHistoryDao;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.service.TranService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * author : 动力节点
 * 2019/4/16 0016
 */
public class TranServiceImpl implements TranService {

    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    public boolean save(Tran t, String customerName) {

        boolean flag = true;

        /*

            1.根据客户名称到到客户表精确查询，判断有没有此客户
                如果有，则将该客户查询出来，取其id，为t对象的customerId字段赋值
                如果没有，则根据该名称创建一个新客户，将新客户的id取得，为t对象的customerId字段赋值

         */
        Customer cus = customerDao.getCustomerByName(customerName);
        if(cus==null){

            cus = new Customer();
            cus.setId(UUIDUtil.getUUID());
            cus.setName(customerName);
            cus.setCreateTime(DateTimeUtil.getSysTime());
            cus.setCreateBy(t.getCreateBy());

            //添加客户操作
            int count1 = customerDao.save(cus);
            if(count1!=1){
                flag = false;
            }

        }

        //添加交易
        t.setCustomerId(cus.getId());
        int count2 = tranDao.save(t);
        if(count2!=1){
            flag = false;
        }

        //添加交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setTranId(t.getId());
        th.setMoney(t.getMoney());
        th.setStage(t.getStage());
        th.setExpectedDate(t.getExpectedDate());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setCreateBy(t.getCreateBy());
        int count3 = tranHistoryDao.save(th);
        if(count3!=1){
            flag = false;
        }

        return flag;


    }

    public Tran detail(String id) {

        Tran t = tranDao.detail(id);

        return t;
    }

    public List<TranHistory> getHistroyListByTranId(String tranId) {

        List<TranHistory> thList = tranHistoryDao.getHistroyListByTranId(tranId);

        return thList;
    }

    public boolean changeStage(Tran t) {

        boolean flag = true;

        //修改交易阶段
        int count1 = tranDao.changeStage(t);

        //交易阶段每一次变更，都需要伴随着生成交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setTranId(t.getId());
        th.setMoney(t.getMoney());
        th.setStage(t.getStage());
        th.setExpectedDate(t.getExpectedDate());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setCreateBy(t.getEditBy());
        int count2 = tranHistoryDao.save(th);
        if(count2!=1){
            flag = false;
        }

        return flag;
    }

    public Map<String, Object> getCharts() {

        //取total
        int total = tranDao.getTotal();


        //取dataList
        /*

            我们要的：
                List<Map<String,Object>> mapList --> [{value: 数量, name: '阶段名称'},{value: 数量, name: '阶段名称'},{value: 数量, name: '阶段名称'}]

                    每一个{value: 数量, name: '阶段名称'}就是一个map

                    Map map1
                        map1.put("value",65);
                        map1.put("name","01资质审查")

                    Map map2
                    map2.put("value",79);
                    map3.put("name","02需求分析")

                    Map map3
                    map3.put("value",165);
                    map3.put("name","03价值建议")

                    ...

                    ...


            如果是以前我们总是要：
                List<User> uList --> [{用户1},{2},{3}]
                List<Activity> aList -->[{市场活动1},{2},{3}]

         */
        List<Map<String,Object>> dataList = tranDao.getCharts();


        //将total和dataList保存到map中
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("total", total);
        map.put("dataList", dataList);

        //返回map
        return map;
    }
}











































