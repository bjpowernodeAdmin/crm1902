package com.bjpowernode.crm.settings.domain;

/**
 * author : 动力节点
 * 2019/4/9 0009
 */
public class User {

    /*
    *
    * 关于日期时间相关的字符串，在实际项目开发中有两种表现形式
    *
    * 年月日：
    *   yyyy-MM-dd 10位
    *
    * 年月日时分秒：
    *   yyyy-MM-dd HH:mm:ss 19位
    *
    * */

    /*
    *
    * 关于登录
    *
    *   验证账号和密码 loginAct和loginPwd
    *   账号密码验证通过后，需要继续验证
    *   验证失效时间：expireTime
    *   验证锁定状态：lockState
    *   验证允许访问的ip地址：allowIps
    *
    *
    * */
    private String id;          //主键 32位UUID
    private String loginAct;    //登录账号
    private String name;        //真实姓名
    private String loginPwd;    //登录密码
    private String email;       //邮箱
    private String expireTime;  //失效时间  年月日时分秒
    private String lockState;   //锁定状态  0：锁定   1：启用
    private String deptno;      //部门编号
    private String allowIps;    //允许访问的ip地址 例如：192.168.1.1,192.168.1.2,192.168.1.3
    private String createTime;  //创建时间 系统当前的时间 年月日时分秒
    private String createBy;    //创建人  当前登录的用户
    private String editTime;    //修改时间 系统当前的时间 年月日时分秒
    private String editBy;      //修改人 当前登录的用户

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginAct() {
        return loginAct;
    }

    public void setLoginAct(String loginAct) {
        this.loginAct = loginAct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginPwd() {
        return loginPwd;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public String getLockState() {
        return lockState;
    }

    public void setLockState(String lockState) {
        this.lockState = lockState;
    }

    public String getDeptno() {
        return deptno;
    }

    public void setDeptno(String deptno) {
        this.deptno = deptno;
    }

    public String getAllowIps() {
        return allowIps;
    }

    public void setAllowIps(String allowIps) {
        this.allowIps = allowIps;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getEditTime() {
        return editTime;
    }

    public void setEditTime(String editTime) {
        this.editTime = editTime;
    }

    public String getEditBy() {
        return editBy;
    }

    public void setEditBy(String editBy) {
        this.editBy = editBy;
    }
}
