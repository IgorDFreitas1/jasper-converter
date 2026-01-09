package com.conversor;

import java.awt.Desktop;
import java.net.URI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JasperConverterApplication {

    public static void main(String[] args) {
        SpringApplication.run(JasperConverterApplication.class, args);

        try {
            Thread.sleep(3000); // aguarda o servidor subir
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI("http://localhost:8080"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
