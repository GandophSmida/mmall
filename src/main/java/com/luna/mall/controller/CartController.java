package com.luna.mall.controller;

import com.luna.mall.common.Constants;
import com.luna.mall.common.ResponseCode;
import com.luna.mall.common.ServerResponse;
import com.luna.mall.pojo.User;
import com.luna.mall.serivce.CartService;
import com.luna.mall.util.CookieUtil;
import com.luna.mall.util.JsonUtil;
import com.luna.mall.util.RedisShardedPoolUtil;
import com.luna.mall.vo.CartVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/cart/")
public class CartController {

    @Autowired
    private CartService cartService;

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse<CartVo> list(HttpServletRequest request){
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前登录信息");
        }
        String userJson = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson,User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.list(user.getId());
    }

    @RequestMapping("add.do")
    @ResponseBody
    public ServerResponse<CartVo> add(HttpServletRequest request, Integer count, Integer productId){
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前登录信息");
        }
        String userJson = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson,User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.add(user.getId(), productId, count);
    }

    @RequestMapping("update.do")
    @ResponseBody
    public ServerResponse<CartVo> update(HttpServletRequest request, Integer count, Integer productId){
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前登录信息");
        }
        String userJson = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson,User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.update(user.getId(), productId, count);
    }

    @RequestMapping("delete.do")
    @ResponseBody
    public ServerResponse<CartVo> delete(HttpServletRequest request, String productIds){
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前登录信息");
        }
        String userJson = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson,User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.delete(user.getId(), productIds);
    }

    @RequestMapping("selectAll.do")
    @ResponseBody
    public ServerResponse<CartVo> selectAll(HttpServletRequest request){
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前登录信息");
        }
        String userJson = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson,User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.selectOrUnSelect(user.getId(), null, Constants.Cart.CHECKED);
    }

    @RequestMapping("unSelectAll.do")
    @ResponseBody
    public ServerResponse<CartVo> unSelectAll(HttpServletRequest request){
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前登录信息");
        }
        String userJson = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson,User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.selectOrUnSelect(user.getId(), null,Constants.Cart.UN_CHECKED);
    }

    @RequestMapping("select.do")
    @ResponseBody
    public ServerResponse<CartVo> select(HttpServletRequest request, Integer productId){
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前登录信息");
        }
        String userJson = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson,User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.selectOrUnSelect(user.getId(), productId, Constants.Cart.CHECKED);
    }

    @RequestMapping("unSelect.do")
    @ResponseBody
    public ServerResponse<CartVo> unSelect(HttpServletRequest request, Integer productId){
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前登录信息");
        }
        String userJson = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson,User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.selectOrUnSelect(user.getId(), productId, Constants.Cart.UN_CHECKED);
    }

    @RequestMapping("getCartProductCount.do")
    @ResponseBody
    public ServerResponse<Integer> getCartProductCount(HttpServletRequest request){
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前登录信息");
        }
        String userJson = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson,User.class);
        if(user == null){
            return ServerResponse.createBySeccessData(0);
        }
        return cartService.getCartProductCount(user.getId());
    }
}
