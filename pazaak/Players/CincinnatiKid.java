package Players;

import java.util.Collection;

import Mechanics.*;

public class CincinnatiKid extends Player {

	public CincinnatiKid() {
		name = "The Cincinnati Kid";
	}

    private static boolean isDebug = false;

    private static final int BEST_HAND = 20;

	public void getResponse(int wins[],
                            boolean isPlayerOne,
                            Collection<Card> yourHand,
                            Collection<Card> opponentHand,
                            Collection<Card> yourSideDeck,
                            int opponentSideDeckCount,
                            Action opponentAction,
                            boolean opponentDidPlay)
    {
        int myValue = handValue(yourHand);
        int oppValue = handValue(opponentHand);

        if (oppValue > BEST_HAND) {
            logMsg("Opponent has busted");
            action = Action.STAND;
        } else if (myValue > BEST_HAND) {
            logMsg("I have busted");
            action = Action.STAND;
        } else if (myValue <= 10) {
            logMsg("I cannot bust with my next move");
            action = Action.END;
        } else {
            handleTrickySituation(myValue, oppValue, wins, isPlayerOne, yourHand, opponentHand, yourSideDeck, opponentSideDeckCount, opponentAction, opponentDidPlay);
        }

        logMsg("My hand value is " + myValue + ", opponent is " + oppValue + ", action is " + action +
               (action == Action.PLAY ? " a " + cardToPlay.toString() : ""));
	}

    private void handleTrickySituation(int myValue, int oppValue,
                                       int wins[],
                                       boolean isPlayerOne,
                                       Collection<Card> yourHand,
                                       Collection<Card> opponentHand,
                                       Collection<Card> yourSideDeck,
                                       int opponentSideDeckCount,
                                       Action opponentAction,
                                       boolean opponentDidPlay)
    {
        logMsg("I am might bust");

        int STAND_VALUE = 17;

        Card bestSideCard = findSideCard(myValue, yourSideDeck);
        int valueWithSideCard = myValue + (bestSideCard != null ? bestSideCard.getValue() : 0);

        if (bestSideCard != null && valueWithSideCard >= oppValue && valueWithSideCard > STAND_VALUE) {
            logMsg("Found a good card in side deck");
            action = Action.PLAY;
            cardToPlay = bestSideCard;
        } else if (myValue < oppValue) {
            logMsg("I am behind and have nothing good in my side deck");
            action = Action.END;
        } else if (oppValue <= 10 && myValue < STAND_VALUE) {
            logMsg("Opponent is guaranteed to hit and I have a low hand, so take another");
            action = Action.END;
        } else if (myValue == oppValue && myValue >= STAND_VALUE) {
            logMsg("We both have equally good hands - stand and hope for the tie");
            action = Action.STAND;
        } else if (myValue < STAND_VALUE) {
            logMsg("I am ahead but have a low score");
            action = Action.END;
        } else {
            logMsg("I am ahead with a decent score");
            action = Action.STAND;
        }
    }

    private double calcBustOdds(int valueSoFar, Collection<Card> myHand, Collection<Card> oppHand) {

        if (valueSoFar >= BEST_HAND) {
            return 1;
        }

        int remainingDeck = 40 - (myHand.size() + oppHand.size());
        int [] cardCounts = new int[10];
        int firstBust = BEST_HAND - valueSoFar;

        for (int i = 0; i < 10; i++) {
            cardCounts[i] = 4;
        }

        for (Card c : myHand) {
            cardCounts[c.getValue()-1]--;
        }

        for (Card c : oppHand) {
            cardCounts[c.getValue()-1]--;
        }

        int bustCards = 0;
        for (int i = firstBust; i < 10; i++) {
            logMsg("cardCounts[" + i + "]=" + cardCounts[i]);
            bustCards += cardCounts[i];
        }

        double retval = (double) bustCards / (double) remainingDeck;
        logMsg("Out of " + remainingDeck + " remaining cards " + bustCards + " will bust, or " + retval);
        return retval;
    }

    private Card findSideCard(int myValue, Collection<Card> sideDeck) {
        int valueNeeded = BEST_HAND - myValue;
        Card bestCard = null;
        if (valueNeeded > 0) {
            for (Card c : sideDeck) {
                if (c.getValue() == valueNeeded) {
                    return c;
                } else if (c.getValue() < valueNeeded) {
                    if (bestCard == null || c.getValue() > bestCard.getValue()) {
                        bestCard = c;
                    }
                }
            }
        }

        return bestCard;
    }

    private void logMsg(String s) {
        if (isDebug) {
            System.out.println("### " + s);
        }
    }

    private int handValue(Collection<Card> hand)  {
		int handValue = 0;
		for (Card c : hand) {
			handValue += c.getValue();
        }
		return handValue;
	}
}
