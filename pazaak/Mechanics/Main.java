package Mechanics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

import Players.*;

public class Main {

    static Scanner in = new Scanner(System.in);

    public static int HAND_GOAL = 20;

    private static boolean isDebug = true;

    public static void main(String[] args) {
        Player[] players = {new DumbBoldPlayer(), new CincinnatiKid(), new DumbCautiousPlayer(), new AustinPowers(), new Bastila(), new Nestor()};
        int[] wins = new int[players.length];

        int [] winningScores = new int[20];
        for (int i = 0; i < 20; i++) {
            winningScores[i] = 0;
        }

        double totalWins = 0;
        for (int numRounds = 0; numRounds < 1000000; numRounds++) {

            for(int a = 0; a < players.length - 1; a++) {
                for(int b = a + 1; b < players.length; b++) {
                    Player[] thisRound = {players[a], players[b]};
                    int[] newWins = playGame(thisRound, winningScores);
                    wins[a]+=newWins[0];
                    wins[b]+=newWins[1];
                    totalWins += newWins[0] + newWins[1];
                }
            }

        }

        for(int i = 0; i < players.length; i++) {
            System.out.println(players[i].name+": " + wins[i] + " " + ((double) wins[i]/totalWins));
        }

        for (int i = 0; i < winningScores.length; i++) {
            int actualScore = i+1;
            System.out.println("Wins for score " + actualScore + "=" + winningScores[i] + ", " + winningScores[i]/totalWins);
        }
    }

    private static void logMsg(String s) {
        if (isDebug) {
            System.out.println(s);
        }
    }

    private static int[] playGame(Player[] players, int [] winningScores) {
        Deck deck = new MainDeck();
        Player p1 = players[0];
        Player p2 = players[1];
        logMsg("New tourney: "+p1.name+" vs. "+p2.name);
        ArrayList<Card> p1SideDeck = new SideDeck().getSideDeck();
        ArrayList<Card> p2SideDeck = new SideDeck().getSideDeck();
        int[] wins = {0, 0};

        while(wins[0] < 3 && wins[1] < 3) {

            logMsg("\nNew Game!");
            logMsg("The standings are "+wins[0]+" to "+wins[1]);

            ArrayList<Card> p1Hand = new ArrayList<Card>();
            ArrayList<Card> p2Hand = new ArrayList<Card>();
            boolean p1DidPlay = false;
            boolean p2DidPlay = false;
            Collections.shuffle(deck.getDeck());
            Queue<Card> gameDeck = new LinkedList<>(deck.getDeck());
            p1.setAction(null);
            p2.setAction(null);

            Player.Action chosenAction = Player.Action.STAND;
            while(!gameIsOver(p1, p2, p1Hand, p2Hand, wins, winningScores)) {
                if(p1.getAction()!=Player.Action.STAND) {
                    p1Hand.add(gameDeck.remove());

                    logMsg(p1.name+"'s Hand: "+p1Hand.toString() + ", value=" + handValue(p1Hand));

                    if(handValue(p1Hand)!=HAND_GOAL) {
                        p1.getResponse(wins, true,
                                       Collections.unmodifiableCollection(p1Hand),
                                       Collections.unmodifiableCollection(p2Hand),
                                       Collections.unmodifiableCollection(p1SideDeck),
                                       p2SideDeck.size(), p2.getAction(), p2DidPlay);

                        chosenAction = p1.getAction();
                        switch(p1.getAction()) {
                        case END:
                            if(handValue(p1Hand)>HAND_GOAL)
                                break;
                        case STAND:
                            p1DidPlay = false;
                            break;
                        case PLAY:
                            p1DidPlay = true;
                            Card cardToPlay = p1.getCardToPlay();
                            if(p1SideDeck.contains(cardToPlay)) {
                                p1SideDeck.remove(cardToPlay);
                                p1Hand.add(cardToPlay);
                            }
                            p1.setAction(Player.Action.STAND);
                            break;
                        }
                    }
                    else {
                        p1DidPlay = false;
                        p1.setAction(Player.Action.STAND);
                    }
                }
                logMsg(p1.name+" has chosen to "+chosenAction);
                if(p2.getAction()!=Player.Action.STAND) {
                    p2Hand.add(gameDeck.remove());

                    logMsg(p2.name+"'s Hand: "+p2Hand.toString() + ", value=" + handValue(p2Hand));

                    if(handValue(p2Hand)!=HAND_GOAL) {
                        p2.getResponse(wins, false,
                                       Collections.unmodifiableCollection(p2Hand),
                                       Collections.unmodifiableCollection(p1Hand),
                                       Collections.unmodifiableCollection(p2SideDeck),
                                       p1SideDeck.size(), p1.getAction(), p1DidPlay);

                        chosenAction = p2.getAction();
                        switch(p2.getAction()) {
                        case END:
                            p2DidPlay = false;
                            break;
                        case STAND:
                            p2DidPlay = false;
                            break;
                        case PLAY:
                            p2DidPlay = true;
                            Card cardToPlay = p2.getCardToPlay();
                            if(p2SideDeck.contains(cardToPlay)) {
                                p2SideDeck.remove(cardToPlay);
                                p2Hand.add(cardToPlay);
                            }
                            p2.setAction(Player.Action.STAND);
                        }
                    }
                    else {
                        p2DidPlay = false;
                        p2.setAction(Player.Action.STAND);
                    }
                }
                logMsg(p2.name+" has chosen to "+chosenAction);
            }
        }
        return wins;
    }

    public static int handValue(ArrayList<Card> hand) {
        int sum = 0;
        for(int i=0; i<hand.size();i++)
            sum += hand.get(i).getValue();
        return sum;
    }

    private static boolean gameIsOver(Player p1, Player p2, ArrayList<Card> p1Hand, ArrayList<Card> p2Hand, int[] wins, int [] winningScores) {
        int p1HandValue = handValue(p1Hand);
        int p2HandValue = handValue(p2Hand);
        /*
         * Checks if one player bombed out.
         * If so, the player who did not bomb out gets the win.
         * If both bombed out, the game ends in a tie.
         */
        if(p1HandValue > HAND_GOAL) {
            if(p2HandValue <= HAND_GOAL) {
                logMsg(p1.name+" has bombed out! "+p2.name+" wins!");
                wins[1]++;
                winningScores[p2HandValue-1]++;
                return true;
            }
            else {
                logMsg("Both players have bombed out! Tie!");
                return true;
            }
        }
        else if(p2HandValue > HAND_GOAL) {
            logMsg(p2.name+" has bombed out! "+p1.name+" wins!");
            wins[0]++;
            winningScores[p1HandValue-1]++;
            return true;
        }
        /*
         * Checks if one player has elected to stand.
         * If so, play will continue with the non-standing player.
         * If both elected to stand, the player with the highest hand value wins earning them two points.
         * If the hand values are equal, the game ends in a tie.
         */
        if(p1.getAction()==Player.Action.STAND) {
            if(p2.getAction()==Player.Action.STAND) {
                if(p1HandValue > p2HandValue) {
                    logMsg(p1.name+" wins!");
                    winningScores[p1HandValue-1]++;
                    wins[0]++;
                    return true;
                }
                else if(p2HandValue>p1HandValue) {
                    logMsg(p2.name+" wins!");
                    winningScores[p2HandValue-1]++;
                    wins[1]++;
                    return true;
                }
                else {
                    return true;
                }
            }
            else if(p2HandValue > p1HandValue) {
                System.out.println(p2.name+" wins!");
                winningScores[p2HandValue-1]++;
                wins[1]++;
                return true;
            }
            else
                return false;
        }
        else if(p2.getAction()==Player.Action.STAND) {
            if(p1HandValue>p2HandValue) {
                logMsg(p1.name+" wins!");
                winningScores[p1HandValue-1]++;
                wins[0]++;
                return true;
            }
        }
        return false;
    }

}
