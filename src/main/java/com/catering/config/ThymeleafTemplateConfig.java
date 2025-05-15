package com.catering.config;

import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

/**
 * Thymeleaf Template Configuration class for setting up email templates.
 * This configuration is used to initialize the Thymeleaf template engine 
 * and define the resolver for loading email templates.
 */
@Configuration
public class ThymeleafTemplateConfig {

	/**
	 * Creates and configures the SpringTemplateEngine that integrates Thymeleaf with Spring.
	 * It adds a custom template resolver for loading email templates from the classpath.
	 *
	 * @return Configured SpringTemplateEngine instance.
	 */
	@Bean
	SpringTemplateEngine springTemplateEngine() {
		SpringTemplateEngine springTemplateEngine = new SpringTemplateEngine();
		springTemplateEngine.addTemplateResolver(emailTemplateResolver());
		return springTemplateEngine;
	}

	/**
	 * Creates and configures the ClassLoaderTemplateResolver to resolve email templates.
	 * This resolver will look for templates in the /templates/ directory with an .html suffix.
	 *
	 * @return Configured ClassLoaderTemplateResolver instance for email templates.
	 */
	public ClassLoaderTemplateResolver emailTemplateResolver() {
		ClassLoaderTemplateResolver emailTemplateResolver = new ClassLoaderTemplateResolver();
		emailTemplateResolver.setPrefix("/templates/");
		emailTemplateResolver.setSuffix(".html");
		emailTemplateResolver.setTemplateMode(TemplateMode.HTML);
		emailTemplateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
		emailTemplateResolver.setCacheable(false);
		return emailTemplateResolver;
	}

}