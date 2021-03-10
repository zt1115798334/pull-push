package com.example.pullpush.base.service;

import org.springframework.data.domain.Page;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/9 11:40
 * description:
 */
public interface BaseService<T, ID> {

    default Page<T> findPageByEntity(int pageNumber, int pageSize) {
        return null;
    }
    
    default Page<T> findPageByEntityStatus(Long status, int pageNumber, int pageSize) {
        return null;
    }

}
