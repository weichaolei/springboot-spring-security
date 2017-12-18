package cn.focus.security.config;

import cn.focus.security.component.MySecurityFilter;
import cn.focus.security.service.MyAccessDeniedHandler;
import cn.focus.security.service.MyUserDetailServiceImpl;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;

import javax.servlet.Filter;

/**
 * Spring Security 配置类.
 *
 * @author wcl
 * @since
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String[] exclusivePaths = {"/main/login", "/main/logout", "/login", "/css/**", "/js/**", "/fonts/**", "/favicon.ico"};

    @Bean
    public Filter myFilterSecurityInterceptorFilter() {
        return new MySecurityFilter();
    }

    @Bean
    public FilterRegistrationBean registrationBean() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(myFilterSecurityInterceptorFilter());
        registration.addUrlPatterns("/*");
        registration.setName("myFilterSecurityInterceptorFilter");
        return registration;
    }

    @Bean
    public AccessDeniedHandler getAccessDeniedHandler() {
        return new MyAccessDeniedHandler("/403");
    }

    @Bean
    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers(exclusivePaths).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/main/login")
                .defaultSuccessUrl("/main/index")
                .failureUrl("/main/login?error=true").permitAll()
                .and().logout().logoutSuccessUrl("/main/login").permitAll()
                .and().sessionManagement().maximumSessions(1);
        http.addFilterBefore(myFilterSecurityInterceptorFilter(), FilterSecurityInterceptor.class);
        http.exceptionHandling().accessDeniedHandler(getAccessDeniedHandler());
    }

    @Override
    @Bean
    public UserDetailsService userDetailsServiceBean() {
        return new MyUserDetailServiceImpl();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceBean());
    }

}
