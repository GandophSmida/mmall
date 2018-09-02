package com.luna.mall.controller;

import com.luna.mall.common.ResponseCode;
import com.luna.mall.common.ServerResponse;
import com.luna.mall.pojo.User;
import com.luna.mall.serivce.CategoryService;
import com.luna.mall.serivce.UserService;
import com.luna.mall.util.CookieUtil;
import com.luna.mall.util.JsonUtil;
import com.luna.mall.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/manage/category/")
public class CategoryManageController {

    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @ResponseBody
    @RequestMapping(value = "addCategory.do", method = RequestMethod.POST)
    public ServerResponse addCategory(HttpServletRequest request, String categoryName,
                                      @RequestParam(value = "parentId",defaultValue = "0") int parentId){
        /*String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前登录信息");
        }
        String userJson = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson,User.class);
        if(null == user){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if(userService.checkAdminRole(user).isSuccess()){
            return categoryService.addCategory(categoryName, parentId);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }*/

        //全部通过拦截器验证是否登录以及权限
        return categoryService.addCategory(categoryName, parentId);
    }

    @ResponseBody
    @RequestMapping(value = "setCategoryName.do", method = RequestMethod.POST)
    public ServerResponse setCategoryName(HttpServletRequest request, Integer categoryId, String categoryName){
        /*String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前登录信息");
        }
        String userJson = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson,User.class);
        if(null == user){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if(userService.checkAdminRole(user).isSuccess()){
            return categoryService.updateCategoryName(categoryId,categoryName);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }*/

        //全部通过拦截器验证是否登录以及权限
        return categoryService.updateCategoryName(categoryId,categoryName);
    }

    @RequestMapping(value = "getCategory.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(
            HttpServletRequest request, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId){
        /*String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前登录信息");
        }
        String userJson = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson,User.class);
        if(null == user){ //查询子节点的category信息，并且不递归保持平级
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if(userService.checkAdminRole(user).isSuccess()){
            return categoryService.getChildrenParallelCategory(categoryId);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }*/

        //全部通过拦截器验证是否登录以及权限
        return categoryService.getChildrenParallelCategory(categoryId);
    }

    @ResponseBody
    @RequestMapping(value = "getDeepCategory.do", method = RequestMethod.GET)
    public ServerResponse getCategoryAndDeepChildrenCategory(
            HttpServletRequest request, @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId){
        /*String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            return ServerResponse.createByErrorMessage("用户未登录，无法获取当前登录信息");
        }
        String userJson = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson,User.class);
        if(null == user){ //查询当前子节点的id和递归子节点的id
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请登录");
        }
        if(userService.checkAdminRole(user).isSuccess()){
            return categoryService.selectChildrenParallelCategory(categoryId);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }*/

        //全部通过拦截器验证是否登录以及权限
        return categoryService.selectChildrenParallelCategory(categoryId);
    }
}
