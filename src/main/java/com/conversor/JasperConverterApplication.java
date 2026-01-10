package com.conversor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JasperConverterApplication {

    public static void main(String[] args) {
        SpringApplication.run(JasperConverterApplication.class, args);
        // Não abre navegador no SaaS
    }
}
