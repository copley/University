import org.junit.*;

import static org.junit.Assert.*;

import java.util.*;

import swen221.monopoly.*;
import swen221.monopoly.GameOfMonopoly.InvalidMove;

public class MonopolyTests {
	// this is where you must write your tests; do not alter the package, or the
    // name of this file.  An example test is provided for you.

	private GameOfMonopoly game;
	private Map<String, Player> players;
	
	@Before
	// Handles creating the board before a test
	public void initBoard() {
		game = new GameOfMonopoly();
		players = new HashMap<String, Player>();
	}
	
	/////////////////////////////
	// Testing Property Buying //
	/////////////////////////////
	
	@Test
	// Should pass as have enough money and can buy
	// location
	public void testValidBuyProperty_1() {
		try {
			tryAndBuy("Dave", "Dog", 1500, "Park Lane");
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	// Should fail as can not buy "Free Parking"
	public void testInvalidBuyProperty_1() {
		try {
			tryAndBuy("Dave", "Dog", 1000, "Free Parking");
			fail("Player cant buy 'Free Parking'");
		} catch (GameOfMonopoly.InvalidMove e) {
			
		}
	}
	
	@Test
	// Should fail as a locaton can only have
	// one owner
	public void testInvalidBuyProperty_2() {
		try {
			tryAndBuy("Dave", "Dog", 1000, "Pall Mall");
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
		
		try {
			tryAndBuy("Mike", "car", 1000, "Pall Mall");
			fail("Shouldent be able to buy as allready has an owner");
		} catch (GameOfMonopoly.InvalidMove e) {
			
		}
	}
	
	@Test
	// Should fail as player dosnt have enough money
	public void testInvalidBuyProperty_3() {
		try {
			tryAndBuy("Dave", "Dog", 0, "Park Lane");
			fail("Player cant buy 'Free Parking'");
		} catch (GameOfMonopoly.InvalidMove e) {
			
		}
	}
	
	/////////////////////////////////
	// End Testing Property Buying //
	/////////////////////////////////
	
	//////////////////////////////
	// Testing Property Selling //
	//////////////////////////////
	
	@Test
	// Should pass as the player owns the property
	// and its not morgaged
	public void testValidSellProperty_1() {
		try {
			tryAndBuy("Dave", "Dog", 1000, "Pall Mall");
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
		
		try {
			tryAndSell("Dave", "Dog", 1000, "Pall Mall");
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	// Should fail as the player is trying to sell something
	// that isnt a property
	public void testInvalidSellProperty_1() {
		try {
			tryAndSell("Dave", "Dog", 1000, "Go");
			fail("Cant sell 'GO' its not a property");
		} catch (GameOfMonopoly.InvalidMove e) {
			
		}
	}
	
	@Test
	// Should fail as they dont own the property
	public void testInvalidSellProperty_2() {
		try {
			tryAndSell("Dave", "Dog", 1000, "Pall Mall");
			fail("Shouldent be able to sell something that isnt yours");
		} catch (GameOfMonopoly.InvalidMove e) {
			
		}
	}
	
	@Test
	// Should fail as player is trying to sell a morgaged
	// property
	public void testInvalidSellProperty_3() {
		try {
			tryAndBuy("Dave", "Dog", 1000, "Pall Mall");
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
		
		try {
			tryAndMorgage("Dave", "Dog", 1000, "Pall Mall");
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
		
		try {
			tryAndSell("Dave", "Dog", 1000, "Pall Mall");
			fail("Shouldent be able to sell a property that is morgaged");
		} catch (GameOfMonopoly.InvalidMove e) {
			
		}
	}
	
	//////////////////////////////////
	// End Testing Property Selling //
	//////////////////////////////////

	//////////////////////////////
	// Testing Property Mortgage //
	//////////////////////////////
	
	@Test
	public void testValidMortgage_1() {
		try {
			tryAndBuy("Dave", "Dog", 1000, "Pall Mall");
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
		
		try {
			tryAndMorgage("Dave", "Dog", 1000, "Pall Mall");
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	// Should fail as cant morgage 'free parking'
	public void testInvalidMortgage_1() {
		try {
			tryAndMorgage("Dave", "Dog", 1000, "Free Parking");
			fail("Shouldent be able to morgage non location places");
		} catch (GameOfMonopoly.InvalidMove e) {
			
		}
	}
	
	@Test
	// Should fail as you cant mortgage a place you dont own
	public void testInvalidMortgage_2() {
		try {
			tryAndMorgage("Dave", "Dog", 1000, "Pall Mall");
			fail("Shouldent be able to mortgage a place you dont own");
		} catch (GameOfMonopoly.InvalidMove e) {
			
		}
	}
	
	@Test
	// Should fail as you cant mortgage a property twice
	public void testInvalidMortgage_3() {
		try {
			tryAndBuy("Dave", "Dog", 1000, "Pall Mall");
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
		
		try {
			tryAndMorgage("Dave", "Dog", 1000, "Pall Mall");
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
		
		try {
			tryAndMorgage("Dave", "Dog", 1000, "Pall Mall");
			fail("Cant mortgage a property twice");
		} catch (GameOfMonopoly.InvalidMove e) {
			
		}
	}
	
	///////////////////////////////////
	// End Testing Property Mortgage //
	///////////////////////////////////

	/////////////////////////////////
	// Testing Property UnMortgage //
	/////////////////////////////////

	@Test
	public void testValidUnMortgage_1() {
		try {
			tryAndBuy("Dave", "Dog", 1000, "Pall Mall");
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
		
		try {
			tryAndMorgage("Dave", "Dog", 1000, "Pall Mall");
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
		
		try {
			tryAndUnMorgage("Dave", "Dog", 1000, "Pall Mall");
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	// Should fail as you cant un mortgage something that isnt
	// a location
	public void testInvalidUnMortgage_1() {
		try {
			tryAndUnMorgage("Dave", "Dog", 1000, "Go");
			fail("Shouldent be able to unmortgage something that isnt a location");
		} catch (GameOfMonopoly.InvalidMove e) {
			
		}
	}
	
	@Test
	// Should fail as you cant unmortage something you dont own
	public void testInvalidUnMortgage_2() {
		try {
			tryAndUnMorgage("Dave", "Dog", 1000, "Pall Mall");
			fail("Shouldent be able to un mortgage something you dont own");
		} catch (GameOfMonopoly.InvalidMove e) {
			
		}
	}
	
	@Test
	// Should fail as you cant un mortgage something that isnt mortgaged
	public void testInvalidUnMortgage_3() {
		try {
			tryAndBuy("Dave", "Dog", 1000, "Pall Mall");
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
		
		try {
			tryAndUnMorgage("Dave", "Dog", 1000, "Pall Mall");
			fail("Shouldent be able to mortgage something that isnt mortgaged");
		} catch (GameOfMonopoly.InvalidMove e) {
			
		}
	}
	
	@Test
	public void testInvalidUnMortgage_4() {
		try {
			tryAndBuy("Dave", "Dog", 120, "Pentonville");
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
		
		try {
			tryAndMorgage("Dave", "Dog", 0, "Pentonville");
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
		
		players.get("Dave").deduct(60);
		
		try {
			tryAndUnMorgage("Dave", "Dog", 0, "Pentonville");
			fail("Shouldent be able unmorgage if not enough money");
		} catch (GameOfMonopoly.InvalidMove e) {
			
		}
	}
	
	/////////////////////////////////////
	// End Testing Property UnMortgage //
	/////////////////////////////////////
	
	/////////////////////////////////////
	// Start Testing Station Functions //
	/////////////////////////////////////
	
	@Test
	public void testStationGetRent() {
		try {
			tryAndBuy("Dave", "Dog", 1200, "Kings Cross Station");
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
		
		Property s = (Property) game.getBoard().findLocation("Kings Cross Station");
		
		assertEquals(s.getRent(), 50);
	}
	
	///////////////////////////////////
	// End Testing Station Functions //
	///////////////////////////////////
	
	////////////////////////////////
	// Start Testing Special Area //
	////////////////////////////////
	
	@Test
	public void testSpecialAreaHasOwner() {
		SpecialArea a = (SpecialArea) game.getBoard().findLocation("Go");
		
		assertFalse(a.hasOwner());
	}
	
	@Test
	public void testSpecialAreaGetOwner() {
		SpecialArea a = (SpecialArea) game.getBoard().findLocation("Go");
		
		try {
			a.getOwner();
			fail();
		} catch (RuntimeException e) {
			
		}
	}
	
	@Test
	public void testSpecialAreaGetRent() {
		SpecialArea a = (SpecialArea) game.getBoard().findLocation("Go");
		
		try {
			a.getRent();
			fail();
		} catch (RuntimeException e) {
			
		}
	}
	
	//////////////////////////////
	// End Testing Special Area //
	//////////////////////////////
	
	// Start Testing Untility //
	
	@Test
	public void testUtilityGetRent() {
		try {
			tryAndBuy("Dave", "Dog", 150, "Water Works");
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
		
		Utility a = (Utility) game.getBoard().findLocation("Water Works");
		
		assertTrue(a.getRent() == 75);
	}
	
	// End Testing Utility //
	
	// Start Testing Street //

	@Test
	public void testStreetGetColourGroup() {
		Street s = (Street) game.getBoard().findLocation("Mayfair");
		assertEquals(s.getColourGroup().getColour(), "Blue");
	}
	
	@Test
	public void testStreetGetRent() {
		Street s = (Street) game.getBoard().findLocation("Mayfair");
		assertEquals(s.getRent(), 50);
	}
	
	@Test
	public void testStreetGetHouses() {
		Street s = (Street) game.getBoard().findLocation("Mayfair");
		assertEquals(s.getHouses(), 0);
	}
	
	@Test
	public void testStreetGetHotels() {
		Street s = (Street) game.getBoard().findLocation("Mayfair");
		assertEquals(s.getHotels(), 0);
	}
	
	// End Testing Street //
	
	// Start Testing Player Functions //
	
	@Test
	public void testSetLocation() {
		Board board = game.getBoard();
		Location start = board.getStartLocation();
		
		Player p = new Player("Dave", "Dog", 1000, start);
		
		p.setLocation(board.findLocation("Old Kent Road"));
		
		assertEquals(p.getLocation().getName(), "Old Kent Road");
	}
	
	@Test
	public void testGetToken() {
		Player p = new Player("Dave", "Dog", 1000, game.getBoard().getStartLocation());
		p.getToken();
	}
	
	// End Testing Player Functions //
	
	// Start Testing GameOfMonopoly Functions //
	
	@Test
	public void testMovePlayer() {
		Player p = new Player("Dave", "Dog", 1000, game.getBoard().getStartLocation());
		
		game.movePlayer(p, 1);
		
		assertEquals(p.getLocation().getName(), "Old Kent Road");
	}
	
	@Test
	public void testMovePlayerRent() {
		Player p1 = new Player("Dave", "Dog", 1000, game.getBoard().getStartLocation());
		Player p2 = new Player("Dave", "Dog", 1000, game.getBoard().getStartLocation());
		
		game.movePlayer(p1, 1);
		try {
			game.buyProperty(p1);
		} catch (InvalidMove e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//assertEquals(p.getLocation().getName(), "Old Kent Road");
		
		game.movePlayer(p2, 1);
		assertFalse(p2.getBalance() == 1000);
	}
	
	
	///////////////////////////////////
	// Test Board Location Functions //
	///////////////////////////////////
	
	@Test
	public void testBoard_GetStart() {
		Board board = game.getBoard();
		assertTrue(board.getStartLocation().getName() == "Go");
	}
	
	@Test
	public void testBoard_GetLocationFromStart() {
		Board board = game.getBoard();
		assertTrue(board.findLocation(board.getStartLocation(), 8).getName() == "Euston Road");
	}
	
	@Test
	public void testGetInvalidLocation() {
		assertNull(game.getBoard().findLocation("NOT A STREET NAME"));
	}
	
	/**
	 * This is a helper function.  Feel free to modify this as you wish.
	 */
	
	private void tryAndBuy(String name, String piece, int cash, String locationName) throws GameOfMonopoly.InvalidMove {
		//GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		Location location = board.findLocation(locationName);
		Player player = getPlayer(name, piece, cash, location);//new Player(name, piece ,cash, location);
		game.buyProperty(player);
	}
	
	private void tryAndSell(String name, String piece, int cash, String locationName) throws GameOfMonopoly.InvalidMove {
		//GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		Location location = board.findLocation(locationName);
		Player player = getPlayer(name, piece, cash, location);//new Player(name, piece ,cash, location);
		game.sellProperty(player, location);
	}
	
	private void tryAndMorgage(String name, String piece, int cash, String locationName) throws GameOfMonopoly.InvalidMove {
		//GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		Location location = board.findLocation(locationName);
		Player player = getPlayer(name, piece, cash, location);//new Player(name, piece ,cash, location);
		game.mortgageProperty(player, location);
	}
	
	private void tryAndUnMorgage(String name, String piece, int cash, String locationName) throws GameOfMonopoly.InvalidMove {
		//GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		Location location = board.findLocation(locationName);
		Player player = getPlayer(name, piece, cash, location);//new Player(name, piece ,cash, location);
		game.unmortgageProperty(player, location);
	}
	
	private Player getPlayer(String name, String piece, int cash, Location location) {
		if (!players.containsKey(name)) {
			players.put(name, new Player(name, piece ,cash, location));
		}
		
		return players.get(name);
	}

}
