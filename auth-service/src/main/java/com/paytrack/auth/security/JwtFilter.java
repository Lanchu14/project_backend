package com.paytrack.auth.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;//handles the token 
    private final CustomUserDetailsService userDetailsService; // load user from db

    
    // this method is used to tell the filter which end points we should not use bcoz the user still do not have the token yet so 
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        return path.equals("/auth/login") ||
               path.equals("/auth/register") ||
               path.equals("/auth/forgot-password") ||
               path.equals("/auth/reset-password") ||
               path.startsWith("/error");
    }
    //this methods run for every request 
    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization"); //getauthorization header

        String username = null;
        String token = null;

        //extract the username 
        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            token = authHeader.substring(7);
            username = jwtUtil.extractUsername(token);
        }

        //check for the authentication if not done do it 
        if (username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            var userDetails = userDetailsService.loadUserByUsername(username);
            System.out.println("USERNAME: " + username);

            System.out.println(
                    "AUTHORITIES: "
                    + userDetails.getAuthorities()
            );

            if (jwtUtil.isTokenValid(token)) {
            	System.out.println("TOKEN VALID");

            	System.out.println("USERNAME: " + username);

            	System.out.println(
            	        "AUTHORITIES: "
            	        + userDetails.getAuthorities()
            	);

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}