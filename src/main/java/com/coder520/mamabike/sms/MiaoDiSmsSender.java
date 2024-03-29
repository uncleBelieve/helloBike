package com.coder520.mamabike.sms;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.coder520.mamabike.Security.MD5Util;
import com.coder520.mamabike.common.constants.Constants;
import com.coder520.mamabike.common.utils.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by JackWangon[www.coder520.com] 2017/8/5.
 */

@Slf4j
@Service("verCodeService")
public class MiaoDiSmsSender implements SmsSender{


    private static String operation = "/industrySMS/sendSMS";


    /**
     *@Author JackWang [www.coder520.com]
     *@Date 2017/8/5 16:23
     *@Description  秒滴发送短信
     */
    @Override
    public  void sendSms(String phone,String tplId,String params){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String timestamp = sdf.format(new Date());
            String sig = MD5Util.getMD5(Constants.MDSMS_ACCOUNT_SID +Constants.MDSMS_AUTH_TOKEN +timestamp);
            String url = Constants.MDSMS_REST_URL +operation;
            Map<String,String> param = new HashMap<>();
            param.put("accountSid",Constants.MDSMS_ACCOUNT_SID);
            param.put("to",phone);
            param.put("templateid",tplId);
            param.put("param",params);
            param.put("timestamp",timestamp);
            param.put("sig",sig);
            param.put("respDataType","json");
            String result = HttpUtil.post(url,param);
            JSONObject jsonObject = JSON.parseObject(result);
            System.out.println("发送短信");
            if(!jsonObject.getString("respCode").equals("00000")){
                log.error("fail to send sms to "+phone+":"+params+":"+result);
            }
        } catch (Exception e) {
            log.error("fail to send sms to "+phone+":"+params);
        }


    }

}
