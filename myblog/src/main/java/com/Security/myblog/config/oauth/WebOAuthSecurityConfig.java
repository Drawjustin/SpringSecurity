package com.Security.myblog.config.oauth;

import com.Security.myblog.config.TokenAuthenticationFilter;
import com.Security.myblog.config.jwt.TokenProvider;
import com.Security.myblog.domain.RefreshToken;
import com.Security.myblog.repository.RefreshTokenRepository;
import com.Security.myblog.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizationSuccessHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
public class WebOAuthSecurityConfig {
    private final OAuth2UserCustomService oAuth2UserCustomService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    @Bean
    public WebSecurityCustomizer configure(){
        return (web) -> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers("/img/**","/css/**","/js/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable);

        http.sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);



        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry.requestMatchers("/api/token").permitAll()
                                .requestMatchers("/api/**").authenticated()
                                .anyRequest().permitAll());

        http.oauth2Login(oauth2Login -> oauth2Login.loginPage("/login")
                .authorizationEndpoint(authorizationEndpointConfig ->
                        authorizationEndpointConfig.authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository()))
                        .successHandler(oAuth2SuccessHandler())
                        .userInfoEndpoint(userInfoEndpointConfig ->
                                userInfoEndpointConfig.userService(oAuth2UserCustomService)));


        http.logout(logout->logout.logoutSuccessUrl("/login"));

        http.exceptionHandling(httpSecurityExceptionHandlingConfigurer ->
                httpSecurityExceptionHandlingConfigurer.defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                        new AntPathRequestMatcher("/api/**")));

        return http.build();

    }
    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository(){
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }
    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler(){
        return new OAuth2SuccessHandler(tokenProvider,refreshTokenRepository,oAuth2AuthorizationRequestBasedOnCookieRepository(),
                userService);
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter(){
        return new TokenAuthenticationFilter(tokenProvider);
    }


}
