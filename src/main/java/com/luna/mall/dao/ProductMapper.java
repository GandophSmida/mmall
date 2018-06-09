package com.luna.mall.dao;

import com.luna.mall.pojo.Product;

public interface ProductMapper {
    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);
}