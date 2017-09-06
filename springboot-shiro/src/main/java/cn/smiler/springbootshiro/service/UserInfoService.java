package cn.smiler.springbootshiro.service;

import cn.smiler.springbootshiro.entity.UserInfo;

public interface UserInfoService {
    /**通过username查找用户信息;*/
    public UserInfo findByUsername(String username);
}
