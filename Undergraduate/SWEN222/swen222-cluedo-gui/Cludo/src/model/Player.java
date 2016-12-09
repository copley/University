package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The {@code Player} class wraps a {@code PlayerCharacter} object and holds the state of the player in the game.
 *
 * @author Liam O'Neill and Daniel Braithwaite
 */
public class Player {

	/**
	 * PlayerCharacter which backs this player.
	 */
	private final PlayerCharacter playerCharacter;

	/**
	 * List of cards the player has.
	 */
	private final List<Card> cards;

	/**
	 * Boolean showing whether this player is still in the game.
	 */
	private boolean isLost;

	/**
	 * Create new Player with a backing PlayerCharacter and a list of cards.
	 *
	 * @param playerCharacter playerCharacter which backs this player instance.
	 * @param cards list of cards which this player has
	 */
	public Player(PlayerCharacter playerCharacter, List<Card> cards){
		this.playerCharacter = playerCharacter;
		this.cards = Collections.unmodifiableList(new ArrayList<>(cards));
		
		isLost = false;
	}

	/**
	 * Get PlayerCharacter instance.
	 *
	 * @return playerCharacter
	 */
	public PlayerCharacter getPlayerCharacter(){
		return playerCharacter;
	}

	/**
	 * Get list of cards.
	 *
	 * @return list of cards.
	 */
	public List<Card> getCards() {
		return cards;
	}

	/**
	 * Set the player to lost.
	 */
	public void lost() {
		isLost = true;
	}

	/**
	 * Is the player still playing.
	 *
	 * @return true iff this player is still playing.
	 */
	public boolean isPlaying() {
		return !isLost;
	}

	/**
	 * Get all cards from this player which can contradict a suggestion.
	 *
	 * @param character character suggestion
	 * @param weapon weapon suggestion
	 * @param room room suggestion
	 *
	 * @return list of all cards which contradict the suggestions
	 */
	public List<Card> refuteSuggestion(PlayerCharacter character, Weapon weapon, Room room) {
		List<Card> c = new ArrayList<>();
		
		if (cards.contains(character)) {
			c.add(character);
		}
		
		if (cards.contains(weapon)) {
			c.add(weapon);
		}
		
		if (cards.contains(room)) {
			c.add(room);
		}
		
		return c;
	}

}
