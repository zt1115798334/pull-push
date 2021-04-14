package com.example.pullpush.mysql.repo;

import com.example.pullpush.mysql.entity.ArticleFile;
import com.example.pullpush.mysql.entity.GatherWord;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ArticleFileRepository extends CrudRepository<ArticleFile, Long>,
        JpaSpecificationExecutor<ArticleFile> {

    @Query(value = "truncate table t_test", nativeQuery = true)
    void truncateTable();

}
