package com.sli.config;

import com.sli.handler.CustomAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
//@EnableGlobalMethodSecurity(jsr250Enabled = true) // 控制权限注解 @RolesAllowed({"ROOT"}) @PermitAll @DenyAll
//@EnableGlobalMethodSecurity(securedEnabled = true) // 控制权限注解 @Secured({ "ROLE_DBA", "ROLE_ADMIN" })
@EnableGlobalMethodSecurity(prePostEnabled = true) // 控制权限注解  @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ROOT')")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .passwordEncoder(passwordEncoder)
                .withUser("root")
                .password(passwordEncoder.encode("123456"))
                .roles("ROOT", "USER")
                .and()
                .withUser("user")
                .password(passwordEncoder.encode("123456"))
                .roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()              // 表单方式
                .and()
                .authorizeRequests()  // 授权配置
//                .antMatchers("/hello").permitAll()
//                .antMatchers("/root/**").hasRole("ROOT")
//                .antMatchers("/user/**").hasRole("USER")
                .anyRequest()         // 所有请求
                .authenticated()    // 都需要认证
                .and().exceptionHandling().accessDeniedHandler(customAccessDeniedHandler)
                .and().csrf().disable();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}