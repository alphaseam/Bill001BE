package com.hotelapi.service;

import com.hotelapi.entity.Bill;
import com.hotelapi.entity.Hotel;
import com.hotelapi.repository.BillRepository;
import com.hotelapi.util.PdfUtility;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class InvoiceService {

    private final BillRepository billRepository;
    private final String invoiceDir;

    public InvoiceService(
            BillRepository billRepository,
            @Value("${invoice.output.directory:invoices}") String invoiceDir
    ) {
        this.billRepository = billRepository;
        this.invoiceDir = invoiceDir;
    }

    public String generateInvoicePdf(Long billId) throws Exception {
        Optional<Bill> optionalBill = billRepository.findById(billId);
        if (optionalBill.isEmpty()) {
            throw new Exception("Bill not found with ID: " + billId);
        }

        Bill bill = optionalBill.get();

        String fileName = "Invoice_" + bill.getBillNumber() + ".pdf";
        
        // Ensure the output directory exists
        File outputDir = new File(invoiceDir);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        
        String filePath = invoiceDir + File.separator + fileName;

        Document document = new Document(PageSize.A4, 50, 50, 80, 50);
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        addHeader(document, bill.getHotel());
        addCustomerDetails(document, bill);
        addBillTable(document, bill);
        addTotals(document, bill);
        addFooter(document, bill.getHotel());

        document.close();

        return filePath;
    }

private void addHeader(Document doc, Hotel hotel) throws DocumentException, IOException {
    PdfUtility.addBusinessHeader(doc, "static/logo.png", hotel);
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
            table.addCell(PdfUtility.getTableCell(item.getItemName()));
            table.addCell(PdfUtility.getTableCell(String.valueOf(item.getQuantity())));
            table.addCell(PdfUtility.getTableCell(String.format("%.2f", item.getUnitPrice())));
            table.addCell(PdfUtility.getTableCell(String.format("%.2f", item.getDiscount())));
            table.addCell(PdfUtility.getTableCell(String.format("%.2f", item.getTotal())));
        });

        doc.add(table);
    }

    private void addTableHeader(PdfPTable table, String... headers) {
        for (String header : headers) {
            table.addCell(PdfUtility.getTableHeaderCell(header));
        }
    }

    private void addTotals(Document doc, Bill bill) throws DocumentException {
        doc.add(Chunk.NEWLINE);
        PdfPTable totalTable = new PdfPTable(2);
        totalTable.setWidthPercentage(40f);
        totalTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

        totalTable.addCell(PdfUtility.getTableCell("Subtotal:"));
        totalTable.addCell(PdfUtility.getTableCell(String.format("%.2f", bill.getSubtotal())));

        totalTable.addCell(PdfUtility.getTableCell("Tax:"));
        totalTable.addCell(PdfUtility.getTableCell(String.format("%.2f", bill.getTax())));

        totalTable.addCell(PdfUtility.getTableCell("Discount:"));
        totalTable.addCell(PdfUtility.getTableCell(String.format("%.2f", bill.getDiscount())));

        totalTable.addCell(PdfUtility.getTableHeaderCell("Grand Total:"));
        totalTable.addCell(PdfUtility.getTableHeaderCell(String.format("%.2f", bill.getTotal())));

        doc.add(totalTable);
    }

    private void addFooter(Document doc, Hotel hotel) throws DocumentException {
        PdfUtility.addThankYouFooter(doc, hotel);
    }
}
