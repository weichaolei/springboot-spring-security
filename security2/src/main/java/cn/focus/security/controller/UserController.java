package cn.focus.security.controller;

import cn.focus.security.service.OperationUserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;


/**
 * 用户控制器.
 * 
 * @author
 * @date 2017年2月26日
 */
@Controller
@RequestMapping("/users")
public class UserController {

	@Resource
	private OperationUserService operationUserService;


	@RequestMapping("/list")
	public String userList(Model model) {
        model.addAttribute("users", operationUserService.userList());
        return "/users/list";
	}

    @RequestMapping("/list2")
    public String userList2(Model model) {
        model.addAttribute("users", operationUserService.userList());
        return "/users/list2";
    }
}
