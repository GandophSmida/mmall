package com.luna.mall.controller.common.interceptor;

import com.google.common.collect.Maps;
import com.luna.mall.common.Constants;
import com.luna.mall.common.ServerResponse;
import com.luna.mall.pojo.User;
import com.luna.mall.util.CookieUtil;
import com.luna.mall.util.JsonUtil;
import com.luna.mall.util.RedisShardedPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

@Slf4j
public class AuthorityInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod)o;
        String methodName = handlerMethod.getMethod().getName();
        String className = handlerMethod.getBean().getClass().getSimpleName();

        StringBuffer sb = new StringBuffer();
        Map paramMap = httpServletRequest.getParameterMap();
        Iterator iterator = paramMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry entry = (Map.Entry)iterator.next();
            String mapKey = (String) entry.getKey();
            String mapValue = StringUtils.EMPTY;
            Object object = entry.getValue();
            if(object instanceof String[]){
                String[] strings = (String[])object;
                mapValue= Arrays.toString(strings);
            }
            sb.append(mapKey).append("=").append(mapValue);
        }
        //防止陷入拦截器的死循环2：拦截器排除法放过不需要拦截的登录请求
        if(StringUtils.equals(className,"UserManagerController") && StringUtils.equals(methodName,"login")){
            log.info("权限拦截器拦截到请求,className:{},methodName:{}",className,methodName); //登录信息不打印参数
            return true;
        }
        User user = null;
        String loginToken = CookieUtil.readLoginToken(httpServletRequest);
        if(StringUtils.isNotEmpty(loginToken)){
            String userJson = RedisShardedPoolUtil.get(loginToken);
            user = JsonUtil.string2Obj(userJson,User.class);
        }
        if(user == null || (user.getRole().intValue()!=Constants.Role.ROLR_ADMIN)){
            httpServletResponse.reset(); //重置response
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json;charset=UTF-8");

            PrintWriter printWriter = httpServletResponse.getWriter();
            if(user == null){
                if(StringUtils.equals(className,"ProductManageController") && StringUtils.equals(methodName,"richImageUpload")){
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success", false);
                    resultMap.put("msg", "请登录管理员");
                    printWriter.print(JsonUtil.obj2String(resultMap));
                    return true;
                }else{
                    printWriter.print(JsonUtil.obj2String(ServerResponse.createByErrorMessage("拦截器拦截，用户未登录")));
                }
            }else{
                if(StringUtils.equals(className,"ProductManageController") && StringUtils.equals(methodName,"richImageUpload")){
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success", false);
                    resultMap.put("msg", "无权限操作");
                    printWriter.print(JsonUtil.obj2String(resultMap));
                    return true;
                }else{
                    printWriter.print(JsonUtil.obj2String(ServerResponse.createByErrorMessage("拦截器拦截，用户无权限操作")));
                }
            }
            printWriter.flush();
            printWriter.close();
            return false;
        }
        log.info("preHandle");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        log.info("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        log.info("afterCompletion");
    }
}
