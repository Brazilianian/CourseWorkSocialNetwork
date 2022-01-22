package com.example.springWebContent.domain;

import com.example.springWebContent.domain.enums.FileType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class MessageFile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    private String content;

    private String fileName;

    @Enumerated(EnumType.STRING)
    private FileType fileType;

    public MessageFile(String content, String fileName, FileType fileType) {
        this.content = content;
        this.fileName = fileName;
        this.fileType = fileType;
    }
}

