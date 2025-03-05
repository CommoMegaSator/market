package com.market.dto;

import com.market.enumeration.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class UserDto implements Serializable {
    @Schema(description = "User ID")
    private Long id;

    @Size(min = 4, max = 30)
    @Schema(description = "User's username")
    private String username;

    @NotNull(message = "Field 'email' can not be NULL")
    @Email
    @Size(min = 4, max = 25)
    @Schema(description = "User's email")
    private String email;

    @NotNull(message = "Field 'password' can not be NULL")
    @Size(min = 8, max = 25)
    @Schema(description = "User's password")
    private String password;

    @Schema(description = "User roles")
    private Set<Role> roles;
}