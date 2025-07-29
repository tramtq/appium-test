package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.*;
import org.testng.xml.XmlSuite;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelReporter implements IReporter {

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("TestNG Report");

        // Header
        String[] headers = {"Test Name", "Class Name", "Method Name", "Status", "Error Message"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        int rowCount = 1;

        for (ISuite suite : suites) {
            for (ISuiteResult suiteResult : suite.getResults().values()) {
                ITestContext testContext = suiteResult.getTestContext();

                rowCount = writeTestResults(sheet, testContext.getPassedTests().getAllResults(), "PASSED", rowCount);
                rowCount = writeTestResults(sheet, testContext.getFailedTests().getAllResults(), "FAILED", rowCount);
                rowCount = writeTestResults(sheet, testContext.getSkippedTests().getAllResults(), "SKIPPED", rowCount);
            }
        }

        // Auto size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Save file
        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File dir = new File("logs");
            if (!dir.exists()) dir.mkdirs();

            FileOutputStream fileOut = new FileOutputStream("logs/TestReport_" + timestamp + ".xlsx");
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
            System.out.println("✅ Excel report created at: logs/TestReport_" + timestamp + ".xlsx");

        } catch (Exception e) {
            System.err.println("❌ Failed to write Excel report: " + e.getMessage());
        }
    }

    private int writeTestResults(Sheet sheet, Set<ITestResult> results, String status, int startRow) {
        for (ITestResult result : results) {
            Row row = sheet.createRow(startRow++);

            row.createCell(0).setCellValue(result.getTestContext().getName());
            row.createCell(1).setCellValue(result.getTestClass().getName());
            row.createCell(2).setCellValue(result.getMethod().getMethodName());
            row.createCell(3).setCellValue(status);

            Throwable throwable = result.getThrowable();
            if (throwable != null) {
                row.createCell(4).setCellValue(throwable.getMessage());
            } else {
                row.createCell(4).setCellValue("");
            }
        }
        return startRow;
    }
}
