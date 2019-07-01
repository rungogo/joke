package com.lad.chestnut.service.impl;

import com.lad.chestnut.dao.MenuDao;
import com.lad.chestnut.pojo.model.Menu;
import com.lad.chestnut.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

    @Autowired
    private MenuDao menuDao;

    @Override
    public List<Menu> getByAllMenu() {
        return menuDao.findAll();
    }
}
