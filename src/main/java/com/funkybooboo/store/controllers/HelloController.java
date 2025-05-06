package com.funkybooboo.store.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {

    // Serves static/hello.html
    @RequestMapping("/helloWorld")
    public String helloWorld() {
        return "redirect:/hello.html"; // Serves static file
    }

    // Uses Thymeleaf (templates/hello.html)
    @RequestMapping("/helloName")
    public String helloName(Model model) {
        model.addAttribute("name", "Nate");
        return "hello"; // Thymeleaf template
    }
}
