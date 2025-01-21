package br.ufrn.imd.pastora.usecases.auth;

import br.ufrn.imd.pastora.config.security.JwtTokenProvider;
import br.ufrn.imd.pastora.controllers.dto.SignInDto;
import br.ufrn.imd.pastora.exceptions.InvalidCredentialsException;
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
    private final JwtTokenProvider jwtTokenProvider;

    public String execute(SignInDto signInDto) throws Exception{
        UserModel user = userRepository.findByEmail(signInDto.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(signInDto.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        return jwtTokenProvider.generateToken(user.getEmail(), user.getId());
    }
}