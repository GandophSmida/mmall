package com.luna.mall.dao;

import com.luna.mall.pojo.Order;
import org.apache.ibatis.annotations.Param;

public interface OrderMapper {
    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    Order selectByUserIdAndOrderNo(@Param("userId")Integer userId, @Param("orderNo")Long orderNo);

    Order selectByOrderNo(Long orderNo);
}