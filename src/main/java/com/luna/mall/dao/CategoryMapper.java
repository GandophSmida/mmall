package com.luna.mall.dao;

import com.luna.mall.pojo.Category;

public interface CategoryMapper {
    int insert(Category record);

    int insertSelective(Category record);

    Category selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);
}