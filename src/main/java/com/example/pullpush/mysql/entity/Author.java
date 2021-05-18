package com.example.pullpush.mysql.entity;

import com.example.pullpush.base.entity.IdEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "t_author")
public class Author  extends IdEntity {
    private String authorName;
}
