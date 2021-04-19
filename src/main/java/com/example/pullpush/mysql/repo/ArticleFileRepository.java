package com.example.pullpush.mysql.repo;

import com.example.pullpush.mysql.entity.ArticleFile;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ArticleFileRepository extends CrudRepository<ArticleFile, Long>,
        JpaSpecificationExecutor<ArticleFile> {

    @Modifying
    @Transactional
    @Query(value = "truncate table rencai_zhengce", nativeQuery = true)
    void truncateTable();

}
