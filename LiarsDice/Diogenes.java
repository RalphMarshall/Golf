public class Diogenes extends Player {

    private void dumpState(int myId, int [] diceEachPlayerHas, int [] myDice, String [] bids) {
        System.out.printf("My id is %d, there are %d players with a total of %d dice%n", myId, Controller.numPlayers(), Controller.diceInPlay());

        for (int i = 0; i < diceEachPlayerHas.length; i++) {
            System.out.printf("Player %d has %d dice%n", i, diceEachPlayerHas[i]);
        }

        System.out.print("My dice are ");
        for (int i = 0; i < myDice.length; i++) {
            System.out.print("|" + myDice[i]);
        }
        System.out.println();

        System.out.print("Bids are ");
        for (int i = 0; i < bids.length; i++) {
            System.out.print("|" + bids[i]);
        }
        System.out.println();
    }

    @Override
    public String bid(int myId,
                      int[] diceEachPlayerHas,
                      int[] myDice,
                      String[] bids) {

        dumpState(myId, diceEachPlayerHas, myDice, bids);

        int wilds = 0;
        int players = Controller.numPlayers();

        double myKnowledge = (double) diceEachPlayerHas[myId]/ Controller.diceInPlay();
        double previousKnowledge = (double) diceEachPlayerHas[(myId-1+players) % players] / Controller.diceInPlay();

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

        final String retval;
        if (bids.length == 0 || Controller.isGreaterThan(best, bids[bids.length - 1])) {
            retval = best;
        } else if (previousKnowledge > 0.4) {
            int prev = Integer.valueOf(bids[bids.length - 1].split(" ")[0]);

            retval = (prev+1) + " " + bids[bids.length - 1].split(" ")[1];
        } else {
            retval = "Liar!";
        }

        System.out.println("I am returning a bid of " + retval);
        return retval;
    }
}
