package com.hotelapi.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;

/**
 * Utility class for generating styled PDF invoices.
 */
public class PdfUtility {

    /**
     * Adds a business header with the hotel logo and contact details.
     * Falls back to hotel name text if the logo is missing.
     *
     * @param doc      the PDF document
     * @param logoPath path relative to resources, for example "static/logo.png"
     * @throws DocumentException in case of PDF errors
     */
    public static void addBusinessHeader(Document doc, String logoPath) throws DocumentException {
        try {
            // attempt to load from classpath
            ClassPathResource resource = new ClassPathResource(logoPath);

            System.out.println("Logo exists? " + resource.exists());

            if (resource.exists()) {
                try (InputStream is = resource.getInputStream()) {
                    byte[] imageBytes = is.readAllBytes();
                    Image logo = Image.getInstance(imageBytes);
                    logo.scaleToFit(80, 80);
                    logo.setAlignment(Image.ALIGN_CENTER);
                    doc.add(logo);
                }
            } else {
                System.err.println("Logo not found in classpath, using fallback text header.");
                addFallbackTitle(doc);
            }
        } catch (Exception e) {
            System.err.println("Error loading logo: " + e.getMessage());
            addFallbackTitle(doc);
        }

        // address block below the logo or fallback
        Paragraph address = new Paragraph(
                "123, Main Road, City, State - 123456\n" +
                        "Phone: +91-9876543210\n" +
                        "Email: contact@hotelroyal.com",
                FontFactory.getFont(FontFactory.HELVETICA, 10));
        address.setAlignment(Element.ALIGN_CENTER);
        doc.add(address);

        doc.add(Chunk.NEWLINE);
    }

    /**
     * Adds a fallback text header if the logo is missing.
     *
     * @param doc the PDF document
     * @throws DocumentException
     */
    private static void addFallbackTitle(Document doc) throws DocumentException {
        Paragraph fallback = new Paragraph("HOTEL ROYAL PALACE",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22));
        fallback.setAlignment(Element.ALIGN_CENTER);
        doc.add(fallback);
    }

    /**
     * Creates a table header cell with consistent style.
     *
     * @param text header text
     * @return styled PdfPCell
     */
    public static PdfPCell getTableHeaderCell(String text) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(BaseColor.DARK_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        return cell;
    }

    /**
     * Creates a normal table cell with standard style.
     *
     * @param text cell text
     * @return styled PdfPCell
     */
    public static PdfPCell getTableCell(String text) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(5);
        return cell;
    }

    /**
     * Adds a thank-you footer at the end of the PDF.
     *
     * @param doc the PDF document
     * @throws DocumentException
     */
    public static void addThankYouFooter(Document doc) throws DocumentException {
        doc.add(Chunk.NEWLINE);
        Paragraph thankYou = new Paragraph(
                "Thank you for choosing Hotel Royal Palace!",
                FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10));
        thankYou.setAlignment(Element.ALIGN_CENTER);
        doc.add(thankYou);
    }
}
