package com.luna.mall.controller;

import com.google.common.collect.Maps;
import com.luna.mall.common.ResponseCode;
import com.luna.mall.common.ServerResponse;
import com.luna.mall.pojo.Product;
import com.luna.mall.pojo.User;
import com.luna.mall.serivce.FileService;
import com.luna.mall.serivce.ProductService;
import com.luna.mall.serivce.UserService;
import com.luna.mall.util.CookieUtil;
import com.luna.mall.util.JsonUtil;
import com.luna.mall.util.PropertiesUtil;
import com.luna.mall.util.RedisShardedPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/manage/product/")
public class ProductManageController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @RequestMapping("saveProduct.do")
    @ResponseBody
    public ServerResponse productSave(HttpServletRequest request, Product product){
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
            return productService.saveOrUpdateProduct(product);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }*/

        //全部通过拦截器验证是否登录以及权限
        return productService.saveOrUpdateProduct(product);
    }

    @RequestMapping("setSaleStatus.do")
    @ResponseBody
    public ServerResponse setSaleStatus(HttpServletRequest request, Integer productId, Integer status){
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
            return productService.setSaleStatus(productId, status);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }*/

        //全部通过拦截器验证是否登录以及权限
        return productService.setSaleStatus(productId, status);
    }

    @RequestMapping("getDetail.do")
    @ResponseBody
    public ServerResponse getDetail(HttpServletRequest request, Integer productId){
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
            return productService.getManagerProductDetail(productId);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }*/

        //全部通过拦截器验证是否登录以及权限
        return productService.getManagerProductDetail(productId);
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ServerResponse getList(HttpServletRequest request, @RequestParam(value = "pageNum", defaultValue = "1")int pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "10")int pageSize){
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
            return productService.getProductList(pageNum, pageSize);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }*/

        //全部通过拦截器验证是否登录以及权限
        return productService.getProductList(pageNum, pageSize);
    }

    @RequestMapping("searchProduct.do")
    @ResponseBody
    public ServerResponse searchProduct(HttpServletRequest request,String productName,Integer productId, @RequestParam(value = "pageNum", defaultValue = "1")int pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "10")int pageSize){
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
            return productService.searchProduct(productName, productId, pageNum, pageSize);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }*/

        //全部通过拦截器验证是否登录以及权限
        return productService.searchProduct(productName, productId, pageNum, pageSize);
    }

    @RequestMapping("upload.do")
    @ResponseBody
    public ServerResponse upload(@RequestParam(value = "uploadFile", required = false)MultipartFile file, HttpServletRequest request){
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
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = fileService.upload(file, path);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
            Map fileMap = Maps.newHashMap();
            fileMap.put("uri", targetFileName);
            fileMap.put("url", url);
            return ServerResponse.createBySeccessData(fileMap);
        }else{
            return ServerResponse.createByErrorMessage("无权限操作，需要管理员权限");
        }*/

        //全部通过拦截器验证是否登录以及权限
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = fileService.upload(file, path);
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
        Map fileMap = Maps.newHashMap();
        fileMap.put("uri", targetFileName);
        fileMap.put("url", url);
        return ServerResponse.createBySeccessData(fileMap);
    }

    @RequestMapping("richImageUpload.do")
    @ResponseBody
    public Map richImageUpload(@RequestParam(value = "uploadFile", required = false)MultipartFile file,
                               HttpServletRequest request, HttpServletResponse response){
        Map resultMap = Maps.newHashMap();
        /*String loginToken = CookieUtil.readLoginToken(request);
        if(StringUtils.isEmpty(loginToken)){
            resultMap.put("success", false);
            resultMap.put("msg", "请登录管理员");
            return resultMap;
        }
        String userJson = RedisShardedPoolUtil.get(loginToken);
        User user = JsonUtil.string2Obj(userJson,User.class);
        if(null == user){
            resultMap.put("success", false);
            resultMap.put("msg", "请登录管理员");
            return resultMap;
        }
        if(userService.checkAdminRole(user).isSuccess()){
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = fileService.upload(file, path);
            if(StringUtils.isBlank(targetFileName)){
                resultMap.put("success", false);
                resultMap.put("msg", "上传失败");
                return resultMap;
            }
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
            resultMap.put("success", true);
            resultMap.put("msg", "上传成功");
            resultMap.put("filePath", url);
            response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
            return resultMap;
        }else{
            resultMap.put("success", false);
            resultMap.put("msg", "无权限操作");
            return resultMap;
        }*/

        //全部通过拦截器验证是否登录以及权限
        String path = request.getSession().getServletContext().getRealPath("upload");
        String targetFileName = fileService.upload(file, path);
        if(StringUtils.isBlank(targetFileName)){
            resultMap.put("success", false);
            resultMap.put("msg", "上传失败");
            return resultMap;
        }
        String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
        resultMap.put("success", true);
        resultMap.put("msg", "上传成功");
        resultMap.put("filePath", url);
        response.addHeader("Access-Control-Allow-Headers", "X-File-Name");
        return resultMap;
    }
}
