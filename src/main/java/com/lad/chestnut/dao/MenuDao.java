package com.lad.chestnut.dao;

import com.lad.chestnut.pojo.model.Menu;
import com.lad.chestnut.pojo.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author lad
 * @date 2019/7/1
 */
@Repository
public interface MenuDao extends JpaRepository<Menu, Integer> {
}
