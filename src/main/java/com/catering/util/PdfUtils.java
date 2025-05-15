package com.catering.util;

import java.io.ByteArrayOutputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.lowagie.text.pdf.BaseFont;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class for converting HTML content to PDF using iTextRenderer.
 * <p>
 * This class provides a method, {@code convertHtmlToPdf}, that takes HTML content as input
 * and generates a corresponding PDF byte array. Custom fonts can be registered, and a ResourceLoader
 * is used to load the font file.
 * </p>
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * byte[] pdfBytes = PdfUtils.convertHtmlToPdf(htmlContent, resourceLoader);
 * }
 * </pre>
 * </p>
 *
 * @author Krushali Talaviya
 * @since 2023-12-22
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PdfUtils {

	/**
	 * Converts HTML content to a PDF byte array.
	 *
	 * @param htmlContent    The HTML content to be converted to PDF.
	 * @param resourceLoader The resource loader used to load custom fonts.
	 * @return A byte array representing the generated PDF.
	 */
	public static byte[] convertHtmlToPdf(String htmlContent, ResourceLoader resourceLoader) {
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
			ITextRenderer renderer = new ITextRenderer();
			// Register custom font
			ITextFontResolver fontResolver = renderer.getFontResolver();

			// Load font file using ResourceLoader
			Resource fontResource = resourceLoader.getResource("classpath:fonts/ArialUnicodeMS/arial-unicode-ms.ttf");
			fontResolver.addFont(fontResource.getFile().getAbsolutePath(), BaseFont.IDENTITY_H, true);
			htmlContent = "<!DOCTYPE html>"
					+ "	<html>"
					+ "<head>"
					+ "<title>PDF Document</title>"
					+ "<style>\n" 
					+ "    body {\n"
					+ "        font-family: 'Arial Unicode MS', sans-serif;\n"
					+ "    }\n" 
					+ "table {\n"
					+ "        border-collapse: collapse;\n"
					+ "        width: 100%;\n"
					+ "    }\n"
					+ "    th, td {\n"
					+ "        border: 1px solid black;\n" 
					+ "    } \n" 
					+ "</style>"
					+ "</head>"
					+ "<body>"
					+ htmlContent
					+ "</body>"
					+ "</html>";
			final Document document = Jsoup.parse(htmlContent, Parser.htmlParser());
			document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
			renderer.setDocumentFromString(document.html());
			renderer.layout();
			renderer.createPDF(outputStream, false);
			renderer.finishPDF();
			return outputStream.toByteArray();
		} catch (Exception e) {
			return new byte[0];
		}
	}

}