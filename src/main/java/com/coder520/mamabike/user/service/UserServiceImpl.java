package com.coder520.mamabike.user.service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.coder520.mamabike.Security.AESUtils;
import com.coder520.mamabike.Security.Base64Util;
import com.coder520.mamabike.Security.MD5Util;
import com.coder520.mamabike.Security.RSAUtil;
import com.coder520.mamabike.cache.CommonCacheUtil;
import com.coder520.mamabike.common.constants.Constants;
import com.coder520.mamabike.common.exception.MaMaBikeException;
import com.coder520.mamabike.common.utils.RandomNumberCode;
import com.coder520.mamabike.jms.SmsProcessor;
import com.coder520.mamabike.user.dao.UserMapper;
import com.coder520.mamabike.user.entity.User;
import com.coder520.mamabike.user.entity.UserElement;
import lombok.extern.slf4j.Slf4j;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.jms.Destination;
import java.util.HashMap;
import java.util.Map;


@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private static final String SMS_QUEUE = "sms.queue";
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommonCacheUtil cacheUtil;

    private static final String VERIFYCODE_PREFIX = "verify.code.";

    @Autowired
    private SmsProcessor smsProcessor;

    @Override
    public String login(String data, String key) throws MaMaBikeException {

        String token = null;
        String decryptData = null;
        try{
            byte[] aesKey = RSAUtil.decryptByPrivateKey(Base64Util.decode(key));
            decryptData=AESUtils.decrypt(data,new String(aesKey,"UTF-8"));
            if(decryptData == null){
                throw new Exception();
            }
            JSONObject jsonObject= JSON.parseObject(decryptData);
            String mobile = jsonObject.getString("mobile");
            String code = jsonObject.getString("code");
            String platform = jsonObject.getString("platform");//机器类型
            String channelId = jsonObject.getString("channelId");//推送频道编码 单个设备唯一


            if(StringUtils.isBlank(mobile)||StringUtils.isBlank(code)){
               throw new Exception();
           }

           cacheUtil.cache(mobile,code);

          //去redis取验证码，比较手机号码验证码是不是匹配   凡有过期特性都要想到redis
            String verCode = cacheUtil.getCacheValue(mobile); //mobile做key
            User user = null;
            if(code.equals(verCode)){
                //手机匹配
                user = userMapper.selectByMobile(mobile);
                if (user==null){
                    user =new User();
                    user.setMobile(mobile);
                    user.setNickname(mobile);
                    //判断用户是否存在，存在生成token 存redis，不存在帮他注册插入数据库
                    userMapper.insertSelective(user);
                }
            }else{
                throw new MaMaBikeException("手机号验证码不匹配");
            }

            //生成token给客户端
            try {
                token = generateToken(user);
            } catch (Exception e) {
                throw new MaMaBikeException("生成Token失败");
            }


            UserElement ue = new UserElement();
            ue.setMobile(mobile);
            ue.setUserId(user.getId());
            ue.setToken(token);
            ue.setPlatform(platform);
            cacheUtil.putTokenWhenLogin(ue);


        }catch (Exception e){
             log.error("fail to decrypt data",e);
             throw new MaMaBikeException("数据解析错误");
        }
        return token;
    }


   /*
    修改用户昵称
   * */
    @Override
    public void modifyNickName(User user) {
        userMapper.updateByPrimaryKeySelective(user);

    }
    /*
      短信发送
     * */
    @Override
    public void sendVercode(String mobile, String ip) throws MaMaBikeException {
        String verCode = RandomNumberCode.verCode();
        //先存redis  reids缓存检查是否恶意请求 决定是否真的发送验证码
        int result = cacheUtil.cacheForVerificationCode(VERIFYCODE_PREFIX+mobile,verCode,"reg",60,ip);
        if (result == 1) {
            log.info("当前验证码未过期，请稍后重试");
            throw new MaMaBikeException("当前验证码未过期，请稍后重试");
        } else if (result == 2) {
            log.info("超过当日验证码次数上线");
            throw new MaMaBikeException("超过当日验证码次数上限");
        } else if (result == 3) {
            log.info("超过当日验证码次数上限 {}", ip);
            throw new MaMaBikeException(ip + "超过当日验证码次数上限");
        }
        log.info("Sending verify code {} for phone {}", verCode, mobile);

        //验证码推送到队列
        Destination destination = new ActiveMQQueue(SMS_QUEUE);
        Map<String,String> smsParam = new HashMap<>();
        smsParam.put("mobile",mobile);
        smsParam.put("tplId",Constants.MDSMS_VERCODE_TPLID);
        smsParam.put("vercode",verCode);
        String message = JSON.toJSONString(smsParam);
        smsProcessor.sendSmsToQueue(destination,message);
    }

    //生成token方法
    private String generateToken(User user)
            throws Exception {
        String source = user.getId() + ":" + user.getMobile() + System.currentTimeMillis(); //根据id,手机号，系统时间生成token
        return MD5Util.getMD5(source);
    }
}
