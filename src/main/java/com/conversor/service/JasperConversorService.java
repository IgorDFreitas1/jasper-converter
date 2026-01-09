package com.conversor.service;

import java.io.File;

import org.springframework.stereotype.Service;

import static com.conversor.controller.LogController.sendLog;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

@Service
public class JasperConversorService {

    public File converter(File jasperFile, File destinoJrxml) throws JRException {

        try {
            sendLog("📄 Arquivo recebido: " + jasperFile.getName());

            JasperReport report =
                    (JasperReport) JRLoader.loadObject(jasperFile);

            sendLog("⚙️ Convertendo arquivo...");

            JasperCompileManager.writeReportToXmlFile(
                    report,
                    destinoJrxml.getAbsolutePath()
            );

            sendLog("✅ Conversão finalizada com sucesso");

            return destinoJrxml;

        } catch (JRException e) {
            sendLog("❌ Erro durante a conversão: " + e.getMessage());
            throw e;
        }
    }
}
