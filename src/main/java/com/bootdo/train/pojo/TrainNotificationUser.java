package com.bootdo.train.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

//通知通报权限表
public class TrainNotificationUser {

    private Long id;

    private Long notificationId;

    private Long userId;

    private int status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String creater;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operateTime;

    private String  operater;

    private String name;
    /*
       关联查询数据
    */
    private String title;
    /*
        自定义数据
     */
    private String statusType;

    public String getStatusType() {
        if (this.status == 0 ){
            return "未查阅";
        }else if (this.status ==1){
            return "已查阅";
        }
        return statusType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(Long notificationId) {
        this.notificationId = notificationId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreater() {
        return creater;
    }

    public void setCreater(String creater) {
        this.creater = creater;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    public String getOperater() {
        return operater;
    }

    public void setOperater(String operater) {
        this.operater = operater;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }
}
