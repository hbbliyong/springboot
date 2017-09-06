package cn.smiler.springbootshiro.dao;

import org.springframework.data.repository.CrudRepository;
import cn.smiler.springbootshiro.entity.UserInfo;
public interface UserInfoDao extends CrudRepository<UserInfo,Long> {
    public UserInfo findByUsername(String username);
}
