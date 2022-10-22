package com.example.businessLogic.repositories;

import com.example.businessLogic.models.Lessor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessorRepository extends JpaRepository<Lessor, Long> {

    boolean existsLessorByEmail(String email);

}
