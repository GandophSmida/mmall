package com.luna.mall.controller;

import com.luna.mall.common.Constants;
import com.luna.mall.common.ServerResponse;
import com.luna.mall.pojo.User;
import com.luna.mall.serivce.UserService;
import com.luna.mall.util.CookieUtil;
import com.luna.mall.util.JsonUtil;
import com.luna.mall.util.RedisPoolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/user/")
public class UserManagerController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "login.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session, HttpServletResponse httpServletResponse){
        ServerResponse<User> response = userService.login(username, password);
        if(response.isSuccess()){
            User user = response.getData();
            if(user.getRole().equals(Constants.Role.ROLR_ADMIN)){
                CookieUtil.writeLoginToken(httpServletResponse,session.getId());
                RedisPoolUtil.setEx(session.getId(),JsonUtil.obj2String(response.getData()),Constants.RedisCacheExtime.REDIS_SESSION_EXTIME);
                return response;
            }else{
                return ServerResponse.createByErrorMessage("不是管理员，无法登录");
            }
        }
        return response;
    }
}
