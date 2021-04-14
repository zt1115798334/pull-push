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
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "t_test")
public class ArticleFile extends IdEntity {
	/**
     * 采集词id
     */
    private Long id;

    /**
     * 词库id
     */
    private String fileName;

    /**
     * 词类别
     */
    private String fileContext;

}
