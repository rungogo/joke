package com.lad.chestnut.pojo.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 用户
 *
 * @author lad
 * @date 2019/5/2
 */
@Entity
@Data
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "username")
    private String username;
    @Column(name = "phone")
    private String phone;
    @Column(name = "password")
    private String password;
    @Column(name = "create_time")
    private LocalDateTime createTime;
    @Column(name = "update_time")
    private LocalDateTime updateTime;
    @Column(name = "status")
    private Boolean status;
}
