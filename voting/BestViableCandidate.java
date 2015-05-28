    /**
     * This bot picks the candidate with the highest personal payoff out of those
     * candidates who are not already mathematically eliminated.
     *
     * @author Ralph Marshall
     * @version 5/28/2015
     */
    
    import java.util.List;
    import java.util.ArrayList;
    
    public class BestViableCandidate implements Player
    {
        private static int NUM_CANDIDATES = 3;
    
        public BestViableCandidate(int ignore) {}
    
        public String getName() {
            return "BestViableCandidate";
        }
    
        public int getVote(int [] voteCounts, int votersRemaining, int [] payoffs, int[] totalPayoffs) {
    
            int i, maxVoteSoFar = 0;
    
            // First we figure out the maximum possible number of votes each candidate would get
            // if every remaining bot voted for it
            int [] maxPossibleVotes = new int[NUM_CANDIDATES];
            for (i = 0; i < NUM_CANDIDATES; i++) {
                // The voters remaining does not include me, so we need to add one to it
                maxPossibleVotes[i] = voteCounts[i] + votersRemaining + 1;
    
                if (voteCounts[i] > maxVoteSoFar) {
                    maxVoteSoFar = voteCounts[i];
                }
            }
    
            // Then we throw out anybody who cannot win even if they did get all remaining votes
            List<Integer> viableCandidates = new ArrayList<Integer>();
            for (i = 0; i < NUM_CANDIDATES; i++) {
                if (maxPossibleVotes[i] >= maxVoteSoFar) {
                    viableCandidates.add(Integer.valueOf(i));
                }
            }
    
            // And of the remaining candidates we pick the one that has the highest payoff,
            int maxPayoff = 0, maxCandidate = -1;
            for (Integer candidateIndex : viableCandidates) {
                if (payoffs[candidateIndex] > maxPayoff) {
                    maxPayoff = payoffs[candidateIndex];
                    maxCandidate = candidateIndex;
                }
            }
    
            return maxCandidate;
        }
    
        public void receiveResults(int[] voteCounts, double result) {}
    }
