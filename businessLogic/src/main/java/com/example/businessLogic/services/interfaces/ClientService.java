package com.example.businessLogic.services.interfaces;

import com.example.businessLogic.models.Client;

public interface ClientService {

    Client create(Client client);
    Client getById(Long id);
    Client updateById(Long id, Client client);
    void deleteById(Long id);

    Iterable<Client> getAll();
    void deleteAll();
}
