package com.example.parking.Security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.parking.Repository.KioskRepository;
import com.example.parking.model.Kiosk;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final KioskRepository kioskRepo;

    public JwtAuthFilter(
            JwtService jwtService,
            CustomUserDetailsService userDetailsService, KioskRepository kiosk) {

        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.kioskRepo=kiosk;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        String subject = jwtService.extractUsername(token);
        String authType = jwtService.extractAuthType(token);

        System.out.println("SUBJECT = " + subject);
        System.out.println("AUTH TYPE = " + authType);

        if (subject != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

        if ("KIOSK".equals(authType)) {

                Kiosk kiosk = kioskRepo.findByName(subject)
                        .orElseThrow(() ->
                                new RuntimeException("Kiosk not found"));

                if (!kiosk.isEnabled()) {
                throw new RuntimeException("Kiosk is disabled");
                }

                String authority = "KIOSK_" + kiosk.getType().name();

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                kiosk,
                                null,
                                List.of(new SimpleGrantedAuthority(authority))
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);

        } else {

                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(subject);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
        }
        }

        filterChain.doFilter(request, response);
    }
}