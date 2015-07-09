package Players;

import java.util.Arrays;
import java.util.Collection;


import Mechanics.Card;
import Mechanics.Player;

public class Nestor extends Player {
    final int TotalWinPayoff = 10;
    final int TotalLosePayoff = 0;
    final int TotalDrawPayoff = 1;
    final int temporaryLosePayoff = 4;
    final int temporayWinPayoff = 19;
    final int temporaryDrawPayoff = 9;
    @Override
    public void getResponse(int[] wins, boolean isPlayerOne,
            Collection<Card> yourHand, Collection<Card> opponentHand,
            Collection<Card> yourSideDeck, int opponentSideDeckCount,
            Action opponentAction, boolean opponentDidPlay) {

        int sumMyHand = SumHand(yourHand);
        int sumOpponentHand = SumHand(opponentHand);
        if(sumMyHand == 20)
        {
            //I'm unbeatable :)
            //System.out.println("\tI'm Unbeatable");
            this.action = Action.STAND;
            return;
        }
        else if(opponentDidPlay || opponentAction == Action.STAND)
        {
            //They've finished
            ///System.out.println("\tThey've Finished");
            if(sumMyHand>sumOpponentHand)
            {
                //I've won
                //System.out.println("\tI've Won");
                this.action = Action.STAND;
                return;
            }
            else if(canBeat(sumMyHand, sumOpponentHand, yourSideDeck))
            {
                //I can beat them
                //System.out.println("\tI can beat them");
                this.action = Action.PLAY;
                return;
            }
            else if(canEven(sumMyHand, sumOpponentHand, yourSideDeck))
            {
                //I can draw with them
                //System.out.println("\tI can draw with them");
                this.action = Action.PLAY;
                return;
            }
            else
            {
                //I need another card
                //System.out.println("\tI need another card");
                this.action = Action.END;
                return;
            }
        }
        else if(deckContains(yourSideDeck, 20-sumMyHand))
        {
            //Let's get 20
            //System.out.println("\tLet's get 20");
            this.action = Action.PLAY;
            this.cardToPlay = getCard(yourSideDeck, 20-sumMyHand);
            return;
        }
        else if(sumOpponentHand==20 && sumMyHand<20)
        {
            //They've got 20 so we need to fight for a draw
            //System.out.println("\tFight for a draw");
            this.action = Action.END;
            return;
        }

        else if(sumMyHand<10)
        {
            //Lets get another card
            //System.out.println("\tLet's get another card");
            this.action = Action.END;
            return;
        }
        else
        {
            //Let's work out some probabilities
            //System.out.println("\tLet's work out some probabilities");
            int[] cardsLeft = {4,4,4,4,4,4,4,4,4,4};
            for (Card card : opponentHand) {
                cardsLeft[card.getValue()-1] --;

            }
            for (Card card : yourHand) {
                cardsLeft[card.getValue()-1] --;

            }

             int numCardsLeft = sumfromToEnd(0, cardsLeft);

             //My Assumptions
             double probabilityTheyStand = (double)sumfromToEnd(20-sumOpponentHand, cardsLeft)/numCardsLeft;

             //What I need to know
             double payoffStanding = 0;
             double payoffDrawing = 0;


             for(int myChoice = -1; myChoice<10; myChoice++)
             {
                 for(int theirChoice = -1; theirChoice<10; theirChoice++)
                 {
                     if(myChoice == -1)
                     {
                         payoffStanding += getProbability(myChoice, theirChoice, Arrays.copyOf(cardsLeft, cardsLeft.length), probabilityTheyStand, numCardsLeft) * getPayoff(sumMyHand, sumOpponentHand,myChoice, theirChoice, TotalWinPayoff, TotalDrawPayoff, TotalLosePayoff);
                     }
                     else
                     {
                         payoffDrawing +=
                                 getProbability(myChoice, theirChoice, Arrays.copyOf(cardsLeft, cardsLeft.length), probabilityTheyStand, numCardsLeft)
                                 * getPayoff(sumMyHand, sumOpponentHand, myChoice, theirChoice, temporayWinPayoff, temporaryDrawPayoff, temporaryLosePayoff);
                     }
                 }
             }
            // System.out.println("\tStanding: " +Double.toString(payoffStanding) + " Ending: " + Double.toString(payoffDrawing));
             if(payoffStanding<payoffDrawing)
             {
                 this.action = Action.END;
             }
             else
             {
                 this.action = Action.STAND;
             }
        }


    }



    private int getPayoff(int sumMyHand, int sumOpponentHand, int myChoice,
            int theirChoice, int WinPayoff, int DrawPayoff,
            int LosePayoff) {
            if(sumMyHand + myChoice + 1 > 20)
            {
                if(sumOpponentHand + theirChoice + 1 > 20)
                {
                    return DrawPayoff;
                }
                else
                {
                    return LosePayoff;
                }
            }
            else if(sumMyHand + myChoice + 1 > sumOpponentHand + theirChoice + 1)
            {
                return WinPayoff;
            }
            else if (sumMyHand + myChoice + 1 < sumOpponentHand + theirChoice + 1)
            {
                return LosePayoff;
            }
            else
            {
                return DrawPayoff;
            }


    }



    private double getProbability(
            int myChoice, int theirChoice, int[] cardsLeft,
            double probabilityTheyStand, int numCardsLeft) {
        double myProb, theirProb;
        if(myChoice<0)
        {
            myProb = 1;
        }
        else
        {
            myProb = ((double)cardsLeft[myChoice])/((double)numCardsLeft);
            cardsLeft[myChoice]--;
            numCardsLeft--;
        }

        if(theirChoice<0)
        {
            theirProb = probabilityTheyStand;
        }
        else
        {
            theirProb = ((double)cardsLeft[theirChoice]) / ((double)numCardsLeft);
        }
        return myProb*theirProb;
    }





    private int sumfromToEnd(int i, int[] cardsLeft) {
        int toRet = 0;
        for(;i<cardsLeft.length; i++)
        {
            toRet += cardsLeft[i];
        }
        return toRet;
    }

    private boolean canEven(int mySum, int opponentSum,
            Collection<Card> yourSideDeck) {
        for (Card card : yourSideDeck) {
            if(mySum + card.getValue() <= 20 && mySum + card.getValue() >= opponentSum)
            {
                this.cardToPlay = card;
                return true;
            }
        }
        return false;
    }

    private boolean canBeat(int mySum, int opponentSum,
            Collection<Card> yourSideDeck) {
        for (Card card : yourSideDeck) {
            if(mySum + card.getValue() <= 20 && mySum + card.getValue() > opponentSum)
            {
                this.cardToPlay = card;
                return true;
            }
        }
        return false;
    }

    private Card getCard(Collection<Card> deck, int value) {
        for (Card card : deck) {
            if(card.getValue() == value)
            {
                return card;
            }
        }
        return null;
    }

    private boolean deckContains(Collection<Card> deck, int value) {
        for (Card card : deck) {
            if(card.getValue() == value)
            {
                return true;
            }
        }
        return false;
    }

    public Nestor()
    {
        super();
        name = "Nestor";
    }

    private int SumHand(Collection<Card> hand)
    {
        int toRet = 0;
        for (Card card : hand) {
            toRet += card.getValue();
        }
        return toRet;
    }
}
