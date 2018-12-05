package com.github.tool.tree.template;

import com.github.tool.tree.model.TreeNode;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>杂乱树包装器</p>
 *  在拥有形成树的根节点和一堆无逻辑的节点型数据的情形下形成一个改根节点扩展出的树结构
 *  提供了preMountNodeParser和postMountNodeParser方法对节点数据进行扩展解释
 * @author PengCheng
 * @date 2018/10/31
 */
public class PromiscuityTreeTemplate<T extends TreeNode<T>> extends AbstractTreeTemplate<T>{

    /**
     *  挂载子节点,形成树结构 （所有可挂载的子节点已确定）
     *  递归处理
     * @param pNode         父节点
     * @param treeNodeList  所有与该根关联的下级节点
     */
    public void mountChildrenNode(T pNode, List<T> treeNodeList){
        //在挂载子节点前对当前节点进行处理
        preMountNodeParser(pNode);

        //找出所有的子节点
        List<T> children = new ArrayList<>();
        for (T child : children){
            //递归形成子节点
            if (StringUtils.isNotEmpty(child.getPcode())
                    && child.getPcode().equals(pNode.getPcode())){
                //递归挂载子节点
                mountChildrenNode(child,treeNodeList);
                children.add(child);
            }
        }

        //节点挂载
        pNode.setChildren(children);
        //在挂载结束后对当前节点做处理
        postMountNodeParser(pNode);
    }
}
