package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.example.service.PdfService;

import java.io.*;

//post Mapping:localhost:9191/pdf/createpdf
//for getMapping: localhost:9191/pdf/getpdf/name of the pdf
@Controller
@RequestMapping("/pdf")
public class PdfController {

	private static final String PDF_FILE_PATH = "C:\\Demo\\Generate.pdf";

	@Autowired
	private PdfService pdfService;

	@PostMapping("/createpdf")
	public ResponseEntity<String> createAndSavePdf(@RequestBody PdfRequest pdfRequest) {
		String title = pdfRequest.getTitle();
		String content = pdfRequest.getContent();

		ByteArrayOutputStream pdfStream = pdfService.createPdf(title, content);

		if (pdfStream != null) {
			byte[] pdfBytes = pdfStream.toByteArray();

			// Save PDF to a file
			String filePath = savePdfToFile(title, pdfBytes);

			if (filePath != null) {
				return ResponseEntity.ok(filePath);
			}
		}

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create and save PDF.");
	}

	private String savePdfToFile(String title, byte[] pdfBytes) {
		String filePath = PDF_FILE_PATH + title + ".pdf";
		try (FileOutputStream fos = new FileOutputStream(filePath)) {
			fos.write(pdfBytes);
			return filePath; // Return the file path if saved successfully
		} catch (IOException e) {
			e.printStackTrace();
			return null; // Return null if an error occurs during file save
		}
	}

	@GetMapping("/getpdf/{title}")
	public ResponseEntity<byte[]> getPdfByTitle(@PathVariable("title") String title) {
		String filePath = PDF_FILE_PATH + title + ".pdf";
		File pdfFile = new File(filePath);

		try {
			if (pdfFile.exists()) {
				byte[] pdfBytes;
				try (InputStream inputStream = new FileInputStream(pdfFile)) {
					pdfBytes = inputStream.readAllBytes();
				}

				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_PDF);
				headers.setContentDispositionFormData("attachment", title + ".pdf");

				return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
