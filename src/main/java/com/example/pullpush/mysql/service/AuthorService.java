package com.example.pullpush.mysql.service;

import com.example.pullpush.base.service.BaseService;
import com.example.pullpush.dto.GatherWordDto;
import com.example.pullpush.mysql.entity.Author;

public interface AuthorService  extends BaseService<Author, Long> {
    boolean isExistsAuthor(String author);
}
