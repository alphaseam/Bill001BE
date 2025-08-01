package com.hotelapi.util;

import com.hotelapi.entity.Hotel;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;

/**
 * Utility class for generating styled PDF invoices.
 */
public class PdfUtility {

    /**
     * Adds a business header with logo and contact details to the PDF.
     * If the logo is not found, a text header is used as a fallback.
     *
     * @param doc      the PDF document
     * @param logoPath classpath location of the logo (e.g., "static/logo.png")
     * @throws DocumentException in case of PDF writing errors
     */
    public static void addBusinessHeader(Document doc, String logoPath, Hotel hotel) throws DocumentException {
        try {
            ClassPathResource resource = new ClassPathResource(logoPath);

            if (resource.exists()) {
                try (InputStream is = resource.getInputStream()) {
                    byte[] imageBytes = is.readAllBytes();
                    Image logo = Image.getInstance(imageBytes);
                    logo.scaleToFit(100, 60);
                    logo.setAlignment(Element.ALIGN_CENTER);
                    doc.add(logo);
                }
            } else {
                System.err.println("⚠️ Logo not found at: " + logoPath + " — using text fallback.");
                addFallbackTitle(doc, hotel);
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error loading logo from path [" + logoPath + "]: " + e.getMessage());
            addFallbackTitle(doc, hotel);
        }

        // Business details below logo/text
        Paragraph contact = new Paragraph(
                hotel.getHotelName() + "\n" +
                hotel.getAddress() + "\n" +
                "Phone: " + hotel.getMobile() + " | Email: " + hotel.getEmail(),
                FontFactory.getFont(FontFactory.HELVETICA, 10)
        );
        contact.setAlignment(Element.ALIGN_CENTER);
        doc.add(contact);
        doc.add(Chunk.NEWLINE);
    }

    /**
     * Fallback title when logo is missing.
     *
     * @param doc the PDF document
     * @throws DocumentException if the document can't be written
     */
    private static void addFallbackTitle(Document doc, Hotel hotel) throws DocumentException {
        Paragraph title = new Paragraph(hotel.getHotelName().toUpperCase(),
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22));
        title.setAlignment(Element.ALIGN_CENTER);
        doc.add(title);
    }

    /**
     * Returns a styled table header cell.
     *
     * @param text the header text
     * @return styled header cell
     */
    public static PdfPCell getTableHeaderCell(String text) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(BaseColor.DARK_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(6f);
        return cell;
    }

    /**
     * Returns a normal styled table cell.
     *
     * @param text the cell text
     * @return styled data cell
     */
    public static PdfPCell getTableCell(String text) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA, 10);
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(6f);
        return cell;
    }

    /**
     * Adds a thank-you footer at the end of the PDF.
     *
     * @param doc the PDF document
     * @throws DocumentException if the document can't be written
     */
    public static void addThankYouFooter(Document doc, Hotel hotel) throws DocumentException {
        doc.add(Chunk.NEWLINE);
        Paragraph thanks = new Paragraph(
                "Thank you for choosing " + hotel.getHotelName() + "!",
                FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10));
        thanks.setAlignment(Element.ALIGN_CENTER);
        doc.add(thanks);
    }
}
