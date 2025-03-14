package com.task.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDto {
    private Long id;

    @NotBlank
    @Size(max = 255)
    private String title;

    private String description;

    private List<UserDto> owners;

    private String url;

    private int version;

    private String status;

    private LocalDateTime createdAt;
    
    private List<AttributeDto> attributes;
}

