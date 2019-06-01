package es.codeurjc.ais.tictactoe;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import es.codeurjc.ais.tictactoe.*;

/**
 * @author Pablo Alonso Lorente 2019
 *
 */

public class boardTest {

	private Board board;


	@Before
	public void setUp() throws Exception {
		board = new Board();
	}

	// Test Jugador 1 Gana
	@Test
	public void testPlayer1Wins() {
		int [] winnerPositionValues = { 6, 4, 2 };
		int [] player1PositionValues;
		
		board.getCell(6).value = "X";
		board.getCell(0).value = "O";
		board.getCell(4).value = "X";
		board.getCell(1).value = "O";
		board.getCell(2).value = "X";

		player1PositionValues = board.getCellsIfWinner("X");

		// Comparación entre los valores que hay en player1PositionValues y winnerPositionValues (si coinciden, es que Jugador 1 ha ganado)		
		for (int i = 0; i <= 2; i++) {
			assertEquals(winnerPositionValues[i], player1PositionValues[i]);
		}
		
	}
	
	// Test Jugador 2 Gana
	@Test
	public void testPlayer2Wins() {
		int [] winnerPositionValues = { 6, 4, 2 };
		int [] player2PositionValues;
		
		board.getCell(0).value = "X";
		board.getCell(6).value = "O";
		board.getCell(1).value = "X";
		board.getCell(4).value = "O";
		board.getCell(3).value = "X";
		board.getCell(2).value = "O";

		player2PositionValues = board.getCellsIfWinner("O");

		// Comparación entre los valores que hay en player2PositionValues y winnerPositionValues (si coinciden, es que Jugador 2 ha ganado )		
		for (int i = 0; i <= 2; i++) {
			assertEquals(winnerPositionValues[i], player2PositionValues[i]);
		}
	}
	
	// Test Empate
	@Test
	public void testDraw() {
		
		// Creación de un estado en el que se produzca un empate
		board.getCell(0).value = "X";
		board.getCell(6).value = "O";
		board.getCell(1).value = "X";
		board.getCell(4).value = "O";
		board.getCell(3).value = "X";
		board.getCell(5).value = "O";
		board.getCell(2).value = "X";
		board.getCell(7).value = "O";
		board.getCell(8).value = "X";
		
		// Comprobación de que en el estado actual hay un empate
		assertTrue(board.checkDraw());
	}

}
