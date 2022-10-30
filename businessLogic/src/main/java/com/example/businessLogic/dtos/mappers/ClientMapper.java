package com.example.businessLogic.dtos.mappers;

import com.example.businessLogic.dtos.clients.ClientSlimGetDto;
import com.example.businessLogic.models.Client;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    ClientSlimGetDto clientToClientSlimGetDto(Client client);

}
