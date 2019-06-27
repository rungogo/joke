package com.lad.chestnut.dao;

import com.lad.chestnut.pojo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author lad
 * @date 2019/4/28
 */
@Repository
public interface UserDao extends JpaRepository<User, Integer> {
    /**
     * 根据用户名查询用户信息
     * @param userName 用户名
     * @return
     */
    Optional<User> getUserByUserName(String userName);

    /**
     * 根据用户名和密码查询用户信息
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    Optional<User> getUserByUserNameAndPassword(String userName, String password);
}
