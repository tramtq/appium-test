package testcases;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class OpenAppTest extends BaseTest {

    @Test
    public void testAppLaunchesSuccessfully() {
        // Lấy tiêu đề của activity hiện tại
        String currentActivity = driver.currentActivity();
        System.out.println("Current Activity: " + currentActivity);

        // Kiểm tra xem Activity có đúng như kỳ vọng không
        Assert.assertTrue(currentActivity.contains("UserAccountActivity"),
                "App không mở đúng activity mong đợi!");
    }
}

