package com.fivesysdev.Fiveogram.models;

import lombok.Getter;

import javax.persistence.*;

@MappedSuperclass
@Getter
public class BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
}
