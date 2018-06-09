package com.luna.mall.dao;

import com.luna.mall.pojo.Cart;

public interface CartMapper {
    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);
}