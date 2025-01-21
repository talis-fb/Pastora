package br.ufrn.imd.pastora.controllers;

import br.ufrn.imd.pastora.controllers.dto.SignInDto;
import br.ufrn.imd.pastora.controllers.dto.SignUpDto;
import br.ufrn.imd.pastora.usecases.auth.SignInUseCase;
import br.ufrn.imd.pastora.usecases.auth.SignUpUseCase;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final SignUpUseCase signUpUseCase;
    private final SignInUseCase signInUseCase;

    @SneakyThrows
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@Valid @RequestBody SignUpDto signUpDto) {
        return ResponseEntity.ok(signUpUseCase.execute(signUpDto));
    }

    @SneakyThrows
    @PostMapping("/signin")
    public ResponseEntity<String> signIn(@Valid @RequestBody SignInDto signInDto) {
        return ResponseEntity.ok(signInUseCase.execute(signInDto));
    }
}
