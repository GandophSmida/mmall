package com.luna.mall.dao;

import com.luna.mall.pojo.OrderItem;

public interface OrderItemMapper {
    int insert(OrderItem record);

    int insertSelective(OrderItem record);

    OrderItem selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderItem record);

    int updateByPrimaryKey(OrderItem record);
}