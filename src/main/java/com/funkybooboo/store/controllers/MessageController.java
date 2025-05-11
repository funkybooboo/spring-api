package com.funkybooboo.store.controllers;

import com.funkybooboo.store.entities.Message;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

/**
 * Controller that returns plain text and JSON responses using @RestController.
 * Methods return actual data, not views.
 */
@RestController
@Tag(name = "Messages")
public class MessageController {

    /**
     * Returns a plain string (text/plain).
     */
    @RequestMapping("/helloText")
    public String helloText() {
        return "Hello World!";
    }

    /**
     * Returns a custom JSON object with a message.
     */
    @RequestMapping("/helloJson")
    public Message helloJson() {
        return new Message("Hello World!");
    }

    /**
     * Returns a greeting using a query parameter (?name=Nate).
     */
    @GetMapping("/greetWithName")
    public String greetWithName(@RequestParam String name) {
        return "Hello, " + name + "!";
    }

    /**
     * Returns a greeting using a path variable.
     */
    @GetMapping("/greetUser/{username}")
    public String greetUser(@PathVariable String username) {
        return "Hi, " + username + "!";
    }
    
    /**
     * Accepts a POST request with a Message in the request body.
     * Expects JSON like: {"text": "Hi from client"}
     * Returns the same message with extra confirmation.
     */
    @PostMapping("/echoMessage")
    public Message echoMessage(@RequestBody Message message) {
        return new Message("You said: " + message.getText());
    }
}
