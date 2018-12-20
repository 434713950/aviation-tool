package com.github.tool.tree.builder;

import com.github.tool.common.CollectionUtil;
import com.github.tool.tree.model.TreeNode;

import java.util.List;

/**
 * <p>有序数据树结构建造器</p>
 * 根据根节点来查询子节点,并形成树
 * 实现obtainChildrenNode方法来决定子节点的获取方式
 * @author PengCheng
 * @date 2018/11/1
 */
public abstract class AbstractSerializeTreeBuilder<T extends TreeNode<T>> extends AbstractTreeBuilder<T>{

    /**
     * 挂载子节点,形成树结构。（子节点数据获取方式自定义）
     * @param pNode
     */
    public void mountChildrenNode(T pNode){
        //在挂载子节点前对当前节点进行处理
        preMountNodeParser(pNode);

        List<T> childrenNode = obtainChildrenNode(pNode);
        if (CollectionUtil.isNotBlank(childrenNode)){
            for (T childNode:childrenNode){
                mountChildrenNode(childNode);
            }
            pNode.setChildren(childrenNode);
        }

        //在挂载子节点后对当前节点进行处理
        postMountNodeParser(pNode);
    }

    /**
     * 获取子节点
     * @param pNode 父节点
     * @return
     */
    protected abstract List<T> obtainChildrenNode(T pNode);

}
