import java.lang.Math;
/**
 * Created by Admin on 5/27/2015.
 */
public class SometimesSecondBestBot implements Player {
    public SometimesSecondBestBot(int e) { }

    public String getName() {
        return "SometimesSecondBestBot";
    }

    public int getVote(int [] voteCounts, int votersRemaining, int [] payoffs, int[] totalPayoffs) {
        int m = 0;
        int n = 0;
        for(int i = 1; i< 3; i++) {
            if(payoffs[i] > payoffs[m]) { n = m; m = i; }
        }
        return (voteCounts[n]>voteCounts[m]&&totalPayoffs[n]>totalPayoffs[m])||(voteCounts[m]+votersRemaining<voteCounts[n])||voteCounts[m]+votersRemaining<voteCounts[Math.min(3-n-m, 2)] ? n : m;
    }

    public void receiveResults(int[] voteCounts, double result) { }
}
