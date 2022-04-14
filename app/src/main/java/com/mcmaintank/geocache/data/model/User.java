package com.mcmaintank.geocache.data.model;


/**
 * @author MCMainTank
 * @version 1.0
 * @date 2022-01-5 22:17
 */

public class User {


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public Integer getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(Integer userGroup) {
        this.userGroup = userGroup;
    }

    public Integer getReported() {
        return reported;
    }

    public void setReported(Integer reported) {
        this.reported = reported;
    }

    private Long userId;

    private String userName;

    private String userPassword;

    private Integer userGroup;

    private Integer reported;



    public boolean equals(Object o) {
        if (o instanceof User) {
            return (userName == ((User) o).userName);
        }
        return false;
    }

}
