package com.luna.mall.serivce;

import com.github.pagehelper.PageInfo;
import com.luna.mall.common.ServerResponse;
import com.luna.mall.pojo.Shipping;

public interface ShippingService {
    ServerResponse add(Integer userId, Shipping shipping);

    ServerResponse<String> delete(Integer userId, Integer shippingId);

    ServerResponse<String> update(Integer userId, Shipping shipping);

    ServerResponse<Shipping> select(Integer userId, Integer shippingId);

    ServerResponse<PageInfo> list(Integer userId, Integer pageNum, Integer pageSize);
}
