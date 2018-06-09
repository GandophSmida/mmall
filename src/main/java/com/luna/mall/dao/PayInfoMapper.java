package com.luna.mall.dao;

import com.luna.mall.pojo.PayInfo;

public interface PayInfoMapper {
    int insert(PayInfo record);

    int insertSelective(PayInfo record);

    PayInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PayInfo record);

    int updateByPrimaryKey(PayInfo record);
}