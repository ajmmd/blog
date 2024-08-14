package com.flybearblog.blogserve.common.pojo.authentication;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentPrincipal implements Serializable {

    private Long id;
    private String username;

}
