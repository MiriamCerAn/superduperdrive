package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.io.File;
import java.time.Duration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--headless");
		options.addArguments("--headless=new");
		options.addArguments("--no-sandbox");
		options.addArguments("--disable-dev-shm-usage");
		options.addArguments("--disable-gpu");
		options.addArguments("--window-size=1920,1080");
		options.addArguments("--remote-allow-origins=*");

		String[] possiblePaths = {
				"/usr/bin/chromium-browser",
				"/usr/bin/chromium",
				"/usr/bin/google-chrome"
		};

		for (String path : possiblePaths) {
			File binary = new File(path);
			if (binary.exists()) {
				options.setBinary(binary);
				break;
			}
		}

		this.driver = new ChromeDriver(options);
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}

	@Test
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		Assertions.assertEquals("Login", driver.getTitle());
	}

	private void doMockSignUp(String firstName, String lastName, String userName, String password) {
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(5));
		driver.get("http://localhost:" + this.port + "/signup");
		webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName"))).sendKeys(firstName);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName"))).sendKeys(lastName);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername"))).sendKeys(userName);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword"))).sendKeys(password);

		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("buttonSignUp"))).click();
	}

	private void doLogIn(String userName, String password) {
		driver.get("http://localhost:" + this.port + "/login");
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(5));

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername"))).sendKeys(userName);
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword"))).sendKeys(password);
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.id("login-button"))).click();

		webDriverWait.until(ExpectedConditions.titleContains("Home"));
	}

	private void navigateToHomeAndClickLink() {
		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(5));
		webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success-link"))).click();
	}

	@Test
	public void testRedirection() {
		doMockSignUp("Redirection", "Test", "RT", "123");
		Assertions.assertTrue(driver.getCurrentUrl().contains("/login"));
	}

	@Test
	public void testBadUrl() {
		doMockSignUp("URL", "Test", "UT", "123");
		doLogIn("UT", "123");

		driver.get("http://localhost:" + this.port + "/some-random-page");
		Assertions.assertFalse(driver.getPageSource().contains("Whitelabel Error Page"));
	}

	@Test
	public void testLargeUpload() {
		doMockSignUp("Large File", "Test", "LFT", "123");
		doLogIn("LFT", "123");

		WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(5));
		String fileName = "upload5m.zip";

		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("fileUpload")));
		WebElement fileSelectButton = driver.findElement(By.id("fileUpload"));
		fileSelectButton.sendKeys(new File(fileName).getAbsolutePath());

		WebElement uploadButton = driver.findElement(By.id("uploadButton"));
		uploadButton.click();
		try {
			webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("success")));
		} catch (org.openqa.selenium.TimeoutException e) {
			System.out.println("Large File upload failed");
		}
		Assertions.assertFalse(driver.getPageSource().contains("HTTP Status 403 – Forbidden"));
	}

	@Test
	public void testNoteCrudOperations() {
		doMockSignUp("Note", "Tester", "noteUser", "123");
		doLogIn("noteUser", "123");

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

		wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab"))).click();

		wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Add a New Note')]")))
				.click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title"))).sendKeys("Test Title");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")))
				.sendKeys("Test Description");
		driver.findElement(By.xpath("//div[@id='noteModal']//button[contains(text(),'Save changes')]")).click();

		navigateToHomeAndClickLink();

		wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab"))).click();
		Assertions.assertTrue(driver.getPageSource().contains("Test Title"));
		Assertions.assertTrue(driver.getPageSource().contains("Test Description"));

		wait.until(ExpectedConditions
				.elementToBeClickable(By.xpath("//table[@id='userTable']//button[contains(text(),'Edit')]"))).click();
		WebElement titleInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
		titleInput.clear();
		titleInput.sendKeys("Updated Title");
		driver.findElement(By.xpath("//div[@id='noteModal']//button[contains(text(),'Save changes')]")).click();

		navigateToHomeAndClickLink();

		wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab"))).click();
		Assertions.assertTrue(driver.getPageSource().contains("Updated Title"));

		wait.until(ExpectedConditions
				.elementToBeClickable(By.xpath("//table[@id='userTable']//a[contains(text(),'Delete')]"))).click();
		navigateToHomeAndClickLink();

		wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-notes-tab"))).click();
		Assertions.assertFalse(driver.getPageSource().contains("Updated Title"));
	}

	@Test
	public void testCredentialCrudOperations() {
		doMockSignUp("Cred", "Tester", "credUser", "123");
		doLogIn("credUser", "123");

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

		wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab"))).click();

		wait.until(
				ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Add a New Credential')]")))
				.click();
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")))
				.sendKeys("http://example.com");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username"))).sendKeys("myUser");
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password"))).sendKeys("mySecret123");
		driver.findElement(By.xpath("//div[@id='credentialModal']//button[contains(text(),'Save changes')]")).click();

		navigateToHomeAndClickLink();

		wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab"))).click();
		Assertions.assertTrue(driver.getPageSource().contains("http://example.com"));
		Assertions.assertFalse(driver.getPageSource().contains("mySecret123"));

		wait.until(ExpectedConditions
				.elementToBeClickable(By.xpath("//table[@id='credentialTable']//button[contains(text(),'Edit')]")))
				.click();
		WebElement passInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
		Assertions.assertEquals("mySecret123", passInput.getAttribute("value"));

		driver.findElement(By.xpath("//div[@id='credentialModal']//button[contains(text(),'Close')]")).click();

		wait.until(ExpectedConditions
				.elementToBeClickable(By.xpath("//table[@id='credentialTable']//a[contains(text(),'Delete')]")))
				.click();
		navigateToHomeAndClickLink();

		wait.until(ExpectedConditions.elementToBeClickable(By.id("nav-credentials-tab"))).click();
		Assertions.assertFalse(driver.getPageSource().contains("http://example.com"));
	}
}