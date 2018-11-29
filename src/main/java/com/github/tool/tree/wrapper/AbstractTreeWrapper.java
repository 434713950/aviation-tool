package com.github.tool.tree.wrapper;

import com.github.tool.common.CollectionUtil;
import com.github.tool.tree.model.TreeNode;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>树结构包装器</p>
 *  所有的树处理方式都要实现该接口
 * @author PengCheng
 * @date 2018/11/1
 */
public abstract class AbstractTreeWrapper<T extends TreeNode<T>> {

    protected T pNode;

    /**
     * 挂载子节点前对当前节点数据的解释处理
     * @param node  节点
     * @return  处理后的节点
     */
    protected void preMountNodeParser(T node){}

    /**
     * 挂载子节点后对当前节点数据的解释处理
     * @param node  节点
     * @return  处理后的节点
     */
    protected void postMountNodeParser(T node){}


    /**
     * 铺平树结构
     * @param isRetainChildren 是否要在铺平树结构后,继续展示各自的子节点数据
     * @return  铺平后的树
     */
    protected List<T> tilingTreeNodes(boolean isRetainChildren){
        List<T> storageContainer = new LinkedList<>();
        openChildrenNode(this.pNode,storageContainer,isRetainChildren);
        return null;
    }

    private void openChildrenNode(T pNode,List<T> storageContainer,boolean isRetainChildren){
        //取出子节点数据
        List<T> childrenNode = pNode.getChildren();
        //判断是否要保留子节点数据做展示
        if (isRetainChildren) {
            pNode.setChildren(null);
        }
        storageContainer.add(pNode);
        if (CollectionUtil.isNotBlank(childrenNode)){
            for (T childNode:childrenNode){
                openChildrenNode(childNode,storageContainer,isRetainChildren);
            }
        }
    }

    /**
     * 获取包装后的数据结构
     * @return
     */
    public T getPNode(){
        return this.pNode;
    }
}
