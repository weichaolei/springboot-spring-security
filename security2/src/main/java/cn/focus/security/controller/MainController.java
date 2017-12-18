package cn.focus.security.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 主页控制器.
 *
 * @author
 */
@Controller
public class MainController {

    @RequestMapping("/main/login")
    public String customLogin() {
        return "/login";
    }

    @RequestMapping("/main/index")
    public String index(HttpServletResponse response, Authentication authentication, HttpSession session) {
        System.out.println(session.getAttribute("SPRING_SECURITY_CONTEXT"));
        Cookie cookie = new Cookie("mySessionId", authentication.getName());
        response.addCookie(cookie);
        return "/menu";
    }

    @RequestMapping("/main/logout")
    public String logout(HttpServletResponse response, HttpServletRequest request) {
        Cookie cookie = new Cookie("mySessionId", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); //使当前会话失效
        }

        SecurityContextHolder.clearContext(); //清空安全上下文
        return "/login";
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
