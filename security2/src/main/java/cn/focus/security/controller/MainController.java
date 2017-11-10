package cn.focus.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 主页控制器.
 * 
 * @since
 * @author
 */
@Controller
public class MainController {

	@RequestMapping("/main/login")
	public String customLogin() {
		return "/login";
	}

	@RequestMapping("/main/index")
	public String index() {
		return "/index2";
	}

    @RequestMapping("/login")
    public String login() {
        return "/index";
    }

	@RequestMapping("/403")
	public String accessDeniedPage(Model model) {
		model.addAttribute("message", "您无权访问该页面");
		return "/403";
	}
}
