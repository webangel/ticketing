package com.scalar.ticketing.app.springboot_crud.infrastructure.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.scalar.ticketing.app.springboot_crud.domain.model.Ticket;
import com.scalar.ticketing.app.springboot_crud.domain.model.Event;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${app.email.from:noreply@ticketing.com}")
    private String fromEmail;

    @Value("${app.email.enabled:false}")
    private boolean emailEnabled;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendTicketPdfEmail(Ticket ticket, String pdfPath) {
        if (!emailEnabled) {
            logger.info("Email deshabilitado. Saltando envío de email para ticket: {}", ticket.getTicketId());
            return;
        }

        try {
            Event event = ticket.getEvent();
            String userEmail = ticket.getUser().getEmail();
            String userName = ticket.getUser().getName();

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(fromEmail);
            helper.setTo(userEmail);
            helper.setSubject("Tu ticket para " + event.getName());

            String htmlContent = buildEmailContent(userName, event, ticket);
            helper.setText(htmlContent, true);

            File pdfFile = new File(pdfPath);
            if (pdfFile.exists()) {
                FileSystemResource resource = new FileSystemResource(pdfFile);
                helper.addAttachment("ticket-" + ticket.getTicketId() + ".pdf", resource);
            }

            mailSender.send(message);
            logger.info("Email enviado exitosamente a {} para ticket {}", userEmail, ticket.getTicketId());

        } catch (MessagingException e) {
            logger.error("Error al enviar email para ticket {}: {}", ticket.getTicketId(), e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al enviar email para ticket {}: {}", ticket.getTicketId(), e.getMessage());
        }
    }

    private String buildEmailContent(String userName, Event event, Ticket ticket) {
        return "<html>" +
                "<body style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>" +
                "<div style='background: linear-gradient(135deg, #1e3c72 0%, #2a9d8f 100%); padding: 20px; text-align: center;'>" +
                "<h1 style='color: white; margin: 0;'>TICKET CONFIRMADO</h1>" +
                "</div>" +
                "<div style='padding: 20px; background-color: #f9f9f9;'>" +
                "<p>Hola <strong>" + userName + "</strong>,</p>" +
                "<p>Tu ticket ha sido generado exitosamente. Adjunto encontrarás el PDF con tu código QR.</p>" +
                "<div style='background-color: white; padding: 15px; border-radius: 5px; margin: 15px 0;'>" +
                "<p><strong>Evento:</strong> " + event.getName() + "</p>" +
                "<p><strong>ID del Ticket:</strong> " + ticket.getTicketId() + "</p>" +
                "<p><strong>Posición en Cola:</strong> " + ticket.getQueuePosition() + "</p>" +
                "<p><strong>Precio:</strong> $" + String.format("%.2f", event.getPrice()) + "</p>" +
                "</div>" +
                "<p>Presenta el código QR del PDF en la entrada del evento.</p>" +
                "<p style='color: #666; font-size: 12px;'>Este es un correo automático, por favor no responder.</p>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}