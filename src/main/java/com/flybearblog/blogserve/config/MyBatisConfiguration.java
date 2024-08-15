package com.flybearblog.blogserve.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.flybearblog.blogserve.dao.persist.mapper")
public class MyBatisConfiguration {
}
