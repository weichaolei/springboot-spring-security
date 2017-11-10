package cn.focus.security.service;

import cn.focus.security.mapper.OperationUserMapper;
import cn.focus.security.mapper.PermissionMapper;
import cn.focus.security.model.OperationUser;
import cn.focus.security.model.Permission;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 验证用户是否存在，并获取该用户的权限
 *
 * @author WCL
 * @since 2017/11/7.
 */
@Service
public class MyUserDetailServiceImpl implements UserDetailsService {

    //list默认大小
    private static final int LIST_INIT_SIZE = 4;

    @Resource
    private OperationUserMapper operationUserMapper;

    @Resource
    private PermissionMapper permissionMapper;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        System.out.println("进来了。。。");
        if (StringUtils.isEmpty(userName)) {
            return null;
        }
        OperationUser user = operationUserMapper.getUserByUserName(userName);
        if (user != null) {
            List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
            List<Permission> permissionList = permissionMapper.getPermissionByUserId(user.getUserId());
            permissionList.forEach(permission -> {
                if (permission != null && !StringUtils.isEmpty(permission.getRoleName())) {
                    GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(permission.getRoleName());
                    grantedAuthorityList.add(grantedAuthority);
                }
            });
            user.setAuthorities(grantedAuthorityList);
            return user;
        } else {
            throw new UsernameNotFoundException("username:" + userName + " do not exist");
        }
    }
}
