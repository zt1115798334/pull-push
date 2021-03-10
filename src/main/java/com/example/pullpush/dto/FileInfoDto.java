package com.example.pullpush.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class FileInfoDto {
    private final String filename;

    private final String content;
}
