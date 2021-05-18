package com.example.pullpush.mysql.repo;

import com.example.pullpush.mysql.entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface AuthorRepository extends CrudRepository<Author, Long>,
        JpaSpecificationExecutor<Author> {
    Page<Author> findAll(Pageable pageable);
}
