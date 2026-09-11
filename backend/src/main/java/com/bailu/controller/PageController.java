package com.bailu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/")
    public String index() {
        return "redirect:/login.html";  // 重定向到外部的前端文件
    }

    // 这些映射到外部的前端HTML文件
    @GetMapping("/profile")
    public String profilePage() {
        return "forward:/profile.html";  // 使用forward保持URL不变
    }

    @GetMapping("/author")
    public String authorPage() {
        return "forward:/author.html";
    }

    @GetMapping("/editor")
    public String editorPage() {
        return "forward:/editor.html";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "forward:/admin.html";
    }
}