package com.eyxyt.basement.constant;

/**
 * 常量
 * @author cd.wang
 * @create 2020-07-23 16:59
 * @since 1.0.0
 */
public class Constant {

    /** 超级管理员 */
    public static final int SUPER_ADMIN_ID = 1;
    public static final String SUPER_ADMIN_NAME = "admin";

    /**
     * 菜单类型
     */
    public enum MenuType {
        /**
         * 目录
         */
        CATALOG(0),
        /**
         * 菜单
         */
        MENU(1),
        /**
         * 按钮
         */
        BUTTON(2);

        private int value;

        MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
