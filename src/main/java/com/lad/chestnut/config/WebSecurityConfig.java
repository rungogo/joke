package com.lad.chestnut.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lad.chestnut.common.ResponseData;
import com.lad.chestnut.common.ResponseEnum;
import com.lad.chestnut.common.Token;
import com.lad.chestnut.pojo.model.User;
import com.lad.chestnut.service.UserService;
import com.lad.chestnut.util.EncryptDecode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 保护web应用
 *
 * @author lad
 * @date 2019/7/2
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Autowired
    private EncryptDecode encryptDecode;

    @Autowired
    CustomMetadataSource metadataSource;

    @Autowired
    UrlAccessDecisionManager urlAccessDecisionManager;

    @Autowired
    AuthenticationAccessDeniedHandler deniedHandler;

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
        http
                .formLogin()                       //  定义当需要用户登录时候，转到的登录页面
                .successHandler((req, resp, auth) -> {
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
                .permitAll()
                .and()
                // 定义哪些URL需要被保护、哪些不需要被保护
                .authorizeRequests()
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                        o.setSecurityMetadataSource(metadataSource);
                        o.setAccessDecisionManager(urlAccessDecisionManager);
                        return o;
                    }
                })
                // 允许请求没有任何安全限制
                .antMatchers("/user/signIn").authenticated()
                .antMatchers("/user/test1").permitAll()
                .antMatchers("/user/test2").authenticated()
                .antMatchers("/user/test/test1").permitAll()
                .antMatchers("/user/test/test2").authenticated()
                .antMatchers("/user/loginSecurity").permitAll()
                // 执行请求时必须以登录了应用
                .anyRequest()
                // 所有请求
                .authenticated()
                .and()
                .csrf().disable()
                .exceptionHandling().accessDeniedHandler(deniedHandler);
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
        auth.userDetailsService(userService).passwordEncoder(new BCryptPasswordEncoder());
    }
}
