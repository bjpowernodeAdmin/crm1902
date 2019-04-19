package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.settings.dao.DicTypeDao;
import com.bjpowernode.crm.settings.dao.DicValueDao;
import com.bjpowernode.crm.settings.domain.DicType;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DicService;
import com.bjpowernode.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author : 动力节点
 * 2019/4/13 0013
 */
public class DicServiceImpl implements DicService {

    private DicTypeDao dicTypeDao = SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    private DicValueDao dicValueDao = SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);

    public Map<String, List<DicValue>> getAll() {

        Map<String, List<DicValue>> map = new HashMap<String, List<DicValue>>();

        //取得所有的字典类型
        List<DicType> dtList = dicTypeDao.getTypeList();

        //遍历所有的字典类型
        for(DicType dt:dtList){

            //取得每一个字典类型的字典类型编码
            String code = dt.getCode();

            //通过每一个字典类型编码来查询对应的字典值的列表
            List<DicValue> dvList = dicValueDao.getValueListByCode(code);

            //将类型为key，对应的字典值列表为value，保存到map中，返回map
            map.put(code+"List", dvList);

        }

        return map;

    }
}






































