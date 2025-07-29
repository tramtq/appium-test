package testcases;

import base.BaseTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.PhoneValidator;
import utils.SoftAssertPlus;
import utils.DataGenerator;

import java.io.IOException;
import java.lang.reflect.Method;

public class LoginTest extends BaseTest {

    private LoginPage loginPage;
    private SoftAssertPlus softAssert;

    @BeforeMethod
    public void setUp(Method method) {
        loginPage = new LoginPage(driver);
        waitUntilActivityContains("UserAccountActivity");

        // Tạo softAssert và gắn thông tin testcase
        softAssert = new SoftAssertPlus(driver);
        softAssert.setTestCaseInfo(this.getClass().getSimpleName(), method.getName());
    }

    @Test
    public void testPlaceholder() {
        String actualPlaceholder = loginPage.getPlaceholderText();
        softAssert.assertEquals(
                actualPlaceholder,
                "Nhập số điện thoại hoặc email",
                "❌ Sai nội dung placeholder",
                "✅ Placeholder hiển thị đúng"
        );

        softAssert.assertAllWithLog();
    }

    @Test
    public void testClickInputField() {
        loginPage.focusInputField();
        boolean isKeyboardVisible = loginPage.isKeyboardVisible();
        softAssert.assertTrue(
                isKeyboardVisible,
                "❌ Bàn phím không hiển thị sau khi focus input",
                "✅ Bàn phím đã hiển thị khi focus input"
        );
        softAssert.assertAllWithLog();
    }

    @Test
    public void testEmptyInput() {
        loginPage.focusInputField();
        loginPage.enterLoginInput("");
        loginPage.tapOutsideInput();

        boolean errorDisplayed = loginPage.isErrorDisplayed();
        softAssert.assertTrue(
                errorDisplayed,
                "❌ Không hiển thị text lỗi",
                "✅ Có hiển thị text lỗi"
        );

        if (errorDisplayed) {
            String actualError = loginPage.getErrorMessageText();
            softAssert.assertEquals(
                    actualError,
                    "Vui lòng không để trống ô nhập.",
                    "❌ Sai nội dung text lỗi",
                    "✅ Nội dung lỗi hiển thị đúng"
            );
        }

        boolean isButtonEnabled = loginPage.isContinueButtonEnabled();
        softAssert.assertFalse(
                isButtonEnabled,
                "❌ Nút Tiếp tục enable",
                "✅ Nút Tiếp tục disable"
        );

        softAssert.assertAllWithLog();
    }

    @Test
    public void testInvalidPhoneFormat() {
        String phone = DataGenerator.generateInvalidPhoneNumber();
        softAssert.logInputData(phone);
        loginPage.focusInputField();
        loginPage.enterLoginInput(phone);
        loginPage.tapOutsideInput();

        boolean errorDisplayed = loginPage.isErrorDisplayed();
        softAssert.assertTrue(
                errorDisplayed,
                "❌ Không hiển thị text lỗi",
                "✅ Có hiển thị text lỗi"
        );

        String actualError = loginPage.getErrorMessageText();
        softAssert.assertEquals(
                actualError,
                "Số điện thoại không đúng định dạng. Vui lòng thử lại.",
                "❌ Sai nội dung text lỗi",
                "✅ Nội dung lỗi đúng"
        );

        boolean isButtonEnabled = loginPage.isContinueButtonEnabled();
        softAssert.assertFalse(
                isButtonEnabled,
                "❌ Nút Tiếp tục enable",
                "✅ Nút Tiếp tục bị disable"
        );

        softAssert.assertAllWithLog();
    }

    @Test
    public void testNonVietnamesePhone() {
        String phone2 = DataGenerator.generateForeignPhoneNumberWithoutPlus();
        softAssert.logInputData(phone2);

        loginPage.focusInputField();
        loginPage.enterLoginInput(phone2);
        loginPage.tapOutsideInput();

        boolean errorDisplayed = loginPage.isErrorDisplayed();
        softAssert.assertTrue(
                errorDisplayed,
                "❌ Không hiển thị text lỗi",
                "✅ Có hiển thị text lỗi"
        );

        String actualError = loginPage.getErrorMessageText();
        softAssert.assertEquals(
                actualError,
                "Chưa hỗ trợ số điện thoại ngoài mã vùng Việt Nam. Vui lòng thử lại.",
                "❌ Sai nội dung text lỗi",
                "✅ Nội dung lỗi đúng"
        );

        boolean isButtonEnabled = loginPage.isContinueButtonEnabled();
        softAssert.assertFalse(
                isButtonEnabled,
                "❌ Nút Tiếp tục được enable",
                "✅ Nút Tiếp tục bị disable"
        );

        softAssert.assertAllWithLog();
    }

    @Test
    public void testInvalidEmailFormat() {
        String invalidEmail = DataGenerator.generateInvalidEmail();
        softAssert.logInputData(invalidEmail);

        loginPage.focusInputField();
        loginPage.enterLoginInput(invalidEmail);
        loginPage.tapOutsideInput();

        boolean errorDisplayed = loginPage.isErrorDisplayed();
        softAssert.assertTrue(
                errorDisplayed,
                "❌ Không hiển thị text lỗi",
                "✅ Có hiển thị text lỗi"
        );

        String actualError = loginPage.getErrorMessageText();
        softAssert.assertEquals(
                actualError,
                "Địa chỉ mail không đúng định dạng. Vui lòng thử lại.",
                "❌ Sai nội dung text lỗi",
                "✅ Nội dung lỗi đúng"
        );
        boolean isButtonEnabled = loginPage.isContinueButtonEnabled();
        softAssert.assertFalse(
                isButtonEnabled,
                "❌ Nút Tiếp tục được enable",
                "✅ Nút Tiếp tục bị disable"
        );

        softAssert.assertAllWithLog();
    }

    @Test
    public void testValidPhone() throws IOException {
        String validPhone = DataGenerator.generateValidPhoneNumber();
        softAssert.logInputData(validPhone);

        loginPage.focusInputField();
        loginPage.enterLoginInput(validPhone);
        loginPage.tapOutsideInput();

        boolean errorDisplayed = loginPage.isErrorDisplayed();
        softAssert.assertFalse(
                errorDisplayed,
                "❌ Có hiển thị lỗi",
                "✅ Không hiển thị lỗi"
        );

        boolean isIconShown = loginPage.isSuccessIconDisplayed();
        softAssert.assertTrue(
                isIconShown,
                "❌ Không hiển thị icon validate",
                "✅ Hiển thị icon validate"
        );
        boolean isButtonEnabled = loginPage.isContinueButtonEnabled();
        softAssert.assertFalse(
                isButtonEnabled,
                "❌ Nút Tiếp tục bị disabled",
                "✅ Nút Tiếp tục enabled"
        );

        boolean exists = PhoneValidator.isPhoneNumberRegistered(validPhone);
        if (exists) {
            System.out.println("📱 Tài khoản đã tồn tại");
        } else {
            System.out.println("🆕 Tài khoản chưa được đăng ký");
        }

        softAssert.assertAllWithLog();
    }

    @Test
    public void testValidEmail() {
        String validEmail = DataGenerator.generateValidEmail();
        softAssert.logInputData(validEmail);

        loginPage.focusInputField();
        loginPage.enterLoginInput(validEmail);
        loginPage.tapOutsideInput();

        boolean errorDisplayed = loginPage.isErrorDisplayed();
        softAssert.assertFalse(
                errorDisplayed,
                "❌ Có hiển thị lỗi",
                "✅ Không hiển thị lỗi"
        );

        boolean isIconShown = loginPage.isSuccessIconDisplayed();
        softAssert.assertTrue(
                isIconShown,
                "❌ Không hiển thị icon validate",
                "✅ Hiển thị icon validate"
        );
        boolean isButtonEnabled = loginPage.isContinueButtonEnabled();
        softAssert.assertTrue(
                isButtonEnabled,
                "❌ Nút Tiếp tục bị disabled",
                "✅ Nút Tiếp tục enabled"
        );

        softAssert.assertAllWithLog();
    }
}
