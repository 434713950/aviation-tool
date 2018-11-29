package com.github.tool.common;

/**
 * <p></p>
 *
 * @author PengCheng
 * @date 2018/11/1
 */
public enum OsType {
    LINUX("linux"),
    WINDOW("window");

    private String name;

    OsType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
