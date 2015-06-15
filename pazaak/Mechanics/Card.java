package Mechanics;

public class Card {

	private int value;
	
	public Card() {
		this.value = 0;
	}
	
	public Card(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
	
	public String toString() {
		return Integer.toString(this.value);
	}

}
