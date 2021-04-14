package com.example.pullpush.mysql.service;

import com.example.pullpush.mysql.entity.ArticleFile;
import org.springframework.data.domain.Page;

public interface ArticleFileService {


    Page<ArticleFile> findPage(int pageNumber, int pageSize);

    long findCount();

    void truncateTable();
}
