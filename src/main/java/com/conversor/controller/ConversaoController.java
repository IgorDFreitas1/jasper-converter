package com.conversor.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.conversor.service.JasperConversorService;

@RestController
@RequestMapping("/api/converter") // <--- Atenção: Isso define o prefixo da URL
public class ConversaoController {

    private final JasperConversorService service;

    public ConversaoController(JasperConversorService service) {
        this.service = service;
    }

    // ✅ NOVO ENDPOINT DE SAÚDE (Adicione aqui)
    // A URL final será: /api/converter/health
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }

    // 👇 SEU ENDPOINT DE CONVERSÃO JÁ EXISTENTE
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Resource> converter(@RequestParam("file") MultipartFile file) {
        
        // 1. Validações básicas
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase().endsWith(".jasper")) {
            return ResponseEntity.badRequest().build();
        }

        try {
            // 2. Chama o serviço (versão em memória que ajustamos)
            byte[] jrxmlBytes = service.convertToJrxml(file.getInputStream());
            ByteArrayResource resource = new ByteArrayResource(jrxmlBytes);

            // 3. Define nome do arquivo de saída
            String outputFileName = fileName.replace(".jasper", ".jrxml");

            // 4. Retorna para download
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + outputFileName + "\"")
                    .contentType(MediaType.APPLICATION_XML)
                    .contentLength(resource.contentLength())
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace(); // Log no console do Railway
            return ResponseEntity.internalServerError().build();
        }
    }
}