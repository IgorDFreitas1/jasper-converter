package com.conversor.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.conversor.service.JasperConversorService;

@RestController
@RequestMapping("/api/converter")
public class ConversaoController {

    private final JasperConversorService service;

    public ConversaoController(JasperConversorService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Resource> converter(@RequestParam("file") MultipartFile file) {
        
        // Validação básica
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase().endsWith(".jasper")) {
            return ResponseEntity.badRequest().build(); // Ou retorne uma mensagem de erro customizada
        }

        try {
            // Conversão em Memória (Rápida e sem disco)
            byte[] jrxmlBytes = service.convertToJrxml(file.getInputStream());
            ByteArrayResource resource = new ByteArrayResource(jrxmlBytes);

            String outputFileName = fileName.replace(".jasper", ".jrxml");

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + outputFileName + "\"")
                    .contentType(MediaType.APPLICATION_XML)
                    .contentLength(resource.contentLength())
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace(); // Aparecerá nos logs do Railway
            return ResponseEntity.internalServerError().build();
        }
    }
}