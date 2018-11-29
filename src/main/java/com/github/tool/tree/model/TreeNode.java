package com.github.tool.tree.model;

import lombok.Data;

import java.util.List;

/**
 * <p></p>
 *
 * @author PengCheng
 * @date 2018/10/31
 */
@Data
public class TreeNode<T extends TreeNode> {

    /**
     * 当前编号
     */
    private String code;

    /**
     * 父级编号
     */
    private String pcode;

    /**
     * 子类数据
     */
    private List<T> children;
}
