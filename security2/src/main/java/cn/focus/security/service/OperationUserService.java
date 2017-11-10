package cn.focus.security.service;

import cn.focus.security.mapper.OperationUserMapper;
import cn.focus.security.model.OperationUser;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by WCL on 2017/11/10.
 */
@Service
public class OperationUserService {

    @Resource
    private OperationUserMapper operationUserMapper;

    public List<OperationUser> userList() {
        return operationUserMapper.getUserList();
    }

}
