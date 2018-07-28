package com.luna.mall.serivce;

import com.github.pagehelper.PageInfo;
import com.luna.mall.common.ServerResponse;
import com.luna.mall.pojo.Product;
import com.luna.mall.vo.ProductDetailVo;

public interface ProductService {
    ServerResponse saveOrUpdateProduct(Product product);

    ServerResponse<String> setSaleStatus(Integer productId, Integer status);

    ServerResponse<ProductDetailVo> getManagerProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductList(int pageNum, int pageSize);

    ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize);

    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);

    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword,Integer categoryId,int pageNum, int pageSize, String orderBy);
}
