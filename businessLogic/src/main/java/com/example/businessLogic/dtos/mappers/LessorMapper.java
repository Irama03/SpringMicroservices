package com.example.businessLogic.dtos.mappers;

import com.example.businessLogic.dtos.LessorPostDto;
import com.example.businessLogic.models.Lessor;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LessorMapper {


    Lessor lessorPostDtoToLessor(LessorPostDto lessorPostDto);

}
