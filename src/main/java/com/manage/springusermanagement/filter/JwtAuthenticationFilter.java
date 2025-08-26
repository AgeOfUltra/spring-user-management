package com.manage.springusermanagement.filter;

import com.manage.springusermanagement.utils.JWTUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    JWTUtils utils;

    @Autowired
    @Lazy
    UserDetailsService service;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");
        String token = null;
        String username = null;
        if(authorization!=null && authorization.startsWith("Bearer ")){
            token = authorization.substring(7);
            username = utils.getUserNameFromToken(token);

        }

        //TODO : validate the token
        if(username!= null && SecurityContextHolder.getContext().getAuthentication()==null){
            UserDetails userDetails = service.loadUserByUsername(username);
            if(utils.validateToken(username,userDetails.getUsername(),token)){
                UsernamePasswordAuthenticationToken authenticationToken =new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                //TODO : set the token to context
                authenticationToken.setDetails(new WebAuthenticationDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken); // now our security context has the details of the user and request type.

            }

        }

        filterChain.doFilter(request,response);

    }
}
