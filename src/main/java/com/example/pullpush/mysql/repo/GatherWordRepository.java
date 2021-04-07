package com.example.pullpush.mysql.repo;

import com.example.pullpush.dto.GatherWordDto;
import com.example.pullpush.mysql.entity.GatherWord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/10 11:17
 * description:
 */
public interface GatherWordRepository extends CrudRepository<GatherWord, Long>,
        JpaSpecificationExecutor<GatherWord> {

    @Query(value = "select new com.example.pullpush.dto.GatherWordDto(name) from GatherWord",
            countQuery = "select count(id) from GatherWord")
    Page<GatherWordDto> findAllGatherWords(Pageable pageable);
    
    @Query(value = "select new com.example.pullpush.dto.GatherWordDto(name) from GatherWord where status=:status",
            countQuery = "select count(id) from GatherWord where status=:status")
    Page<GatherWordDto> findAllGatherWordsStatus(@Param(value="status")Long status, Pageable pageable);

}
