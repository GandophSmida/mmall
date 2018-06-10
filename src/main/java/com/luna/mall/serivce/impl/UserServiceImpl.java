package com.luna.mall.serivce.impl;

import com.luna.mall.common.Constants;
import com.luna.mall.common.ServerResponse;
import com.luna.mall.common.TokenCache;
import com.luna.mall.dao.UserMapper;
import com.luna.mall.pojo.User;
import com.luna.mall.serivce.UserService;
import com.luna.mall.util.Md5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public ServerResponse<User> login(String username, String password) {
        int userCount = userMapper.checkUsername(username);
        if(userCount == 0){
            return ServerResponse.createByErrorMessage("用户名不存在");
        }

        String md5Password = Md5Util.MD5EncodeUtf8(password);

        User loginUser = userMapper.selectLoginUser(username,md5Password);
        if(null == loginUser){
            return ServerResponse.createByErrorMessage("密码错误");
        }

        loginUser.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySeccessMessageData("登录成功",loginUser);
    }

    @Override
    public ServerResponse<User> register(User user) {
        ServerResponse validateResponse = this.validate(user.getUsername(),Constants.USERNAME);
        if(validateResponse.isSuccess()){
            return validateResponse;
        }
        validateResponse = this.validate(user.getEmail(),Constants.EMAIL);
        if(validateResponse.isSuccess()){
            return validateResponse;
        }

        user.setRole(Constants.Role.ROLR_CUSTOMER);
        user.setPassword(Md5Util.MD5EncodeUtf8(user.getPassword()));
        int userCount = userMapper.insert(user);
        if(userCount == 0){
            return ServerResponse.createByErrorMessage("用户注册失败");
        }
        return ServerResponse.createBySeccessMessage("用户注册成功");
    }

    @Override
    public ServerResponse<String> validate(String type, String value) {
        if(StringUtils.isNotBlank(type)){
             if(type.equals(Constants.USERNAME)){
                 int userCount = userMapper.checkUsername(value);
                 if(userCount > 0){
                     return ServerResponse.createByErrorMessage("用户名已存在");
                 }
             }
             if(type.equals(Constants.EMAIL)){
                 int userCount = userMapper.checkEmail(value);
                 if(userCount > 0){
                     return ServerResponse.createByErrorMessage("Email已存在");
                 }
             }
        }else{
            return ServerResponse.createByErrorMessage("参数错误");
        }
        return ServerResponse.createBySeccessMessage("校验成功");
    }


    public ServerResponse<String> selectQuestion(String username){
        ServerResponse serverResponse = this.validate(username,Constants.USERNAME);
        if(serverResponse.isSuccess()){
            ServerResponse.createByErrorMessage("用户不存在");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if(StringUtils.isNotBlank(question)){
            return ServerResponse.createBySeccessData(question);
        }
        return ServerResponse.createByErrorMessage("找回密码的问题是空的");
    }

    public ServerResponse<String> checkUserAnswer(String username, String question, String answer){
        int resultCount = userMapper.checkUserAnswer(username, question, answer);
        if(resultCount>0){
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX+username, forgetToken);
            return ServerResponse.createBySeccessMessage(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题的答案错误");
    }

    public ServerResponse<String> forgetResetPassword(String username, String newPassword, String forgetToken){
        if(StringUtils.isBlank(forgetToken)){
            return ServerResponse.createByErrorMessage("参数错误，token需要传递");
        }
        ServerResponse serverResponse = this.validate(username,Constants.USERNAME);
        if(serverResponse.isSuccess()){
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX+username);
        if(StringUtils.isBlank(token)){
            return ServerResponse.createByErrorMessage("token无效或者过期");
        }

        if(StringUtils.equals(token,forgetToken)){
            String md5Password = Md5Util.MD5EncodeUtf8(newPassword);
            int rowCount = userMapper.updatePasswordByUsername(username, md5Password);
            if(rowCount>0){
                return ServerResponse.createBySeccessMessage("密码修改成功");
            }
        }else{
            return ServerResponse.createByErrorMessage("token错误，请重新获取重置密码的token ");
        }
        return ServerResponse.createByErrorMessage("修改密码失败");
    }

    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user){
        int rowCount = userMapper.checkPassword(Md5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if(rowCount==0){
            return ServerResponse.createByErrorMessage("旧密码错误");
        }

        user.setPassword(Md5Util.MD5EncodeUtf8(passwordNew));
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if(updateCount>0){
            return ServerResponse.createBySeccessMessage("密码重置成功");
        }
        return ServerResponse.createByErrorMessage("密码重置失败");
    }

    public ServerResponse<User> updateUserInfo(User user){
        int checkRow = userMapper.checkEmailByUserId(user.getEmail(), user.getId());
        if(checkRow>0){
            return ServerResponse.createByErrorMessage("Email已经存在，请更换Email再尝试更新");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setMobile(user.getMobile());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int updateRow = userMapper.updateByPrimaryKeySelective(updateUser);
        if(updateRow>0){
             return ServerResponse.createBySeccessMessageData("更新个人信息成功",updateUser);
        }
        return ServerResponse.createByErrorMessage("更新个人信息失败");
    }

    public ServerResponse<User> getUserInfo(Integer userId){
        User user = userMapper.selectByPrimaryKey(userId);
        if(user == null){
            return ServerResponse.createByErrorMessage("找不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);
        return ServerResponse.createBySeccessData(user);
    }
}
