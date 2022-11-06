package com.example.reportservice.services.implementations;

import com.example.reportservice.apiCommunication.BusinessLogicWebClient;
import com.example.reportservice.models.StatisticsValues;
import com.example.reportservice.services.interfaces.StatisticsService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Stream;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private static final String STATISTICS_FILE_NAME = "src/main/resources/static/Statistics.pdf";
    private final float SMALL_ROW_GAP = 5f;
    private final float BIG_ROW_GAP = 40f;

    private final BusinessLogicWebClient webClient;
    private volatile boolean allDataGot = false;
    private Font textFont;
    private final PdfPTable table;

    public StatisticsServiceImpl(BusinessLogicWebClient webClient) {
        this.webClient = webClient;
        this.table = new PdfPTable(6);
    }

    @Override
    public byte[] getStatistics() {
        Flux<StatisticsValues> statisticsValuesFlux = webClient.fetchDataForStatistics();
        statisticsValuesFlux.doOnComplete(() -> {
            allDataGot = true;
            System.out.println("All data received");
        }).subscribe(values -> {
            System.out.println(values.getName());
            addOneRow(table, values, textFont);
        });
        generateBaseStatisticsFile();
        return getContentOfPDFFile(STATISTICS_FILE_NAME);
    }

    private byte[] getContentOfPDFFile(String fileName) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(new File(fileName)));
            int read = 0;
            byte[] buff = new byte[1024];
            while ((read = inputStream.read(buff)) != -1) {
                bos.write(buff, 0, read);
            }
            return bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void generateBaseStatisticsFile() {
        System.out.println("Start generating file...");
        try {
            BaseFont baseFont = BaseFont.createFont("src/main/resources/static/SFNSRounded.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            Font headerFont = new Font(baseFont, 16);
            this.textFont = new Font(baseFont, 14);

            Document document = new Document(PageSize.A4.rotate());
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(STATISTICS_FILE_NAME));

            PdfHeader event = new PdfHeader();
            writer.setPageEvent(event);

            document.open();

            Paragraph p = new Paragraph("Загальний звіт", headerFont);
            p.setAlignment(Element.ALIGN_CENTER);
            p.setSpacingAfter(SMALL_ROW_GAP);

            String currentDate = formatDate(new Date());
            Paragraph p1 = new Paragraph("Звіт складений "+ currentDate, headerFont);
            p1.setAlignment(Element.ALIGN_LEFT);
            p1.setSpacingAfter(BIG_ROW_GAP);

            document.add(p);
            document.add(p1);

            Paragraph tableHeader = new Paragraph("Інформація про бронювання приміщень по орендодавцям", headerFont);
            tableHeader.setAlignment(Element.ALIGN_CENTER);
            tableHeader.setSpacingAfter(SMALL_ROW_GAP);
            document.add(tableHeader);

            addTableHeader(Stream.of("Id орендодавця", "ПІБ орендодавця", "К-ть завершених бронювань", "К-ть поточних бронювань", "Прибуток орендодавця", "К-ть клієнтів орендодавця"),
                    table, headerFont);

            while (!allDataGot) {
                Thread.onSpinWait();
            };

            table.setWidthPercentage(100);

            document.add(table);
            document.close();
        }
        catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

    private String formatDate(Date d) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(d);

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String dayStr = day < 10 ? "0"+day: day+"";
        int month = calendar.get(Calendar.MONTH)+1;
        String monthStr = month < 10 ? "0"+month: month+"";
        int year = calendar.get(Calendar.YEAR);
        return dayStr+'.'+monthStr+'.'+year;
    }

    private void addTableHeader(Stream<String> headers, PdfPTable table, Font font) {
        headers.forEach(columnTitle -> {
            PdfPCell header = new PdfPCell();
            header.setBorderWidth(2);
            font.setStyle(Font.BOLD);
            header.setPhrase(new Phrase(columnTitle, font));
            table.addCell(header);
        });
    }

    private void addOneRow(PdfPTable table, StatisticsValues stV, Font font) {
            table.addCell(new Phrase(stV.getLessorId() + "", font));
            table.addCell(new Phrase(stV.getName(), font));
            table.addCell(new Phrase(stV.getQuantityOfFinishedBookings() + "", font));
            table.addCell(new Phrase(stV.getQuantityOfBookingsInProgress() + "", font));
            table.addCell(new Phrase(stV.getLessorIncome() + "", font));
            table.addCell(new Phrase(stV.getQuantityOfClients() + "", font));
    }

    public static class PdfHeader extends PdfPageEventHelper {

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            try {
                Rectangle pageSize = document.getPageSize();
                ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT,
                        new Phrase(""+writer.getCurrentPageNumber()),
                        pageSize.getRight(30), pageSize.getBottom(15),0);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
