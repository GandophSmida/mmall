package com.luna.mall.serivce;

import com.luna.mall.common.ServerResponse;
import com.luna.mall.pojo.Category;

import java.util.List;

public interface CategoryService {
    ServerResponse addCategory(String categoryName, Integer parentId);

    ServerResponse updateCategoryName(Integer categoryId, String categoryName);

    ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);

    ServerResponse selectChildrenParallelCategory(Integer categoryId);
}
