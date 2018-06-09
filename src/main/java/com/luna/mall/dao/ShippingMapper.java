package com.luna.mall.dao;

import com.luna.mall.pojo.Shipping;

public interface ShippingMapper {
    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);
}