package testcases;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;

public class LoginTest extends BaseTest {

    private LoginPage loginPage;

    @BeforeMethod
    public void setUpPage() {
        loginPage = new LoginPage(driver);
        waitUntilActivityContains("UserAccountActivity");
    }

    @Test
    public void testEmptyInput() {
        loginPage.focusInputField();
        loginPage.enterLoginInput("");
        loginPage.tapOutsideInput();
        Assert.assertTrue(loginPage.isErrorDisplayed());
        Assert.assertEquals(loginPage.getErrorMessageText(), "Vui lòng không để trống ô nhập.");
    }

    @Test
    public void testInvalidPhoneFormat() {
        loginPage.focusInputField();
        loginPage.enterLoginInput("12345");
        loginPage.tapOutsideInput();
        Assert.assertTrue(loginPage.isErrorDisplayed());
        Assert.assertEquals(loginPage.getErrorMessageText(), "Số điện thoại không đúng định dạng. Vui lòng thử lại.");
    }

    @Test
    public void testNonVietnamesePhone() {
        loginPage.focusInputField();
        loginPage.enterLoginInput("0066000000");
        loginPage.tapOutsideInput();
        Assert.assertTrue(loginPage.isErrorDisplayed());
        Assert.assertEquals(loginPage.getErrorMessageText(), "Chưa hỗ trợ số điện thoại ngoài mã vùng Việt Nam. Vui lòng thử lại.");
    }

    @Test
    public void testInvalidEmailFormat() {
        loginPage.focusInputField();
        loginPage.enterLoginInput("qtramtran1501@gmai.c");
        loginPage.tapOutsideInput();
        Assert.assertTrue(loginPage.isErrorDisplayed());
        Assert.assertEquals(loginPage.getErrorMessageText(), "Địa chỉ mail không đúng định dạng. Vui lòng thử lại.");
    }

    @Test
    public void testValidPhoneNumber() {
        loginPage.focusInputField();
        loginPage.enterLoginInput("0973488362");
        loginPage.tapOutsideInput();
        Assert.assertFalse(loginPage.isErrorDisplayed());
        Assert.assertTrue(loginPage.isSuccessIconDisplayed());
    }
}
