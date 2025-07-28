package com.masi.sale.utilities;

import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.RenderData;
import com.deepoove.poi.data.TextRenderData;
import com.deepoove.poi.data.style.Style;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.pdf.PdfDocument;

import org.apache.poi.xwpf.usermodel.LineSpacingRule;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.deepoove.poi.data.Texts;
import com.masi.sale.domain.QuotationDetail;
import com.masi.sale.service.dto.QuotationDTO;
import com.masi.sale.web.rest.QuotationResource;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandleExportQuotation {
    private static final String KIM_LONG = "KIM_LONG";
    private static final String MMS = "MMS";
    private static final Logger log = LoggerFactory.getLogger(HandleExportQuotation.class);

    public static Tuple2<String, String> exportQuotation(String company, QuotationDTO quotations, int op) {
        // get template with company
        try {
            // sort quotation details by created date
            var sortedQuotationDetails = quotations.getQuotationDetails()
                .stream()
                .filter(x -> x.getIndex() != null)
                .sorted(Comparator.comparing(QuotationDetail::getIndex))
                .toList();
            quotations.setQuotationDetails(new HashSet<>(sortedQuotationDetails));
            // check company
            boolean isKIM_LONG = String.valueOf(company).contains(KIM_LONG);
            if(isKIM_LONG) op = 2;

            String templateName = "quotation_template_" + company + ".docx";
            if (isKIM_LONG) {
                templateName = "quotation_template_" + company + "_" + op + ".docx";
            }
            System.out.println("Template name: " + templateName);
            // get path of template
            String templatePath = "/templates/docx/" + templateName;
            // open template
            var fis = HandleExportQuotation.class.getResourceAsStream(templatePath);
            XWPFDocument xwpfDocument = null;
            if (fis != null) {
                xwpfDocument = new XWPFDocument(fis);
            } else {
                throw new IOException("Không thể tạo thư mục: " + templatePath);
            }
            String directoryPath = "../export/quotation/";
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                if (!directory.mkdirs()) {
                    fis.close();
                    xwpfDocument.close();
                    throw new IOException("Không thể tạo thư mục: " + directoryPath);
                }
            }
            // get first table in template
            XWPFTable table = xwpfDocument.getTables().get(0);
            // remove all data table
            if (isKIM_LONG && op == 1) {
                for (int i = table.getRows().size() - 1; i > 1; i--) {
                    table.removeRow(i);
                }

            } else {
                for (int i = table.getRows().size() - 1; i > 0; i--) {
                    table.removeRow(i);
                }
            }
            var idFile = UUID.randomUUID().toString();
            var packagingEn = quotations.getPackagingEn();
            var packaging = quotations.getPackaging();
            var paymentEn = quotations.getPaymentMethodEn();
            var payment = quotations.getPaymentMethod();


            var deliveryLocation = quotations.getDeliveryLocation();
            var deliveryLocationEn = quotations.getDeliveryLocationEn();


            var minimumWeight = quotations.getMinimumWeight();

            var today = Date.from(ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).toInstant());
            var localToday = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")).toLocalDate();
            var headerMonthYear = getFormattedDate(today);

            var deliveryDate = quotations.getDeliveryDate();
            if (deliveryDate == null) {
                log.debug("delivery date not found");
                deliveryDate = ZonedDateTime.now();
            }
            if (isKIM_LONG) {
                Instant instant = deliveryDate.toInstant();
                Date date = Date.from(instant);
                var deliveryDateFormatted = getFormattedDate(date);
                if (op == 2) {

                    for (QuotationDetail item : quotations.getQuotationDetails()) {

                        XWPFTableRow row = table.createRow();

                        // Căn chỉnh từng ô và thiết lập font
                        setCellText(row.getCell(0), item.getMaterial().getName(), ParagraphAlignment.CENTER, true);
                        setCellText(row.getCell(1), item.getMaterial().getNote(), ParagraphAlignment.LEFT, true);
                        setCellText(row.getCell(2), item.getPrice(), ParagraphAlignment.CENTER, true);
                        setCellText(row.getCell(3), String.valueOf(item.getWeight()), ParagraphAlignment.CENTER, true);
                    }
                    mergeCellVertically(table, 4, 1, table.getRows().size() - 1);
                    setCellText(table.getRow(1).getCell(4), quotations.getDeliveryLocation() + "\n" + quotations.getDeliveryLocationEn(), ParagraphAlignment.LEFT, true);

                    // Kiểm tra sự tồn tại của thư mục

                    XWPFTemplate.compile(xwpfDocument).render(new HashMap<String, Object>() {
                        {
                            put("deliveryDateVn", Texts.of(deliveryDateFormatted.getT1()).create());
                            put("deliveryDateEn", Texts.of(deliveryDateFormatted.getT2()).italic().create());
                            put("packaging", packaging);
                            put("packagingEn", Texts.of(packagingEn).italic().create());
                            put("payment", payment);
                            put("paymentEn", paymentEn);
                            put("minimumWeight", minimumWeight);
                            put("companyName", quotations.getCustomer().getCompanyName());
                            put("companyAddress", quotations.getCustomer().getAddress());
                        }
                    }).writeToFile(directoryPath + idFile + ".docx");
                } else {
                    if (op == 1) {
                        for (QuotationDetail item : quotations.getQuotationDetails()) {
                            XWPFTableRow row = table.createRow();
                            int numberOfCells = 5;
                            System.out.println(row);
                            while (row.getTableCells().size() > numberOfCells) {
                                row.removeCell(row.getTableCells().size() - 1);
                            }
                            while (row.getTableCells().size() < numberOfCells) {
                                row.addNewTableCell();
                            }
                            // Căn chỉnh từng ô và thiết lập font
                            setCellText(row.getCell(0), item.getMaterial().getName(), ParagraphAlignment.CENTER, true);
                            setCellText(row.getCell(1), item.getNote(), ParagraphAlignment.LEFT, true);
                            setCellText(row.getCell(2), item.getNitrogen150Price() == null ? "" : item.getNitrogen150Price(),
                                ParagraphAlignment.CENTER,
                                true);
                            setCellText(row.getCell(3), item.getNitrogen180Price() == null ? "" : item.getNitrogen180Price(),
                                ParagraphAlignment.CENTER,
                                true);
                            setCellText(row.getCell(4), item.getWeight(), ParagraphAlignment.CENTER, true);

                        }

                        var priceType = quotations.getPriceType();
                        var priceTypeEn = quotations.getPriceTypeEn();

                        // Combine segments into a single field
                        var prefix = localToday.getDayOfMonth();
                        XWPFTemplate.compile(xwpfDocument).render(new HashMap<String, Object>() {
                            {
                                put("deliveryDateVn", Texts.of(deliveryDateFormatted.getT1()).create());
                                put("deliveryDateEn", Texts.of(deliveryDateFormatted.getT2()).italic().create());

                                put("deliveryLocation", deliveryLocation);
                                put("deliveryLocation1", Texts.of(deliveryLocationEn).italic().create());
                                put("payment", payment);
                                put("packaging", packaging);
                                put("packagingEn", Texts.of(packagingEn).italic().create());
                                put("paymentEn", paymentEn);
                                put("minimumWeight", minimumWeight);
                                put("priceType", Texts.of(priceType).bold().create());
                                put("priceType1", Texts.of(priceTypeEn).bold().italic().create());
                                put("headerMonthYear", headerMonthYear.getT2());
                                put("prefix", getDayOfMonthSuffix(prefix));
                                put("headerDay", localToday.getDayOfMonth());
                                put("companyName", quotations.getCustomer().getCompanyName());
                                put("companyAddress", quotations.getCustomer().getAddress());
                            }
                        }).writeToFile(directoryPath + idFile + ".docx");
                    } else {
                        xwpfDocument.close();
                        fis.close();
                        return Tuples.of("ERROR", quotations.getName());
                    }
                }
            } else {
                if (String.valueOf(company).contains(MMS)) {
                    int i = 0;
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/yyyy");

                    for (QuotationDetail item : quotations.getQuotationDetails()) {
                        XWPFTableRow row = table.createRow();
                        i++;
                        // Căn chỉnh từng ô và thiết lập font
                        var stringFormatted = String.format("%d", i);
                        if (i < 10) {
                            stringFormatted = String.format("%02d", i);
                        }
                        var deliDate = item.getDeliveryDate() == null ? quotations.getDeliveryDate() == null ? ZonedDateTime.now() : quotations.getDeliveryDate() : item.getDeliveryDate();
                        String deliveryDateFormatMMS = deliDate.format(dateTimeFormatter);
                        setCellText(row.getCell(0), stringFormatted, ParagraphAlignment.CENTER, true);
                        setCellText(row.getCell(1), item.getMaterial().getName(), ParagraphAlignment.LEFT, true);
                        setCellText(row.getCell(2), deliveryDateFormatMMS, ParagraphAlignment.CENTER, true);
                        setCellText(row.getCell(3), item.getPrice(), ParagraphAlignment.CENTER, true);
                        setCellText(row.getCell(4), item.getWeight(), ParagraphAlignment.CENTER, true);
                        setCellText(row.getCell(5), item.getDeliveryLocation() + "\n" + item.getDeliveryLocationEn(), ParagraphAlignment.LEFT, true);
                        setCellText(row.getCell(6), item.getNote(), ParagraphAlignment.LEFT, true);

                    }

                    var materialCriteria = quotations.getMaterialCriteria();
                    var materialCriteriaEn = quotations.getMaterialCriteriaEn();
                    XWPFTemplate.compile(xwpfDocument).render(new HashMap<String, Object>() {
                        {
                            put("materialCriteria", materialCriteria);
                            put("packaging", packaging);
                            put("packingEn", Texts.of(packagingEn).italic().create());
                            put("materialCriteriaEn", Texts.of(materialCriteriaEn).italic().create());
                            put("paymentEn", paymentEn);
                            put("payment", payment);
                            put("headerMonthYear", headerMonthYear.getT2());
                            put("headerDay", localToday.getDayOfMonth());
                            put("headerMonth", localToday.getMonthValue());
                            put("headerYear", localToday.getYear());
                            put("companyName", quotations.getCustomer().getCompanyName());
                            put("companyAddress", quotations.getCustomer().getAddress());
                        }
                    }).writeToFile(directoryPath + idFile + ".docx");

                    // check dir /usr/share/fonts/truetype/msttcorefonts exist:
                } else {
                    xwpfDocument.close();
                    fis.close();
                    return Tuples.of("ERROR", quotations.getName());
                }
            }
            // Kiểm tra sự tồn tại của đường dẫn
            // Kiểm tra và in ra đường dẫn
            PdfDocument.setCustomFontsFolders("/fonts");
            var doc = new Document();
            doc.loadFromFile(directoryPath + idFile + ".docx");
            doc.saveToFile(directoryPath + idFile + ".pdf", FileFormat.PDF);
            return Tuples.of(directoryPath + idFile + ".pdf", quotations.getName());

        } catch (

            Exception e) {
            e.printStackTrace();
            return Tuples.of("ERROR", quotations.getName());
        }
    }

    // tạ ơn https://stackoverflow.com/questions/73529637/vertically-merging-multiple-rows-in-poi-xwpftable-results-in-malformed-table
    private static void mergeCellVertically(XWPFTable table, int col, int fromRow, int toRow) {
        for (int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {
            XWPFTableCell cell = table.getRow(rowIndex).getCell(col);
            CTVMerge vmerge = CTVMerge.Factory.newInstance();
            if (rowIndex == fromRow) {
                // The first merged cell is set with RESTART merge value
                vmerge.setVal(STMerge.RESTART);
            } else {
                // Cells which join (merge) the first one, are set with CONTINUE
                vmerge.setVal(STMerge.CONTINUE);
                // and the content should be removed
                for (int i = cell.getParagraphs().size(); i > 0; i--) {
                    cell.removeParagraph(0);
                }
                cell.addParagraph();
            }
            // Try getting the TcPr. Not simply setting an new one every time.
            CTTcPr tcPr = cell.getCTTc().getTcPr();
            if (tcPr == null) tcPr = cell.getCTTc().addNewTcPr();
            tcPr.setVMerge(vmerge);
        }
    }

    private static String getDayOfMonthSuffix(final int n) {
        if (n >= 1 && n <= 31) {
            if (n >= 11 && n <= 13) {
                return "th";
            }
            return switch (n % 10) {
                case 1 -> "st";
                case 2 -> "nd";
                case 3 -> "rd";
                default -> "th";
            };
        }
        return "th";
    }

    private static Tuple2<String, String> getFormattedDate(Date dateInput) {
        // Định dạng ngày tháng
        if (dateInput == null) {
            dateInput = new Date();
        }
        SimpleDateFormat monthFormatter = new SimpleDateFormat("MMyyyy", Locale.getDefault());
        SimpleDateFormat monthNameFormatter = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        SimpleDateFormat monthNameShortFormatter = new SimpleDateFormat("MMM yyyy", Locale.getDefault());

        String monthYear = monthFormatter.format(dateInput);
        String monthName = monthNameFormatter.format(dateInput);
        String monthNameShort = monthNameShortFormatter.format(dateInput);

        // Chia tháng và năm
        String month = monthYear.substring(0, 2);
        String year = monthYear.substring(2);

        // Xử lý tên tháng
        String[] months = {"", "Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6", "Tháng 7", "Tháng 8",
            "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"};
        String formattedMonth = months[Integer.parseInt(month)];
        return Tuples.of(formattedMonth + " " + year, monthName);
    }

    private static void setCellText(XWPFTableCell cell, String text, ParagraphAlignment alignment,
                                    boolean centerVertically) {
        // Xóa nội dung cũ trong ô (nếu có)
        cell.removeParagraph(0);

        // Kiểm tra nếu văn bản không null
        text = text == null ? "" : text;

        // Tách văn bản thành các dòng theo dấu xuống hàng
        var lines = text.split("\n");

        // Tạo đoạn văn bản mới
        XWPFParagraph paragraph = cell.addParagraph();
        paragraph.setAlignment(alignment);

        // Thiết lập khoảng cách dòng
        paragraph.setSpacingBetween(1.0, LineSpacingRule.AUTO);

        // Thêm từng dòng văn bản vào ô
        for (int i = 0; i < lines.length; i++) {
            XWPFRun run = paragraph.createRun();
            run.setText(lines[i]);
            run.setFontFamily("Times New Roman");
            run.setFontSize(11); // Kích thước font là 11

            // Nếu không phải là dòng cuối cùng, thêm dấu xuống hàng
            if (i < lines.length - 1) {
                run.addBreak();
            }
        }

        // Căn giữa văn bản theo chiều dọc
        if (centerVertically) {
            cell.getCTTc().addNewTcPr().addNewVAlign()
                .setVal(STVerticalJc.CENTER);
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(
            "C:\\Users\\Laffy\\AppData\\Local\\Temp\\quotation131212.bin");
        ObjectInputStream ois = new ObjectInputStream(fis);
        QuotationDTO quotation = (QuotationDTO) ois.readObject();
        ois.close();
        fis.close();
//        return Tuples.of(directoryPath + idFile + ".pdf", quotations.getName());

        var res = exportQuotation("KIM_LONG", quotation, 2);
        String path = res.getT1();
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(new File(path));
        }


    }
}
