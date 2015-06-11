public class SmashAttemptByEquality implements Player {
    static private int elections;

    public SmashAttemptByEquality(int e) { 
        this.elections = e;
    }

    public String getName() {
        return "SmashAttemptByEquality (on " + String.valueOf(this.elections) + " elections)";
    }

    public int getVote(int[] voteCounts, int votersRemaining, int[] payoffs, int[] totalPayoffs) {

        //if there are no votes or it is a tie
        if(voteCounts.length == 0 || (voteCounts[0] == voteCounts[1] && voteCounts[1] == voteCounts[2]))
        {
            //let the system handle the (distributed?) randomness
            return 3;
        }

        //we want to win, so, lets not mess when there are no voters left
        if( votersRemaining > 0 )
        {
            //lets bring some equality!
            if( voteCounts[0] >= voteCounts[1] )
            {
                if(voteCounts[0] > voteCounts[2])
                {
                    return 2;
                }
                else
                {
                    return 0;
                }
            }
            else if( voteCounts[1] >= voteCounts[2] )
            {
                if(voteCounts[1] > voteCounts[0])
                {
                    return 0;
                }
                else
                {
                    return 1;
                }
            }
            else
            {
                return 0;
            }
        }
        else
        {
            //just play for the winner!
            if( voteCounts[0] >= voteCounts[1] )
            {
                if(voteCounts[0] > voteCounts[2])
                {
                    return 0;
                }
                else
                {
                    return 2;
                }
            }
            else if( voteCounts[1] >= voteCounts[2] )
            {
                if(voteCounts[1] > voteCounts[0])
                {
                    return 1;
                }
                else
                {
                    return 0;
                }
            }
            else
            {
                return 0;
            }
        }
    }

    public void receiveResults(int[] voteCounts, double result) { }
}
