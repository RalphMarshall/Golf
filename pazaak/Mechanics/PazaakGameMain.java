package Mechanics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import Players.*;

public class PazaakGameMain {

	static Scanner in = new Scanner(System.in);

	public static int HAND_GOAL = 20;

    private static boolean isDebug = false;

    private static void logMsg(String s) {
        if (isDebug) {
            System.out.println(s);
        }
    }

	public static void main(String[] args) {
		Player[] players = {
            new DumbBoldPlayer(), new DumbCautiousPlayer(),
            new Bastila(), new CincinnatiKid(),
            new AustinPowers(), new T3M4()
        };
		int[] handWins = new int[players.length];
		int[] tourneyWins = new int[players.length];

        double totalWins = 0;

        int tourneyCounter = 0;

		for (int a = 0; a < players.length - 1; a++) {
			for (int b = a + 1; b < players.length; b++) {
				
				for (int i = 0; i < 100000; i++) {

                    logMsg("ROUND # " + ++tourneyCounter);
					if(i % 2 == 0) {
						Player[] thisRound = { players[a], players[b] };
						int[] newWins = playGame(thisRound);
						handWins[a] += newWins[0];
						handWins[b] += newWins[1];
						if (newWins[2] == 1) {
                            totalWins++;
							tourneyWins[a]++;
                        } else {
                            totalWins++;
							tourneyWins[b]++;
                        }
					}
					else {
						Player[] thisRound = { players[b], players[a] };
						int[] newWins = playGame(thisRound);
						handWins[b] += newWins[0];
						handWins[a] += newWins[1];
						if (newWins[2] == 1) {
                            totalWins++;
							tourneyWins[b]++;
                        } else {
                            totalWins++;
							tourneyWins[a]++;
                        }
					}	
				}
			}
		}
        for(int i = 0; i < players.length; i++) {
            System.out.println(players[i].name+": " + tourneyWins[i] + " " + handWins[i] + " " + ((double) tourneyWins[i]/totalWins));
        }
    }

	private static int[] playGame(Player[] players) {
		Deck deck = new MainDeck();
		Player p1 = players[0];
		Player p2 = players[1];
        logMsg("New tourney: "+p1.name+" vs. "+p2.name);
		ArrayList<Card> p1SideDeck = new SideDeck().getSideDeck();
		ArrayList<Card> p2SideDeck = new SideDeck().getSideDeck();
		int[] wins = { 0, 0, -1 };
		while (wins[0] < 3 && wins[1] < 3) {
			// System.out.println("New Game!");
			// System.out.println("The standings are "+wins[0]+" to "+wins[1]);
			ArrayList<Card> p1Hand = new ArrayList<Card>();
			ArrayList<Card> p2Hand = new ArrayList<Card>();
			boolean p1DidPlay = false;
			boolean p2DidPlay = false;
			Collections.shuffle(deck.getDeck());
			Queue<Card> gameDeck = new LinkedList<>(deck.getDeck());
			p1.setAction(null);
			p2.setAction(null);
			while (!gameIsOver(p1, p2, p1Hand, p2Hand, wins)) {
				if (p1.getAction() != Player.Action.STAND) {
					p1Hand.add(gameDeck.remove());
                    logMsg(p1.name+"'s Hand: "+p1Hand.toString());
					if (handValue(p1Hand) != HAND_GOAL) {
						p1.getResponse(wins, true,
								Collections.unmodifiableCollection(p1Hand),
								Collections.unmodifiableCollection(p2Hand),
								Collections.unmodifiableCollection(p1SideDeck),
								p2SideDeck.size(), p2.getAction(), p2DidPlay);
						switch (p1.getAction()) {
						case END:
							if (handValue(p1Hand) > HAND_GOAL)
								break;
						case STAND:
							p1DidPlay = false;
							break;
						case PLAY:
							p1DidPlay = true;
							Card cardToPlay = p1.getCardToPlay();
							if (p1SideDeck.contains(cardToPlay)) {
								p1SideDeck.remove(cardToPlay);
								p1Hand.add(cardToPlay);
							}
							p1.setAction(Player.Action.STAND);
							break;
						}
					} else {
						p1DidPlay = false;
						p1.setAction(Player.Action.STAND);
					}
				}
				logMsg(p1.name+" has chosen to "+p1.getAction());
				if (p2.getAction() != Player.Action.STAND) {
					p2Hand.add(gameDeck.remove());
					logMsg(p1.name+"'s Hand: "+p2Hand.toString());
					if (handValue(p2Hand) != HAND_GOAL) {
						p2.getResponse(wins, false,
								Collections.unmodifiableCollection(p2Hand),
								Collections.unmodifiableCollection(p1Hand),
								Collections.unmodifiableCollection(p2SideDeck),
								p1SideDeck.size(), p1.getAction(), p1DidPlay);
						switch (p2.getAction()) {
						case END:
							p2DidPlay = false;
							break;
						case STAND:
							p2DidPlay = false;
							break;
						case PLAY:
							p2DidPlay = true;
							Card cardToPlay = p2.getCardToPlay();
							if (p2SideDeck.contains(cardToPlay)) {
								p2SideDeck.remove(cardToPlay);
								p2Hand.add(cardToPlay);
							}
							p2.setAction(Player.Action.STAND);
							break;
						}
					} else {
						p2DidPlay = false;
						p2.setAction(Player.Action.STAND);
					}
				}
				// System.out.println(p2.name+" has chosen to "+p2.getAction());
				// System.out.println("Press enter to continue...");
				// in.nextLine();
			}
		}
		if (wins[0] > wins[1])
			wins[2] = 1;
		else
			wins[2] = 2;
		return wins;
	}

	public static int handValue(ArrayList<Card> hand) {
		int sum = 0;
		for (int i = 0; i < hand.size(); i++)
			sum += hand.get(i).getValue();
		return sum;
	}

	private static boolean gameIsOver(Player p1, Player p2,
			ArrayList<Card> p1Hand, ArrayList<Card> p2Hand, int[] wins) {
		int p1HandValue = handValue(p1Hand);
		int p2HandValue = handValue(p2Hand);
		/*
		 * Checks if one player bombed out. If so, the player who did not bomb
		 * out gets the win. If both bombed out, the game ends in a tie.
		 */
		if (p1HandValue > HAND_GOAL) {
			if (p2HandValue <= HAND_GOAL) {
				logMsg(p1.name+" has bombed out! "+p2.name+" wins!");
				wins[1]++;
				return true;
			} else {
				logMsg("Both players have bombed out! Tie!");
				return true;
			}
		} else if (p2HandValue > HAND_GOAL) {
			logMsg(p2.name+" has bombed out! "+p1.name+" wins!");
			wins[0]++;
			return true;
		}
		/*
		 * Checks if one player has elected to stand. If so, play will continue
		 * with the non-standing player. If both elected to stand, the player
		 * with the highest hand value wins earning them two points. If the hand
		 * values are equal, the game ends in a tie.
		 */
		if (p1.getAction() == Player.Action.STAND) {
			if (p2.getAction() == Player.Action.STAND) {
				if (p1HandValue > p2HandValue) {
					logMsg(p1.name+" wins!");
					wins[0]++;
					return true;
				} else if (p2HandValue > p1HandValue) {
					logMsg(p2.name+" wins!");
					wins[1]++;
					return true;
				} else {
					return true;
				}
			} else if (p2HandValue > p1HandValue) {
				logMsg(p2.name + " wins!");
				wins[1]++;
				return true;
			} else
				return false;
		} else if (p2.getAction() == Player.Action.STAND) {
			if (p1HandValue > p2HandValue) {
				logMsg(p1.name+" wins!");
				wins[0]++;
				return true;
			}
		}
		return false;
	}

}
