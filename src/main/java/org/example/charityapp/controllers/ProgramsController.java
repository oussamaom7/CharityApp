package org.example.charityapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ProgramsController {
    @GetMapping("/programs")
    public String programs() {
        return "programs";
    }
}
