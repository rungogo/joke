package com.lad.chestnut.security;

import com.lad.chestnut.pojo.model.User;
import com.lad.chestnut.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 *
 * @Description: 保护web应用
 *
 * @auther: lad
 * @date: 9:34 2019/6/30
 * @param:
 * @return:
 *
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

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
        http.formLogin()                       //  定义当需要用户登录时候，转到的登录页面
                .and()
                .authorizeRequests()                // 定义哪些URL需要被保护、哪些不需要被保护
                .antMatchers("/user/login")
//                .hasAuthority("ROLE_SPITTER")
                .authenticated()                    // 执行请求时必须以登录了应用
                .antMatchers(HttpMethod.GET, "/user/loginSecurity")
//                .authenticated()
                .hasAuthority("ROLE_USER")          // 具备的访问权限
                .anyRequest()                       // 任何请求,登录后可以访问
                .permitAll()                       // 允许请求没有任何安全限制
                .and().rememberMe();
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
                .usersByUsernameQuery("select username, password, enabled from user where username = ?")
                .authoritiesByUsernameQuery("select username, 'ROLE_USER' from user where username = ?")
        .passwordEncoder(new BCryptPasswordEncoder());
    }
}
