package br.ufrn.imd.pastora.controllers.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Value;

@Value
public class SignInDto {
    @NotBlank
    @Email
    String email;

    @NotBlank
    String password;
}
