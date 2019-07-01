package com.lad.chestnut.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lad.chestnut.common.ResponseData;
import com.lad.chestnut.common.ResponseEnum;
import com.lad.chestnut.common.Token;
import com.lad.chestnut.config.CustomMetadataSource;
import com.lad.chestnut.config.UrlAccessDecisionManager;
import com.lad.chestnut.pojo.model.User;
import com.lad.chestnut.service.UserService;
import com.lad.chestnut.util.EncryptDecode;
import org.apache.http.protocol.RequestDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

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

    @Autowired
    private UserService userService;

    @Autowired
    private EncryptDecode encryptDecode;

    @Autowired
    CustomMetadataSource metadataSource;

    @Autowired
    UrlAccessDecisionManager urlAccessDecisionManager;

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
        http.authorizeRequests()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setSecurityMetadataSource(metadataSource);
                        o.setAccessDecisionManager(urlAccessDecisionManager);
                        return o;
                    }
                })
                .and()
                .formLogin()                       //  定义当需要用户登录时候，转到的登录页面
                .successHandler((req, resp, auth) ->{
                    resp.setContentType("application/json;charset=utf-8");
                    User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                    Token token = new Token();
                    token.setUsername(user.getUsername());
                    token.setExpireTime(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000);
                    Map<String, String> map = new HashMap<>(2);
                    map.put("token", encryptDecode.encryptToken(token));
                    ResponseData responseData = new ResponseData(map);
                    ObjectMapper om = new ObjectMapper();
                    PrintWriter out = resp.getWriter();
                    out.write(om.writeValueAsString(responseData));
                    out.flush();
                    out.close();
                })
                .failureHandler((req, resp, e) -> {
                    resp.setContentType("application/json;charset=utf-8");
                   ResponseData responseData;
                    if (e instanceof BadCredentialsException || e instanceof UsernameNotFoundException) {
                        responseData = new ResponseData(ResponseEnum.LOGIN_FAILURE_USERNAME_OR_PASSWORD_WRONG);
                    } else if (e instanceof LockedException) {
                        responseData = new ResponseData(ResponseEnum.ACCOUNT_LOCKED);
                    } else if (e instanceof CredentialsExpiredException) {
                        responseData = new ResponseData(ResponseEnum.PASSWORD_AGING);
                    } else if (e instanceof AccountExpiredException) {
                        responseData = new ResponseData(ResponseEnum.OVERDUE_ACCOUNT);
                    } else if (e instanceof DisabledException) {
                        responseData = new ResponseData(ResponseEnum.ACCOUNT_DISABLED);
                    } else {
                        responseData = new ResponseData(ResponseEnum.FAIL);
                    }
                    resp.setStatus(401);
                    ObjectMapper om = new ObjectMapper();
                    PrintWriter out = resp.getWriter();
                    out.write(om.writeValueAsString(responseData));
                    out.flush();
                    out.close();
                })
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler((req, resp, authentication) -> {
                    resp.setContentType("application/json;charset=utf-8");
                    ObjectMapper om = new ObjectMapper();
                    PrintWriter out = resp.getWriter();
                    out.write(om.writeValueAsString(ResponseData.success()));
                    out.flush();
                    out.close();
                })
                .and()
                .authorizeRequests()                // 定义哪些URL需要被保护、哪些不需要被保护
                .antMatchers("/user/login")
                .authenticated()                    // 执行请9求时必须以登录了应用
//                .antMatchers(HttpMethod.GET, "/user/loginSecurity")
//                .hasAuthority("ROLE_manager")          // 具备的访问权限
//                .anyRequest()                       // 任何请求,登录后可以访问
//                .permitAll()                       // 允许请求没有任何安全限制
                .and()
                .csrf().disable()
                .rememberMe();
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
//        auth.jdbcAuthentication()
//                .dataSource(dataSource)
//                .usersByUsernameQuery("select username, password, enabled from user where username = ?")
//                .authoritiesByUsernameQuery("select username, 'ROLE_USER' from user where username = ?")
//        .passwordEncoder(new BCryptPasswordEncoder());
        auth.userDetailsService(userService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }
}
