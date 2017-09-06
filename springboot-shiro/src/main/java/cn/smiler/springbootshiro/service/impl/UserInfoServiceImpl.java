package cn.smiler.springbootshiro.service.impl;

import cn.smiler.springbootshiro.dao.UserInfoDao;
import cn.smiler.springbootshiro.entity.UserInfo;
import cn.smiler.springbootshiro.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements UserInfoService {
   @Autowired
   private UserInfoDao userInfoDao;
    /**
     * 通过username查找用户信息;
     *
     * @param username
     */
    @Override
    public UserInfo findByUsername(String username) {
        System.out.println("UserInfoServiceImpl.findByUsername()");
        return userInfoDao.findByUsername(username);
    }
}
