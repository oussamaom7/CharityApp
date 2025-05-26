package org.example.charityapp.controllers;

import org.example.charityapp.entities.Blog;
import org.example.charityapp.repositories.BlogRepository;
import org.example.charityapp.services.UserActionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin/blog")
@PreAuthorize("hasRole('ADMIN')")
public class AdminBlogController {
    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private UserActionLogService userActionLogService;

    @GetMapping("/create")
    public String showCreateBlogForm(Model model) {
        model.addAttribute("blog", new Blog());
        return "admin/create-blog";
    }

    @PostMapping("/create")
    public String createBlog(@ModelAttribute Blog blog, Principal principal, Model model) {
        blog.setAuthor(principal.getName());
        blog.setCreatedAt(LocalDateTime.now());
        blogRepository.save(blog);
        userActionLogService.logAction(null, principal.getName(), "BLOG_CREATE", "Created blog: " + blog.getTitle());
        model.addAttribute("success", "Blog post created successfully!");
        model.addAttribute("blog", new Blog());
        return "admin/create-blog";
    }

    @GetMapping("/list")
    public String listBlogs(Model model) {
        model.addAttribute("blogs", blogRepository.findAll());
        return "admin/list-blogs";
    }

    @GetMapping("/edit/{id}")
    public String showEditBlogForm(@PathVariable Long id, Model model) {
        Blog blog = blogRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Blog not found"));
        model.addAttribute("blog", blog);
        return "admin/edit-blog";
    }

    @PostMapping("/edit/{id}")
    public String editBlog(@PathVariable Long id, @ModelAttribute Blog blog, Model model) {
        Blog existing = blogRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Blog not found"));
        existing.setTitle(blog.getTitle());
        existing.setContent(blog.getContent());
        blogRepository.save(existing);
        userActionLogService.logAction(null, existing.getAuthor(), "BLOG_EDIT", "Edited blog: " + existing.getTitle());
        model.addAttribute("blog", existing);
        model.addAttribute("success", "Blog post updated successfully!");
        return "admin/edit-blog";
    }

    @PostMapping("/delete/{id}")
    public String deleteBlog(@PathVariable Long id) {
        blogRepository.deleteById(id);
        userActionLogService.logAction(null, null, "BLOG_DELETE", "Deleted blog with id: " + id);
        return "redirect:/admin/blog/list";
    }
}
