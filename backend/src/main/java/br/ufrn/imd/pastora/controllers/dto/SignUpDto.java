package br.ufrn.imd.pastora.controllers.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class SignUpDto {
    @NotBlank
    String name;

    @NotBlank
    @Email
    String email;

    @NotBlank
    @Size(min = 6)
    String password;
}
