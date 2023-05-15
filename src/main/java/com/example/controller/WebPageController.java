package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Lex
 */
@Controller
public class WebPageController {

    @GetMapping("/")
    public String home() {
        return "index.html";
    }

    @GetMapping("/websocket")
    public String page1() {
        return "websocket.html";
    }

    @GetMapping("/file-list")
    public String page2() {
        return "file-list.html";
    }
}

