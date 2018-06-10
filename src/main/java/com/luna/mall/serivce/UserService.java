package com.luna.mall.serivce;

import com.luna.mall.common.ServerResponse;
import com.luna.mall.pojo.User;

public interface UserService {
    ServerResponse<User> login(String username, String password);

    ServerResponse<User> register(User user);

    ServerResponse<String> validate(String type, String value);

    ServerResponse<String> selectQuestion(String username);

    ServerResponse<String> checkUserAnswer(String username, String question, String answer);

    ServerResponse<String> forgetResetPassword(String username, String newPassword, String forgetToken);

    ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user);

    ServerResponse<User> updateUserInfo(User user);

    ServerResponse<User> getUserInfo(Integer userId);
}
