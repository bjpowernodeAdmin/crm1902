package com.bjpowernode.crm.web.listener;

import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DicService;
import com.bjpowernode.crm.settings.service.impl.DicServiceImpl;
import com.bjpowernode.crm.utils.ServiceFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

/**
 * author : 动力节点
 * 2019/4/13 0013
 */
public class SysInitListener implements ServletContextListener {

    /*

        参数event：可以取得监听的对象
            例如我们现在监听的是上下文对象，那么我们就可以使用event来取得上下文对象

     */
    public void contextInitialized(ServletContextEvent event) {

        //System.out.println("上下文对象创建了");

        System.out.println("初始化数据字典开始");

        //取得上下文对象
        ServletContext application = event.getServletContext();

        DicService ds = (DicService) ServiceFactory.getService(new DicServiceImpl());

        Map<String, List<DicValue>> map = ds.getAll();

        Set<String> set = map.keySet();

        for(String key:set){

            application.setAttribute(key, map.get(key));

        }

        System.out.println("初始化数据字典结束");


        //处理阶段和可能性之间的对应关系
        //解析stage2Possibility.properties文件，将该文件中的信息保存为map
        //将map保存到application中

        ResourceBundle rb = ResourceBundle.getBundle("Stage2Possibility");

        System.out.println("-----------------"+rb);

        Enumeration<String> e = rb.getKeys();

        Map<String,String> pMap = new HashMap<String,String>();

        while(e.hasMoreElements()){

            String stage = e.nextElement();
            String possibility = rb.getString(stage);

            pMap.put(stage, possibility);

        }

        application.setAttribute("pMap",pMap);

    }
}






























