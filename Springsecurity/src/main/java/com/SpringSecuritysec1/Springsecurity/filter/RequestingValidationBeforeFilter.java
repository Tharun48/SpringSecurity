package com.SpringSecuritysec1.Springsecurity.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class RequestingValidationBeforeFilter implements Filter {
    /**
     * @param filterConfig The configuration information associated with the filter instance being initialised
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    /**
     * @param request  The request to process
     * @param response The response associated with the request
     * @param chain    Provides access to the next filter in the chain for this filter to pass the request and response
     *                 to for further processing
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httprequest = (HttpServletRequest) request;
        HttpServletResponse httpresponse = (HttpServletResponse) response;
        String header = httprequest.getHeader("Authorization");
        if(header!=null) {
            header=header.trim();
            if(header.toLowerCase().startsWith("basic")) {
                byte[] base64token = header.substring(6).getBytes(StandardCharsets.UTF_8);
                byte[] decode;
                try {
                    decode = Base64.getDecoder().decode(base64token);
                    String token = new String(decode, StandardCharsets.UTF_8);
                    String[] parts = token.split(":");
                    String username = parts[0];
                    String password = parts[1];
                    if (username.contains("test")) {
                        httpresponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        return ;
                    }
                }
                catch (IllegalArgumentException e) {
                    throw new RuntimeException("failed to load user credentials from basic auth token", e);
                }
            }
        }
        chain.doFilter(request, response);
    }

    /**
     *
     */
    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
