package com.task.dto;

import com.task.enumeration.Status;
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

    private int version;

    private List<UserDto> owners;

    private String url;

    private Status status;
    
    private List<AttributeDto> attributes;
}

