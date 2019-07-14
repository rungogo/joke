package com.lad.chestnut.dao;

import com.lad.chestnut.pojo.model.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


/**
 * @author lad
 * @date 2019/7/1
 */
@Repository
public interface MenuDao extends JpaRepository<Menu, Integer>, JpaSpecificationExecutor<Menu> {
}
