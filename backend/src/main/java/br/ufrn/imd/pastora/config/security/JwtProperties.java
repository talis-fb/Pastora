package br.ufrn.imd.pastora.config.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import jakarta.annotation.PostConstruct;
import java.security.SecureRandom;
import java.util.Base64;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@RequiredArgsConstructor
@Getter
public class JwtProperties {
    private final Environment environment;
    private String secret;
    private long expirationMs = 86400000;
    private static final int KEY_LENGTH = 64;
    private static final String SECRET_FILE_PATH = "config/jwt.key";

    @PostConstruct
    public void init() {
        String envSecret = environment.getProperty("JWT_SECRET");
        if (envSecret != null && !envSecret.isEmpty()) {
            this.secret = envSecret;
            return;
        }

        try {
            Path secretPath = Paths.get(SECRET_FILE_PATH);
            if (Files.exists(secretPath)) {
                this.secret = Files.readString(secretPath).trim();
                return;
            }
        } catch (Exception e) {
            System.err.println("Erro ao ler arquivo de chave JWT: " + e.getMessage());
        }

        generateAndSaveNewSecret();
    }

    private void generateAndSaveNewSecret() {
        try {
            SecureRandom secureRandom = new SecureRandom();
            byte[] keyBytes = new byte[KEY_LENGTH];
            secureRandom.nextBytes(keyBytes);
            this.secret = Base64.getEncoder().encodeToString(keyBytes);

            Path secretPath = Paths.get(SECRET_FILE_PATH);
            Files.createDirectories(secretPath.getParent());
            Files.writeString(secretPath, this.secret);

            System.out.println("Nova chave JWT gerada e salva em: " + secretPath.toAbsolutePath());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar e salvar nova chave JWT", e);
        }
    }
}