package com.example.businessLogic.controllers;

import com.example.businessLogic.models.Client;
import com.example.businessLogic.services.interfaces.ClientService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api/clients")
@Validated
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public Iterable<Client> getAll() {
        return clientService.getAll();
    }

    @GetMapping(path = "/{id}")
    public Client getById(@PathVariable long id) {
        return clientService.getById(id);
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
