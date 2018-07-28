package com.luna.mall.serivce;

import com.luna.mall.common.ServerResponse;

import java.util.Map;

public interface OrderService {
    ServerResponse pay(Long orderNo, Integer userId, String path);

    ServerResponse aliCallBack(Map<String,String> params);

    ServerResponse queryOrderPayStatus(Integer userId,Long orderNo);
}
