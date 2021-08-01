package br.com.treino.api_livros;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("br.com.treino.api_livros")
@EnableCaching
public class ApiLivrosApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiLivrosApplication.class, args);
	}

}
