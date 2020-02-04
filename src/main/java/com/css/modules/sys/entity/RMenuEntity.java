package com.css.modules.sys.entity;

import java.io.Serializable;
import java.util.List;

public class RMenuEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 菜单代码
     */
    private String id;

    /**
     * 菜单名称
     */
    private String name;
    /**
     * 访问路径
     */
    private String path;
    /**
     * 图标
     */
    private String icon;
    /**
     * 打开图标
     */
    private String openIcon;

    /**
     * 打开图标
     */
    private String funcode;
    private String visible;
    String isLast;

    /**
     * 父节点ID
     */
    private String parentId;

    private int level;

    private List<RMenuEntity> menus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getOpenIcon() {
        return openIcon;
    }

    public void setOpenIcon(String openIcon) {
        this.openIcon = openIcon;
    }

    public String getFuncode() {
        return funcode;
    }

    public void setFuncode(String funcode) {
        this.funcode = funcode;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getIsLast() {
        return isLast;
    }

    public void setIsLast(String isLast) {
        this.isLast = isLast;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<RMenuEntity> getMenus() {
        return menus;
    }

    public void setMenus(List<RMenuEntity> menus) {
        this.menus = menus;
    }
}
