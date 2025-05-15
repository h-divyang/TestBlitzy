package com.catering.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Utility class for modifying and converting HTML content for CKEditor.
 */
public class CKEditorContentUtils {

	/**
	 * Modifies the given HTML content by adjusting `margin-left` styles,
	 * replacing `h1-h7` tags with styled `span` tags, and converting `strong` tags to styled `span` tags.
	 *
	 * @param htmlContent The input HTML content.
	 * @return The modified HTML content.
	 */
	public static String modifyHtmlContent(String htmlContent) {
		Document document = Jsoup.parse(htmlContent);

		// Handle elements with margin-left styles
		Elements elements = document.select("[style*=margin-left]");

		for (Element element : elements) {
			String style = element.attr("style");

			String marginLeftValue = style.replaceAll(".*margin-left\\s*:\\s*([^;]+);.*", "$1").trim();

			style = style.replaceAll("margin-left\\s*:\\s*[^;]+;?", "").trim().replaceAll(";+\\s*$", "");

			if (style.isEmpty()) {
				element.removeAttr("style");
			} else {
				element.attr("style", style);
			}

			int marginPixels = 0;
			if (marginLeftValue.endsWith("px")) {
				marginPixels = Integer.parseInt(marginLeftValue.replace("px", "").trim());
			}

			int numberOfNbsp = marginPixels / 4;

			StringBuilder nbspBuilder = new StringBuilder();
			for (int i = 0; i < numberOfNbsp; i++) {
				nbspBuilder.append("&nbsp;");
			}
			element.html(nbspBuilder.toString() + element.html());
		}

		// Replace heading tags (h1-h7) with span tags and apply font-size styles
		for (int i = 1; i <= 7; i++) {
			String tag = "h" + i;
			for (Element heading : document.select(tag)) {
				heading.tagName("span");
				int fontSize = 28 - (i - 1) * 4;

				String style = "font-size:" + fontSize + "px;";
				heading.attr("style", style);
			}
		}

		// Replace <strong> tags with <span> tags having bold style
		Elements strongTags = document.select("strong");
		for (Element strong : strongTags) {
			Element span = new Element("span").attr("style", "font-weight: bold;");
			span.html(strong.html());
			strong.replaceWith(span);
		}

		return document.body().html();
	}

	/**
	 * Converts modified HTML content back into a CKEditor-compatible format.
	 * Restores headings (h1-h5) from styled `span` tags and adjusts `margin-left` styles from `&nbsp;` occurrences.
	 *
	 * @param htmlContent The input HTML content.
	 * @return The converted CKEditor-compatible HTML content.
	 */
	public static String convertToCkEditorForm(String htmlContent) {
		Document document = Jsoup.parse(htmlContent);

		// Convert styled spans back to heading tags
		Elements spans = document.select("span[style]");
		for (Element span : spans) {
			String style = span.attr("style");

			if (style.contains("font-size:28px;")) {
				span.tagName("h1");
			} else if (style.contains("font-size:24px;")) {
				span.tagName("h2");
			} else if (style.contains("font-size:20px;")) {
				span.tagName("h3");
			} else if (style.contains("font-size:16px;")) {
				span.tagName("h4");
			} else if (style.contains("font-size:12px;")) {
				span.tagName("h5");
			}
		}

		// Adjust margin-left styles based on leading &nbsp; occurrences
		Elements elements = document.select("body *");

		for (Element element : elements) {
			String html = element.html();

			int nbspCount = 0;
			while (html.startsWith("&nbsp;")) {
				nbspCount++;
				html = html.substring(6);
			}

			if (nbspCount > 0) {
				int marginLeftValue = nbspCount * 4;

				String existingStyle = element.attr("style");
				if (!existingStyle.isEmpty()) {
					existingStyle = existingStyle.trim() + "; ";
				}
				element.attr("style", existingStyle + "margin-left: " + marginLeftValue + "px;");

				element.html(html);
			}
		}
		return document.body().html();
	}

}
