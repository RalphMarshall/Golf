import java.lang.Integer;
import java.lang.String;
import java.util.Arrays;
import java.util.Comparator;

public class Optimist implements Player
{
    public Optimist(int _) { }
    public String getName() { return "Optimist"; }
    public int getVote(int[] curVotes, int rem, final int[] payoffs, int[] _)
    {
        Integer[] opt = new Integer[] { 0, 1, 2 };
        Arrays.sort(opt, new Comparator<Integer>() { public int compare(Integer i1, Integer i2) { return payoffs[i1] > payoffs[i2] ? -1 : payoffs[i1] == payoffs[i2] ? 0 : 1; } });
        int a = curVotes[opt[0]], b = curVotes[opt[1]], c = curVotes[opt[2]];
        double rest = (double)rem / 4;
        if (b <= a + rest && c <= a + rest)
            return opt[0];
        else if (c <= b)
            return opt[1];
        else
            return opt[0];
    }
    public void receiveResults(int[] _, double __) { }
}
