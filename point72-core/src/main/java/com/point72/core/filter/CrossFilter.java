package com.point72.core.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 允许跨域的Filter
 * 
 * @author Houkm
 *
 *         2017年5月16日
 */
@WebFilter(urlPatterns = "/**")
public class CrossFilter extends OncePerRequestFilter {

	private Logger logger = LoggerFactory.getLogger(CrossFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		logger.info("CrossFilter：{}", request.getContextPath());
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Methods", "POST, PUT, DELETE, OPTIONS, HEAD");
		response.setHeader("Access-Control-Allow-Headers",
				"User-Agent,Origin,Cache-Control,Content-type,Date,Server,withCredentials,AccessToken,lf-None-Matoh");
		response.setHeader("Access-Control-Max-Age", "1209600");
		response.setHeader("Access-Control-Expose-Headers", "lf-None-Matoh");
		response.setHeader("Access-Control-Request-Headers", "lf-None-Matoh");
		response.setHeader("Expires", "-1");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("pragma", "no-cache");
		filterChain.doFilter(request, response);

	}

}