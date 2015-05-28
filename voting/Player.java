public interface Player
{
	public String getName();
	public int getVote(int [] voteCounts, int votersRemaining, int [] payoffs, int[] totalPayoffs);
	public void receiveResults(int[] voteCounts, double result);
}
