package com.biosphere.usermodule.config;

// import com.biosphere.zuccbiosphere.filter.JWTAuthenticationFilter;
import com.biosphere.usermodule.filter.JWTAuthenticationFilter;
import com.biosphere.usermodule.handler.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    /**
     * 无权限处理类
     */
    private final UserAccessDeniedHandler userAccessDeniedHandler;

    /**
     * 用户未登录处理类
     */
    private final UserNotLoginHandler userNotLoginHandler;

    /**
     * 用户登录成功处理类
     */
    private final UserLoginSuccessHandler userLoginSuccessHandler;

    /**
     * 用户登录失败处理类
     */
    private final UserLoginFailureHandler userLoginFailureHandler;

    /**
     * 用户登出成功处理类
     */
    private final UserLogoutSuccessHandler userLogoutSuccessHandler;

    /**
     * 用户登录验证
     */
    // private final UserAuthenticationProvider userAuthenticationProvider;

    /**
     * 用户权限注解
     */
    // private final UserPermissionEvaluator userPermissionEvaluator;

    public SecurityConfig(UserAccessDeniedHandler userAccessDeniedHandler, UserNotLoginHandler userNotLoginHandler, UserLoginSuccessHandler userLoginSuccessHandler, UserLoginFailureHandler userLoginFailureHandler, UserLogoutSuccessHandler userLogoutSuccessHandler) {
        this.userAccessDeniedHandler = userAccessDeniedHandler;
        this.userNotLoginHandler = userNotLoginHandler;
        this.userLoginSuccessHandler = userLoginSuccessHandler;
        this.userLoginFailureHandler = userLoginFailureHandler;
        this.userLogoutSuccessHandler = userLogoutSuccessHandler;
        // this.userAuthenticationProvider = userAuthenticationProvider;
        // this.userPermissionEvaluator = userPermissionEvaluator;
    }

    /**
     * 加密方式
     *
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        return new JWTAuthenticationFilter(authenticationManager());
    }

    /**
     * 注入自定义PermissionEvaluator
     *
     */
    /*@Bean
    public DefaultWebSecurityExpressionHandler userSecurityExpressionHandler() {
        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();
        handler.setPermissionEvaluator(userPermissionEvaluator);
        return handler;
    }*/

    /**
     * 用户登录验证
     */
    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(userAuthenticationProvider);
    }*/

    /**
     * 安全权限配置
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilter(jwtAuthenticationFilter()); //// 添加JWT过滤器
        http.authorizeRequests() // 权限配置
                .antMatchers(
                        "/user/login",
                        "/user/exposure/**",
                        "/user/test",
                        "/css/**",
                        "/js/**",
                        "/webjars/**",
                        "/swagger-resources/**",
                        "/v2/api-docs/**",
                        "/swagger-ui.html"

                ).permitAll()//配置免拦截接口
                .anyRequest().authenticated() // 其他的需要登陆后才能访问
                .and().httpBasic().authenticationEntryPoint(userNotLoginHandler) // 配置未登录处理类
                // .and().formLogin().loginProcessingUrl("/api/login")// 配置登录URL
                .and().formLogin()
                .successHandler(userLoginSuccessHandler) // 配置登录成功处理类
                .failureHandler(userLoginFailureHandler) // 配置登录失败处理类
                // .and().logout().logoutUrl("/api/logout")// 配置登出地址
                // .logoutSuccessHandler(userLogoutSuccessHandler) // 配置用户登出处理类
                // .and().exceptionHandling().accessDeniedHandler(userAccessDeniedHandler)// 配置没有权限处理类
                .and().cors()// 开启跨域
                .and().csrf().disable(); // 禁用跨站请求伪造防护
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // 禁用session（使用Token认证）
        http.headers().cacheControl(); // 禁用缓存
    }
}
