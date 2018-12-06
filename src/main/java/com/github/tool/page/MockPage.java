package com.github.tool.page;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p></p>
 *
 * @author PengCheng
 * @date 2018/12/6
 */
@Data
@Accessors(chain = true)
public class MockPage {

   private List<Object> result;

   private long total=0;

   private int currentPage;

   private int pageSize;
}
