package Players;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import Mechanics.*;

public class T3M4 extends Player {

	/*
	 * Constants
	 */
	
	private static int DECK_SIZE = 40;
	private static int GOAL = 20;
	
	/*
	 * Instance Variables
	 */
	
	private boolean isNewGame;
	private int[] previousWinQuantity;
	private int myCardCount;
	private int oppCardCount;
	private int cardsInDeck;
	private List<Card> deck;
	private Card[] myHand;
	private Card[] oppHand;
	private Action previousAction;
	private int deckValue;
	private int[] deckCount;
	private Card[] deckAsArray;
	
	public T3M4() {
		name = "T3-M4";
		isNewGame = true;
		previousWinQuantity = new int[3];
	}

	public void getResponse(int wins[], boolean isPlayerOne,
			Collection<Card> yourHand, Collection<Card> opponentHand,
			Collection<Card> yourSideDeck, int opponentSideDeckCount,
			Action opponentAction, boolean opponentDidPlay) {
		
		Random r = new Random();
		
		myCardCount = yourHand.size();
		oppCardCount = opponentHand.size();
		cardsInDeck = DECK_SIZE - (myCardCount + oppCardCount);
		myHand = yourHand.toArray(new Card[yourHand.size()]);
		oppHand = opponentHand.toArray(new Card[opponentHand.size()]);
		
		//System.out.println("S1");
		// If the number of wins changed since the previous turn, we know someone one and it is a new game.
		if(wins != previousWinQuantity)
			isNewGame = true;
		
		if(isNewGame) {
			deck = createDeck();
		}
		
		this.isNewGame = false;
		
		int myWins = 0;
		int theirWins = 0;
		if(isPlayerOne) {
			myWins = wins[0];
			theirWins = wins[1];
		}
		else {
			myWins = wins[1];
			theirWins = wins[0];
		}
		
		//System.out.println("S2");
		// Removes the last card I was dealt from my virtual deck.
		if(previousAction!=Action.PLAY)
			deck.remove(myHand[myHand.length-1]);
		else
			deck.remove(myHand[myHand.length-2]);
		
		//Removes the last card my opponent was dealt from my virtual deck
		if(!opponentDidPlay)
			if(oppHand.length>0)
				deck.remove(oppHand[oppHand.length-1]);
		else
			if(oppHand.length>1)
				deck.remove(oppHand[oppHand.length-2]);
		
		//System.out.println("S3");
		/*
		 * Calculations
		 */
		
		deckAsArray = deck.toArray(new Card[deck.size()]);
		// What is the value of the main deck?
		deckValue = deckValue(deckAsArray);
		
		// How many of each card are left in the main deck?
		deckCount = new int[10];
		
		for(int i = 1; i <= 10; i++)
		{
			for(int j = 0; j < deckAsArray.length; j++) {
				if(i == deckAsArray[j].getValue())
					deckCount[i-1]++;
			}
		}
		
		double[] chanceNumbers = new double[10];
		
		for(int i = 0; i < deckCount.length; i++)
			chanceNumbers[i] = deckCount[i] / deckAsArray.length;
		
		int myValue = handValue(myHand);
		int valueNeeded = GOAL - myValue;
		
		if(valueNeeded < 0) {
			// Oops, I busted...
			this.action = Action.STAND;
			return;
		}
		else if(valueNeeded > 10) {
			//No chance to bust, so I'll draw again...
			this.action = Action.END;
			return;
		}
		
		double chanceBust = sumArray(chanceNumbers, valueNeeded);
		
		int riskFactor = -1;
		
		if(chanceBust > 0.99) {
			// I am going to bust
			riskFactor = 5;
		}
		else if(chanceBust > .95) {
			// I am basically guaranteed to bust...
			riskFactor = 4;
		}
		else if(chanceBust > .68) {
			// Busting is extremely likely
			riskFactor = 3;
		}
		else if(chanceBust > .50) {
			// Busting is probable, but the risk could be worth it...
			riskFactor = 2;
		}
		else if(chanceBust > .33) {
			// The odds are distributed...
			riskFactor = 1;
		}
		else {
			// Busting is unlikely...
			riskFactor = 0;
		}
		
		int oppValue = handValue(oppHand);
		
		//System.out.println("S4");
		if(oppValue > GOAL) {
			// Um, they busted...
			action = Action.STAND;
			return;
		}
		//System.out.println("S5");
		if(oppValue == GOAL) {
			//The opponent will win unless I get to 20...
			//First I'll check my side deck...
			for(Card c: yourSideDeck) {
	            if(c.getValue() == valueNeeded) {
	            	//YES! Tie the game!
	                cardToPlay = c;
	                action = Action.PLAY;
	                return;
	            }
	        }
			// Well, I guess not...
			// Time to draw and pray to the robot gods then...
			action = Action.END;
			return;
		}
		//System.out.println("S6");
		// I'll see if I can get to 20...
		for(Card c: yourSideDeck) {
            if(c.getValue() == valueNeeded) {
            	//YES!
                cardToPlay = c;
                action = Action.PLAY;
                return;
            }
        }
		
		int valueDiff = oppValue - myValue;
		//System.out.println("S7");
		if(opponentAction == Action.STAND) {
			// If the opponent is standing, they can't do anything else.
			// I need to analyze the risks/rewards of my potential actions...
			// First, I'll make sure I'm not already winning this hand
			if(valueDiff < 0) {
				// I have a greater value than them! I win!
				action = Action.STAND;
				return;
			}
			// Next, I'll see if I can win with a side deck card...
			for(Card c: yourSideDeck) {
	            if(c.getValue() <= valueNeeded && c.getValue() > valueDiff) {
	            	//YES! Win the game!
	                cardToPlay = c;
	                action = Action.PLAY;
	                return;
	            }
	        }
			// If I can't win, I'll see if I can tie...
			boolean canTie = false;
			for(Card c: yourSideDeck) {
	            if(c.getValue() == valueDiff) {
	            	//Well, I can tie...
	                cardToPlay = c;
	                canTie = true;
	            }
	        }
			if(canTie) {
				int playFactor = -1;
				// But do I want to...
				if(myWins < theirWins) {
					// They have more wins than me, so playing it too risky is probably not a good idea...
					if(theirWins == 2) {
						// If they win this hand, they win the tourney. No sense in risking it when I can tie.
						action = Action.PLAY;
						return;
					}
					else {
						// It's still early in the game. I'll do a risk analysis before I decide...
						if(yourSideDeck.size() > opponentSideDeckCount + 1) {
							// I would have at least two more side deck cards than them if I don't play this hand.
							playFactor = 1;
						}
						else if(yourSideDeck.size() > opponentSideDeckCount) {
							// I have one more card than they do.
							playFactor = 0;
						}
						else if(yourSideDeck.size() == opponentSideDeckCount) {
							// I have the same number of cards as they do. Playing would give me a disadvantage.
							playFactor = -1;
						}
						else {
							// I have fewer cards than they do. Playing would give me a serious disadvantage...
							playFactor = -2;
						}
						if(playFactor + riskFactor > 2) {
							action = Action.PLAY;
							return;
						}
						else if(playFactor + riskFactor > 0) {
							// Let's go 50/50...
							if(r.nextInt(2) == 0) {
								action = Action.END;
								return;
							}
							else {
								action = Action.PLAY;
								return;
							}
						}
						else {
							action = Action.END;
							return;
						}
					}
				}
				else if(theirWins == 2) {
					// We are tied at two. No sense in risking it when I can tie.
					action = Action.PLAY;
					return;
				}
				else if(myWins > theirWins) {
					// I have some breathing room.
					playFactor--;
					double chanceTieOrWin = chanceNumbers[valueNeeded-1] + chanceNumbers[valueDiff-1];
					if(chanceTieOrWin >= chanceBust || riskFactor + playFactor < 0) {
						// Meh, I'll risk it.
						action = Action.END;
						return;
					}
					else if(riskFactor + playFactor < 1) {
						// Let's go 50/50...
						if(r.nextInt(2) == 0) {
							action = Action.END;
							return;
						}
						else {
							action = Action.PLAY;
							return;
						}
					}
					else {
						// I guess I'll play...
						action = Action.PLAY;
						return;
					}
				}
				else {
					// We must be tied at 0 or 1.
					// Side deck analysis time!
					if(yourSideDeck.size() > opponentSideDeckCount + 1) {
						// I would have at least two more side deck cards than them if I don't play this hand.
						playFactor = 1;
					}
					else if(yourSideDeck.size() > opponentSideDeckCount) {
						// I have one more card than they do.
						playFactor = 0;
					}
					else if(yourSideDeck.size() == opponentSideDeckCount) {
						// I have the same number of cards as they do. Playing would give me a disadvantage.
						playFactor = -1;
					}
					else {
						// I have fewer cards than they do. Playing would give me a serious disadvantage...
						playFactor = -2;
					}
					if(playFactor + riskFactor > 4) {
						action = Action.PLAY;
						return;
					}
					else if(playFactor + riskFactor > 2) {
						// Let's go 50/50...
						if(r.nextInt(2) == 0) {
							action = Action.END;
							return;
						}
						else {
							action = Action.PLAY;
							return;
						}
					}
					else {
						action = Action.END;
						return;
					}
				}
			}
		}
		
		//System.out.println("S8");
		if(valueDiff > 0) {
			// They are ahead of me. I must do something to catch up or get the lead...
			// First, I'll see how close my side deck would get me to 20...
			boolean canPlay = false;
			for(Card c: yourSideDeck) {
	            if(c.getValue() < valueNeeded && c.getValue() >= valueDiff) {
	                cardToPlay = c;
	                canPlay = true;
	            }
	        }
			//System.out.println("S8.1");
			if(canPlay) {
				// Well, I can play a card and at least tie them...
				if(cardToPlay.getValue() == valueDiff) {
					if(oppValue > 17) {
						if(theirWins == 2) {
							// Best not give them the chance...
							action = Action.PLAY;
							return;
						}
						// They are most likely going to bomb out anyway!
						action = Action.STAND;
						return;
					}
					else if(oppValue > 14) {
						if(theirWins >= myWins) {
							// Best not give them the chance...
							action = Action.PLAY;
							return;
						}
						// They have a chance. Stand or draw...
						if(r.nextInt(2) == 0) {
							action = Action.END;
							return;
						}
						else {
							action = Action.STAND;
							return;
						}
					}
					else {
						// I have a good chance here...
						action = Action.END;
						return;
					}
				}
			}
			else {
				//System.out.println("S8.2");
				// I can't play, so how likely are they to bomb out?
				if(oppValue > 17) {
					// Likely to bomb out.
					action = Action.STAND;
					return;
				}
				else if(oppValue > 14) {
					// 50/50
					if(r.nextInt(2) == 0) {
						action = Action.END;
						return;
					}
					else {
						action = Action.STAND;
						return;
					}
				}
				else {
					// I need to draw...
					action = Action.END;
					return;
				}
			}
			
		}
		else {
			// They are behind me
			action = Action.END;
		}
	}
	
	private static List<Card> createDeck() {
		List<Card> deck = new ArrayList<Card>();
		for(int i = 0; i < 4; i++) {
			for(int j = 1; j <= 10; j++) {
				deck.add(new Card(i));
			}
		}
		return deck;
	}
	
	private static int deckValue(Card[] deck) {
		int deckValue = 0;
		for(int i = 0; i < deck.length; i++)
			deckValue += deck[i].getValue();
		return deckValue;
	}
	
	private int handValue(Card[] hand) {
		int handValue = 0;
		for(int i = 0; i < hand.length; i++)
			handValue += hand[i].getValue();
		return handValue;
	}
	
	private static double sumArray(double[] array, int index) {
		double sum = 0;
		for(int i = index; i < array.length; i++) {
			sum += array[i];
		}
		return sum;
	}

}
