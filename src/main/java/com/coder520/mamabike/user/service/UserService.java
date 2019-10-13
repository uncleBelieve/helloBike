package com.coder520.mamabike.user.service;

import com.coder520.mamabike.common.exception.MaMaBikeException;
import com.coder520.mamabike.user.entity.User;

public interface UserService {
    String login(String data, String key) throws MaMaBikeException;

    void modifyNickName(User user) throws MaMaBikeException;

    void sendVercode(String mobile,String ip)throws MaMaBikeException;
}
