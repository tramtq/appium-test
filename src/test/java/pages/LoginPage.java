package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;

public class LoginPage extends BasePage {

    public LoginPage(AndroidDriver driver) {
        super(driver);
    }

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

    public void focusInputField() {
        inputLoginField().click();
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
        } catch (Exception e) {
            return false;
        }
    }

    public String getErrorMessageText() {
        try {
            return errorMessage().getText();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isSuccessIconDisplayed() {
        try {
            return successIcon().isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}
