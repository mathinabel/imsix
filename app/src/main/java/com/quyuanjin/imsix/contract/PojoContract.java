package com.quyuanjin.imsix.contract;

import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
@Entity
public class PojoContract {
    @Id(autoincrement = true)
    private  Long id;
    private   String portraitImageView;//头像
    private   String nameTextView;//用户昵称（自己设定的优先）
    private  String contentTextView;//个性签名
    private  String sex;
    private  String userid;
    private String tag;
    private String pinyin;
    private String myUserId;//实际上是数字userid
    private String contractUserId;


    @Generated(hash = 1539707127)
    public PojoContract(Long id, String portraitImageView, String nameTextView, String contentTextView, String sex, String userid, String tag, String pinyin, String myUserId, String contractUserId) {
        this.id = id;
        this.portraitImageView = portraitImageView;
        this.nameTextView = nameTextView;
        this.contentTextView = contentTextView;
        this.sex = sex;
        this.userid = userid;
        this.tag = tag;
        this.pinyin = pinyin;
        this.myUserId = myUserId;
        this.contractUserId = contractUserId;
    }
    @Generated(hash = 493916022)
    public PojoContract() {
    }
    public String getMyUserId() {
        return myUserId;
    }

    public void setMyUserId(String myUserId) {
        this.myUserId = myUserId;
    }

    public String getContractUserId() {
        return contractUserId;
    }

    public void setContractUserId(String contractUserId) {
        this.contractUserId = contractUserId;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getPortraitImageView() {
        return this.portraitImageView;
    }
    public void setPortraitImageView(String portraitImageView) {
        this.portraitImageView = portraitImageView;
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
    public String getUserid() {
        return this.userid;
    }
    public void setUserid(String userid) {
        this.userid = userid;
    }
    public String getTag() {
        return this.tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
    public String getPinyin() {
        return this.pinyin;
    }
    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }



}
