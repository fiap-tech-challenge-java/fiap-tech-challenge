package com.fiap.techchallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class TechchallengeApplication {

	public static void main(String[] args) {

		// sobe o contexto
		SpringApplication.run(TechchallengeApplication.class, args);
		// imprime mensagem verde com foguete
		System.out.println("\u001B[32m\uD83D\uDE80 TechChallenge iniciado com sucesso! \u001B[0m");
	}

}
