package com.example.loggingservice.repositories;

import com.example.loggingservice.models.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<LogEntity, Long> {

    @Query(value = "SELECT * FROM logs WHERE service_tag = :tag", nativeQuery = true)
    Iterable<LogEntity> findByServiceTag(String tag);

    @Query(value = "SELECT * FROM logs WHERE log_type = :type", nativeQuery = true)
    Iterable<LogEntity> findByType(String type);
}
