package com.evobox.reportexporter.controller;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/1.0/report", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class ReportExporterController {
	
	@GetMapping(value = "/sale")
	public ResponseEntity<byte[]> getReport(@RequestParam Map<String, Object> param){
		ResponseEntity<byte[]> response = null;
		try {
	        byte[] contents = new com.evobox.reportexporter.utils.ReportGenerator().getReportPDF("sale_internal", param);
	
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_PDF);
	        String filename = "sale_internal";
	        headers.setContentDispositionFormData(filename, filename);
	        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
	        response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
		} catch (Exception e) {
            log.error(e.getMessage(), e);
        }
		return response;
	}
	
}
