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
class OrderFeatureFunctionalTest {

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
    void userCanOpenOrderCreatePage(WebDriver driver) {
        driver.get(baseUrl + "/order/create");

        assertEquals("Create Order", driver.getTitle());
        assertTrue(driver.findElement(By.id("authorInput")).isDisplayed());
        assertTrue(driver.findElement(By.id("productNameInput")).isDisplayed());
        assertTrue(driver.findElement(By.id("productQuantityInput")).isDisplayed());
    }

    @Test
    void userCanOpenOrderHistoryFormPage(WebDriver driver) {
        driver.get(baseUrl + "/order/history");

        assertEquals("Order History", driver.getTitle());
        assertTrue(driver.findElement(By.id("authorInput")).isDisplayed());
        assertTrue(driver.findElement(By.id("searchHistoryButton")).isDisplayed());
    }
}
