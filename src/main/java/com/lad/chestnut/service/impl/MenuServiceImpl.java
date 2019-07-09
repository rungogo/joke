package com.lad.chestnut.service.impl;

import com.lad.chestnut.dao.MenuDao;
import com.lad.chestnut.pojo.model.Menu;
import com.lad.chestnut.pojo.model.Role;
import com.lad.chestnut.pojo.model.User;
import com.lad.chestnut.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lad
 * @date 2019/7/1
 */
@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDao menuDao;

    @Override
    public List<Menu> getByAllMenu() {
        return menuDao.findAll();
    }

    @Override
    public List<Menu> getCurrentUserMenu(User currentUser) {
        // todo
        List<Role> roleList = currentUser.getRoles();
        List<Menu> menuList = menuDao.findAll((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();
            Join<Menu,Role> join = root.join("roles", JoinType.LEFT);
            CriteriaBuilder.In<List<Integer>> biddingStatusIn = criteriaBuilder.in(join.get("id"));
            biddingStatusIn.value(roleList.stream().map(s -> s.getId()).collect(Collectors.toList()));
            list.add(biddingStatusIn);
            return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
        });
        return menuList;
    }
}
