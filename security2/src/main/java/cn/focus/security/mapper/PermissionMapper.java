package cn.focus.security.mapper;

import cn.focus.security.model.Permission;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by WCL on 2017/11/1.
 */
public interface PermissionMapper {

    @Select("<script>" +
            "SELECT description,url,role_name FROM permission p " +
            "INNER JOIN role_permission rp on rp.perm_id = p.id " +
            "INNER JOIN role r ON r.role_id = rp.role_id " +
            "</script>")
    List<Permission> allPermission();

    @Select("<script>" +
            "SELECT role_name FROM operation_user u " +
            "INNER JOIN role_user ru ON ru.user_id = u.user_id " +
            "INNER JOIN role r ON ru.role_id = r.role_id " +
            "INNER JOIN role_permission rp ON rp.role_id = r.role_id " +
            "INNER JOIN permission p ON rp.perm_id = p.id " +
            "WHERE u.user_id = #{userId} GROUP BY ru.role_id " +
            "</script>")
    List<Permission> getPermissionByUserId(@Param("userId") int userId);


}
