package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SoftAssertExcelLogger {

    //Mỗi lần run test tạo 1 file mới
    private static final String FILE_PATH;

    static {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        FILE_PATH = "logs/soft_assert_log_" + timestamp + ".xlsx";
    }

    private static Workbook workbook;
    private static Sheet sheet;
    private static int currentRow = 1;
    private static boolean initialized = false;

    // Initialize workbook, sheet, and headers
    private static void init() {
        if (initialized) return;

        try {
            // Tạo thư mục logs nếu chưa tồn tại
            File logDir = new File("logs");
            if (!logDir.exists()) logDir.mkdirs();

            File file = new File(FILE_PATH);
            if (file.exists()) {
                try (FileInputStream in = new FileInputStream(file)) {
                    workbook = new XSSFWorkbook(in);
                }
                sheet = workbook.getSheetAt(0);
                currentRow = sheet.getLastRowNum() + 1;
            } else {
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet("SoftAssert Logs");

                Row header = sheet.createRow(0);
                header.createCell(0).setCellValue("Timestamp");
                header.createCell(1).setCellValue("Test Method");
                header.createCell(2).setCellValue("Expected");
                header.createCell(3).setCellValue("Actual");
                header.createCell(4).setCellValue("Result");
            }

            initialized = true;
        } catch (Exception e) {
            System.err.println("❌ Failed to initialize Excel logger: " + e.getMessage());
        }
    }

    // Ghi log 1 dòng vào workbook
    public static void log(String method, String expected, String actual, boolean passed) {
        init();
        if (sheet == null) return;

        Row row = sheet.createRow(currentRow++);
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        row.createCell(0).setCellValue(time);
        row.createCell(1).setCellValue(method);
        row.createCell(2).setCellValue(expected);
        row.createCell(3).setCellValue(actual);
        row.createCell(4).setCellValue(passed ? "✅ PASS" : "❌ FAIL");

        saveWorkbook();
    }

    // Ghi workbook ra file duy nhất
    private static void saveWorkbook() {
        try (FileOutputStream out = new FileOutputStream(FILE_PATH)) {
            workbook.write(out);
        } catch (IOException e) {
            System.err.println("❌ Failed to write log to Excel: " + e.getMessage());
        }
    }
}
