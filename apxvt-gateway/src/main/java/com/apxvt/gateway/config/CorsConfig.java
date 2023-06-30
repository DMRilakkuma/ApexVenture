/**
 * @projectName Pyd-SynergisticAI
 * @package com.pydance.gateway.config
 * @className com.pydance.gateway.config.CorsConfig
 * @copyright Copyright 2020 Thunisoft, Inc All rights reserved.
 */
package com.apxvt.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;

/**
 * CorsConfig 网关跨域的问题
 * @author MC
 * @description
 * @date 2023/6/30 10:08
 * @version 1.0
 */
public class CorsConfig {
    @Bean
    public CorsWebFilter corsWebFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource (new PathPatternParser());
        CorsConfiguration corsConfig = new CorsConfiguration();
        // 允许所有请求方法
        corsConfig.addAllowedMethod ("*");
        // 允许所有域，当请求头
        corsConfig.addAllowedOrigin ("*");
        // 允许全部请求头
        corsConfig.addAllowedHeader ("*");
        // 允许携带 Cookie 等用户凭证
        corsConfig.setAllowCredentials (true);
        // 允许全部请求路径
        source.registerCorsConfiguration ("/**", corsConfig);
        return new CorsWebFilter (source);
    }
}

 