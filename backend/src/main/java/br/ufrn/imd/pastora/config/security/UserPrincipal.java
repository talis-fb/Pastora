package br.ufrn.imd.pastora.config.security;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserPrincipal {
    private String id;
    private String email;
}
