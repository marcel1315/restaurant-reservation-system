package com.zerobase.shopreservation.common.security;

import com.zerobase.shopreservation.common.type.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter authenticationFilter;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.POST, "/member/signup").permitAll()
                        .requestMatchers(HttpMethod.POST, "/member/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/member/info").hasAnyAuthority(MemberRole.ROLE_CUSTOMER.toString(), MemberRole.ROLE_MANAGER.toString())
                        .requestMatchers(HttpMethod.GET, "/customer/reservations/timetable").permitAll()
                        .requestMatchers(HttpMethod.GET, "/customer/shops/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/customer/reviews/**").permitAll()
                        .requestMatchers("/customer/**").hasAuthority(MemberRole.ROLE_CUSTOMER.toString())
                        .requestMatchers("/manager/**").hasAuthority(MemberRole.ROLE_MANAGER.toString())
                        .anyRequest().authenticated()
                )
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
