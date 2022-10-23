package com.example.businessLogic.services.interfaces;

import com.example.businessLogic.dtos.LessorPostDto;
import com.example.businessLogic.models.Lessor;

public interface LessorService {

    Lessor create(LessorPostDto lessor);

    Lessor getById(long id);

    Lessor updateById(long id, LessorPostDto lessor) ;

    void deleteById(long id) ;

    void deleteAll();

    Iterable<Lessor> getAll();

}
