package com.bjpowernode.crm.workbench.domain;

/**
 * author : 动力节点
 * 2019/4/11 0011
 */
public class ActivityRemark {

    private String id;  //编号
    private String noteContent;  //备注信息
    private String createTime;  //创建时间
    private String createBy;  //创建人
    private String editTime;  //修改时间
    private String editBy;  //修改人
    private String editFlag;  //修改标记 通过该字段用来表名该条信息有没有修改过 0：未修改 1：以修改
    private String activityId;  //市场活动编号

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
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

    public String getEditFlag() {
        return editFlag;
    }

    public void setEditFlag(String editFlag) {
        this.editFlag = editFlag;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }
}
