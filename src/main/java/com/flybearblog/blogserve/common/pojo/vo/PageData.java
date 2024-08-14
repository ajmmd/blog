package com.flybearblog.blogserve.common.pojo.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true) // 链式Setter
public class PageData<T> implements Serializable {

    private Integer pageNum;
    private Integer pageSize;
    private Long total;
    private Integer maxPage;
    private List<T> list;

}
