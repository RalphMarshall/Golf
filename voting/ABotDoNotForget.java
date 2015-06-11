import java.util.ArrayList;

public class ABotDoNotForget implements Player
{
    private int nbElec;
    private int countElec=0;
    private int[] currPayoffs=new int[3];
    private int[] lmh=new int[3];
    private int[] wins=new int[3];

    public ABotDoNotForget(int nbElec)
    {
        this.nbElec=nbElec;
    }

    public String getName() {return "ABotDoNotForget";}

    public int getVote(int[] voteCounts, 
                        int votersRemaining, 
                        int[] payoffs,
                        int[] totalPayoffs) 
    {
        countElec++;
        System.arraycopy(totalPayoffs, 0, currPayoffs, 0, totalPayoffs.length);

        if(countElec<=nbElec/20&&countElec<=20)
        {
            int best=0;
            for(int i=1;i<payoffs.length;i++)
                if(payoffs[i]>=payoffs[best])
                    best=i;
            return best;
        }

        for(int i =1;i<totalPayoffs.length;i++)
        {
            if(totalPayoffs[i]<totalPayoffs[i-1])
            {
                int tmp= totalPayoffs[i];
                totalPayoffs[i]=totalPayoffs[i-1];
                totalPayoffs[i-1]=tmp;
                if(i==2&&totalPayoffs[i-1]<totalPayoffs[i-2]){
                    tmp= totalPayoffs[i-1];
                    totalPayoffs[i-1]=totalPayoffs[i-2];
                    totalPayoffs[i-2]=tmp;
                }
            }
        }
        lmhDist(currPayoffs,totalPayoffs);
        int best=0;
        for(int i=1;i<wins.length;i++)
            if(wins[i]>=wins[best]){
                best=i;
            }
        return lmh[best];

    }

    public void receiveResults(int[] voteCounts, double result) 
    {
        int best=0,bestV=voteCounts[best];
        for(int i=1;i<voteCounts.length;i++)
            if(voteCounts[i]>=bestV){
                best=i;
                bestV=voteCounts[i];
            }
        wins[lmh[best]]++;

    }

    private void lmhDist(int[] a,int[] s)
    {
        ArrayList<Integer> al = new ArrayList<Integer>();
        al.add(a[0]);al.add(a[1]);al.add(a[2]);
        lmh[0]=al.indexOf(s[0]);
        lmh[1]=al.indexOf(s[1]);
        lmh[2]=al.indexOf(s[2]);

    }
}
