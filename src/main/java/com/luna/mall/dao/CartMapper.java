package com.luna.mall.dao;

import com.luna.mall.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);

    Cart selectCartByUserIdAndProductId(@Param("userId")Integer userId, @Param("productId")Integer productId);

    List<Cart> selectCartByUserId(Integer userId);

    int selectCartProductCheckedStatusByUserId(Integer userId);

    int deleteByUserIdAndProductIds(@Param("userId")Integer userId, @Param("productIdList")List<String> productIdList);

    int checkedOrUnCheckedProduct(@Param("userId")Integer userId, @Param("productId")Integer productId, @Param("checked")Integer checked);

    int selectCartProductCount(@Param("userId")Integer userId);
}