package com.example.pullpush.mysql.entity;

import com.example.pullpush.base.entity.IdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Transient;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author fan
 *
 */
@Data
@Entity
@Table(name = "rencai_zhengce")
public class ArticleFile{
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 词类别
     */
    @Column(name = "JsonData")
    private String fileContext;

}
