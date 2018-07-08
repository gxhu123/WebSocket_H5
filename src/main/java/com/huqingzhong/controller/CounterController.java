package com.huqingzhong.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class CounterController {

    @RequestMapping("/")
    public String index(HttpSession session){
        System.out.println("Controller... ..session="+session.getId());
        return "index";
    }
}
