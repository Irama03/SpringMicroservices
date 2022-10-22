package com.example.businessLogic.controllers;

import com.example.businessLogic.dtos.LessorPostDto;
import com.example.businessLogic.models.Lessor;
import com.example.businessLogic.services.LessorService;
import org.springframework.web.bind.annotation.*;

@RestController(value = "api/lessors")
public class LessorController {

    private final LessorService lessorService;

    public LessorController(LessorService lessorService) {
        this.lessorService = lessorService;
    }

    @GetMapping
    public Iterable<Lessor> getAll() {
        return lessorService.getAll();
    }

    @GetMapping(path = "/{id}")
    public Lessor getById(@PathVariable long id) {
        return lessorService.getById(id);
    }

    @PostMapping
    public Lessor create(@RequestBody LessorPostDto lessor) {
        return lessorService.create(lessor);
    }

    @PutMapping(path = "/{id}")
    public Lessor update(@PathVariable long id, @RequestBody LessorPostDto lessor) {
        return lessorService.updateById(id, lessor);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteById(@PathVariable long id) {
        lessorService.deleteById(id);
    }

    @DeleteMapping
    public void deleteAll() {
        lessorService.deleteAll();
    }






}
