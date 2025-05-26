package org.example.charityapp.services;

import org.example.charityapp.entities.Program;
import org.example.charityapp.repositories.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgramService {
    private final ProgramRepository programRepository;

    @Autowired
    public ProgramService(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    public void createProgram(Program program) {
        programRepository.save(program);
    }

    public List<Program> getAllPrograms() {
        return programRepository.findAll();
    }
}
