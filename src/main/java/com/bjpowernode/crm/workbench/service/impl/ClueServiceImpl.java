package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.*;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.service.ClueService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * author : 动力节点
 * 2019/4/13 0013
 */
public class ClueServiceImpl implements ClueService {

    private ClueDao clueDao = SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueRemarkDao clueRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);
    private ClueActivityRelationDao clueActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);

    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao = SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);

    private ContactsDao contactsDao = SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao = SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao = SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);

    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);



    public boolean save(Clue c) {

        boolean flag = true;

        int count = clueDao.save(c);

        if(count!=1){
            flag = false;
        }

        return flag;

    }

    public Clue detail(String id) {

        Clue c = clueDao.detail(id);

        return c;
    }

    public boolean unbund(String id) {
        boolean flag = true;
        int count = clueActivityRelationDao.unbund(id);
        if(count!=1){
            flag = false;
        }

        return flag;
    }

    public boolean bund(String cid, String[] aids) {

        boolean flag = true;

        for(String aid:aids){

            ClueActivityRelation car = new ClueActivityRelation();
            car.setId(UUIDUtil.getUUID());
            car.setActivityId(aid);
            car.setClueId(cid);

            int count = clueActivityRelationDao.bund(car);
            if(count!=1){
                flag = false;
            }

        }

        return flag;
    }

    public boolean convert(String clueId, Tran t, String createBy) {

        boolean flag = true;
        String createTime = DateTimeUtil.getSysTime();

        //(1)获取到线索id，通过线索id获取线索对象（线索对象当中封装了线索的信息）
        Clue c = clueDao.getById(clueId);

        //c对象取得之后，将c中的信息提取出来
        //将c中与公司相关的信息提取转换成为客户，将c中与人相关的信息提取出来转换成联系人

        //(2) 通过线索对象c提取客户（公司）信息，当该客户（公司）不存在的时候，新建客户（根据公司的名称精确匹配，判断该客户是否存在！）
        //从线索对象c中，取得公司名称
        String customerName = c.getCompany();

        //根据customerName，到客户表tbl_customer中，查询有没有这个客户，根据公司名称精确匹配

        //查询结果返回Customer cus对象，因为下面的流程要使用到cus对象

        Customer cus = customerDao.getCustomerByName(customerName);

        //判断客户有没有查询到
        //如果没有查询到，需要创建一个新客户
        if(cus==null){

            cus = new Customer();
            cus.setId(UUIDUtil.getUUID());
            cus.setAddress(c.getAddress());
            cus.setContactSummary(c.getContactSummary());
            cus.setCreateBy(createBy);
            cus.setCreateTime(createTime);
            cus.setDescription(c.getDescription());
            cus.setName(customerName);
            cus.setNextContactTime(c.getNextContactTime());
            cus.setOwner(c.getOwner());
            cus.setPhone(c.getPhone());
            cus.setWebsite(c.getWebsite());

            //添加客户
            int count1 = customerDao.save(cus);
            if(count1!=1){
                flag = false;
            }

        }

        //执行完第二步之后，就有客户信息了,以下操作取得客户id，使用cus.getId()就可以了---------------------------------------------------------------

        //(3)通过线索对象提取联系人信息，保存联系人
        Contacts con = new Contacts();
        con.setId(UUIDUtil.getUUID());
        con.setAddress(c.getAddress());
        con.setAppellation(c.getAppellation());
        con.setContactSummary(c.getContactSummary());
        con.setCreateBy(createBy);
        con.setCreateTime(createTime);
        con.setCustomerId(cus.getId());
        con.setDescription(c.getDescription());
        con.setEmail(c.getEmail());
        con.setFullname(c.getFullname());
        con.setJob(c.getJob());
        con.setMphone(c.getMphone());
        con.setSource(c.getSource());
        con.setNextContactTime(c.getNextContactTime());
        con.setOwner(c.getOwner());

        //添加联系人
        int count2 = contactsDao.save(con);
        if(count2!=1){
            flag = false;
        }

        //执行完第三步之后，就有联系人信息了,以下操作取得联系人id，使用con.getId()就可以了------------------------------------------------------

        //(4)线索备注转换到客户备注以及联系人备注
        //查询该线索对应的备注信息列表
        List<ClueRemark> clueRemarkList = clueRemarkDao.getRemarkListByClueId(clueId);
        //遍历线索备注列表,取得每一条线索备注
        for(ClueRemark clueRemark : clueRemarkList){

            //根据每一条线索备注，取得备注信息
            String noteContent = clueRemark.getNoteContent();

            //创建客户备注对象
            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(createTime);
            customerRemark.setCustomerId(cus.getId());
            customerRemark.setEditFlag("0");
            customerRemark.setNoteContent(noteContent);
            //添加客户备注
            int count3 = customerRemarkDao.save(customerRemark);
            if(count3!=1){
                flag = false;
            }


            //创建联系人备注对象
            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setContactsId(con.getId());
            contactsRemark.setEditFlag("0");
            contactsRemark.setNoteContent(noteContent);
            //添加联系人备注
            int count4 = contactsRemarkDao.save(contactsRemark);
            if(count4!=1){
                flag = false;
            }

        }

        //(5) “线索和市场活动”的关系转换到“联系人和市场活动”的关系
        //查询出与该线索关联的线索市场活动的关联关系列表
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationDao.getRelationListByClueId(clueId);
        //遍历查询得到的关联关系列表
        for (ClueActivityRelation clueActivityRelation : clueActivityRelationList){

            //从每一个查询得到的关联关系中，取得关联的市场活动id
            String activityId = clueActivityRelation.getActivityId();

            //将每一个与该线索关联的市场活动id，与第三步产生的联系人做关联
            ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setContactsId(con.getId());
            contactsActivityRelation.setActivityId(activityId);
            //添加联系人与市场活动的关联关系
            int count5 = contactsActivityRelationDao.save(contactsActivityRelation);
            if(count5!=1){
                flag = false;
            }

        }

        //(6) 如果有创建交易需求，创建一条交易
        //如果需要创建交易：判断t是否为null
        if(t!=null){

            //在控制器中，t对象中已经封装了如下属性信息:id,money,name,expectedDate,stage,activityId,createBy,createTime
            //其他的信息，能使用Clue c转换的，尽量转换一下，使我们的t对象的记录更加丰富一些
            t.setContactsId(con.getId());
            t.setCustomerId(cus.getId());
            t.setOwner(c.getOwner());
            t.setContactSummary(c.getContactSummary());
            t.setDescription(c.getDescription());
            t.setNextContactTime(c.getNextContactTime());
            t.setSource(c.getSource());

            //添加交易
            int count6 = tranDao.save(t);
            if(count6!=1){
                flag = false;
            }

            //(7) 如果创建了交易，则创建一条该交易下的交易历史
            TranHistory th = new TranHistory();
            th.setId(UUIDUtil.getUUID());
            th.setCreateBy(createBy);
            th.setCreateTime(createTime);
            th.setExpectedDate(t.getExpectedDate());
            th.setStage(t.getStage());
            th.setMoney(t.getMoney());
            th.setTranId(t.getId());

            //添加交易历史
            int count7 = tranHistoryDao.save(th);
            if(count7!=1){
                flag = false;
            }


        }


        //(8) 删除线索备注
        for(ClueRemark clueRemark : clueRemarkList){

            int count8 = clueRemarkDao.delete(clueRemark);
            if(count8!=1){
                flag = false;
            }

        }

        //(9) 删除线索和市场活动的关系
        for (ClueActivityRelation clueActivityRelation : clueActivityRelationList){

            int count9 = clueActivityRelationDao.delete(clueActivityRelation);
            if(count9!=1){
                flag = false;
            }
        }

        //(10) 删除线索
        int count10 = clueDao.delete(clueId);
        if(count10!=1){
            flag = false;
        }

        return flag;

    }


}


























































































