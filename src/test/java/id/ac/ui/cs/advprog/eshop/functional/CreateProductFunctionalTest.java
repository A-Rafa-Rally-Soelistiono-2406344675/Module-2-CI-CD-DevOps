package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class CreateProductFunctionalTest {

    @LocalServerPort
    private int port;

    @Test
    void userCanCreateProductAndSeeInProductList(WebDriver driver) {
        String productName = "Sampo Cap Selenium";

        driver.get("http://localhost:" + port + "/product/create");

        driver.findElement(By.id("nameInput")).sendKeys(productName);
        driver.findElement(By.id("quantityInput")).sendKeys("10");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        List<WebElement> nameCells = driver.findElements(By.xpath("//table//tr/td[1]"));
        boolean found = nameCells.stream().anyMatch(cell -> productName.equals(cell.getText()));
        assertTrue(found);
    }
}
