package com.aquaman.security.admin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 资源服务器配置
 * @author 创建者 wei.wang
 * @author 修改者 wei.wang
 * @version 2019/2/26
 * @since 2019/2/26
 */
@Configuration
public class AquamanWebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 声明一个Bean，该Bean主要用于spring security拿到用户对象password后进行matches匹配密码是否正确
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.
            httpBasic().and()
            // 请求授权
            .authorizeRequests()
            // 过滤OPTIONS请求
            .antMatchers(HttpMethod.OPTIONS).permitAll()
            // 过滤该URL
            // .antMatchers("/test").permitAll()
            // 任何请求
            .anyRequest().authenticated()
            // 取消跨站请求防护
            .and().csrf().disable();
    }
}
