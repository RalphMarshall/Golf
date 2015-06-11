public abstract class Player {
    public Player(){}
    public String toString(){
        return this.getClass().getSimpleName();
    }
    public abstract String bid(int yourId, int[] diceEachPlayerHas,int[] yourDice,String[] bids);
}
