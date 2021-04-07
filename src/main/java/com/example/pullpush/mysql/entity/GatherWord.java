package com.example.pullpush.mysql.entity;

import com.example.pullpush.base.entity.IdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * @author fan
 *
 */
@SuppressWarnings("serial")
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "t_gather")
public class GatherWord  extends IdEntity {
	/**
     * 采集词id
     */
    private Long id;

    /**
     * 词库id
     */
    private String wordId;

    /**
     * 词类别
     */
    private Long type;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 修改时间
     */
    private LocalDateTime modifyTime;
    
    /**
     * 同步时间
     */
    private LocalDateTime syncTime;
    
    /**
     * 状态
     */
    private Long status;
    
    /**
     * 更新节点标志位
     */
    private Long mark;
    
    /**
     * 词
     */
    private String name;
    
    /**
     * 用户id
     */
    private String userIds;
    
    /**
     * 用户数量
     */
    private Long userCount;
}
