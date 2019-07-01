package com.lad.chestnut.pojo.model;

import lombok.Data;

import javax.persistence.*;

/**
 * @author lad
 * 角色表
 */
@Entity
@Table(name = "role")
@Data
public class Role {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    /**
     * 角色名
     */
    @Column(name = "name")
    private String name;

    /**
     * 角色名中文名
     */
    @Column(name = "name_zh")
    private String nameZh;
}
