package com.udacity.jwdnd.course1.cloudstorage.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        private final AuthenticationService authenticationService;

        public SecurityConfig(AuthenticationService authenticationService) {
                this.authenticationService = authenticationService;
        }

        @Bean
        public AuthenticationManager authManager(HttpSecurity http) throws Exception {
                AuthenticationManagerBuilder authenticationManagerBuilder = http
                                .getSharedObject(AuthenticationManagerBuilder.class);
                authenticationManagerBuilder.authenticationProvider(authenticationService);
                return authenticationManagerBuilder.build();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .csrf(csrf -> csrf.disable())
                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(
                                                                "/", "/signup", "/login", "/css/**", "/js/**",
                                                                "/proxy/8080", "/proxy/8080/", "/proxy/8080/signup",
                                                                "/proxy/8080/login",
                                                                "/proxy/8080/css/**", "/proxy/8080/js/**")
                                                .permitAll()
                                                .anyRequest().authenticated())
                                .formLogin(form -> form
                                                .loginPage("/proxy/8080/login")
                                                .loginProcessingUrl("/login")
                                                .defaultSuccessUrl("/proxy/8080/home", true)
                                                .failureUrl("/proxy/8080/login?error")
                                                .permitAll())
                                .logout(logout -> logout
                                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                                .logoutSuccessUrl("/proxy/8080/login?logout")
                                                .invalidateHttpSession(true)
                                                .clearAuthentication(true)
                                                .permitAll());

                return http.build();
        }
}