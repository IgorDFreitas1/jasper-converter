package com.conversor.service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlWriter;

@Service
public class JasperConversorService {

    /**
     * Converte um InputStream (.jasper) para byte[] (.jrxml)
     */
    public byte[] convertToJrxml(InputStream inputStream) throws Exception {
        // 1. Carrega o objeto Jasper compilado da memória
        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(inputStream);

        // 2. Prepara um fluxo de saída na memória
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // 3. Escreve o XML (JRXML) no fluxo de saída
        JRXmlWriter.writeReport(jasperReport, outputStream, "UTF-8");

        return outputStream.toByteArray();
    }
}