public class Follower implements Player
{
    public Follower(int e) { }

    public String getName()
    {
        return "Follower";
    }

    public int getVote(int [] voteCounts, int votersRemaining, int [] payoffs, int[] totalPayoffs)
    {
        int mostPopular = 0;
        int mostVotes = voteCounts[0];
        for (int i = 1; i < voteCounts.length; i++) {
            int votes = voteCounts[i];
            if (votes > mostVotes || (votes == mostVotes && payoffs[i] > payoffs[mostPopular])) {
                mostPopular = i;
                mostVotes = votes;
            }
        }
        return mostPopular;

    }

    public void receiveResults(int[] voteCounts, double result) { }
}
