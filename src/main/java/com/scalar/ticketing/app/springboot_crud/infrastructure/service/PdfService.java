package com.scalar.ticketing.app.springboot_crud.infrastructure.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.DeviceGray;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import com.scalar.ticketing.app.springboot_crud.domain.model.Ticket;
import com.scalar.ticketing.app.springboot_crud.domain.model.Event;

@Service
public class PdfService {

    @Value("${app.pdf.output-directory:./pdf-tickets}")
    private String outputDirectory;

    private static final int QR_CODE_SIZE = 200;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public String generateTicketPdf(Ticket ticket) throws WriterException, IOException {
        Event event = ticket.getEvent();
        String ticketId = ticket.getTicketId();
        
        Path outputPath = Paths.get(outputDirectory, ticketId + ".pdf");
        outputPath.getParent().toFile().mkdirs();

        PdfWriter writer = new PdfWriter(outputPath.toString());
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc, PageSize.A4);

        DeviceRgb primaryColor = new DeviceRgb(30, 60, 114);
        DeviceRgb accentColor = new DeviceRgb(42, 157, 143);
        DeviceGray lightGray = new DeviceGray(0.95f);

        Paragraph title = new Paragraph("TICKET DE ENTRADA")
                .setFontSize(24)
                .setFontColor(primaryColor)
                .setTextAlignment(TextAlignment.CENTER)
                .setBold();
        document.add(title);

        document.add(new Paragraph("\n"));

        Table infoTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                .useAllAvailableWidth()
                .setBackgroundColor(lightGray);

        addTableCell(infoTable, "ID del Ticket:", ticketId);
        addTableCell(infoTable, "Evento:", event.getName());
        addTableCell(infoTable, "Fecha:", event.getEventDate() != null ? event.getEventDate().format(DATE_FORMAT) : "N/A");
        addTableCell(infoTable, "Estado:", ticket.getStatus().name());
        addTableCell(infoTable, "Posición en Cola:", String.valueOf(ticket.getQueuePosition()));
        addTableCell(infoTable, "Precio:", String.format("$%.2f", event.getPrice()));
        addTableCell(infoTable, "Fecha de Compra:", LocalDateTime.now().format(DATE_FORMAT));

        document.add(infoTable);

        document.add(new Paragraph("\n"));

        Paragraph qrTitle = new Paragraph("CÓDIGO QR")
                .setFontSize(16)
                .setFontColor(primaryColor)
                .setTextAlignment(TextAlignment.CENTER)
                .setBold();
        document.add(qrTitle);

        document.add(new Paragraph("\n"));

        byte[] qrCodeImage = generateQrCode(ticketId);
        Image qrImage = new Image(ImageDataFactory.create(qrCodeImage))
                .setWidth(QR_CODE_SIZE)
                .setHeight(QR_CODE_SIZE)
                .setHorizontalAlignment(com.itextpdf.layout.properties.HorizontalAlignment.CENTER);
        document.add(qrImage);

        document.add(new Paragraph("\n"));

        Paragraph instructions = new Paragraph("Presenta este código QR en la entrada del evento.")
                .setFontSize(12)
                .setFontColor(new DeviceGray(0.4f))
                .setTextAlignment(TextAlignment.CENTER);
        document.add(instructions);

        Paragraph footer = new Paragraph("Generado el " + LocalDateTime.now().format(DATE_FORMAT))
                .setFontSize(10)
                .setFontColor(new DeviceGray(0.6f))
                .setTextAlignment(TextAlignment.CENTER);
        document.add(footer);

        document.close();

        return outputPath.toString();
    }

    public byte[] generateQrCode(String content) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, QR_CODE_SIZE, QR_CODE_SIZE);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        return outputStream.toByteArray();
    }

    private void addTableCell(Table table, String label, String value) {
        Cell labelCell = new Cell()
                .add(new Paragraph(label).setBold().setFontSize(11))
                .setPadding(5)
                .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER);
        Cell valueCell = new Cell()
                .add(new Paragraph(value).setFontSize(11))
                .setPadding(5)
                .setBorder(com.itextpdf.layout.borders.Border.NO_BORDER);
        table.addCell(labelCell);
        table.addCell(valueCell);
    }
}