package br.ufrn.imd.pastora;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class PastoraApplication {

	public static void main(String[] args) {
		SpringApplication.run(PastoraApplication.class, args);
	}

}
