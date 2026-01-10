package com.conversor.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/api/converter")
public class ConversaoController {

    // 1. Cria o Logger profissional
    private static final Logger logger = LoggerFactory.getLogger(ConversaoController.class);

    private final JasperConversorService service;

    public ConversaoController(JasperConversorService service) {
        this.service = service;
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Resource> converter(@RequestParam("file") MultipartFile file) {
        
        // Validações
        if (file == null || file.isEmpty()) {
            logger.warn("Tentativa de upload sem arquivo.");
            return ResponseEntity.badRequest().build();
        }

        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase().endsWith(".jasper")) {
            logger.warn("Arquivo inválido recebido: {}", fileName);
            return ResponseEntity.badRequest().build();
        }

        try {
            logger.info("Iniciando conversão para o arquivo: {}", fileName);

            byte[] jrxmlBytes = service.convertToJrxml(file.getInputStream());
            ByteArrayResource resource = new ByteArrayResource(jrxmlBytes);

            String outputFileName = fileName.replace(".jasper", ".jrxml");

            logger.info("Conversão bem-sucedida: {}", outputFileName);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + outputFileName + "\"")
                    .contentType(MediaType.APPLICATION_XML)
                    .contentLength(resource.contentLength())
                    .body(resource);

        } catch (Exception e) {
            // 2. Log de erro limpo, sem stack trace gigante no console
            logger.error("Erro durante a conversão do arquivo {}: {}", fileName, e.getMessage());
            // Emite apenas a mensagem do erro, não a pilha toda
            return ResponseEntity.internalServerError().build();
        }
    }
}