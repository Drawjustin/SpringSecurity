package com.Security.myblog.config;

import com.Security.myblog.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {
    private final UserDetailService userService;

    @Bean
    public WebSecurityCustomizer configure(){
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console())
                .requestMatchers("/static/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws  Exception {
        return http
                .authorizeHttpRequests(authorize-> authorize
                        .requestMatchers("/login","/signup","/user").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form.loginPage("/login")
                        .defaultSuccessUrl("/articles"))
                .logout(logout -> logout.logoutSuccessUrl("/login")
                        .invalidateHttpSession(true))
                .csrf(AbstractHttpConfigurer :: disable)
                .build();
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(bCryptPasswordEncoder());
        return authProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder,
//                                                       UserDetailService userDetailService) throws Exception{
//        return http.getSharedObject(AuthenticationManagerBuilder.class)
//                .userDetailsService(userService)
//                .passwordEncoder(bCryptPasswordEncoder)
//                .and().build();
//
//    }
}
