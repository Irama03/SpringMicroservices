package com.example.businessLogic.services.implementations;

import com.example.businessLogic.dtos.LessorPostDto;
import com.example.businessLogic.dtos.mappers.LessorMapper;
import com.example.businessLogic.exceptions.RecordNotFoundException;
import com.example.businessLogic.exceptions.ValueNotUniqueException;
import com.example.businessLogic.models.Lessor;
import com.example.businessLogic.repositories.LessorRepository;
import com.example.businessLogic.services.interfaces.LessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LessorServiceImpl implements LessorService {


    private final LessorMapper lessorMapper;

    private final LessorRepository lessorRepository;

    @Autowired
    public LessorServiceImpl(LessorMapper lessorMapper, LessorRepository lessorRepository) {
        this.lessorMapper = lessorMapper;
        this.lessorRepository = lessorRepository;
    }

    public Lessor create(LessorPostDto lessor) {
        emailShouldBeUnique(lessor.getEmail());
        return lessorRepository.save(lessorMapper.lessorPostDtoToLessor(lessor));
    }

    @Override
    public Lessor getById(long id) {
        return lessorRepository
                .findById(id)
                .orElseThrow(() -> new RecordNotFoundException(Lessor.class, "id", id));
    }

    public Lessor updateById(long id, LessorPostDto lessor) {
        emailShouldBeUnique(lessor.getEmail());
        return lessorRepository
                .findById(id)
                .map(l -> lessorRepository.save(new Lessor(id, lessor.getName(), lessor.getEmail(), lessor.getPhone())))
                .orElseThrow(() -> new RecordNotFoundException(Lessor.class, "id", id));
    }

    public void deleteById(long id) {
        lessorRepository
                .findById(id)
                .ifPresentOrElse(l -> lessorRepository.deleteById(id),
                        () -> {
                            throw new RecordNotFoundException(Lessor.class, "id", id);
                        });
    }

    public void deleteAll() {
        lessorRepository.deleteAll();
    }

    public Iterable<Lessor> getAll() {
        return lessorRepository.findAll();
    }

    private void emailShouldBeUnique(String email) {
        if (!emailIsUnique(email)) {
            throw new ValueNotUniqueException("email", email);
        }
    }

    private boolean emailIsUnique(String email) {
        return !lessorRepository.existsLessorByEmail(email);
    }


}
