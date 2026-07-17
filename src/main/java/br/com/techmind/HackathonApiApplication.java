package br.com.techmind;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing  // ← ADICIONE ESTA LINHA
public class HackathonApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(HackathonApiApplication.class, args);
    }
}