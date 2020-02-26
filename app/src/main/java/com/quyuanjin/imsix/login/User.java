package com.quyuanjin.imsix.login;

public class User {
    String id;
    String createAt;
    String description;
    String name;
    String user;
    String pwd;
    String phone;
    String portrait;
    String sex;
    String token;
    String updateAt;

    public User() {
    }

    public User(String id,String createAt, String description, String name, String user, String pwd, String phone, String portrait, String sex, String token, String updateAt) {
       this.id=id;
        this.createAt = createAt;
        this.description = description;
        this.name = name;
        this.user = user;
        this.pwd = pwd;
        this.phone = phone;
        this.portrait = portrait;
        this.sex = sex;
        this.token = token;
        this.updateAt = updateAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }
}
