package org.example.charityapp.controllers;

import org.example.charityapp.entities.Program;
import org.example.charityapp.services.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/programs")
public class AdminProgramsController {
    private final ProgramService programService;

    @Autowired
    public AdminProgramsController(ProgramService programService) {
        this.programService = programService;
    }

    @GetMapping
    public String adminPrograms(Model model) {
        model.addAttribute("programs", programService.getAllPrograms());
        return "admin/programs";
    }

    @GetMapping("/add")
    public String showAddProgramForm(Model model) {
        model.addAttribute("program", new Program());
        return "admin/add-program";
    }

    @PostMapping("/add")
    public String handleAddProgram(@ModelAttribute Program program) {
        programService.createProgram(program);
        return "redirect:/admin/programs";
    }
}
