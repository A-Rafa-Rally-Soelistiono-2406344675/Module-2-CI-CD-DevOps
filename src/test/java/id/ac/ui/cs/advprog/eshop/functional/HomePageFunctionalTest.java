package id.ac.ui.cs.advprog.eshop.functional;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ExtendWith(SeleniumJupiter.class)
class HomePageFunctionalTest {

    /**
     * Nomor port yang diberikan ke aplikasi saat test berjalan.
     * Nilai ini diisi otomatis pada setiap test oleh test context Spring Framework.
     */
    @LocalServerPort
    private int serverPort;

    /**
     * URL dasar untuk pengujian. Nilai default adalah {@code http://localhost}.
     */
    @Value("${app.baseUrl:http://localhost}")
    private String testBaseUrl;

    private String baseUrl;

    @BeforeEach
    void setupTest() {
        baseUrl = String.format("%s:%d", testBaseUrl, serverPort);
    }

    @Test
    void pageTitle_isCorrect(ChromeDriver driver) throws Exception {
        // Eksekusi
        driver.get(baseUrl);
        String pageTitle = driver.getTitle();

        // Verifikasi
        assertEquals("ADV Shop", pageTitle);
    }

    @Test
    void welcomeMessage_homePage_isCorrect(ChromeDriver driver) throws Exception {
        // Eksekusi
        driver.get(baseUrl);
        String welcomeMessage = driver.findElement(By.tagName("h3"))
                .getText();

        // Verifikasi
        assertEquals("Welcome", welcomeMessage);
    }
}
