package com.coder520.mamabike.user.controller;


import com.coder520.mamabike.common.constants.Constants;
import com.coder520.mamabike.common.exception.MaMaBikeException;
import com.coder520.mamabike.common.resp.ApiResult;
import com.coder520.mamabike.common.rest.BaseController;
import com.coder520.mamabike.user.dao.UserMapper;
import com.coder520.mamabike.user.entity.LoginInfo;
import com.coder520.mamabike.user.entity.User;
import com.coder520.mamabike.user.entity.UserElement;
import com.coder520.mamabike.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("user")
@EnableTransactionManagement  //开启事务
@Slf4j   //log注解
public class UserController extends BaseController {

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;


    @Autowired
    private UserMapper userMapper;

    /*登录验证 不需要token*/
    @RequestMapping(value = "/login",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResult login(@RequestBody LoginInfo loginInfo){

        ApiResult<String> resp = new ApiResult<>();

        try{
            String data = loginInfo.getData();
            String key = loginInfo.getKey();
            if(StringUtils.isBlank(data)||StringUtils.isBlank(data)){
                throw new MaMaBikeException("参数校验失败");
            }

            String token =  userService.login(data,key);
            resp.setData(token);

        }
        catch (MaMaBikeException e){
            resp.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
            resp.setMessage(e.getMessage());
        }

        catch (Exception e){
           log.error("fail to login",e);
           resp.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
           resp.setMessage("内部错误");
        }

        return resp;
    }


    @RequestMapping("/modifyNickName")
    public ApiResult modifyNickName(@RequestBody User user){
           ApiResult resp = new ApiResult();
           try{

              UserElement ue = getCurrentUser();//从用户token中去redis获取用户信息
              user.setId(ue.getUserId());//userID不是前端决定了，安全
               userService.modifyNickName(user);

           } catch (MaMaBikeException e){
               resp.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
               resp.setMessage(e.getMessage());
           }

           catch (Exception e){
               log.error("fail to login",e);
               resp.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
               resp.setMessage("内部错误");
           }

          return resp;
    }


    @RequestMapping("/sendVercode")
    public ApiResult sendVercode(@RequestBody User user,HttpServletRequest request){
        ApiResult resp = new ApiResult();
        try{
             userService.sendVercode(user.getMobile(),getIpFromRequest(request));  //给谁发送 短信


        } catch (MaMaBikeException e){
            resp.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
            resp.setMessage(e.getMessage());
        }

        catch (Exception e){
            log.error("fail to send vercodes",e);
            resp.setCode(Constants.RESP_STATUS_INTERNAL_ERROR);
            resp.setMessage("内部错误");
        }

        return resp;
    }
}
