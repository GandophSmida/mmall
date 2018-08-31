package com.luna.mall.controller;

import com.luna.mall.common.Constants;
import com.luna.mall.common.ResponseCode;
import com.luna.mall.common.ServerResponse;
import com.luna.mall.pojo.User;
import com.luna.mall.serivce.UserService;
import com.luna.mall.util.CookieUtil;
import com.luna.mall.util.JsonUtil;
import com.luna.mall.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user/")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session, HttpServletResponse httpServletResponse){
        ServerResponse<User> response = userService.login(username,password);
        if(response.isSuccess()){
            CookieUtil.writeLoginToken(httpServletResponse,session.getId());
            RedisPoolUtil.setEx(session.getId(),JsonUtil.obj2String(response.getData()),Constants.RedisCacheExtime.REDIS_SESSION_EXTIME);
        }
        return response;
    }

    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpServletRequest request,HttpServletResponse response){
        String loginToken = CookieUtil.readLoginToken(request);
        CookieUtil.delLoginToken(request,response);
        RedisPoolUtil.del(loginToken);
        return ServerResponse.createBySuccess();
    }

    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> register(User user){
        return userService.register(user);
    }

    @RequestMapping(value = "validate.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> validate(String type, String value){
        return userService.validate(type,value);
    }

    @RequestMapping(value = "getUserInfo.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpServletRequest request){
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前登录信息");
        }
        String userJson = RedisPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson,User.class);
        if(user != null){
            return ServerResponse.createBySeccessData(user);
        }
        return ServerResponse.createByErrorMessage("用户未登录，无法获取当前登录信息");
    }

    @RequestMapping(value = "getUserQuestion.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> getUserQuestion(String username){
        return userService.selectQuestion(username);
    }

    @RequestMapping(value = "checkUserAnswer.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkUserAnswer(String username, String question, String answer){
        return userService.checkUserAnswer(username, question, answer);
    }

    @RequestMapping(value = "forgetResetPassword.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username, String newPassword, String forgetToken){
        return userService.forgetResetPassword(username, newPassword, forgetToken);
    }

    @RequestMapping(value = "resetPassword.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword(HttpSession session, String passwordOld, String passwordNew){
        User user = (User)session.getAttribute(Constants.CURRENT_USER);
        if(null != user){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        return userService.resetPassword(passwordOld,passwordNew,user);
    }

    @RequestMapping(value = "updateUserInfo.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateUserInfo(HttpSession session, User user){
        User currentUser = (User)session.getAttribute(Constants.CURRENT_USER);
        if(null != currentUser){
            return ServerResponse.createByErrorMessage("用户未登录");
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> response = userService.updateUserInfo(user);
        if(response.isSuccess()){
             session.setAttribute(Constants.CURRENT_USER, response.getData());
        }
        return response;
    }

    @RequestMapping(value = "getUserDetail.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserDetail(HttpSession session){
        User currentUser = (User)session.getAttribute(Constants.CURRENT_USER);
        if(null != currentUser){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录需要强制登录，status=10");
        }
        return userService.getUserInfo(currentUser.getId());
    }
}
