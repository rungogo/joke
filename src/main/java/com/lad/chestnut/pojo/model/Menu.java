package com.lad.chestnut.pojo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by sang on 2017/12/28.
 */
@Entity
@Table(name = "menu")
public class Menu implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "url")
    private String url;

    @Column(name = "path")
    private String path;

    @Column(name = "component")
    private String component;

    @Column(name = "name")
    private String name;

    @Column(name = "icon_cls")
    private String iconCls;

    @Column(name = "parent_id")
    private Integer parentId;

    /**
     * menu_role:中间表名字
     * joinColumns:这行配置本表与中间表对应关系
     * inverseJoinColumns:这行配置中间表另一字段与对应表关联关系
     */
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinTable(name = "menu_role",
            joinColumns = @JoinColumn(name = "mid", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "rid", referencedColumnName = "id"))
    private List<Role> roles;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="parent_id")
    private List<Menu> children;

    @ManyToOne
    @JoinColumn(name="parent_id",insertable=false,updatable=false)
    private Menu parent;

    public Menu getParent() {
        return parent;
    }

    public void setParent(Menu parent) {
        this.parent = parent;
    }

    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @JsonIgnore
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    @JsonIgnore
    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    @JsonIgnore
    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
