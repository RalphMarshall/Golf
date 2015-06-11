import java.lang.Math;
import java.util.Random;
/**
 * This bot votes for a random candidate.
 * 
 * @author PhiNotPi 
 * @version 5/27/15
 */
public class RandomBot implements Player
{
    Random rnd;
    String name;
    /**
     * Constructor for objects of class RandomBot
     */
    public RandomBot(int e)
    {
       rnd = new Random(); 
       name = "RandomBot" + rnd.nextInt(1000);
    }

    public String getName()
    {
        return name;
    }
    
    public int getVote(int [] voteCounts, int votersRemaining, int [] payoffs, int[] totalPayoffs)
    {
        return rnd.nextInt(3);
    }
    
    public void receiveResults(int[] voteCounts, double result)
    {
        
    }
}
