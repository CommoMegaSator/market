package com.task.repository;

import com.task.entity.Document;
import com.task.enumeration.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    @Query("SELECT d FROM Document d WHERE d.createdAt <= :date ORDER BY d.createdAt DESC")
    Page<Document> findByCreatedAtBefore(@Param("date") LocalDateTime date, Pageable pageable);

    @Query("SELECT d FROM Document d JOIN d.attributes attr WHERE d.status = :status AND attr.attributeKey = :key AND attr.attributeValue = :value")
    Page<Document> findByStatusAndAttribute(@Param("status") Status status, @Param("key") String key, @Param("value") String value, Pageable pageable);
}
