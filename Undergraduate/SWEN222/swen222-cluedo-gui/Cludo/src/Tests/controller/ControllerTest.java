package tests.controller;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import model.PlayerCharacter;

import org.junit.Before;
import org.junit.Test;

import controller.Controller;

public class ControllerTest {

	Controller controller;

	/**
	 * Create a new controller before all the tests
	 */
	@Before
	public void setUpController() {
		controller = new Controller();
	}

	/**
	 * Make sure that if you try set the number of players
	 * to an invalid number an exception is thrown
	 */
	@Test
	public void testNumPlayersInvalid() {
		try {
			controller.setNumberOfPlayers(1);
			fail();
		} catch(IllegalArgumentException e) {

		}
	}

	/**
	 * Make sure no exception is thrown if we set
	 * the number of players to a valid number
	 */
	@Test
	public void testNumPlayersValid() {
		try {
			controller.setNumberOfPlayers(3);
		} catch (IllegalArgumentException e) {
			fail();
		}
	}

	/**
	 * Try set up a controller without adding enough
	 * players, should throw an exception
	 */
	@Test
	public void testInitInvalid() {
		controller.setNumberOfPlayers(3);
		controller.addPlayerCharacter(PlayerCharacter.COLONEL_MUSTARD, null);

		try {
			controller.init();
			fail();
		} catch (IllegalStateException e) {

		}
	}

	/**
	 * Make sure that setting up a controller when
	 * all the players have been added doesn't throw an error
	 */
	@Test
	public void testInitValid() {
		controller.setNumberOfPlayers(3);
		controller.addPlayerCharacter(PlayerCharacter.COLONEL_MUSTARD, null);
		controller.addPlayerCharacter(PlayerCharacter.MRS_PEACOCK, null);
		controller.addPlayerCharacter(PlayerCharacter.THE_REVEREND_GREEN, null);

		try {
			controller.init();
		} catch (IllegalStateException e) {
			fail();
		}
	}

	@Test
	public void testGetAvailableCharacters() {
		// Get a list of all the player characters
		List<PlayerCharacter> characters = new ArrayList<>();
		for (PlayerCharacter pc : PlayerCharacter.values()) {
			characters.add(pc);
		}

		controller.setNumberOfPlayers(3);
		controller.addPlayerCharacter(PlayerCharacter.COLONEL_MUSTARD, null);

		characters.remove(PlayerCharacter.COLONEL_MUSTARD);

		for (PlayerCharacter pc : controller.getAvailableCharacters()) {
			assertTrue(characters.contains(pc));
		}
	}

}
