package com.example.server.security;

import com.example.server.entity.User;
import com.example.server.services.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.hibernate.annotations.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Log4j2
@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JWTTokenProvider jwtTokenProvider;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       try {
           String token = getJWTTokenFromRequest(request);
           if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
               Long userId = jwtTokenProvider.getUserIdFromToken(token);
               User user = customUserDetailsService.loadUserById(userId);
               UsernamePasswordAuthenticationToken authentication = UsernamePasswordAuthenticationToken.authenticated(user, null, Collections.emptyList());
               authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
               SecurityContextHolder.getContext().setAuthentication(authentication);
           }
       }
       catch (Exception e){
           log.error(e.getMessage());

       }
        filterChain.doFilter(request,response);
    }
    private String getJWTTokenFromRequest(HttpServletRequest request){
        String token = request.getHeader(SecurityConstant.HEADER_STRING);
        if (StringUtils.hasText(token) && token.startsWith(SecurityConstant.TOKEN_PREFIX)){
            return token.split(" ")[1];
        }
        return null;
    }
}
