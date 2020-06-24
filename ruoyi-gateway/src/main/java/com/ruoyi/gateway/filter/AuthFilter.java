package com.ruoyi.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class AuthFilter implements GatewayFilter, Ordered {
	Logger logger = LoggerFactory.getLogger(AuthFilter.class);

	@Override
	public int getOrder() {
		return -100;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null && authentication instanceof OAuth2Authentication)
        {
            OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;
            Authentication  userAuthentication  = oAuth2Authentication.getUserAuthentication();
            String principal = userAuthentication.getName();
            
          //向headers中放文件，记得build
            ServerHttpRequest host = exchange.getRequest().mutate().header("uuu", principal).build();
            //将现在的request 变成 change对象 
            ServerWebExchange build = exchange.mutate().request(host).build();
            return chain.filter(build);
        }
		return null;
	}

}
