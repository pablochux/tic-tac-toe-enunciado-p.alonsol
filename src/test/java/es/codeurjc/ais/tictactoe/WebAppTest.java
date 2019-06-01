package es.codeurjc.ais.tictactoe;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * @author Pablo Alonso Lorente 2019
 *
 */

public class WebAppTest {

	protected WebDriver webDriverPlayer1;
	protected WebDriver webDriverPlayer2;
	WebDriverWait webWaitdriver1;
	WebDriverWait webWaitdriver2;

	
	@BeforeClass
	public static void setupClass() {
		ChromeDriverManager.chromedriver().setup();
		WebApp.start();
	}
	
	@Before
	public void setupTest() {
		
		// Creación de Chrome Drivers
		webDriverPlayer1 = new ChromeDriver();
		webDriverPlayer2 = new ChromeDriver();
		webWaitdriver1 = new WebDriverWait(webDriverPlayer1,60);
		webWaitdriver2 = new WebDriverWait(webDriverPlayer2,60);

		// Simulación de que P1 entra a la partida y añade su nombre
		webDriverPlayer1.get("http://localhost:8080/");
		String namePlayer1 = "P1";
		webWaitdriver1.until(ExpectedConditions.visibilityOfElementLocated(By.id("nickname")));
		webDriverPlayer1.findElement(By.id("nickname")).sendKeys(namePlayer1);
		webWaitdriver1.until(ExpectedConditions.elementToBeClickable(By.id("startBtn")));
		webDriverPlayer1.findElement(By.id("startBtn")).click();
		
		// Simulación de que P2 entra a la partida y añade su nombre
		webDriverPlayer2.get("http://localhost:8080/");
		String namePlayer2 = "P2";
		webDriverPlayer2.findElement(By.id("nickname")).sendKeys(namePlayer2);
		webWaitdriver2.until(ExpectedConditions.elementToBeClickable(By.id("startBtn")));
		webDriverPlayer2.findElement(By.id("startBtn")).click();
		
		// A partir de aquí ya hay una partida empezada, solo falta que los jugadores eligan las posiciones del tres en raya
	}
	
	@AfterClass
	public static void teardownClass() {
		WebApp.stop();
	}
	
	@After
	public void teardown() {
		
		if (webDriverPlayer1 != null) {
			webDriverPlayer1.quit();
		}
		
		if (webDriverPlayer2 != null) {
			webDriverPlayer2.quit();
		}
	}
	
	// Test Jugador 1 Gana
	@Test
	public void Player1Wins() throws InterruptedException {

		// Se simula un juego en el que la combinación ganadora del P1 es { 6, 4, 2 };
		webDriverPlayer1.findElement(By.id("cell-6")).click();
		webDriverPlayer2.findElement(By.id("cell-1")).click();
		webDriverPlayer1.findElement(By.id("cell-4")).click();
		webDriverPlayer2.findElement(By.id("cell-5")).click();
		webDriverPlayer1.findElement(By.id("cell-2")).click();

		// Comprobación de que el texto que se muestra al terminar la partida es que el Jugador 1 ha ganado (en la sesión del Jugador 1)
		webWaitdriver1.until(ExpectedConditions.alertIsPresent());
		String browser1Message = webDriverPlayer1.switchTo().alert().getText();
		assertEquals(browser1Message, "P1 wins! P2 looses.");
		
		// Comprobación de que el texto que se muestra al terminar la partida es que el Jugador 1 ha ganado (en la sesión del Jugador 2)
		webWaitdriver2.until(ExpectedConditions.alertIsPresent());
		String browser2Message = webDriverPlayer2.switchTo().alert().getText();
		assertEquals(browser2Message, "P1 wins! P2 looses.");
	}
	
	// Test Jugador 2 Gana
	@Test
	public void Player2Wins() {
		
		// Se simula un juego en el que la combinación ganadora del P2 es { 6, 4, 2 };
		webDriverPlayer1.findElement(By.id("cell-0")).click();
		webDriverPlayer2.findElement(By.id("cell-6")).click();
		webDriverPlayer1.findElement(By.id("cell-7")).click();
		webDriverPlayer2.findElement(By.id("cell-4")).click();
		webDriverPlayer1.findElement(By.id("cell-8")).click();
		webDriverPlayer2.findElement(By.id("cell-2")).click();

		// Comprobación de que el texto que se muestra al terminar la partida es que el Jugador 2 ha ganado (en la sesión del Jugador 1)
		webWaitdriver1.until(ExpectedConditions.alertIsPresent());
		String browser1Message = webDriverPlayer1.switchTo().alert().getText();
		assertEquals(browser1Message, "P2 wins! P1 looses.");
		
		// Comprobación de que el texto que se muestra al terminar la partida es que el Jugador 2 ha ganado (en la sesión del Jugador 2)
		webWaitdriver2.until(ExpectedConditions.alertIsPresent());
		String browser2Message = webDriverPlayer2.switchTo().alert().getText();
		assertEquals(browser2Message, "P2 wins! P1 looses.");
		
	}
	
	// Test Empate
	@Test
	public void Draw() {		
		
		// Se simula un juego en el que se empata
		webDriverPlayer1.findElement(By.id("cell-2")).click();
		webDriverPlayer2.findElement(By.id("cell-4")).click();
		webDriverPlayer1.findElement(By.id("cell-7")).click();
		webDriverPlayer2.findElement(By.id("cell-8")).click();
		webDriverPlayer1.findElement(By.id("cell-0")).click();
		webDriverPlayer2.findElement(By.id("cell-6")).click();
		webDriverPlayer1.findElement(By.id("cell-5")).click();
		webDriverPlayer2.findElement(By.id("cell-1")).click();
		webDriverPlayer1.findElement(By.id("cell-3")).click();
		
		// Comprobación de que el texto que se muestra al terminar la partida es que ha habido empate (en la sesión del Jugador 2)
		webWaitdriver1.until(ExpectedConditions.alertIsPresent());
		String browser1Message = webDriverPlayer1.switchTo().alert().getText();
		assertEquals(browser1Message, "Draw!");
		
		// Comprobación de que el texto que se muestra al terminar la partida es que ha habido empate (en la sesión del Jugador 2)
		webWaitdriver2.until(ExpectedConditions.alertIsPresent());
		String browser2Message = webDriverPlayer2.switchTo().alert().getText();
		assertEquals(browser2Message, "Draw!");	
	}
}
