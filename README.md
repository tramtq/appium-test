
## 📚 Mục lục

1. [Yêu cầu hệ thống](#yêu-cầu-hệ-thống)
2. [Cài đặt các công cụ cần thiết](#cài-đặt-các-công-cụ-cần-thiết)
3. [Cấu trúc dự án](#cấu-trúc-dự-án)
4. [Thêm dependency vào Maven](#thêm-dependency-vào-maven)
5. [Cấu hình Appium server](#cấu-hình-appium-server)
6. [Thiết lập thiết bị Android](#thiết-lập-thiết-bị-android)
7. [Cấu hình BaseTest](#cấu-hình-basetest)
8. [Chạy test](#chạy-test)
9. [Lỗi thường gặp & cách khắc phục](#lỗi-thường-gặp--cách-khắc-phục)
10. [Gợi ý mở rộng](#gợi-ý-mở-rộng)

---

## ✅ Yêu cầu hệ thống

- Hệ điều hành: Windows 10/11 hoặc macOS
- Java JDK 11+ (nên dùng JDK 17 hoặc 21)
- Maven
- Node.js (để chạy Appium Server)
- Android Studio hoặc Android SDK
- Thiết bị thật hoặc trình giả lập (emulator)

---

## ⚙️ Cài đặt các công cụ cần thiết

### 1. Cài Java & Maven

- Cài JDK: https://adoptium.net
- Cài Maven: https://maven.apache.org

```bash
java -version
mvn -version
2. Cài Node.js và Appium
bash
Sao chép
Chỉnh sửa
npm install -g appium
Kiểm tra Appium đã cài thành công:

bash
Sao chép
Chỉnh sửa
appium -v
3. Cài Appium Inspector (GUI)
Tải tại: https://github.com/appium/appium-inspector/releases

📁 Cấu trúc dự án
bash
Sao chép
Chỉnh sửa
AppiumAndroid/
├── pom.xml
├── src/
│   └── test/
│       ├── java/
│       │   ├── base/        # Chứa BaseTest.java
│       │   ├── pages/       # Chứa các page object
│       │   └── tests/       # Chứa các file test
│       └── resources/
│           └── testng.xml   # File cấu hình test suite
📦 Thêm dependency vào Maven
Trong file pom.xml, thêm các thư viện sau:

xml
Sao chép
Chỉnh sửa
<dependencies>
    <dependency>
        <groupId>io.appium</groupId>
        <artifactId>java-client</artifactId>
        <version>8.5.1</version>
    </dependency>

    <dependency>
        <groupId>org.seleniumhq.selenium</groupId>
        <artifactId>selenium-java</artifactId>
        <version>4.12.1</version>
    </dependency>

    <dependency>
        <groupId>org.testng</groupId>
        <artifactId>testng</artifactId>
        <version>7.9.0</version>
        <scope>test</scope>
    </dependency>
</dependencies>
Sau đó chạy:

bash
Sao chép
Chỉnh sửa
mvn clean install
🧪 Cấu hình Appium server
Chạy lệnh để bật server:

bash
Sao chép
Chỉnh sửa
appium
Mặc định Appium chạy tại địa chỉ:

cpp
Sao chép
Chỉnh sửa
http://127.0.0.1:4723
Bạn có thể kiểm tra log tại terminal để xác nhận server hoạt động.

📱 Thiết lập thiết bị Android
1. Cắm thiết bị hoặc mở emulator
bash
Sao chép
Chỉnh sửa
adb devices
2. Xác định appPackage & appActivity
bash
Sao chép
Chỉnh sửa
adb shell dumpsys window windows | findstr -E 'mCurrentFocus'
Ví dụ:

appPackage: ftel.cmr.enterprise

appActivity: vn.fpt.camera.ui.main.MainActivity

🔧 Cấu hình BaseTest
Tạo file BaseTest.java trong src/test/java/base/ và thiết lập các capability như:

deviceName

platformName

automationName

appPackage

appActivity

noReset

Dùng class UiAutomator2Options để khởi tạo AndroidDriver.

▶️ Chạy test
1. Chạy qua TestNG XML
bash
Sao chép
Chỉnh sửa
mvn clean test -DsuiteXmlFile=src/test/resources/testng.xml
2. Chạy class riêng:
bash
Sao chép
Chỉnh sửa
Right click vào file test > Run
