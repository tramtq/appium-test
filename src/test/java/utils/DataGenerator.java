package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataGenerator {


    public static String generateInvalidPhoneNumber() {
        Random random = new Random();
        List<String> invalidPhones = new ArrayList<>();

        // Quá ngắn (ít hơn 9 hoặc 10 số)
        invalidPhones.add(generateRandomDigits(random.nextInt(3) + 3)); // 3-5 số

        // Quá dài (hơn 10 số)
        invalidPhones.add(generateRandomDigits(random.nextInt(5) + 11)); // 11-15 số

        // Không bắt đầu bằng đầu số hợp lệ (không phải 03, 05, 07, 08, 09)
        String[] invalidStarts = {"00", "11", "12", "10", "06"};
        String randomInvalidStart = invalidStarts[random.nextInt(invalidStarts.length)];
        String remainingDigits = generateRandomDigits(7 + random.nextInt(2)); // tổng thành 9-10 số
        invalidPhones.add(randomInvalidStart + remainingDigits);

        return invalidPhones.get(random.nextInt(invalidPhones.size()));
    }

    public static String generateRandomDigits(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10)); // Tạo số từ 0-9
        }
        return sb.toString();
    }


    public static String generateForeignPhoneNumberWithoutPlus() {
        // Tạo các số ngoài mã vùng
        String[] foreignPrefixes = {"01", "044", "081", "066", "049", "091", "0000"};
        String prefix = foreignPrefixes[new Random().nextInt(foreignPrefixes.length)];
        String number = String.valueOf(10000000 + new Random().nextInt(89999999)); // Số 8 chữ số
        return prefix + number;
    }

    public static String generateInvalidEmail() {
        String local = generateRandomString(5);
        String domain = generateRandomString(4);
        String tld = generateRandomString(3);

        List<String> invalidEmails = new ArrayList<>();

        // Các lỗi thường gặp
        invalidEmails.add(local);                          // Thiếu @ và domain
        invalidEmails.add("@" + domain + "." + tld);       // Thiếu local-part
        invalidEmails.add(local + "@" + domain);           // Thiếu TLD
        invalidEmails.add(local + "@" + "." + tld);        // Thiếu domain name
        invalidEmails.add(local + "@" + domain + "." + tld + "."); // Dư dấu chấm
        invalidEmails.add(local + "@" + domain + ",com");  // Sai dấu phân cách
        invalidEmails.add(local + " " + "@" + domain + "." + tld); // Khoảng trắng
        invalidEmails.add(local + "@-domain." + tld);      // Domain bắt đầu bằng dấu gạch
        invalidEmails.add(local + "@domain..com");         // Liên tiếp dấu chấm
        invalidEmails.add(local + "@.com");                // Domain rỗng
        invalidEmails.add(local + "@");                    // Thiếu TLD

        // Trả về 1 email sai định dạng ngẫu nhiên
        return invalidEmails.get(new Random().nextInt(invalidEmails.size()));
    }

    // Hàm hỗ trợ: Sinh chuỗi ngẫu nhiên
    public static String generateRandomString(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }
    // Hàm sinh số điện thoại Việt Nam hợp lệ (theo các đầu số chính thống)
    public static String generateValidPhoneNumber() {
        String[] prefixes = {
                "032", "033", "034", "035", "036", "037", "038", "039",  // Viettel
                "070", "076", "077", "078", "079",                      // Mobifone
                "081", "082", "083", "084", "085", "086",              // Vinaphone
                "056", "058",                                           // Vietnamobile
                "059"                                                   // Gmobile
        };

        Random random = new Random();
        String prefix = prefixes[random.nextInt(prefixes.length)];
        StringBuilder phoneNumber = new StringBuilder(prefix);

        // Sinh thêm 7 chữ số
        for (int i = 0; i < 7; i++) {
            phoneNumber.append(random.nextInt(10));
        }

        return phoneNumber.toString();
    }

    // Hàm sinh email hợp lệ ngẫu nhiên
    public static String generateValidEmail() {
        String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder localPart = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 8; i++) {
            localPart.append(chars.charAt(random.nextInt(chars.length())));
        }

        String[] domains = {"gmail.com", "yahoo.com", "outlook.com", "fpt.vn", "example.com"};
        String domain = domains[random.nextInt(domains.length)];

        return localPart + "@" + domain;
    }
}
