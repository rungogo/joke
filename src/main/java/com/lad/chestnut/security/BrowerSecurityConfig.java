package com.lad.chestnut.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author lad
 */
@Configuration
public class BrowerSecurityConfig extends WebSecurityConfigurerAdapter {

//
//    @Resource
//    private UserDetailsService customUserService;

    @Autowired
    private DataSource dataSource;

    /**
     *
     * @Description: 通过重载，配置Spring Security的Filer链
     *
     * @auther: lad
     * @date: 22:39 2019/6/28
     * @param: [http]
     * @return: void
     *
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()                            //  定义当需要用户登录时候，转到的登录页面
                .and()
                .authorizeRequests()                // 定义哪些URL需要被保护、哪些不需要被保护
                .antMatchers("/login")
//                .hasAuthority("ROLE_SPITTER")
                .authenticated()                    // 执行请求时必须以登录了应用
                .antMatchers(HttpMethod.GET, "/loginSecurity")
//                .authenticated()
                .hasAuthority("ROLE_ADMIN")          // 具备的访问权限
                .anyRequest()                       // 任何请求,登录后可以访问
                .permitAll();                       // 允许请求没有任何安全限制
    }

    /**
     *
     * @Description: 通过重载，配置如何通过拦截器保护请求
     * 配置用户存储
     *
     * @auther: lad
     * @date: 22:40 2019/6/28
     * @param: [auth]
     * @return: void
     *
     */
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select user_name, password, true from user where user_name = ?")
                .authoritiesByUsernameQuery("select user_name, 'ROLE_ADMIN' from user where user_name = ?")
        .passwordEncoder(new BCryptPasswordEncoder());
    }
}
