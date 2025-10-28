package com.project.community.config;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.filter.CorsFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // -> proxy is created. maintains correct singleton semantics even for self-calls.
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsFilter corsFilter;

    @Bean // register the object to be returned to IoC
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // wrapping as passwordEncoder since it might be changed later, the hashing algorithm.
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(corsFilter) // every request passes through the filter
                .formLogin().disable() // not using form tag, original login style
                .httpBasic().disable();
        http.authorizeHttpRequests(authorize -> authorize
                // specific request url to be protected
                // for other requests, permit all
                .anyRequest().permitAll());
        return http.build();
    }
}
