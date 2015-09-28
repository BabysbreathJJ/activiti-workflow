package me.kafeitu.demo.activiti.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * µÇÂ¼Ò³Ãæ
 *
 * @author HenryYan
 */
@Controller
public class LoginController {

    @RequestMapping(value = "/login")
    public String login() {
        return "login";
    }

}
