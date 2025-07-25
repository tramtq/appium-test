package base;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import pages.BasePage;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class BaseTest {
    protected AndroidDriver driver;
    protected BasePage basePage;

    @BeforeClass
    public void setup() throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options();
        options.setPlatformName("Android");
        options.setPlatformVersion("13");
        options.setDeviceName("R58N94YZ0CD");
        options.setAutomationName("UiAutomator2");
        options.setAppPackage("beta.ftel.cmr.enterprise");
        options.setAppActivity("vn.fpt.camera.ui.userModule.splashScreen.SplashScreenActivity");
        options.setNoReset(true);
        options.setCapability("autoGrantPermissions", true);


        // Tắt Unicode Keyboard để giữ bàn phím thật
        options.setCapability("unicodeKeyboard", false);
        options.setCapability("resetKeyboard", false);


        URL appiumServer = new URL("http://127.0.0.1:4723");
        driver = new AndroidDriver(appiumServer, options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    protected void waitUntilActivityContains(String partialActivity) {
        for (int i = 0; i < 10; i++) {
            String currentActivity = driver.currentActivity();
            if (currentActivity != null && currentActivity.contains(partialActivity)) {
                return;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected void tapByCoordinates(int x, int y) {
        new io.appium.java_client.TouchAction(driver)
                .tap(new io.appium.java_client.touch.offset.PointOption().withCoordinates(x, y))
                .perform();
    }
}