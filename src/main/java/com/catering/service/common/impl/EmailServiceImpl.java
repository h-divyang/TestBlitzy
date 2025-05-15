package com.catering.service.common.impl;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.catering.bean.EmailDetails;
import com.catering.service.common.EmailService;

/**
 * Implementation of the EmailService interface for sending emails.
 *
 * This service provides functionalities to send plain text emails and
 * template-based emails using configuration settings and the
 * Spring JavaMailSender. It employs threading to send emails asynchronously
 * and validates email details before sending.
 */
@Service
public class EmailServiceImpl implements EmailService {

	Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

	/**
	 * Bean for sending emails via JavaMailSender.
	 */
	@Autowired
	private JavaMailSender javaMailSender;

	/**
	 * The username of the email sender, retrieved from the configuration property "spring.mail.username".
	 */
	@Value("${spring.mail.username}")
	private String sender;

	/**
	 * Template engine for processing email templates.
	 */
	@Autowired
	private SpringTemplateEngine templateEngine;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean sendMailTemplet(EmailDetails emailDetailsBean) {
		return sendMail(emailDetailsBean, true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean sendMailText(EmailDetails emailDetailsBean) {
		return sendMail(emailDetailsBean, false);
	}

	/**
	 * Sends an email using the provided email details and determines whether to use a template.
	 *
	 * This method constructs and sends an email based on the provided details. It supports plain text
	 * email as well as template-based email. The email is sent asynchronously in a separate thread to
	 * avoid blocking the caller's execution. It validates the email details before sending the email and
	 *
	 * @param emailDetailsBean The details of the email to be sent, including recipient, subject, content,
	 *						   template, and additional properties if applicable.
	 * @param isTemplet A flag to indicate if the email content should be derived from a template.
	 *					If true, the method processes the template using the provided properties.
	 *					If false, the method uses the raw message content.
	 * @return A boolean indicating the success or failure of the email sending process.
	 *		   Returns true if the email was successfully constructed and sent, false otherwise.
	 */
	private boolean sendMail(EmailDetails emailDetailsBean, boolean isTemplet) {
		try {
			if (isEmailDetailsValid(emailDetailsBean, true)) {
				MimeMessage mimeMessage = javaMailSender.createMimeMessage();
				MimeMessageHelper helper = isTemplet ? new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name()): new MimeMessageHelper(mimeMessage, true);
				helper.setFrom(sender);
				helper.setTo(emailDetailsBean.getTo());
				helper.setSubject(emailDetailsBean.getSubject());
				String body;
				if (isTemplet) {
					Context context = new Context();
					context.setVariables(emailDetailsBean.getProperties());
					body = templateEngine.process(emailDetailsBean.getTemplet(), context);
				} else {
					body = emailDetailsBean.getMessage();
				}
				helper.setText(body, isTemplet);
				new Thread(() -> javaMailSender.send(mimeMessage)).start();
				return true;
			}
			return false;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return false;
	}

	/**
	 * Validates the provided email details to ensure all necessary fields are present.
	 * The validation checks:
	 * - Whether the recipient's email address (`to`) is provided.
	 * - Whether the subject is provided.
	 * - Based on the `isTemplet` flag, whether the message or template is present.
	 *
	 * @param emailDetailsBean The details of the email to be validated, including the recipient,
	 *						   subject, message, template, and other properties.
	 * @param isTemplet	A flag indicating if the email should use a template. If true, the
	 *					validation ensures the template is present. Otherwise, it checks for
	 */
	private boolean isEmailDetailsValid(EmailDetails emailDetailsBean, boolean isTemplet) {
		return Objects.nonNull(emailDetailsBean.getTo()) && Objects.nonNull(emailDetailsBean.getSubject())
			&& (isTemplet ? Objects.nonNull(emailDetailsBean.getTemplet()) : Objects.nonNull(emailDetailsBean.getMessage()));
	}

}