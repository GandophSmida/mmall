package com.luna.mall.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.luna.mall.common.Constants;
import com.luna.mall.common.ResponseCode;
import com.luna.mall.common.ServerResponse;
import com.luna.mall.pojo.User;
import com.luna.mall.serivce.OrderService;
import com.luna.mall.util.CookieUtil;
import com.luna.mall.util.JsonUtil;
import com.luna.mall.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Iterator;
import java.util.Map;

@Controller
@RequestMapping("/order/")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;


    @RequestMapping("create.do")
    @ResponseBody
    public ServerResponse create(HttpServletRequest request, Integer shippingId){
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前登录信息");
        }
        String userJson = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson,User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return orderService.createOrder(user.getId(),shippingId);
    }

    @RequestMapping("cancel.do")
    @ResponseBody
    public ServerResponse cancel(HttpServletRequest request, Long orderNo){
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前登录信息");
        }
        String userJson = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson,User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return orderService.cancel(user.getId(),orderNo);
    }

    @RequestMapping("getOrderCartProduct.do")
    @ResponseBody
    public ServerResponse getOrderCartProduct(HttpServletRequest request){
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前登录信息");
        }
        String userJson = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson,User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return orderService.getOrderCartProduct(user.getId());
    }

    @RequestMapping("detail.do")
    @ResponseBody
    public ServerResponse detail(HttpServletRequest request, Long orderNo){
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前登录信息");
        }
        String userJson = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson,User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return orderService.getOrderDetail(user.getId(),orderNo);
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse list(HttpServletRequest request, @RequestParam(value = "pageNum", defaultValue = "1")int pageNum,
                               @RequestParam(value = "pageSize", defaultValue = "10")int pageSize){
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前登录信息");
        }
        String userJson = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson,User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return orderService.getOrderList(user.getId(),pageNum,pageSize);
    }

    /**
     * 支付宝付款接口
     * @param orderNo
     * @param request
     * @return
     */
    @RequestMapping("pay.do")
    @ResponseBody
    public ServerResponse pay(Long orderNo, HttpServletRequest request){
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前登录信息");
        }
        String userJson = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson,User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        String path = request.getSession().getServletContext().getRealPath("upload");
        return orderService.pay(orderNo, user.getId(), path);
    }

    /**
     * 支付宝回调接口
     * @param request
     * @return
     */
    @RequestMapping("alipayCallbakc.do")
    @ResponseBody
    public Object alipayCallbakc(HttpServletRequest request){
        Map<String,String> params = Maps.newHashMap();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator();iter.hasNext();){
            String name = (String) iter.next();
            String values[] = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i<values.length; i++){
                valueStr = (i == values.length-1) ? valueStr+values[i] : valueStr+values[i]+",";

            }
            params.put("name",valueStr);
        }
        logger.info("支付宝回调，sign:{},trade_status:{},参数:{}",params.get("sign"),params.get("trade_status"),params.toString());

        //非常重要，验证回调的正确性，是不是支付宝发的，并且还要避免重复通知
        params.remove("sign_type");
        boolean alipayRSACheckedV2 = false;
        try {
            alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(),"utf-8", Configs.getSignType());
            if(!alipayRSACheckedV2){
                return ServerResponse.createByErrorMessage("非法请求，验证不通过，再恶意请求我就找网警了！");
            }
        } catch (AlipayApiException e) {
            logger.error("支付宝验证回调异常", e);
        }

        //todo 验证各种数据

        ServerResponse serverResponse = orderService.aliCallBack(params);
        if(serverResponse.isSuccess()){
            return Constants.AlipayCallBack.RESPONSE_SUCCESS;
        }
        return Constants.AlipayCallBack.RESPONSE_FAILED;
    }

    /**
     * 支付宝状态查询接口
     * @param request
     * @param orderNo
     * @return
     */
    @RequestMapping("queryOrderPayStatus.do")
    @ResponseBody
    public ServerResponse<Boolean> queryOrderPayStatus(HttpServletRequest request, Long orderNo){
        String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前登录信息");
        }
        String userJson = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson,User.class);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        ServerResponse serverResponse = orderService.queryOrderPayStatus(user.getId(),orderNo);
        if(serverResponse.isSuccess()){
            return ServerResponse.createBySeccessData(true);
        }
        return ServerResponse.createBySeccessData(false);
    }
}
