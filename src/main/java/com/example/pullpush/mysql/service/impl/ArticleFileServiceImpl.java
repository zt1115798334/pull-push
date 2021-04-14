package com.example.pullpush.mysql.service.impl;

import com.example.pullpush.base.service.PageUtils;
import com.example.pullpush.mysql.entity.ArticleFile;
import com.example.pullpush.mysql.repo.ArticleFileRepository;
import com.example.pullpush.mysql.service.ArticleFileService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ArticleFileServiceImpl implements ArticleFileService {

    private final ArticleFileRepository articleFileRepository;

    @Override
    public Page<ArticleFile> findPage(int pageNumber, int pageSize) {
        return articleFileRepository.findAll((root, query, builder) -> null,PageUtils.buildPageRequest(pageNumber, pageSize));
    }

    @Override
    public long findCount() {
        return articleFileRepository.count();
    }

    @Override
    public void truncateTable() {
        articleFileRepository.truncateTable();
    }
}
