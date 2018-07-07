package com.luna.mall.dao;

import com.luna.mall.pojo.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingMapper {
    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    int deleteByShippingIdAndUserId(@Param("userId")Integer userId, @Param("shippingId")Integer shippingId);

    int updateShipping(Shipping record);

    Shipping selectByShippingIdAndUserId(@Param("userId")Integer userId, @Param("shippingId")Integer shippingId);

    List<Shipping> selectByUserId(@Param("userId")Integer userId);
}