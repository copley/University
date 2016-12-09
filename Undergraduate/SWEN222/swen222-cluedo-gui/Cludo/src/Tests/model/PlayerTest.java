package tests.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import model.Card;
import model.Player;
import model.PlayerCharacter;
import model.Room;
import model.Weapon;

import org.junit.Test;

public class PlayerTest {
	
	
	/**
	 * Make sure that the character of the player
	 * is stored correctly
	 */
	@Test
	public void testPlayerCharacter() {
		Player p = new Player(PlayerCharacter.MRS_PEACOCK, new ArrayList<Card>());
		assertTrue(p.getPlayerCharacter() == PlayerCharacter.MRS_PEACOCK);
	}
	
	/**
	 * Make sure that the cards given to the player are
	 * properly stored
	 */
	@Test
	public void testPlayerCards() {
		List<Card> cards = new ArrayList<Card>();
		cards.add(Weapon.CANDLESTICK);
		Player p = new Player(PlayerCharacter.MISS_SCARLETT, cards);
		
		assertTrue(p.getCards().contains(Weapon.CANDLESTICK));
	}
	
	
	/**
	 * Make sure that a player doesn't refute a suggestion
	 * unless they have the right cards
	 */
	@Test
	public void testPlayerNotRefuteSugestion() {
		List<Card> cards = new ArrayList<Card>();
		cards.add(Room.CONSERVATORY);
		cards.add(Weapon.REVOLVER);
		
		Room room = Room.BALL_ROOM;
		Weapon weapon = Weapon.KNIFE;
		PlayerCharacter character = PlayerCharacter.MRS_WHITE;
		
		Player p = new Player(PlayerCharacter.COLONEL_MUSTARD, cards);
		List<Card> matched = p.refuteSuggestion(character, weapon, room);
		assertTrue(matched.size() == 0);
	}
	
	/**
	 * Make sure that a player refutes a suggestion
	 * if they can
	 */
	@Test
	public void testPlayerRefuteSugestion() {
		List<Card> cards = new ArrayList<Card>();
		cards.add(Room.BALL_ROOM);
		cards.add(Weapon.KNIFE);
		
		Room room = Room.BALL_ROOM;
		Weapon weapon = Weapon.KNIFE;
		PlayerCharacter character = PlayerCharacter.MRS_WHITE;
		
		Player p = new Player(PlayerCharacter.COLONEL_MUSTARD, cards);
		List<Card> matched = p.refuteSuggestion(character, weapon, room);
		
		assertTrue(matched.contains(Room.BALL_ROOM));
		assertTrue(matched.contains(Weapon.KNIFE));
	}
}
