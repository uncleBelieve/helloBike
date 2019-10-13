package com.coder520.mamabike.user.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hjy[www.coder520.com] 2017/8/1.
 *
 * 用于缓存的user信息体
 */
@Data
public class UserElement {

    private long userId;

    private String mobile;

    private String token;

    private String platform;  //安卓或者ios

    private String pushUserId;   //为单用户推送所需id

    private String pushChannelId;   //为群组推送所需id

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPushUserId() {
        return pushUserId;
    }

    public void setPushUserId(String pushUserId) {
        this.pushUserId = pushUserId;
    }

    public String getPushChannelId() {
        return pushChannelId;
    }

    public void setPushChannelId(String pushChannelId) {
        this.pushChannelId = pushChannelId;
    }

    /**
     * 转 map                   redis 哈希数据结构相当于map  所以需要一个转map方法  还要一个转对象方法
     * @return
     */
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("platform", this.platform);
        map.put("userId", this.userId + "");
        map.put("token", token);
        map.put("mobile", mobile);
        if (this.pushUserId != null) {
            map.put("pushUserId", this.pushUserId);
        }
        if (this.pushChannelId != null) {
            map.put("pushChannelId", this.pushChannelId);
        }
        return map;
    }

    /**
     * map转对象
     * @param map
     * @return
     */
    public static UserElement fromMap(Map<String, String> map) {
        UserElement ue = new UserElement();
        ue.setPlatform(map.get("platform"));
        ue.setToken(map.get("token"));
        ue.setMobile(map.get("mobile"));
        ue.setUserId(Long.parseLong(map.get("userId")));
        ue.setPushUserId(map.get("pushUserId"));
        ue.setPushChannelId(map.get("pushChannelId"));
        return ue;
    }

}
