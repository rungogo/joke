package com.lad.chestnut.service;

import com.lad.chestnut.pojo.model.Menu;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * 用户service
 *
 * @author lad
 * @date 2019/4/29
 */
public interface MenuService {
    List<Menu> getByAllMenu();
}
