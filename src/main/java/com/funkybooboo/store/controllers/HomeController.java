package com.funkybooboo.store.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @RequestMapping("/")
    public String helloWorld() {
        return "redirect:/index.html";
    }
}
