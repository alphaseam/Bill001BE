package com.hotelapi.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

public class PdfUtility {

    public static void addBusinessHeader(Document doc, String logoPath) throws DocumentException {
        try {
            Image logo = Image.getInstance(logoPath);
            logo.scaleToFit(80, 80);
            logo.setAlignment(Image.ALIGN_CENTER);
            doc.add(logo);
        } catch (Exception e) {
            Paragraph fallback = new Paragraph("HOTEL ROYAL PALACE", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22));
            fallback.setAlignment(Element.ALIGN_CENTER);
            doc.add(fallback);
        }

        Paragraph address = new Paragraph("123, Main Road, City, State - 123456\nPhone: +91-9876543210\nEmail: contact@hotelroyal.com",
                FontFactory.getFont(FontFactory.HELVETICA, 10));
        address.setAlignment(Element.ALIGN_CENTER);
        doc.add(address);

        doc.add(Chunk.NEWLINE);
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
        Paragraph thankYou = new Paragraph("Thank you for choosing Hotel Royal Palace!",
                FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10));
        thankYou.setAlignment(Element.ALIGN_CENTER);
        doc.add(thankYou);
    }
}
