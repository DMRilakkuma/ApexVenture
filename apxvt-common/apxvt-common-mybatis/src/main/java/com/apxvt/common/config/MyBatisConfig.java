package com.apxvt.common.config;


import com.apxvt.common.interceptor.SqlLoggerInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * MyBatisConfig
 *
 * @author MC
 * @version 1.0
 * @description
 * @date 2023/6/20 12:32
 */
@Configuration
public class MyBatisConfig {

    @Bean
    public String myInterceptor(SqlSessionFactory sqlSessionFactory) {
        //实例化插件
       SqlLoggerInterceptor sqlInterceptor = new SqlLoggerInterceptor();
        //创建属性值
        Properties properties = new Properties();
        properties.setProperty("prop1", "value1"); // 可设置可不设置
        //将属性值设置到插件中
        sqlInterceptor.setProperties(properties);
        //将插件添加到SqlSessionFactory工厂
        sqlSessionFactory.getConfiguration().addInterceptor(sqlInterceptor);
        return "interceptor";
    }

}
