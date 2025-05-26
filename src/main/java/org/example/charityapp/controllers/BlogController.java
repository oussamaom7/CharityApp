package org.example.charityapp.controllers;

import org.example.charityapp.entities.Blog;
import org.example.charityapp.repositories.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@Controller
public class BlogController {
    @Autowired
    private BlogRepository blogRepository;

    @GetMapping("/blog")
    public String blog(Model model) {
        List<Blog> blogs = blogRepository.findAll();
        model.addAttribute("blogs", blogs);
        return "blog";
    }
}
