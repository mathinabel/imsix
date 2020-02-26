package com.quyuanjin.imsix.addfriend;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class PojoPullUnReadAddFriendMsgFromNet {
    @Id(autoincrement = true)
    private Long id;
    private String nameTextView;//用户昵称
    private String contentTextView;//个性签名
    private String sex;
    private String userid;
    private String receiverId;
    private String msg;
    private String createTime;
    private String portraitImageViewlocalPath;//头像
    private String portraitImageViewnetPath;//头像
    private String sendSucceedType;
    private String readType;

    @Generated(hash = 925435744)
    public PojoPullUnReadAddFriendMsgFromNet() {
    }

    @Generated(hash = 14026413)
    public PojoPullUnReadAddFriendMsgFromNet(Long id, String nameTextView, String contentTextView, String sex, String userid, String receiverId, String msg, String createTime, String portraitImageViewlocalPath, String portraitImageViewnetPath, String sendSucceedType, String readType) {
        this.id = id;
        this.nameTextView = nameTextView;
        this.contentTextView = contentTextView;
        this.sex = sex;
        this.userid = userid;
        this.receiverId = receiverId;
        this.msg = msg;
        this.createTime = createTime;
        this.portraitImageViewlocalPath = portraitImageViewlocalPath;
        this.portraitImageViewnetPath = portraitImageViewnetPath;
        this.sendSucceedType = sendSucceedType;
        this.readType = readType;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getSendSucceedType() {
        return sendSucceedType;
    }

    public void setSendSucceedType(String sendSucceedType) {
        this.sendSucceedType = sendSucceedType;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNameTextView() {
        return this.nameTextView;
    }

    public void setNameTextView(String nameTextView) {
        this.nameTextView = nameTextView;
    }

    public String getContentTextView() {
        return this.contentTextView;
    }

    public void setContentTextView(String contentTextView) {
        this.contentTextView = contentTextView;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUserid() {
        return this.userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPortraitImageViewlocalPath() {
        return this.portraitImageViewlocalPath;
    }

    public void setPortraitImageViewlocalPath(String portraitImageViewlocalPath) {
        this.portraitImageViewlocalPath = portraitImageViewlocalPath;
    }

    public String getPortraitImageViewnetPath() {
        return this.portraitImageViewnetPath;
    }

    public void setPortraitImageViewnetPath(String portraitImageViewnetPath) {
        this.portraitImageViewnetPath = portraitImageViewnetPath;
    }

    public String getReadType() {
        return this.readType;
    }

    public void setReadType(String readType) {
        this.readType = readType;
    }
}
