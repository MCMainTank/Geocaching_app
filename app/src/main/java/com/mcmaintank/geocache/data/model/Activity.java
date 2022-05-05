package com.mcmaintank.geocache.data.model;

import java.util.Date;

public class Activity {

    private Integer activityId;

    private Date activityDateOfUpload;

    private Integer userId;

    private Integer geocacheId;

    private String activityType;

    private String activityContent;

    private boolean deleted;

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Date getActivityDateOfUpload() {
        return activityDateOfUpload;
    }

    public void setActivityDateOfUpload(Date activityDateOfUpload) {
        this.activityDateOfUpload = activityDateOfUpload;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getGeocacheId() {
        return geocacheId;
    }

    public void setGeocacheId(Integer geocacheId) {
        this.geocacheId = geocacheId;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getActivityContent() {
        return activityContent;
    }

    public void setActivityContent(String activityContent) {
        this.activityContent = activityContent;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }





}
