package Players;

import java.util.Collection;

import Mechanics.Card;
import Mechanics.Player;

public class DumbBoldPlayer extends Player {

	private Card[] hand;
	private int handValue;
	
	public DumbBoldPlayer() {
		name = "Dumb Bold Player";
	}
	
	public void getResponse(int wins[], boolean isPlayerOne, Collection<Card> yourHand, Collection<Card> opponentHand, Collection<Card> yourSideDeck, int opponentSideDeckCount, Action opponentAction, boolean opponentDidPlay) {
		this.hand = yourHand.toArray(new Card[yourHand.size()]);
		this.handValue = this.handValue();
		if(this.handValue < 20) {
			action = Action.END;
		}
		else
			action = Action.STAND;
	}
	
	private int handValue() {
		int handValue = 0;
		for(int i = 0; i < this.hand.length; i++)
			handValue += hand[i].getValue();
		return handValue;
	}

}
