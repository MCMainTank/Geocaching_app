package com.mcmaintank.geocache.data.model;

import java.util.Date;

public class Geocache {
    private Integer geocacheId;
    private Double latitudes;
    private Double longitudes;
    private String description;
    private Date geocacheDateOfUpload;
    private Integer pid;
    private boolean deleted;

    public boolean isDeleted() {
        return deleted;
    }

    public Integer getReported() {
        return reported;
    }

    public void setReported(Integer reported) {
        this.reported = reported;
    }

    private Integer reported;

//    public Geocache(int geocacheId, int pid, double geocacheLatitudes, double geocacheLongitudes, String geocacheLocationDescription, boolean deleted) {
//        this.geocacheId = geocacheId;
//        this.pid = pid;
//        this.latitudes = latitudes;
//        this.longitudes = longitudes;
//        this.description = description;
//        this.deleted = deleted;
//    }

    public void setGeocacheId(Integer geocacheId){
        this.geocacheId = geocacheId;
    }

    public void setPid(Integer pid){
        this.pid = pid;
    }

    public void setLatitudes(Double latitudes){
        this.latitudes = latitudes;
    }

    public void setLongitudes(Double longitudes){
        this.longitudes = longitudes;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setDeleted(boolean deleted){
        this.deleted = deleted;
    }

    public void setGeocacheDateOfUpload(Date geocacheDateOfUpload){this.geocacheDateOfUpload = geocacheDateOfUpload;}

    public String getDescription(){
        return this.description;
    }

    public Double getLongitudes(){
        return this.longitudes;
    }

    public Double getLatitudes() {
        return this.latitudes;
    }

    public Integer getGeocacheId(){
        return this.geocacheId;
    }

    public Integer getPid(){
        return this.pid;
    }

    public Date getGeocacheDateOfUpload(){return this.geocacheDateOfUpload; }

}
