package com.hotelapi.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;

public class PdfUtility {

    public static void addBusinessHeader(Document doc, String logoPath) throws DocumentException {
        try {
            ClassPathResource resource = new ClassPathResource(logoPath);
            System.out.println("Logo exists: " + resource.exists());  // Debug log

            if (!resource.exists()) {
                addFallbackTitle(doc);
                return;
            }

            try (InputStream is = resource.getInputStream()) {
                byte[] imageBytes = is.readAllBytes();
                Image logo = Image.getInstance(imageBytes);
                logo.scaleToFit(80, 80);
                logo.setAlignment(Image.ALIGN_CENTER);
                doc.add(logo);
            }
        } catch (Exception e) {
            System.err.println("Error loading logo: " + e.getMessage());
            addFallbackTitle(doc);
        }

        Paragraph address = new Paragraph(
                "123, Main Road, City, State - 123456\n" +
                        "Phone: +91-9876543210\n" +
                        "Email: contact@hotelroyal.com",
                FontFactory.getFont(FontFactory.HELVETICA, 10));
        address.setAlignment(Element.ALIGN_CENTER);
        doc.add(address);

        doc.add(Chunk.NEWLINE);
    }

    private static void addFallbackTitle(Document doc) throws DocumentException {
        Paragraph fallback = new Paragraph("HOTEL ROYAL PALACE",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22));
        fallback.setAlignment(Element.ALIGN_CENTER);
        doc.add(fallback);
    }

    public static PdfPCell getTableHeaderCell(String text) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(BaseColor.DARK_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        return cell;
    }

    public static PdfPCell getTableCell(String text) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(5);
        return cell;
    }

    public static void addThankYouFooter(Document doc) throws DocumentException {
        doc.add(Chunk.NEWLINE);
        Paragraph thankYou = new Paragraph(
                "Thank you for choosing Hotel Royal Palace!",
                FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10));
        thankYou.setAlignment(Element.ALIGN_CENTER);
        doc.add(thankYou);
    }
}
