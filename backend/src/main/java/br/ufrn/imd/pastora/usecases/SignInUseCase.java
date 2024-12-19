package br.ufrn.imd.pastora.usecases;

import br.ufrn.imd.pastora.controllers.dto.SignInDto;
import br.ufrn.imd.pastora.persistence.UserModel;
import br.ufrn.imd.pastora.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignInUseCase {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String execute(SignInDto signInDto) {
        UserModel user = userRepository.findByEmail(signInDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        if (!passwordEncoder.matches(signInDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        return user.getId(); // Isso vai retornar um JWT TOken no futuro
    }
}