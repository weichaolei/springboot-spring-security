package cn.focus.security.component;

import cn.focus.security.service.MyAccessDecisionManager;
import cn.focus.security.service.MyInvocationSecurityMetadataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by WCL on 2017/11/9.
 * @author Wcl
 * @since 2017/11/9
 */
@Component
public class MySecurityFilter extends AbstractSecurityInterceptor implements Filter {

    private static final List<String> EXCLUSIVE_PATH_LIST =
            Arrays.asList(new String[] {"/main/login", "/login", "/css/**", "/js/**", "/fonts/**", "/favicon.ico"}) ;

    @Resource
    private MyInvocationSecurityMetadataSourceService myInvocationSecurityMetadataSourceService;

    @Autowired
    public void setMyAccessDecisionManager(MyAccessDecisionManager myAccessDecisionManager) {
        super.setAccessDecisionManager(myAccessDecisionManager);
    }

    @Override
    public Class<?> getSecureObjectClass() {
        return FilterInvocation.class;
    }

    @Override
    public SecurityMetadataSource obtainSecurityMetadataSource() {
        return this.myInvocationSecurityMetadataSourceService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        FilterInvocation fi = new FilterInvocation(servletRequest, servletResponse, filterChain);
        if (EXCLUSIVE_PATH_LIST.contains(fi.getHttpRequest().getRequestURI())) {
            fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
        } else {
            invoke(fi);
        }
    }

    public void invoke(FilterInvocation filterInvocation) throws IOException, ServletException {
        InterceptorStatusToken token = super.beforeInvocation(filterInvocation);
        try {
            filterInvocation.getChain().doFilter(filterInvocation.getRequest(), filterInvocation.getResponse());
        } finally {
            super.afterInvocation(token, null);
        }
    }

    @Override
    public void destroy() {

    }
}
