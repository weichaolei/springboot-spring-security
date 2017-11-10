package cn.focus.security.mapper;

import cn.focus.security.model.OperationUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by WCL on 2017/11/9.
 */
public interface OperationUserMapper {

    @Select("SELECT * FROM operation_user WHERE user_name = #{username} LIMIT 1")
    OperationUser getUserByUserName(@Param("username") String userName);

    @Select("SELECT * FROM operation_user")
    List<OperationUser> getUserList();
}
