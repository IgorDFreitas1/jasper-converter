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

import static com.conversor.controller.LogController.sendLog;
import com.conversor.service.JasperConversorService;

@RestController
@RequestMapping("/api/converter")
public class ConversaoController {

    private final JasperConversorService service;

    public ConversaoController(JasperConversorService service) {
        this.service = service;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> converter(@RequestParam("file") MultipartFile file) {

        // 1️⃣ Validação inicial
        if (file == null || file.isEmpty()) {
            sendLog("❌ Nenhum arquivo enviado");
            return ResponseEntity.badRequest().body(null);
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".jasper")) {
            sendLog("❌ Arquivo inválido: " + originalFilename);
            return ResponseEntity.badRequest().body(null);
        }

        // 2️⃣ Nome do arquivo de saída
        String outputFilename =
                originalFilename.substring(0, originalFilename.lastIndexOf(".")) + ".jrxml";

        File tempJasper = null;
        File tempJrxml = null;

        try {
            sendLog("📄 Arquivo recebido: " + originalFilename);

            // 3️⃣ Criar arquivos temporários
            tempJasper = File.createTempFile("upload-", ".jasper");
            tempJrxml = File.createTempFile("result-", ".jrxml");

            file.transferTo(tempJasper);

            sendLog("⚙️ Iniciando conversão...");

            // 4️⃣ Converter
            service.converter(tempJasper, tempJrxml);

            sendLog("✅ Conversão finalizada");

            // 5️⃣ Retornar arquivo
            byte[] jrxmlBytes = Files.readAllBytes(tempJrxml.toPath());

            return ResponseEntity.ok()
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + outputFilename + "\""
                    )
                    .contentType(MediaType.APPLICATION_XML)
                    .body(jrxmlBytes);

        } catch (Exception e) {
            sendLog("❌ Erro durante a conversão: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);

        } finally {
            // 6️⃣ Limpeza
            if (tempJasper != null && tempJasper.exists()) tempJasper.delete();
            if (tempJrxml != null && tempJrxml.exists()) tempJrxml.delete();
        }
    }
}
