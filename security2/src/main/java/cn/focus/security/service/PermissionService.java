package cn.focus.security.service;

import cn.focus.security.mapper.PermissionMapper;
import cn.focus.security.model.Permission;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by WCL on 2017/11/2.
 */
@Service
public class PermissionService {

    @Resource
    private PermissionMapper permissionMapper;

    public List<Permission> allPermissionList() {
        return permissionMapper.allPermission();
    }

}
