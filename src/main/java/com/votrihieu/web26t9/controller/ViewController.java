package com.votrihieu.web26t9.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    public String index() {
        // Luôn chuyển hướng từ trang gốc đến trang admin
        return "redirect:/admin/home";
    }
    
    @GetMapping("/admin/home")
    public String adminHomePage() {
        // Trả về file templates/admin_home.html
        return "admin_home"; 
    }

    // Đảm bảo có phương thức này để xử lý GET request đến trang login
    @GetMapping("/login")
    public String loginPage() {
        // Tên trả về "login" phải khớp với tên file login.html
        return "login";
    }
}