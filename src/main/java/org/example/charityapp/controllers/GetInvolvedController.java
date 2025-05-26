package org.example.charityapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GetInvolvedController {
    @GetMapping("/getinvolved")
    public String getInvolved() {
        return "getinvolved";
    }
}
