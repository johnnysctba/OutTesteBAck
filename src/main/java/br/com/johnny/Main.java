package br.com.johnny;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal da aplicação API.
 *
 * Esta aplicação fornece uma API RESTful para consulta de dados
 * relacionados aos prêmios Golden Raspberry Awards, especificamente
 * para a categoria "Pior Filme".
 *
 * @author Johnny
 * @version 1.0.0
 */
@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}

