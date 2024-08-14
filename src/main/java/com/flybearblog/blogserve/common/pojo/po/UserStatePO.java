package com.flybearblog.blogserve.common.pojo.po;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserStatePO implements Serializable {

    private Integer enable;
    private String authoritiesJsonString;

}
