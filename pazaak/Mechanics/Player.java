package Mechanics;

import java.util.Collection;

public class Player {

	public enum Action {END, STAND, PLAY}
	
	protected String name;
	protected Action action;
	protected Card cardToPlay;
	
	public Player() {
		this.name = "Unnamed";
		this.action = null;
	}
	
	public void getResponse(int wins[], boolean isPlayerOne, Collection<Card> yourHand, Collection<Card> opponentHand, Collection<Card> yourSideDeck, int opponentSideDeckCount, Action opponentAction, boolean opponentDidPlay) {
		
	}
	
	public Action getAction() {
		return this.action;
	}
	
	public Card getCardToPlay() {
		return this.cardToPlay;
	}
	
	public void setAction(Action action) {
		this.action = action;
	}

}
