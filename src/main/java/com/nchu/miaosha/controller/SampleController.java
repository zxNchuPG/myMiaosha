package com.nchu.miaosha.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/demo")
public class SampleController {
    @RequestMapping(value = "/thymeleaf")
    public String thymeleaf(Model model) {
        model.addAttribute("name", "zxnchu");
        return "hello";
    }
}
