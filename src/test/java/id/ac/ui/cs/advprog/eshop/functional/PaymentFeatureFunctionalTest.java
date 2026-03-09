package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class PaymentFeatureFunctionalTest {

    @LocalServerPort
    private int serverPort;

    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
    }

    @Test
    void userCanOpenPaymentDetailFormPage(WebDriver driver) {
        driver.get(baseUrl + "/payment/detail");

        assertEquals("Payment Detail", driver.getTitle());
        assertTrue(driver.findElement(By.id("paymentIdInput")).isDisplayed());
        assertTrue(driver.findElement(By.id("searchPaymentButton")).isDisplayed());
    }

    @Test
    void userCanOpenPaymentAdminListPage(WebDriver driver) {
        driver.get(baseUrl + "/payment/admin/list");

        assertEquals("Payment Admin List", driver.getTitle());
        assertTrue(driver.findElement(By.id("paymentAdminTable")).isDisplayed());
    }
}
