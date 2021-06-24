package com.evobox.reportexporter.controller;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.evobox.reportexporter.utils.BahtText;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/1.0/report", produces = "application/pdf")
@Slf4j
public class ReportExporterController {
	
	private Map<String, String > report;
	
	@GetMapping(value = "/sale")
	public ResponseEntity<byte[]> getReport(@RequestParam Map<String, Object> param){
		ResponseEntity<byte[]> response = null;
		try {
			prepare();
			String amountTotal = param.get("prm_amount_total").toString();
			param.put("logo", ImageIO.read(getClass().getResource("/LOGO OKONTEK-01.png")));
			param.put("prm_amount_total", BahtText.getBahtTextByString(amountTotal));
			param.put("prm_id", Integer.parseInt(param.get("prm_id").toString()));
			
			String reportName = "";
			String filename = "";
			if("internal".equals(param.get("type").toString())) {
				filename = "sale_internal_"+param.get("cust_name").toString()+"_"+param.get("project_name").toString();
				if("True".equals(param.get("have_discount").toString())) {
					reportName = report.get("internal_discount");
				}else {
					reportName = report.get("internal");
				}
			}else {
				filename = "Quotation_"+param.get("quo_number").toString()+"_"+param.get("cust_name").toString()+"_"+param.get("project_name").toString();
				if("True".equals(param.get("have_discount").toString())) {
					reportName = report.get("external_discount");
				}else {
					reportName = report.get("external");
				}
			}
			
	        byte[] contents = new com.evobox.reportexporter.utils.ReportGenerator().getReportPDF(reportName, param);
	
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_PDF);
	        filename = filename+".pdf";
	        headers.add("content-disposition", "inline; filename=" + URLEncoder.encode(filename, "UTF-8"));
	        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
	        response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
		} catch (Exception e) {
            log.error(e.getMessage(), e);
        }
		return response;
	}
	
	private void prepare() {
		report = new HashMap<>();
		report.put("internal_discount", "sale_internal_discount");
    	report.put("internal", "sale_internal");
    	report.put("external_discount", "sale_external_discount");
    	report.put("external", "sale_external");
	}
	
}
