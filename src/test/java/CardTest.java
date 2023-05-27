import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

class CardTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldTest() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys(("Алексеев Михаил"));
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys(("+79266565566"));
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button")).click();
        String expected = "  Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.";
        String actual = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void whenNameLatin() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Alekseev");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79266565566");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button")).click();
        String expected = "Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void whenNameNull() {
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79266565566");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button")).click();
        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void whenPhoneOverLimit() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Алексеев Михаил");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+792665655667");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void whenPhoneUnderLimit() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Алексеев Михаил");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+7926656556");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button")).click();
        String expected = "Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.";
        String actual = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void EmptyForm() {
        driver.get("http://localhost:9999/");
        driver.findElement(By.className("button__text")).click();
        String expected = "Поле обязательно для заполнения";
        String actual = driver.findElement(By.className("input__sub")).getText();
        Assertions.assertEquals(expected, actual);
    }
}
