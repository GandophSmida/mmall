package com.luna.mall.common;

public class Constants {
    public static final String CURRENT_USER="currentUser";
    public static final String EMAIL="email";
    public static final String USERNAME="username";



    public interface Role{
        int ROLR_CUSTOMER = 0; //普通用户
        int ROLR_ADMIN = 1; //管理员
    }
}
