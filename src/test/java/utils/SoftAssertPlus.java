package utils;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.asserts.SoftAssert;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.*;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;

public class SoftAssertPlus extends SoftAssert {

    private final List<Failure> failures = new ArrayList<>();
    private final List<String> inputDataList = new ArrayList<>();

    private static final String FILE_PATH = "logs/soft_assert_log.xlsx";
    private static final String EVIDENCE_DIR = "logs/evidence/";
    private static Workbook workbook;
    private static final Map<String, Sheet> sheets = new HashMap<>();
    private static boolean initialized = false;

    private String testCaseName = "UnknownTestCase";
    private String methodName = "UnknownMethod";
    private AppiumDriver driver;

    public SoftAssertPlus(AppiumDriver driver) {
        this.driver = driver;
    }

    public void setTestCaseInfo(String testClass, String testMethod) {
        this.testCaseName = testClass;
        this.methodName = testMethod;
        clearOldEvidence();
    }

    public void logInputData(String inputData) {
        inputDataList.add(inputData);
    }

    public void assertAllWithLog() {
        writeLog();
        if (failures.stream().anyMatch(Failure::isFail)) {
            throw new AssertionError("❌ Có lỗi xảy ra trong testcase: " + testCaseName);
        }
    }

    private void writeLog() {
        initWorkbook();
        Sheet sheet = sheets.computeIfAbsent(testCaseName, key -> {
            Sheet newSheet = workbook.createSheet(key);
            Row header = newSheet.createRow(0);
            header.createCell(0).setCellValue("Testcase");
            header.createCell(1).setCellValue("InputData");
            header.createCell(2).setCellValue("Pass");
            header.createCell(3).setCellValue("Fail");
            header.createCell(4).setCellValue("Expected");
            header.createCell(5).setCellValue("Actual");
            header.createCell(6).setCellValue("Evidence");
            return newSheet;
        });

        int rowNum = sheet.getLastRowNum() + 1;

        for (int i = 0; i < failures.size(); i++) {
            Failure failure = failures.get(i);
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(methodName);
            String inputData = i < inputDataList.size() ? inputDataList.get(i) : "";
            row.createCell(1).setCellValue(inputData);

            if (failure.isFail()) {
                row.createCell(3).setCellValue(failure.message);
                row.createCell(4).setCellValue(failure.expected != null ? failure.expected.toString() : "N/A");
                row.createCell(5).setCellValue(failure.actual != null ? failure.actual.toString() : "N/A");

                if (failure.screenshotPath != null) {
                    CreationHelper helper = workbook.getCreationHelper();
                    Hyperlink link = helper.createHyperlink(HyperlinkType.FILE);
                    link.setAddress(failure.screenshotPath.replace("\\", "/"));

                    Cell linkCell = row.createCell(6);
                    linkCell.setHyperlink(link);
                    linkCell.setCellValue("📷 Xem ảnh");

                    CellStyle style = workbook.createCellStyle();
                    Font font = workbook.createFont();
                    font.setUnderline(Font.U_SINGLE);
                    font.setColor(IndexedColors.BLUE.getIndex());
                    style.setFont(font);
                    linkCell.setCellStyle(style);
                }
            } else {
                row.createCell(2).setCellValue(failure.message);
            }
        }

        saveWorkbook();
        failures.clear();
        inputDataList.clear();
    }

    private void initWorkbook() {
        if (initialized) return;

        try {
            File logDir = new File("logs");
            if (!logDir.exists() && !logDir.mkdirs()) {
                System.err.println("⚠️ Failed to create log directory.");
            }

            File file = new File(FILE_PATH);
            if (file.exists()) {
                try (FileInputStream in = new FileInputStream(file)) {
                    workbook = new XSSFWorkbook(in);
                }
                for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                    Sheet sheet = workbook.getSheetAt(i);
                    sheets.put(sheet.getSheetName(), sheet);
                }
            } else {
                workbook = new XSSFWorkbook();
            }
            initialized = true;
        } catch (IOException e) {
            System.err.println("❌ Failed to init Excel: " + e.getMessage());
        }
    }

    private void saveWorkbook() {
        try (FileOutputStream out = new FileOutputStream(FILE_PATH)) {
            workbook.write(out);
        } catch (IOException e) {
            System.err.println("❌ Failed to save Excel: " + e.getMessage());
        }
    }

    private String takeScreenshot() {
        if (driver == null) return null;
        try {
            File dir = new File(EVIDENCE_DIR);
            if (!dir.exists() && !dir.mkdirs()) {
                System.err.println("⚠️ Failed to create evidence directory.");
            }

            String filename = testCaseName + "_" + methodName + "_" +
                    new SimpleDateFormat("HHmmss").format(new Date()) + ".png";
            String path = EVIDENCE_DIR + filename;

            File srcFile = driver.getScreenshotAs(OutputType.FILE);
            File destFile = new File(path);
            Files.copy(srcFile.toPath(), destFile.toPath());
            return path;
        } catch (Exception e) {
            System.err.println("⚠️ Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }

    private void clearOldEvidence() {
        File dir = new File(EVIDENCE_DIR);
        if (dir.exists()) {
            File[] files = dir.listFiles((d, name) -> name.startsWith(testCaseName + "_" + methodName));
            if (files != null) {
                for (File f : files) {
                    if (!f.delete()) {
                        System.err.println("⚠️ Failed to delete file: " + f.getName());
                    }
                }
            }
        }
    }

    // ===== Custom assertions with screenshot on fail =====

    public void assertTrue(boolean condition, String failMessage, String passMessage) {
        if (condition) {
            failures.add(new Failure(passMessage, null, null));
        } else {
            failures.add(new Failure(failMessage, "true", "false", takeScreenshot()));
        }
        super.assertTrue(condition, failMessage);
    }

    public void assertFalse(boolean condition, String failMessage, String passMessage) {
        if (!condition) {
            failures.add(new Failure(passMessage, null, null));
        } else {
            failures.add(new Failure(failMessage, "false", "true", takeScreenshot()));
        }
        super.assertFalse(condition, failMessage);
    }

    public void assertEquals(Object actual, Object expected, String failMessage, String passMessage) {
        if (Objects.equals(actual, expected)) {
            failures.add(new Failure(passMessage, null, null));
        } else {
            failures.add(new Failure(failMessage, expected, actual, takeScreenshot()));
        }
        super.assertEquals(actual, expected, failMessage);
    }

    // ===== Failure Entry =====

    private static class Failure {
        String message;
        Object expected;
        Object actual;
        String screenshotPath;

        public Failure(String message, Object expected, Object actual) {
            this(message, expected, actual, null);
        }

        public Failure(String message, Object expected, Object actual, String screenshotPath) {
            this.message = message;
            this.expected = expected;
            this.actual = actual;
            this.screenshotPath = screenshotPath;
        }

        public boolean isFail() {
            return expected != null || actual != null;
        }
    }
}
