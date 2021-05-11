package com.evobox.reportexporter.utils;

import static com.evobox.reportexporter.config.Const.FILE_JRXML;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

@Data
@Slf4j
public class ReportGenerator {
    private JRPdfExporter exporter = new JRPdfExporter();

    private JRXlsxExporter exporterXlsx = new JRXlsxExporter();

    private byte[] generateByteReport() throws JRException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
        exporter.setConfiguration(new SimplePdfExporterConfiguration());
        exporter.exportReport();
        return outputStream.toByteArray();
    }
    private byte[] generateByteReportXlsx() throws JRException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        exporterXlsx.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
        SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
        configuration.setOnePagePerSheet(false);
        configuration.setRemoveEmptySpaceBetweenRows(true);
        configuration.setRemoveEmptySpaceBetweenColumns(true);
        configuration.setDetectCellType(true);
        configuration.setWhitePageBackground(false);

        exporterXlsx.setConfiguration(configuration);
        exporterXlsx.exportReport();
        return outputStream.toByteArray();
    }

    private byte[] getByteReport(JasperPrint jasperPrint) throws JRException {
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        return generateByteReport();
    }

    private byte[] getByteReportXlsx(JasperPrint jasperPrint) throws JRException {
        exporterXlsx.setExporterInput(new SimpleExporterInput(jasperPrint));
        return generateByteReportXlsx();
    }

    public byte[] generateJasperReportPDF(String jasperReportName, Map<String, Object> parameters) {

        try (InputStream in = getClass().getResourceAsStream(String.format(FILE_JRXML, jasperReportName))){

            JasperReport jsRpt = JasperCompileManager
                    .compileReport(in);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jsRpt, parameters, getConnection());

            return getByteReport(jasperPrint);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error in generate Report..." + e);
        }
        return new byte[0];
    }

    public byte[] getReportPDF(String jasperReportName, Map<String, Object> parameters) {

        try (InputStream in = getClass().getResourceAsStream(String.format(FILE_JRXML, jasperReportName))) {
        	log.warn("found report file.");
            JasperReport jsRpt = JasperCompileManager.compileReport(in);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jsRpt, parameters,
                    getConnection());
            return getByteReport(jasperPrint);
        } catch (JRException | IOException | SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }
        return new byte[0];
    }

    public byte[] getReportXlsx(String jasperReportName, Map<String, Object> parameters) {

        try (InputStream in = getClass().getResourceAsStream(String.format(FILE_JRXML, jasperReportName))) {
            JasperReport jsRpt = JasperCompileManager.compileReport(in);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jsRpt, parameters,
                    getConnection());

            exporterXlsx.setExporterInput(new SimpleExporterInput(jasperPrint));

            return getByteReportXlsx(jasperPrint);
        } catch (JRException | IOException | SQLException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    private Connection getConnection() throws SQLException {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUsername("odoo");
        dataSource.setPassword("odoo");
        dataSource.setUrl("jdbc:postgresql://localhost:5433/okontek");
        return dataSource.getConnection();
    }
}
