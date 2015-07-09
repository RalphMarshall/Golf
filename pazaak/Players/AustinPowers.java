package Players;
import java.util.Collection;

import Mechanics.*;

public class AustinPowers extends Player {
    public AustinPowers() {
        name = "Austin Powers";
    }
    int MAX_VALUE = 20;
    public void getResponse(int wins[], boolean isPlayerOne,
            Collection<Card> yourHand, Collection<Card> opponentHand,
            Collection<Card> yourSideDeck, int opponentSideDeckCount,
            Action opponentAction, boolean opponentDidPlay) {
        action = null;
        cardToPlay = null;
        int myWins = isPlayerOne?wins[0]:wins[1];
        int oppWins = isPlayerOne?wins[1]:wins[0];
        int oppTotal = calcHand(opponentHand);
        int myTotal = calcHand(yourHand);
        boolean liveDangerously = ((oppTotal>=myTotal && opponentAction==Action.STAND) || opponentAction==Action.END) && myTotal<MAX_VALUE && canNotBust(yourHand,opponentHand,myTotal) && myWins<oppWins;

        if(myTotal==MAX_VALUE || oppTotal>MAX_VALUE || myTotal>MAX_VALUE ||(oppTotal<myTotal&&opponentAction==Action.STAND))
        {
            action = Action.STAND;
        }
        else if((opponentAction==Action.STAND&&hasGoodEnoughSideCard(yourSideDeck,myTotal,oppTotal))||hasPerfectSideCard(yourSideDeck, myTotal))
        {
            action = Action.PLAY;
        }
        else if(liveDangerously||betterThan20(myTotal, getDeck(yourHand, opponentHand)))
        {
            action = Action.END;
        }
        else
        {
            action=Action.STAND;
        }

    }

    private boolean hasGoodEnoughSideCard(Collection<Card> yourSideDeck,
            int myTotal, int oppTotal) {
        for(Card c: yourSideDeck)
        {
            if(MAX_VALUE>=myTotal+c.getValue()&&myTotal+c.getValue()>oppTotal)
            {
                cardToPlay=c;
                return true;
            }
        }
        return false;
    }

    private boolean betterThan20(int myTotal, int[] deck) {
        int deckSize=0;
        int nonBustCards=0;
        for(int i=0;i<10;i++)
        {
            deckSize+=deck[i];
            if(MAX_VALUE-myTotal>i)
                nonBustCards+=deck[i];
        }
        return (double)nonBustCards/(double)deckSize>0.2;
    }

    private boolean hasPerfectSideCard(Collection<Card> yourSideDeck,
            int myTotal) {
        for(Card c:yourSideDeck)
        {
            if(MAX_VALUE-myTotal== c.getValue())
            {
                cardToPlay = c;
                return true;
            }
        }
        return false;
    }

    private boolean canNotBust(Collection<Card> yourHand,
            Collection<Card> opponentHand, int myTotal) {
        if(myTotal<=10) return true;
        int[] deck = getDeck(yourHand, opponentHand);
        for(int i=0;i<MAX_VALUE-myTotal;i++)
            if(deck[i]>0)
                return true;
        return false;
    }

    private int[] getDeck(Collection<Card> yourHand,
            Collection<Card> opponentHand) {
        int[] deck = new int[10];
        for (int i = 0; i < 10; i++) {
            deck[i] = 4;
        }
        for(Card c:yourHand){deck[c.getValue()-1]--;}
        for(Card c:opponentHand){deck[c.getValue()-1]--;}
        return deck;
    }

    private int calcHand(Collection<Card> hand)
    {
        int ret = 0;
        for(Card c: hand){ret+=c.getValue();}
        return ret;
    }
}
