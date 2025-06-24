package com.hotelapi.service;

import com.hotelapi.entity.Bill;
import com.hotelapi.repository.BillRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class InvoiceService {

    private final BillRepository billRepository;

    @Value("${invoice.output.directory:invoices}")
    private String invoiceDir;

    public InvoiceService(BillRepository billRepository) {
        this.billRepository = billRepository;
    }

    public String generateInvoicePdf(Long billId) throws Exception {
        Optional<Bill> optionalBill = billRepository.findById(billId);
        if (optionalBill.isEmpty()) {
            throw new Exception("Bill not found with ID: " + billId);
        }

        Bill bill = optionalBill.get();
        
        String fileName = "Invoice_" + bill.getBillNumber() + ".pdf";
        String filePath = invoiceDir + "/" + fileName;

        Document document = new Document(PageSize.A4, 50, 50, 80, 50);
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        addHeader(document);
        addCustomerDetails(document, bill);
        addBillTable(document, bill);
        addTotals(document, bill);
        addFooter(document);

        document.close();

        return filePath;
    }

    private void addHeader(Document doc) throws DocumentException, IOException {
        Paragraph businessName = new Paragraph("Hotel Royal Palace", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20));
        businessName.setAlignment(Element.ALIGN_CENTER);
        doc.add(businessName);

        Paragraph address = new Paragraph("123, Main Road, City, State - 123456", FontFactory.getFont(FontFactory.HELVETICA, 10));
        address.setAlignment(Element.ALIGN_CENTER);
        doc.add(address);

        Paragraph phone = new Paragraph("Phone: +91-9876543210 | Email: contact@hotelroyal.com", FontFactory.getFont(FontFactory.HELVETICA, 10));
        phone.setAlignment(Element.ALIGN_CENTER);
        doc.add(phone);

        doc.add(Chunk.NEWLINE);
    }

    private void addCustomerDetails(Document doc, Bill bill) throws DocumentException {
        Paragraph billInfo = new Paragraph("Invoice #: " + bill.getBillNumber() +
                "       Date: " + bill.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
        billInfo.setSpacingAfter(10);
        doc.add(billInfo);

        Paragraph customer = new Paragraph("Customer: " + bill.getCustomer().getName() +
                "       Mobile: " + bill.getCustomer().getMobile());
        customer.setSpacingAfter(10);
        doc.add(customer);
    }

    private void addBillTable(Document doc, Bill bill) throws DocumentException {
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100f);
        table.setWidths(new float[]{3, 1, 1, 1, 1});

        addTableHeader(table, "Item", "Qty", "Unit Price", "Discount", "Total");

        bill.getItems().forEach(item -> {
            table.addCell(item.getItemName());
            table.addCell(String.valueOf(item.getQuantity()));
            table.addCell(String.format("%.2f", item.getUnitPrice()));
            table.addCell(String.format("%.2f", item.getDiscount()));
            table.addCell(String.format("%.2f", item.getTotal()));
        });

        doc.add(table);
    }

    private void addTableHeader(PdfPTable table, String... headers) {
        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(header, headFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(cell);
        }
    }

    private void addTotals(Document doc, Bill bill) throws DocumentException {
        doc.add(Chunk.NEWLINE);
        PdfPTable totalTable = new PdfPTable(2);
        totalTable.setWidthPercentage(40f);
        totalTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

        totalTable.addCell("Subtotal:");
        totalTable.addCell(String.format("%.2f", bill.getSubtotal()));

        totalTable.addCell("Tax:");
        totalTable.addCell(String.format("%.2f", bill.getTax()));

        totalTable.addCell("Discount:");
        totalTable.addCell(String.format("%.2f", bill.getDiscount()));

        totalTable.addCell("Grand Total:");
        totalTable.addCell(String.format("%.2f", bill.getTotal()));

        doc.add(totalTable);
    }

    private void addFooter(Document doc) throws DocumentException {
        doc.add(Chunk.NEWLINE);
        Paragraph thankYou = new Paragraph("Thank you for visiting Hotel Royal Palace!", FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10));
        thankYou.setAlignment(Element.ALIGN_CENTER);
        doc.add(thankYou);
    }
}
