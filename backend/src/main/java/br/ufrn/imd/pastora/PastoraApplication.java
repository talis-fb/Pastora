package br.ufrn.imd.pastora;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class PastoraApplication {

	public static void main(String[] args) {
		SpringApplication.run(PastoraApplication.class, args);
	}


	@Bean
	public ExecutorService executorService() {
		return Executors.newVirtualThreadPerTaskExecutor();
	}
}
