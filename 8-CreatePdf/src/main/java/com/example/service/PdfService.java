package com.example.service;

import java.io.ByteArrayOutputStream;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PdfService {

	private Logger logger = LoggerFactory.getLogger(PdfService.class);

	public ByteArrayOutputStream createPdf(String title, String content) {

		logger.info("Create PDF Started:");

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		Document document = new Document();

		try {
			PdfWriter.getInstance(document, out);

			document.open();

			Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
			Paragraph titlePara = new Paragraph(title, titleFont);
			titlePara.setAlignment(Element.ALIGN_CENTER);
			document.add(titlePara);

			Font paraFont = FontFactory.getFont(FontFactory.HELVETICA, 18);
			Paragraph contentPara = new Paragraph(content, paraFont);
			document.add(contentPara);

			document.close();

		} catch (DocumentException e) {
			logger.error("Error while creating PDF: " + e.getMessage());
		}

		return out; // Return the generated ByteArrayOutputStream containing the PDF data
	}
}
