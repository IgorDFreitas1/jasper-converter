package com.conversor.controller;

import java.io.File;
import java.nio.file.Files;

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
    public ResponseEntity<byte[]> converter(@RequestParam("file") MultipartFile file)
            throws Exception {

        // 1. Obter o nome original e preparar o nome de saída (.jrxml)
        String originalFilename = file.getOriginalFilename();
        String outputFilename = "arquivo_convertido.jrxml"; // Nome padrão caso o original falhe

        if (originalFilename != null && !originalFilename.isEmpty()) {
            // Remove a extensão original (ex: .jasper) e adiciona .jrxml
            int lastDotIndex = originalFilename.lastIndexOf(".");
            if (lastDotIndex != -1) {
                outputFilename = originalFilename.substring(0, lastDotIndex) + ".jrxml";
            } else {
                outputFilename = originalFilename + ".jrxml";
            }
        }

        // 2. Criar arquivo .jasper temporário para processamento
        File tempJasper = File.createTempFile("upload-", ".jasper");
        file.transferTo(tempJasper);

        // 3. Criar arquivo .jrxml temporário para receber a conversão
        File tempJrxml = File.createTempFile("result-", ".jrxml");

        try {
            // 4. Executa a conversão através do service
            service.converter(tempJasper, tempJrxml);

            // 5. Lê os bytes do arquivo convertido
            byte[] jrxmlBytes = Files.readAllBytes(tempJrxml.toPath());

            // 6. Retorna o arquivo com o nome original (com extensão .jrxml)
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + outputFilename + "\"")
                    .contentType(MediaType.APPLICATION_XML)
                    .body(jrxmlBytes);

        } finally {
            // 7. Garante a limpeza dos arquivos temporários no servidor
            if (tempJasper.exists()) tempJasper.delete();
            if (tempJrxml.exists()) tempJrxml.delete();
        }
    }
}