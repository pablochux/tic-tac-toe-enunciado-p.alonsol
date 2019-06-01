package es.codeurjc.ais.tictactoe;
import es.codeurjc.ais.tictactoe.*;
import es.codeurjc.ais.tictactoe.TicTacToeGame.EventType;
import es.codeurjc.ais.tictactoe.TicTacToeGame.WinnerValue;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.ChromeDriverManager;

/**
 * @author Pablo Alonso Lorente 2019
 *
 */

public class TicTacToeGameTest {
	
	public TicTacToeGame ticTacToeGame;
	public Connection connectionPlayer1;
	public Connection connectionPlayer2;
	public Player player1;
	public Player player2;
	private List<Player> players = new CopyOnWriteArrayList<>();;
	
	@Before
	public void SetUp() {
		
		// Inicialización de los tests
		ticTacToeGame = new TicTacToeGame();
		
		connectionPlayer1 = mock(Connection.class);
		connectionPlayer2 = mock(Connection.class);
		
		ticTacToeGame.addConnection(connectionPlayer1);
		ticTacToeGame.addConnection(connectionPlayer2);
		
		
		player1 = new Player(1, "X", "Player1" );
		player2 = new Player(2, "O", "Player2");
		
		// Añadir al jugador 1 a la partida
		ticTacToeGame.addPlayer(player1);
		players.add(player1);
		verify(connectionPlayer1).sendEvent(EventType.JOIN_GAME, players);
		verify(connectionPlayer2).sendEvent(EventType.JOIN_GAME, players);
		
		reset(connectionPlayer1);
		reset(connectionPlayer2);
		
		ticTacToeGame.addPlayer(player2);
		players.add(player2);
		verify(connectionPlayer1).sendEvent(EventType.JOIN_GAME, players);
		verify(connectionPlayer2).sendEvent(EventType.JOIN_GAME, players);
		
		verify(connectionPlayer1).sendEvent(EventType.SET_TURN, player1);
		verify(connectionPlayer2).sendEvent(EventType.SET_TURN, player1);
	
	}

	// Test Jugador 1 Gana
	@Test 
	public void Player1Wins () {

		// Simulación de partida en la que gana Player 2
		ticTacToeGame.mark(6);
		ticTacToeGame.mark(0);
		ticTacToeGame.mark(4);
		ticTacToeGame.mark(1);
		ticTacToeGame.mark(2);
		
		// Comprobación de que se han recibido los eventos SET_TURN para cada jugador
		verify(connectionPlayer1, times(3)).sendEvent(EventType.SET_TURN, player1);
		verify(connectionPlayer1, times(2)).sendEvent(EventType.SET_TURN, player2);
		verify(connectionPlayer2, times(3)).sendEvent(EventType.SET_TURN, player1);
		verify(connectionPlayer2, times(2)).sendEvent(EventType.SET_TURN, player2);
		
		// Comprobación de que ha ganado el Player 1
		ArgumentCaptor<WinnerValue> argument = ArgumentCaptor.forClass(WinnerValue.class);
	    verify(connectionPlayer1).sendEvent(eq(EventType.GAME_OVER), argument.capture());
	    assertEquals(argument.getValue().player, player1);		
		
	}

	// Test Jugador 2 Gana
	@Test 
	public void Player2Wins () {
		
		// Simulación de partida en la que gana Player 2
		ticTacToeGame.mark(0);
		ticTacToeGame.mark(6);
		ticTacToeGame.mark(1);
		ticTacToeGame.mark(4);
		ticTacToeGame.mark(3);
		ticTacToeGame.mark(2);
		
		// Comprobación de que se han recibido los eventos SET_TURN para cada jugador
		verify(connectionPlayer1, times(3)).sendEvent(EventType.SET_TURN, player1);
		verify(connectionPlayer1, times(3)).sendEvent(EventType.SET_TURN, player2);
		verify(connectionPlayer2, times(3)).sendEvent(EventType.SET_TURN, player1);
		verify(connectionPlayer2, times(3)).sendEvent(EventType.SET_TURN, player2);
		
		// Comprobación de que ha ganado el Player 2
		ArgumentCaptor<WinnerValue> argument = ArgumentCaptor.forClass(WinnerValue.class);
	    verify(connectionPlayer1).sendEvent(eq(EventType.GAME_OVER), argument.capture());
	    assertEquals(argument.getValue().player, player2);		
		
	}
	
	// Test Empate
	@Test 
	public void Draw() {
		
		// Simulación de partida con empate
		ticTacToeGame.mark(6);
		ticTacToeGame.mark(2);
		ticTacToeGame.mark(4);
		ticTacToeGame.mark(0);
		ticTacToeGame.mark(1);
		ticTacToeGame.mark(3);
		ticTacToeGame.mark(8);
		ticTacToeGame.mark(7);
		ticTacToeGame.mark(5);
		
		// Comprobación de que se han recibido los eventos SET_TURN para cada jugador
		verify(connectionPlayer1, times(5)).sendEvent(EventType.SET_TURN, player1);
		verify(connectionPlayer1, times(4)).sendEvent(EventType.SET_TURN, player2);
		verify(connectionPlayer2, times(5)).sendEvent(EventType.SET_TURN, player1);
		verify(connectionPlayer2, times(4)).sendEvent(EventType.SET_TURN, player2);
		
		// Comprobación de que ha sido un empate
		ArgumentCaptor<WinnerValue> argument = ArgumentCaptor.forClass(WinnerValue.class);
	    verify(connectionPlayer1).sendEvent(eq(EventType.GAME_OVER), argument.capture());
	    assertNull(argument.getValue());		
		
	}
}
