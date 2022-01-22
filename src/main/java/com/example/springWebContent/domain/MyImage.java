package com.example.springWebContent.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class MyImage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int key;

    @Lob
    private String value;

    public MyImage(int key, String value) {
        this.key = key;
        this.value = value;
    }
}
