public class ExtremistBot implements Player
{
    public ExtremistBot(int e){}

    public void receiveResults(int[] voteCounts, double result){}

    public String getName(){
        return "ExtremistBot";
    }

    public int getVote(int [] voteCounts, int votersRemaining, int [] payoffs, int[] totalPayoffs)
    {
        int min = 0;
        for(int i = 1; i<payoffs.length; i++){
            if(payoffs[i] <payoffs[min]){
                min = i;
            }
        }
        return min;
    }
}
