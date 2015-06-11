public class Odysseus implements Player {

    @Override
    public String getName() {
        return "Quick Thinking Odysseus";
    }

    @Override
    public int getVote(int[] voteCounts, int votersRemaining, int[] payoffs,
            int[] totalPayoffs) {

        double c0 = probablePayoff(voteCounts.clone(), votersRemaining, payoffs, 0);
        double c1 = probablePayoff(voteCounts.clone(), votersRemaining, totalPayoffs, 1);
        double c2 = probablePayoff(voteCounts.clone(), votersRemaining, totalPayoffs, 2);
        if(c0 > c1 && c0 > c2)
        {
            return 0;
        }
        else if (c1>c2)
        {
            return 1;
        }
        else
        {
            return 2;
        }

    }

    private double probablePayoff(int[] voteCounts, int votersRemaining,
            int[] payoffs, int i) {

        if(votersRemaining < 0)
        {
            if(voteCounts[0] > voteCounts[1] && voteCounts[0] > voteCounts[2])
            {

                return payoffs[0];
            }
            else if (voteCounts[1]>voteCounts[0] && voteCounts[1]>voteCounts[2])
            {
                return payoffs[1];
            }
            else if (voteCounts[2]>voteCounts[0] && voteCounts[2] > voteCounts[1])
            {
                return payoffs[2];
            }
            else if (voteCounts[0] == voteCounts[1])
            {
                if(voteCounts[0]==voteCounts[2])
                {
                    return ((double)(payoffs[0] + payoffs[1] + payoffs[2]))/3;
                }
                else
                {
                    return ((double)(payoffs[0]+payoffs[1]))/2;
                }
            }
            else if(voteCounts[0]==voteCounts[2])
            {
                return ((double)(payoffs[0]+payoffs[2]))/2;
            }
            else if (voteCounts[1]==voteCounts[2])
            {
                return ((double)(payoffs[1]+payoffs[2]))/2;
            }
            else
            {
                //something went very wrong
                return 0;
            }
        }
        else
        {
            voteCounts[i]++;
            if((voteCounts[0]-voteCounts[1])>votersRemaining+1 && (voteCounts[0]-voteCounts[2])>votersRemaining+1)
            {
                return payoffs[0];
            }
            else if((voteCounts[1]-voteCounts[0])>votersRemaining+1 && (voteCounts[1]-voteCounts[2])>votersRemaining+1)
            {
                return payoffs[1];
            }
            else if((voteCounts[2]-voteCounts[1])>votersRemaining+1 && (voteCounts[2]-voteCounts[0])>votersRemaining+1)
            {
                return payoffs[2];
            }
            votersRemaining--;
            double p = probablePayoff(voteCounts.clone(), votersRemaining, payoffs, 0)/3;
            p+= probablePayoff(voteCounts.clone(), votersRemaining, payoffs, 1)/3;
            p+=probablePayoff(voteCounts.clone(), votersRemaining, payoffs, 2)/3;
            return p;
        }

    }

    @Override
    public void receiveResults(int[] voteCounts, double result) {
        //Don't do anything

    }
    public Odysseus(int i)
    {

    }
}
