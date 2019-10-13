package com.coder520.mamabike.common.rest;

import com.coder520.mamabike.cache.CommonCacheUtil;
import com.coder520.mamabike.common.constants.Constants;
import com.coder520.mamabike.user.entity.UserElement;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 操作缓存方法
 */
public class BaseController {

    @Autowired
    private CommonCacheUtil cacheUtil;

    protected UserElement getCurrentUser() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String token = request.getHeader(Constants.REQUEST_TOKEN_KEY);//获取token
        if (StringUtils.isNotEmpty(token)) {
            try {
                UserElement ue = cacheUtil.getUserByToken(token);
                return ue;
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    protected String getIpFromRequest(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;  //本机测试
    }
}
