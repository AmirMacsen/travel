package com.example.travel.sercurity;

import com.example.travel.pojo.Admin;
import com.example.travel.pojo.Permission;
import com.example.travel.service.AdminService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.List;

// security配置类
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/backstage/admin/login").permitAll()
                        .requestMatchers("/backstage/admin_login").permitAll()
                        .requestMatchers("/backstage/admin_fail").permitAll()
                        // 明确放开对.css和.js文件的访问
                        .requestMatchers((request) ->
                                request.getRequestURI().endsWith(".css") ||
                                        request.getRequestURI().endsWith(".js")
                                || request.getRequestURI().endsWith("favicon.ico")
                                || request.getRequestURI().endsWith(".jpg")
                                || request.getRequestURI().startsWith("/backstage/js")
                                || request.getRequestURI().startsWith("/backstage/img")
                                || request.getRequestURI().startsWith("/backstage/plugins")
                        ).permitAll()
                        .requestMatchers("/backstage/**").authenticated()
                )
                .formLogin((auth) -> auth.loginPage("/backstage/admin_login")  // 自定义登录表单
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .loginProcessingUrl("/backstage/admin/login") // 表单提交路径
                    .successForwardUrl("/backstage/index") // 登录成功后跳转路径
                    .failureForwardUrl("/backstage/admin_fail") // 登录失败后跳转路径
                )
                .logout((logout) -> logout
                        .logoutUrl("/backstage/admin/logout")
                        .logoutSuccessUrl("/backstage/admin/login/")
                        .clearAuthentication(true) // 退出登录之后清除认证信息
                        .invalidateHttpSession(true) // 退出登录之后清除seesion
                )
                .exceptionHandling((exception) -> exception
                        .accessDeniedHandler(new LocalAccessDeniedHandler()) // 自定义无权限访问时的处理
                )
                .csrf(AbstractHttpConfigurer::disable) // 禁用csrf
                .cors().configurationSource(corsConfigurationSource()); // 开启跨域访问

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(AdminService adminService) {
        return username -> {
            Admin admin = adminService.findByUsername(username);
            if (adminService.findByUsername(username) == null) {
                throw new UsernameNotFoundException("用户不存在");
            }
            if (!admin.isStatus()) {
                throw new UsernameNotFoundException("用户被禁用");
            }
            List<Permission> permissionList = adminService.findPermissionByUsername(username);
            List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
            permissionList.forEach(permission -> grantedAuthorities.add(new SimpleGrantedAuthority(permission.getPermissionDesc())));
            // 3.封装为UserDetails对象
            return User.withUsername(admin.getUsername())
                    .password(admin.getPassword())
                    .authorities(grantedAuthorities)
                    .build();
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder, UserDetailsService userDetailsService)
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder).and()
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*"); // 允许所有来源
        configuration.addAllowedHeader("*"); // 允许所有头
        configuration.addAllowedMethod("*"); // 允许所有方法
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
