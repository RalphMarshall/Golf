package Mechanics;

import java.util.ArrayList;
import java.util.Random;

public class SideDeck extends Deck {

	private static final int NUM_CARDS = 10;
	private static final int MAX_VALUE = 5;
	private static final int SIDE_DECK_SIZE = 4;
	
	public SideDeck() {
		super(NUM_CARDS, MAX_VALUE);
	}
	
	public ArrayList<Card> getSideDeck() {
		ArrayList<Card> deck = this.getDeck();
		ArrayList<Card> sideDeck = new ArrayList<Card>();
		while(sideDeck.size()<=SIDE_DECK_SIZE) {
			Card randomCard = deck.get(new Random().nextInt(getDeck().size()));
			deck.remove(randomCard);
			sideDeck.add(randomCard);
		}
		return sideDeck;
	}

}
