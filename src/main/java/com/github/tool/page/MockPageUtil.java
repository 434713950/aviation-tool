package com.github.tool.page;

import java.util.List;

/**
 * <p>假分页工具</p>
 *
 * @author PengCheng
 * @date 2018/12/6
 */
public class MockPageUtil {

    public static MockPage page(List<?> objectList,int currentPage,int pageSize, boolean isZeroStart){
        return new MockPage()
                .setCurrentPage(currentPage)
                .setPageSize(pageSize)
                .setResult(subList(objectList, currentPage, pageSize,isZeroStart))
                .setTotal(objectList.size());
    }


    public static List<?> subList(List<?> objectList,int currentPage,int pageSize, boolean isZeroStart){
        //最终下标
        int finallyIndex = objectList.size()-1;

        //起始下标
        int startIndex = 0;
        if (isZeroStart){
            startIndex = currentPage * pageSize;
        }else {
            startIndex = (currentPage-1) * pageSize;
        }

        //结束下标
        int endIndex = startIndex + pageSize - 1;

        //如果起始下标超出当前的下标则直接返回null
        if (startIndex > finallyIndex){
            return null;
        }

        if (endIndex < finallyIndex){
            return objectList.subList(startIndex,endIndex);
        }else {
            return objectList.subList(startIndex,finallyIndex);
        }
    }
}
