package com.inno72.task.dispatch.config;

import java.io.IOException;
import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
public class WebConfig {

	@Bean
	public FilterRegistrationBean crossFilter() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new OncePerRequestFilter() {
			@Override
			protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
					FilterChain filterChain) throws ServletException, IOException {
				response.setHeader("Access-Control-Allow-Origin", "*");
				response.setHeader("Access-Control-Allow-Credentials", "true");
				response.setHeader("Access-Control-Allow-Methods", "POST, PUT, DELETE, OPTIONS, HEAD");
				response.setHeader("Access-Control-Allow-Headers",
						"User-Agent,Origin,Cache-Control,Content-type,Date,Server,withCredentials,AccessToken,lf-None-Matoh, X-Requested-With, X-File-Name, Content-Length, authorization, Accept, X-CSRF-TOKEN");
				response.setHeader("Access-Control-Max-Age", "1209600");
				response.setHeader("Access-Control-Expose-Headers", "lf-None-Matoh");
				response.setHeader("Access-Control-Request-Headers", "lf-None-Matoh");
				response.setHeader("Expires", "-1");
				response.setHeader("Cache-Control", "no-cache");
				response.setHeader("pragma", "no-cache");
				filterChain.doFilter(request, response);
			}
		});
		registration.setDispatcherTypes(EnumSet.allOf(DispatcherType.class));
		registration.setOrder(999999999);
		return registration;
	}

}
