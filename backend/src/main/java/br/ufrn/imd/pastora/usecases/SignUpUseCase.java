package br.ufrn.imd.pastora.usecases;

import br.ufrn.imd.pastora.controllers.dto.SignUpDto;
import br.ufrn.imd.pastora.exceptions.BusinessException;
import br.ufrn.imd.pastora.persistence.UserModel;
import br.ufrn.imd.pastora.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpUseCase {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String execute(SignUpDto signUpDto) throws BusinessException {
        if (userRepository.existsByEmail(signUpDto.getEmail())) {
            throw new BusinessException("Email already registered");
        }

        UserModel user = UserModel.builder()
                .email(signUpDto.getEmail())
                .name(signUpDto.getName())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .build();

        UserModel savedUser = userRepository.save(user);
        return savedUser.getId();
    }
}
