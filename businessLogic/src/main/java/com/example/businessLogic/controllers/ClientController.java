package com.example.businessLogic.controllers;

import com.example.businessLogic.dtos.clients.ClientSlimGetDto;
import com.example.businessLogic.dtos.mappers.ClientMapper;
import com.example.businessLogic.models.Client;
import com.example.businessLogic.services.interfaces.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Iterable<Client> getAll() {
        return clientService.getAll();
    }

    @GetMapping(path = "/{id}")
    public ClientSlimGetDto getById(@PathVariable long id) {
        return mapper.clientToClientSlimGetDto(clientService.getById(id));
    }

    @PostMapping
    public Client create(@Valid @RequestBody Client client) {
        System.out.println(client);
        return clientService.create(client);
    }

    @PutMapping(path = "/{id}")
    public Client update(@PathVariable Long id, @Valid @RequestBody Client client) {
        return clientService.updateById(id, client);
    }

    @DeleteMapping(path = "/{id}")
    public void deleteById(@PathVariable Long id) {
        clientService.deleteById(id);
    }

    @DeleteMapping
    public void deleteAll() {
        clientService.deleteAll();
    }

}
