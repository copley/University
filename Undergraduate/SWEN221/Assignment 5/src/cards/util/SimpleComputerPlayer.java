package cards.util;

import java.util.concurrent.TimeUnit;

import cards.core.Card;
import cards.core.Card.Rank;
import cards.core.Card.Suit;
import cards.core.Player;
import cards.core.Trick;

/**
 * Implements a simple computer player who plays the highest card available when
 * the trick can still be won, otherwise discards the lowest card available. In
 * the special case that the player must win the trick (i.e. this is the last
 * card in the trick), then the player conservatively plays the least card
 * needed to win.
 * 
 * @author David J. Pearce
 * 
 */
public class SimpleComputerPlayer extends AbstractComputerPlayer {

	public SimpleComputerPlayer(Player player) {
		super(player);
	}

	@Override
	public Card getNextCard(Trick trick) {
		Card firstPlayed = trick.getCardPlayed(trick.getLeadPlayer());
		Card bestCard = null;
		int c = 0;

		// Go tthrough all the cards played and get the best one
		for (; c < trick.getCardsPlayed().size(); c++) {
			Card current = trick.getCardsPlayed().get(c);

			if (bestCard == null) {
				bestCard = current;
			} else if (current != null && (current.suit() == firstPlayed.suit() || current.suit() == trick.getTrumps())) {
				bestCard = getBetterCard(bestCard, current, trick);
			}
		}

		// Determin if we can play a card that folows the suit
		boolean canFolowSuit = false;

		// If we arnt the first to play then check to see if we can
		// folow suit
		if (firstPlayed != null) {
			for (Card card : player.hand) {
				if (card.suit() == firstPlayed.suit()) {
					canFolowSuit = true;
					break;
				}
			}
		}

		// Find the best, worst and closest cards
		Card best = null;
		Card closest = null;
		Card worst = null;
		
		// If we are the first then we want to pick the
		// card with the highest rank, regardless than suit
		if (firstPlayed == null) {
			for (Card card : player.hand) {
				if (best == null) {
					best = card;
				} else if (card.suit() == trick.getTrumps()) {
					if (best.suit() == trick.getTrumps()) {
						best = getBetterCard(best, card, trick);
					} else {
						best = card;
					}
				} else if (best.suit() != trick.getTrumps()) {
					best = (best.rank().ordinal() > card.rank().ordinal()) ? best : card;
				}
			}
		} else {
			for (Card card : player.hand) {
				if (canFolowSuit) {
					if (card.suit() != firstPlayed.suit()) {
						continue;
					}
				}
				
				best = getBetterCard(best, card, trick);
				worst = getBetterCard(worst, card, trick) == worst || worst == null ? card
						: worst;

				
				Card tmp = getBetterCard(card, bestCard, trick);
				if (tmp.equals(card)) {
					tmp = getBetterCard(card, closest, trick);
					
					if ((tmp.equals(closest) || closest == null) && (card.suit() == firstPlayed.suit() || card.suit() == trick.getTrumps())) {
						closest = card;
					}
				}
			}
		}

		// If we are the first to play
		if (c == 0) {
			return best;
		}
		
		// If we cant folow suit then we just have to discard
		if (!canFolowSuit && best.suit().ordinal() != trick.getTrumps().ordinal()) {
			return worst;
		}

		// If we are the last to play then we can play
		// conservatily
		if (c == 3) {
			// If there is a closest card to play then we want
			// to play that one, otherwise we must discard
			if (closest != null) {
				return closest;
			} else {
				return worst;
			}
		} else {
			// If our best card is better than the best played so far
			// then play our best, othewise play our worst
			if (getBetterCard(best, bestCard, trick) == best) {
				return best;
			} else {
				return worst;
			}
		}
	}

	/**
	 * Takes two cards and returns the one that is better
	 * 
	 * @param c1 - First card
	 * @param c2 - Seconds card
	 * @param t - Trick
	 * @return The best card from c1 and c2
	 */
	public Card getBetterCard(Card c1, Card c2, Trick t) {

		// If one of the cards is null then return the other
		if (c1 == null) {
			return c2;
		} else if (c2 == null) {
			return c1;
		}

		if (c1.suit() == t.getTrumps() && c2.suit() == t.getTrumps()) {
			// If both cards have the trumps suit
			return (c1.rank().ordinal() > c2.rank().ordinal()) ? c1 : c2;
		} else if (c1.suit() == t.getTrumps()) {
			return c1;
		} else if (c2.suit() == t.getTrumps()) {
			return c2;
		} else {
			
			if (c1.rank() != c2.rank()) {
				return (c1.rank().ordinal() > c2.rank().ordinal()) ? c1 : c2; 
			} else {
				return (c1.compareTo(c2) > 0) ? c1 : c2;
			}
		}
	}
}
