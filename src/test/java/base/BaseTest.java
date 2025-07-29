package base;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import pages.BasePage;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class BaseTest {
    protected AppiumDriver driver;
    protected BasePage basePage;

    // true: Android, false: iOS
    private final boolean isAndroid = true;

    @BeforeClass
    public void setup() throws MalformedURLException {
        URL appiumServer = new URL("http://192.168.120.186:4723");

        if (isAndroid) {
            driver = new AndroidDriver(appiumServer, getUiAutomator2Options());
        } else {
            driver = new IOSDriver(appiumServer, getXCUITestOptions());
        }

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    private UiAutomator2Options getUiAutomator2Options() {
        return new UiAutomator2Options()
                .setPlatformName("Android")
                .setPlatformVersion("13")
                .setDeviceName("R58N94YZ0CD")
                .setAutomationName("UiAutomator2")
                .setAppPackage("beta.ftel.cmr.enterprise")
                .setAppActivity("vn.fpt.camera.ui.userModule.splashScreen.SplashScreenActivity")
                .setNoReset(true)
                .amend("autoGrantPermissions", true)
                .amend("unicodeKeyboard", false)
                .amend("resetKeyboard", false);
    }

    private XCUITestOptions getXCUITestOptions() {
        return new XCUITestOptions()
                .setPlatformName("iOS")
                .setPlatformVersion("16.0")
                .setDeviceName("iPhone 14 Pro")
                .setAutomationName("XCUITest")
                .setBundleId("your.ios.bundle.id");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * Chỉ gọi được nếu driver là AndroidDriver.
     * Tối ưu để đợi activity chứa partialActivity.
     */
    protected void waitUntilActivityContains(String partialActivity) {
        if (!(driver instanceof AndroidDriver androidDriver)) return;

        for (int i = 0; i < 10; i++) {
            String currentActivity = androidDriver.currentActivity();
            if (currentActivity != null && currentActivity.contains(partialActivity)) {
                return;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("[waitUntilActivityContains] Interrupted: " + e.getMessage());
                return;
            }
        }
        System.err.println("[waitUntilActivityContains] Timeout: activity doesn't match " + partialActivity);
    }
}
