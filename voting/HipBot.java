public class HipBot implements Player{

    public HipBot(int rounds){}

    public String getName(){ return "HipBot"; }

    public int getVote(int [] voteCounts, int votersRemaining, int [] payoffs, int[] totalPayoffs){

        int coolest = 0;
        int lowest = 100000000;

        for( int count = 0; count < voteCounts.length; count++ ){

            if( voteCounts[count] <= lowest ){

                lowest = voteCounts[count];
                coolest = count;

            }

        }

        return coolest;


    }

    public void receiveResults(int[] voteCounts, double result){}

}
