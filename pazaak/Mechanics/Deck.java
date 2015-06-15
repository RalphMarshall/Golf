package Mechanics;

import java.util.ArrayList;

public class Deck {
	
	private ArrayList<Card> cards;
	
	public Deck(int numCards, int maxValue) {
		this.cards = new ArrayList<Card>();
		while(cards.size() <= numCards)
			for(int i = 0; i < maxValue; i++)
				cards.add(new Card(i+1));
	}
	
	public ArrayList<Card> getDeck() {
		return this.cards;
	}

}
