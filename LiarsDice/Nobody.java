public class Nobody extends Player{

    @Override
    public String bid(int myId, int[] diceEachPlayerHas, int[] myDice,
            String[] bids) {
        int wilds = 0;
        int players = Controller.numPlayers();
        double myKnowledge = (double)diceEachPlayerHas[myId]/Controller.diceInPlay();
        double previousKnowledge = (double)diceEachPlayerHas[(myId-1+players)%players] / Controller.diceInPlay();
        int[] dice = new int[5];
        for (int i = 0; i < myDice.length; i++) {
            if (myDice[i] == 1) {
                wilds++;
            } else {
                dice[myDice[i]-2]++;
            }
        }
        wilds = (int) (1/myKnowledge+wilds-1);
        for (int i = 2; i <= 6; i++) {
            dice[i-2] += wilds;
        }
        String best = "0 0";
        for (int i = 2; i <= 6; i++) {
            if (Controller.isGreaterThan(dice[i-2] + " " + i, best)) {
                best = dice[i-2] + " " + i;
            }
        }
        if (bids.length == 0 || Controller.isGreaterThan(best, bids[bids.length - 1])) {
            return best;
        }
        if (previousKnowledge > 0.4) {
            int prev = Integer.valueOf(bids[bids.length - 1].split(" ")[0]);
            return (prev+1) + " " + bids[bids.length - 1].split(" ")[1];
        }
        return "Liar!";
    }
}
