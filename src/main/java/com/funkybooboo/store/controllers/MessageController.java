package com.funkybooboo.store.controllers;

import com.funkybooboo.store.entities.Message;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {
    @RequestMapping("/helloText")
    public String helloText() {
        return "Hello World!";
    }

    @RequestMapping("/helloJson")
    public Message helloJson() {
        return new Message("Hello World!");
    }
}
