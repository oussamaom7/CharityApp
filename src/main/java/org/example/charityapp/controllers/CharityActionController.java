package org.example.charityapp.controllers;

import jakarta.validation.Valid;
import org.example.charityapp.DTO.CharityActionRequest;
import org.example.charityapp.DTO.CharityActionResponse;
import org.example.charityapp.services.CharityActionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/charity-actions")
public class CharityActionController {

    private final CharityActionService charityActionService;

    public CharityActionController(CharityActionService charityActionService) {
        this.charityActionService = charityActionService;
    }

    @GetMapping
    public ResponseEntity<List<CharityActionResponse>> getAllCharityActions() {
        List<CharityActionResponse> actions = charityActionService.getAllCharityActions();
        return ResponseEntity.ok(actions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CharityActionResponse> getCharityActionById(@PathVariable Long id) {
        CharityActionResponse action = charityActionService.getCharityActionById(id);
        return ResponseEntity.ok(action);
    }

    @PostMapping
    public ResponseEntity<CharityActionResponse> createCharityAction(@Valid @RequestBody CharityActionRequest request) {
        CharityActionResponse response = charityActionService.createCharityAction(request);
        return ResponseEntity.status(201).body(response);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteCharityAction(@PathVariable Long id) {
        charityActionService.deleteCharityAction(id);
        return ResponseEntity.noContent().build();
    }
}