package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.NoSuchElementException;

public class LoginPage extends BasePage {
    protected AppiumDriver driver;

    public LoginPage(AppiumDriver driver) {
        super(driver);
        this.driver = driver;
    }

    // Locators
    private WebElement inputLoginField() {
        return driver.findElement(AppiumBy.id("beta.ftel.cmr.enterprise:id/edt_input"));
    }

    private WebElement continueButton() {
        return driver.findElement(AppiumBy.id("beta.ftel.cmr.enterprise:id/button"));
    }

    private WebElement errorMessage() {
        return driver.findElement(AppiumBy.id("beta.ftel.cmr.enterprise:id/tv_error"));
    }

    private WebElement successIcon() {
        return driver.findElement(AppiumBy.id("beta.ftel.cmr.enterprise:id/iv_suffix_end"));
    }

    // Actions
    public String getPlaceholderText() {
        return inputLoginField().getAttribute("hint");
    }

    public void focusInputField() {
        inputLoginField().click();
    }

    public boolean isKeyboardVisible() {
        // Chỉ Android mới hỗ trợ kiểm tra bàn phím
        if (driver instanceof AndroidDriver) {
            return ((AndroidDriver) driver).isKeyboardShown();
        }
        return false; // iOS không hỗ trợ
    }

    public void enterLoginInput(String input) {
        WebElement inputField = inputLoginField();
        inputField.clear();
        inputField.sendKeys(input);
    }

    public void tapContinue() {
        continueButton().click();
    }

    public boolean isErrorDisplayed() {
        try {
            return errorMessage().isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public String getErrorMessageText() {
        try {
            return errorMessage().getText();
        } catch (NoSuchElementException e) {
            return "";
        }
    }

    public boolean isSuccessIconDisplayed() {
        try {
            return successIcon().isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isContinueButtonEnabled() {
        return continueButton().isEnabled();
    }
}
