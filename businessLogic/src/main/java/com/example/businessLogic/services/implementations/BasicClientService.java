package com.example.businessLogic.services.implementations;

import com.example.businessLogic.exceptions.RecordNotFoundException;
import com.example.businessLogic.exceptions.ValueNotUniqueException;
import com.example.businessLogic.models.Client;
import com.example.businessLogic.repositories.ClientRepository;
import com.example.businessLogic.services.interfaces.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
public class BasicClientService implements ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public BasicClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Client create(@Valid Client client) {
        checkForUniqueEmail(client.getEmail());
        checkForUniquePhone(client.getPhone());

        return clientRepository.save(client);
    }

    @Override
    public Client getById(Long id) {
        return clientRepository
                .findById(id)
                .orElseThrow(() -> new RecordNotFoundException(Client.class, "id", id));
    }

    @Override
    public Client updateById(Long id, @Valid Client client) {
        Client oldClient = getById(id);

        if(!client.getEmail().equals(oldClient.getEmail())) {
            checkForUniqueEmail(client.getEmail());
        }
        if(!client.getPhone().equals(oldClient.getPhone())) {
            checkForUniquePhone(client.getPhone());
        }

        client.setId(id);

        return clientRepository.save(client);
    }

    @Override
    public void deleteById(Long id) {
        //check if there is a client with such id
        getById(id);

        clientRepository.deleteById(id);
    }

    @Override
    public Iterable<Client> getAll() {
        return clientRepository.findAll();
    }

    @Override
    public void deleteAll() {
        clientRepository.deleteAll();
    }

    private void checkForUniqueEmail(String email) {
        if(clientRepository.findByEmail(email).isPresent())
            throw new ValueNotUniqueException("email", email);
    }

    private void checkForUniquePhone(String phone) {
        if(clientRepository.findByPhone(phone).isPresent())
            throw new ValueNotUniqueException("phone", phone);
    }
}
