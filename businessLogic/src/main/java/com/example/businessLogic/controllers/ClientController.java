package com.example.businessLogic.controllers;

import com.example.businessLogic.dtos.clients.ClientPostDto;
import com.example.businessLogic.dtos.clients.ClientSlimGetDto;
import com.example.businessLogic.dtos.mappers.ClientMapper;
import com.example.businessLogic.models.Client;
import com.example.businessLogic.services.interfaces.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/clients")
@Validated
public class ClientController {

    private final ClientService clientService;

    private final ClientMapper mapper;

    @Autowired
    public ClientController(ClientService clientService, ClientMapper mapper) {
        this.clientService = clientService;
        this.mapper = mapper;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Iterable<Client> getAll() {
        return clientService.getAll();
    }

    @GetMapping(path = "/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','LESSOR')")
    public ClientSlimGetDto getById(@PathVariable long id) {
        return mapper.clientToClientSlimGetDto(clientService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','CLIENT')")
    public Client create(@Valid @RequestBody ClientPostDto clientPostDto) {
        return clientService.create(mapper.clientPostDtoToClient(clientPostDto));
    }

    @PutMapping(path = "/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','CLIENT')")
    public Client update(@PathVariable Long id, @Valid @RequestBody ClientPostDto clientPostDto) {
        return clientService.updateById(id, mapper.clientPostDtoToClient(clientPostDto));
    }

    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','CLIENT')")
    public void deleteById(@PathVariable Long id) {
        clientService.deleteById(id);
    }

    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public void deleteAll() {
        clientService.deleteAll();
    }

}
