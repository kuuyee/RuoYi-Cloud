package com.ruoyi.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class AuthFilter implements GlobalFilter, Ordered {
	Logger logger = LoggerFactory.getLogger(AuthFilter.class);

	@Override
	public int getOrder() {
		return -100;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		logger.info("in filter." );
		// 向headers中放文件，记得build
		ServerHttpRequest host = exchange.getRequest().mutate().header("uuu", "11").build();
		// 将现在的request 变成 change对象
		ServerWebExchange build = exchange.mutate().request(host).build();
		return chain.filter(build);
	}

}
