package Players;

import java.util.Collection;

import Mechanics.*;

public class Bastila extends Player {

    public Bastila() {
        name = "Bastila";
    }

    public void getResponse(int wins[], boolean isPlayerOne,
            Collection<Card> myHand, Collection<Card> opponentHand,
            Collection<Card> mySideDeck, int opponentSideDeckCount,
            Action opponentAction, boolean opponentDidPlay) {


        action = null;
        cardToPlay = null;

        //Constants
        int stand = 17;
        int conservatism = 2;

        //Get some info
        int handVal = handValue(myHand);
        int expected = expectedValue(myHand);

        //Can I play from my side deck?
        for(Card side: mySideDeck){
            int total = side.getValue() + handVal;
            if(total >= stand && total <= 20){
                cardToPlay = side;
                action = Player.Action.PLAY;
            }
        }
        if(action == Player.Action.PLAY){
            return;
        }

        //Otherwise, will I go bust?
        if(handVal + expected > 20 - conservatism){
            action = Player.Action.STAND;
        }
        else{
            action = Player.Action.END;
        }

        return;

    }

    private int handValue(Collection<Card> hand) {
        int handValue = 0;
        for(Card c : hand){
            handValue += c.getValue();
        }
        return handValue;
    }

    private int expectedValue(Collection<Card> hand){
        //Net value of the deck is 55*4 = 220
        int total = 220;
        int count = 40;
        for(Card c : hand){
            total -= c.getValue();
            count--;
        }
        return total/count;
    }

}
