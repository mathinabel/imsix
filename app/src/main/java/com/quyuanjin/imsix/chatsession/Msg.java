package com.quyuanjin.imsix.chatsession;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class Msg {
    @Id(autoincrement = true)
    private  Long id;
    private String sendID;
    private String receiveId;


    private String msg;
    private String createTime;
    private String localPath;
    private String netPath;
    private int contentType;
    private float recorderTime;
    private String sendSucceedType;
    private String readType;
    @Generated(hash = 23037457)
    public Msg() {
    }
    @Generated(hash = 1784794371)
    public Msg(Long id, String sendID, String receiveId, String msg, String createTime, String localPath, String netPath, int contentType, float recorderTime, String sendSucceedType, String readType) {
        this.id = id;
        this.sendID = sendID;
        this.receiveId = receiveId;
        this.msg = msg;
        this.createTime = createTime;
        this.localPath = localPath;
        this.netPath = netPath;
        this.contentType = contentType;
        this.recorderTime = recorderTime;
        this.sendSucceedType = sendSucceedType;
        this.readType = readType;
    }
    public String getReadType() {
        return readType;
    }

    public void setReadType(String readType) {
        this.readType = readType;
    }

    public String getSendID() {
        return sendID;
    }

    public void setSendID(String sendID) {
        this.sendID = sendID;
    }

    public String getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(String receiveId) {
        this.receiveId = receiveId;
    }



    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getNetPath() {
        return netPath;
    }

    public void setNetPath(String netPath) {
        this.netPath = netPath;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public float getRecorderTime() {
        return recorderTime;
    }

    public void setRecorderTime(float recorderTime) {
        this.recorderTime = recorderTime;
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
}
