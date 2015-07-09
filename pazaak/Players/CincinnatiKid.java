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
                handleTrickySituation(myValue, oppValue, wins, isPlayerOne, yourHand, opponentHand,
                                      yourSideDeck, opponentSideDeckCount, opponentAction, opponentDidPlay);
            }
    
            if (action == Action.PLAY && cardToPlay == null) {
                logMsg("ERROR - Action is Play but no card chosen");
            }
            logMsg("My hand value is " + myValue + ", opponent is " + oppValue + ", action is " + action +
                   ((action == Action.PLAY && cardToPlay != null) ? " a " + cardToPlay.toString() : ""));
    	}
    
        int [] branchCounts = new int[12];

        public void dumpBranchCounts() {
            if (isDebug) {
                for (int i = 0; i < branchCounts.length; i++) {
                    System.out.print("b[" + i + "]=" + branchCounts[i] + " ");
                }
                System.out.println();
            }
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
            dumpBranchCounts();
            logMsg("I am might bust");
    
            int STAND_VALUE = 18;
            int chosenBranch = 0;
            
            Card bestSideCard = findSideCard(myValue, yourSideDeck);
            int valueWithSideCard = myValue + (bestSideCard != null ? bestSideCard.getValue() : 0);
    
            if (bestSideCard != null && valueWithSideCard >= oppValue && valueWithSideCard > STAND_VALUE) {
                logMsg("Found a good card in side deck");
                action = Action.PLAY;
                cardToPlay = bestSideCard;
                chosenBranch = 1;
            } else if (opponentDidPlay || opponentAction == Action.STAND) {
                logMsg("Opponent is done");
                // Opponent is done, so get another card if I'm behind
                if (myValue < oppValue) {
                    logMsg("I am behind");
                    if (bestSideCard != null && valueWithSideCard >= oppValue) {
                        logMsg("My best side card is good enough to tie or win");
                        action = Action.PLAY;
                        cardToPlay = bestSideCard;
                        chosenBranch = 2;
                    } else {
                        logMsg("My best side card won't do so I'm going to hit");
                        // No side card and I'm losing, so I might as well hit
                        action = Action.END;
                        chosenBranch = 3;
                    }
                } else if (myValue == oppValue) {
                    logMsg("Game is tied");
                    logMsg("Looking for lowest card in the side deck");
                    cardToPlay = findWorstSideCard(myValue, yourSideDeck);
                    if (cardToPlay != null) {
                        action = Action.PLAY;
                        chosenBranch = 4;
                    } else {
                        logMsg("Tied with no side cards - accept the draw");
                        action = Action.STAND;
                        chosenBranch = 5;
                    }
                } else {
                    logMsg("I'm ahead and opponent has given up");
                    action = Action.STAND;
                    chosenBranch = 6;
                }
            } else if (myValue < oppValue) {
                logMsg("I am behind and have nothing good in my side deck");
                action = Action.END;
                chosenBranch = 7;
            } else if (oppValue <= 10 && myValue < STAND_VALUE) {
                logMsg("Opponent is guaranteed to hit and I have a low hand, so take another");
                action = Action.END;
                chosenBranch = 8;
            } else if (myValue == oppValue && myValue >= STAND_VALUE) {
                logMsg("We both have equally good hands - stand and hope for the tie");
                action = Action.STAND;
                chosenBranch = 9;
            } else if (myValue < STAND_VALUE) {
                logMsg("I am ahead but have a low score");
                action = Action.END;
                chosenBranch = 10;
            } else {
                logMsg("I am ahead with a decent score");
                action = Action.STAND;
                chosenBranch = 11;
            }

            branchCounts[chosenBranch]++;
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
    
        private Card findWorstSideCard(int myValue, Collection<Card> sideDeck) {
            int valueNeeded = BEST_HAND - myValue;

            logMsg("Searching side deck for something with value <= " + valueNeeded);
            Card bestCard = null;

            for (Card c : sideDeck) {
                logMsg("Examining side card " + c.getValue());

                // Find the worst card in the deck, but not if it exceeds the amount left
                if (c.getValue() <= valueNeeded && (bestCard == null || c.getValue() < bestCard.getValue())) {
                    logMsg("This is the new best side card");
                    bestCard = c;
                }
            }

            logMsg("Worst side card found is " + (bestCard != null ? bestCard.getValue() : " n/a"));
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
