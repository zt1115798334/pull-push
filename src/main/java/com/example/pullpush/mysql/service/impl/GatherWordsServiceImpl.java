package com.example.pullpush.mysql.service.impl;

import com.example.pullpush.base.service.PageUtils;
import com.example.pullpush.dto.GatherWordDto;
import com.example.pullpush.mysql.repo.GatherWordRepository;
import com.example.pullpush.mysql.service.GatherWordsService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/10 11:18
 * description:
 */
@AllArgsConstructor
@Service
public class GatherWordsServiceImpl implements GatherWordsService {

    private final GatherWordRepository gatherWordRepository;

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public Page<GatherWordDto> findPageByEntity(int pageNumber, int pageSize) {
        return gatherWordRepository.findAllGatherWords(PageUtils.buildPageRequest(pageNumber, pageSize));
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public Page<GatherWordDto> findPageByEntityStatus(Long status, int pageNumber, int pageSize) {
        return gatherWordRepository.findAllGatherWordsStatus(status, PageUtils.buildPageRequest(pageNumber, pageSize));
    }
}
