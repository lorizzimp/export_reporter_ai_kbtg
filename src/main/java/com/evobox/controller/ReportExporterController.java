package com.evobox.controller;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/1.0/report/", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReportExporterController {
	
	@GetMapping(value = "/exporter")
	public ResponseEntity<byte[]> getReport(@RequestParam Map<String, String> param){
		
		ResponseEntity<byte[]> response = null;
		return response;
	}
	
}
