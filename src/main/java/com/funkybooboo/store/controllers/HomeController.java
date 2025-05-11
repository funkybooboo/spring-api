package com.funkybooboo.store.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Tag(name = "Home")
public class HomeController {
    @RequestMapping("/")
    public String helloWorld() {
        return "redirect:/index.html";
    }
}
