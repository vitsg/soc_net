package com.homework.social_network.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.homework.social_network.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("#{'${spring.security.allow.urls}'.split(',')}")
    private List<String> allowUrls;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable()
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint((httpServletRequest, httpServletResponse, e) ->
                httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value())).and()
                .formLogin().loginProcessingUrl("/api/login")
                .usernameParameter("login")
                .passwordParameter("password")
                .successHandler((httpServletRequest, httpServletResponse, authentication) ->
                        httpServletResponse.setStatus(HttpStatus.OK.value()))
                .failureHandler((httpServletRequest, httpServletResponse, e) -> {
                    httpServletResponse.setContentType("text/html; charset=UTF-8");
                    httpServletResponse.setCharacterEncoding("UTF-8");
                    httpServletResponse.setStatus(HttpStatus.BAD_REQUEST.value());
                    Map<String, Object> data = new HashMap<>();
                    data.put("error", "Неправильный логин или пароль");
                    httpServletResponse.getWriter()
                            .println(new ObjectMapper().writeValueAsString(data));
                })
                .and()
                .logout().logoutUrl("/api/logout")
                .logoutSuccessHandler((httpServletRequest, httpServletResponse, authentication) ->
                        httpServletResponse.setStatus(HttpStatus.OK.value()))
                .and()
                .authorizeRequests()
                .antMatchers(allowUrls.toArray(new String[allowUrls.size()])).permitAll()
                .antMatchers("/api/**").authenticated()
                .anyRequest().permitAll()
                .and()
                .rememberMe().rememberMeParameter("rememberMe").tokenValiditySeconds(86400 * 365).key("socialNetwork")
                .rememberMeCookieName("socialNetwork");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authService::findByEmail).passwordEncoder(passwordEncoder);
    }
}
