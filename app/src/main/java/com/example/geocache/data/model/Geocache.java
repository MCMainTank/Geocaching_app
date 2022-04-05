package com.example.geocache.data.model;

public class Geocache {
    private Integer geocacheId;
    private Double latitudes;
    private Double longitudes;
    private String description;
    private Integer pid;
    private boolean deleted;

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
}
