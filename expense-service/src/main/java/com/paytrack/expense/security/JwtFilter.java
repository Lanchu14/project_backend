package com.paytrack.expense.security;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger log =
            LoggerFactory.getLogger(
                    JwtFilter.class
            );

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        if (log.isInfoEnabled()) {

            log.info("JWT Filter Running");
        }

        final String authHeader =
                request.getHeader("Authorization");

        if (log.isInfoEnabled()) {

            log.info(
                    "Auth Header: {}",
                    authHeader
            );
        }

        String username = null;
        String token = null;

        if (authHeader != null
                && authHeader.startsWith("Bearer ")) {

            token = authHeader.substring(7);

            username =
                    jwtUtil.extractUsername(token);
        }

        if (username != null
                && SecurityContextHolder
                .getContext()
                .getAuthentication() == null
                && jwtUtil.isTokenValid(token)) {

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            List.of(() -> "ROLE_USER")
                    );

            authToken.setDetails(username);

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authToken);
        }

        filterChain.doFilter(
                request,
                response
        );
    }
}