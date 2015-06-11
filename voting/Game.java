import java.lang.Math;
import java.util.Random;
/**
 * Procedure for a single election
 * 
 * @author PhiNotPi 
 * @version 5/27/15
 */
public class Game
{
    Player[] players;
    int[][] payoffs;
    int[] totalPayoffs;
    int[] voteCounts;
    int debug;
    Random rnd;
    public Game(Player [] p, int dbg)
    {
        rnd = new Random();
        players = p;
        debug = dbg;
        shuffle(players);
        payoffs = new int[players.length][3];
        totalPayoffs = new int[]{0,0,0};
        for(int i = 0; i < players.length; i++){
            payoffs[i] = createPlayerPayoffs();
        }
        
        if(debug > 0){
            System.out.printf("%n%5d%5d%5d - %-40s%n",totalPayoffs[0],totalPayoffs[1],totalPayoffs[2],"Total Payoffs");
        }
    }
    
    public void shuffle(Object[] ar)
    {
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            Object a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }
    
    public int[] createPlayerPayoffs()
    {
        int cut1;
        int cut2;
        do{
            cut1 = rnd.nextInt(101);
            cut2 = rnd.nextInt(101);  
        } while (cut1 + cut2 > 100);
        int rem = 100 - cut1 - cut2;
        int[] set = new int[]{cut1,cut2,rem};
        totalPayoffs[0] += set[0];
        totalPayoffs[1] += set[1];
        totalPayoffs[2] += set[2];
        return set;
    }
    
    public int[] copy(int[] source)  // I think this is needed to prevent players from modifying our stuff
    {
        int [] dest = new int[source.length];
        for(int i = 0; i<source.length; i++)
        {
            dest[i] = source[i];
        }
        return dest;
    }
    
    public double[] run()
    {
        voteCounts = new int[]{0,0,0};
        if(debug > 0){
            System.out.println("vote vc0 vc1 vc2 vrm po0 po1 po2 - name");
        }
        for(int i = players.length - 1; i >= 0; i--)
        {
            int vote = players[i].getVote(copy(voteCounts),i,copy(payoffs[i]),copy(totalPayoffs));
            if(vote >= 0 && vote <= 2)
            {
                voteCounts[vote]++;
            }
            else
            {
                voteCounts[rnd.nextInt(3)]++;
            }
            if(debug > 0){
                System.out.printf("%4d",vote);
                for(int vc : voteCounts){System.out.printf("%4d",vc);}
                System.out.printf("%4d",i);
                for(int po : payoffs[i]){System.out.printf("%4d",po);}
                System.out.printf(" - %-40s%n",players[i].getName());
            }
        }
        int highest = 0;
        for(int count : voteCounts)
        {
            if(count > highest)
            {
                highest = count;
            }
        }
        boolean[] won = new boolean[]{false,false,false};
        int winCount = 0;
        double[] results = new double[players.length];
        for(int c = 0; c < 3; c++)
        {
            if(voteCounts[c] == highest)
            {
                winCount++;
                for(int p = 0; p < players.length; p++)
                {
                    results[p] += payoffs[p][c];
                }
            }
        }
        
        for(int p = 0; p < players.length; p++)
        {
            results[p] /= winCount;
            players[p].receiveResults(copy(voteCounts),results[p]);
        }
        return results;
    }
}
