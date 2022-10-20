package com.mysite.sbb.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

    @RequestMapping("/sbb")
    public String index() {
        return "index";
    }

    @RequestMapping("/")
    public String root() {
        return "redirect:/question/list";
    }
}
