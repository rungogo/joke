package com.lad.chestnut.service;

import com.lad.chestnut.pojo.model.Menu;
import com.lad.chestnut.pojo.model.User;

import java.util.List;

/**
 * 用户service
 *
 * @author lad
 * @date 2019/4/29
 */
public interface MenuService {
    /**
     * 获取所有菜单列表
     *
     * @return
     */
    List<Menu> getByAllMenu();

    /**
     * 获取当前用户的菜单列表
     *
     * @param user
     * @return
     */
    List<Menu> getCurrentUserMenu(User user);
}
