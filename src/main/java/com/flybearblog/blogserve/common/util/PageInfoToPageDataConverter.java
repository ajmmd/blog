package com.flybearblog.blogserve.common.util;

import com.flybearblog.blogserve.common.pojo.vo.PageData;
import com.github.pagehelper.PageInfo;

public class PageInfoToPageDataConverter {

    public static <T> PageData<T> convert(PageInfo<T> pageInfo) {
        PageData<T> pageData = new PageData<>();
        pageData.setPageNum(pageInfo.getPageNum())
                .setPageSize(pageInfo.getPageSize())
                .setTotal(pageInfo.getTotal())
                .setMaxPage(pageInfo.getPages())
                .setList(pageInfo.getList());
        return pageData;
    }

}
