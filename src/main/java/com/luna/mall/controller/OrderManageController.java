package com.luna.mall.controller;

import com.github.pagehelper.PageInfo;
import com.luna.mall.common.Constants;
import com.luna.mall.common.ResponseCode;
import com.luna.mall.common.ServerResponse;
import com.luna.mall.pojo.User;
import com.luna.mall.serivce.OrderService;
import com.luna.mall.serivce.UserService;
import com.luna.mall.util.CookieUtil;
import com.luna.mall.util.JsonUtil;
import com.luna.mall.util.RedisShardedPoolUtil;
import com.luna.mall.vo.OrderVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/manage/order")
public class OrderManageController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<PageInfo> orderList(HttpServletRequest request, @RequestParam(value = "pageNum", defaultValue = "1")int pageNum,
                                              @RequestParam(value = "pageSize", defaultValue = "10")int pageSize){
        /*String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前登录信息");
        }
        String userJson = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson,User.class);
        if(null == user){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if(userService.checkAdminRole(user).isSuccess()){ //填充业务逻辑
            return orderService.manageList(pageNum,pageSize);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }*/

        //全部通过拦截器验证是否登录以及权限
        return orderService.manageList(pageNum,pageSize);
    }

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse<OrderVo> orderDetail(HttpServletRequest request, Long orderNo){
        /*String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前登录信息");
        }
        String userJson = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson,User.class);
        if(null == user){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if(userService.checkAdminRole(user).isSuccess()){ //填充业务逻辑
            return orderService.manageDetail(orderNo);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }*/

        //全部通过拦截器验证是否登录以及权限
        return orderService.manageDetail(orderNo);

    }

    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse<PageInfo> orderSearch(HttpServletRequest request, Long orderNo, @RequestParam(value = "pageNum", defaultValue = "1")int pageNum,
                                               @RequestParam(value = "pageSize", defaultValue = "10")int pageSize){
        /*String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前登录信息");
        }
        String userJson = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson,User.class);
        if(null == user){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if(userService.checkAdminRole(user).isSuccess()){ //填充业务逻辑
            return orderService.manageSearch(orderNo,pageNum,pageSize);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }*/

        //全部通过拦截器验证是否登录以及权限
        return orderService.manageSearch(orderNo,pageNum,pageSize);

    }

    @RequestMapping("sendGoods.do")
    @ResponseBody
    public ServerResponse<String> sendGoods(HttpServletRequest request, Long orderNo){
        /*String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前登录信息");
        }
        String userJson = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson,User.class);
        if(null == user){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if(userService.checkAdminRole(user).isSuccess()){ //填充业务逻辑
            return orderService.manageSendGoods(orderNo);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }*/

        //全部通过拦截器验证是否登录以及权限
        return orderService.manageSendGoods(orderNo);

    }
}
