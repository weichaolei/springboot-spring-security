package cn.focus.security.service;

import cn.focus.security.model.Permission;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 *
 * @author WCL
 * @since 2017/11/9
 */
@Service
public class MyInvocationSecurityMetadataSourceService implements FilterInvocationSecurityMetadataSource {

    @Resource
    private PermissionService permissionService;

    private static final int MAP_INIT_SIZE = 5;
    private Map<String, Collection<ConfigAttribute>> permissionMap = null;

    /**
     * 加载所有权限
     */
    public void loadResourceDefine() {
        permissionMap = new HashMap<>(MAP_INIT_SIZE);
        List<Permission> permissions = permissionService.allPermissionList();

        for (Permission perm : permissions) {
            if (permissionMap.containsKey(perm.getUrl())) {
                Collection<ConfigAttribute> configAttributeList = permissionMap.get(perm.getUrl());
                ConfigAttribute cfg = new SecurityConfig(perm.getRoleName());
                configAttributeList.add(cfg);
            } else {
                Collection<ConfigAttribute> configAttributeList = new LinkedList<>();
                ConfigAttribute cfg = new SecurityConfig(perm.getRoleName());
                configAttributeList.add(cfg);
                permissionMap.put(perm.getUrl(), configAttributeList);
            }
        }
    }


    /**
     * 为了判定用户请求的url 是否在权限表中，如果在权限表中，则返回给 decide 方法，用来判定用户是否有此权限。如果不在权限表中则放行。
     * @param object
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        //加载所有权限
        loadResourceDefine();
        //object 中包含用户请求的request 信息
        HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();

        Iterator<String> iterator = permissionMap.keySet().iterator();
        while (iterator.hasNext()) {
            String url = iterator.next();
            AntPathRequestMatcher matcher = new AntPathRequestMatcher(url);
            if (matcher.matches(request)) {
                return permissionMap.get(url);
            }
        }
        return null;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
