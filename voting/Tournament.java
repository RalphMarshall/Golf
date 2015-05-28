import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.lang.Math;
import java.util.Comparator;
import java.util.Set;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.FileWriter;
/**
 * This is the main controller for the Strategic Voting KOTH
 *
 * @author PhiNotPi
 * @version 5/27/15
 */
public class Tournament
{
    static final int elections = 10000; //number of elections to be performed
    static final int debug = 0;
    public static void main(String [] args)
    {
        long startTime = System.nanoTime();

        Player[] players = new Player[] {  //There's no separate file for contestants
            new PersonalFavoriteBot(elections), //name
            new RandomBot(elections),
            new Follower(elections),
            new NoThirdPartyBot(elections),
            new CircumspectBot(elections),
            new SometimesSecondBestBot(elections),
            new Optimist(elections),
            new ABotDoNotForget(elections),
            new ExtremistBot(elections),
            new SmashAttemptByEquality(elections),
            new Odysseus(elections),
            new HipBot(elections),
            new BestViableCandidate(elections)
        };
        final Map<Player,Double>  score = new HashMap<Player,Double>();
        for(Player p : players) {
            score.put(p, new Double(0));
        }

        for(int e = 0; e < elections; e++) {
            //            System.out.printf("%nRound %d%n", e);
            Game g = new Game(players,debug);
            double[] results = g.run();
            for(int p = 0; p < players.length; p++) {
                score.put(players[p], score.get(players[p]) + results[p]);
                //                System.out.printf("Player %s scored %f%n", players[p].getName(), results[p]);
            }
        }

        Arrays.sort(players, new Comparator<Player>() {
                public int compare(Player a, Player b) {
                    return score.get(b).compareTo(score.get(a));
                }
            });

        System.out.printf("%nLeaderboard:%n");;
        for(Player p : players) {
            System.out.printf("%15.2f (%7.2f) - %-40s%n",score.get(p),getNormalizedScore(score.get(p)),p.getName());
        }

        long endTime = System.nanoTime();

        System.out.println("The tournament took " + ((endTime-startTime)/1000000000.0) + " seconds" );
    }

    public static double getNormalizedScore(double score) {
        score -= elections * 100.0 / 3;
        score /= Math.sqrt(elections) * 23.923489154664153;
        return score;
    }
}
