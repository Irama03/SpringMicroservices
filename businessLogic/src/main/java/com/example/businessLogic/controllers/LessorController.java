package com.example.businessLogic.controllers;

import com.example.businessLogic.dtos.lessor.LessorPostDto;
import com.example.businessLogic.models.Lessor;
import com.example.businessLogic.services.interfaces.LessorService;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/lessors")
@Validated
public class LessorController {

    private final LessorService lessorService;

    public LessorController(LessorService lessorService) {
        this.lessorService = lessorService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMIN','CLIENT','LESSOR')")
    public Iterable<Lessor> getAll() {
        return lessorService.getAll();
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMIN','CLIENT','LESSOR')")
    public Lessor getById(@PathVariable long id) {
        return lessorService.getById(id);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMIN','LESSOR')")
    public Lessor create(@Valid @RequestBody LessorPostDto lessor) {
        return lessorService.create(lessor);
    }

    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMIN','LESSOR')")
    public Lessor update(@PathVariable long id, @Valid @RequestBody LessorPostDto lessor) {
        return lessorService.updateById(id, lessor);
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','LESSOR')")
    public void deleteById(@PathVariable long id) {
        lessorService.deleteById(id);
    }

    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void deleteAll() {
        lessorService.deleteAll();
    }



}
