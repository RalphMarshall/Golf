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
        private int relativeCount = 0;
        private int relativeCountLowerTotal = 0;
        private int totalRuns;
    
        public BestViableCandidate(int r) {
            totalRuns = r;
        }
    
        public String getName() {
            return "BestViableCandidate (" + relativeCount + " from ratio, with " + relativeCountLowerTotal + " tie-breakers of " + totalRuns + " total runs)";
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
    
            // And of the remaining candidates we pick the one that has the personal highest payoff
            // relative to the payoff to the rest of the voters
            int maxCandidate = -1;
            double maxRelativePayoff = -1;
            int maxPayoff = -1;
            int minTotalPayoff = Integer.MAX_VALUE;
            
            int originalMaxCandidate = -1;
            double originalMaxPayoff = -1;
            
            double DELTA = 0.01;
            
            double tiebreakerCandidate = -1;
            
            for (Integer candidateIndex : viableCandidates) {
                double relativePayoff = (double) payoffs[candidateIndex] / (double) totalPayoffs[candidateIndex];
                if (maxRelativePayoff < 0 || relativePayoff - DELTA > maxRelativePayoff) {
                    maxRelativePayoff = relativePayoff;
                    maxCandidate = candidateIndex;

                    maxPayoff = payoffs[candidateIndex];
                    minTotalPayoff = totalPayoffs[candidateIndex];

                } else if (Math.abs(relativePayoff - maxRelativePayoff) < DELTA) {
                    if (totalPayoffs[candidateIndex] < minTotalPayoff) {
                        tiebreakerCandidate = candidateIndex;

                        maxRelativePayoff = relativePayoff;
                        maxCandidate = candidateIndex;

                        maxPayoff = payoffs[candidateIndex];
                        minTotalPayoff = totalPayoffs[candidateIndex];

                    }
                }

                if (payoffs[candidateIndex] > originalMaxPayoff) {
                    originalMaxPayoff = payoffs[candidateIndex];
                    originalMaxCandidate = candidateIndex;
                }
            }
    
            if (tiebreakerCandidate == maxCandidate) {
                relativeCountLowerTotal++;
            }
            
            if (originalMaxCandidate != maxCandidate) {
                /*                System.out.printf("%nSelecting candidate %d with relative payoff %f (%d/%d) instead of %d with relative payoff %f (%d/%d)%n",
                                  maxCandidate, (double) payoffs[maxCandidate]/(double)totalPayoffs[maxCandidate], payoffs[maxCandidate], totalPayoffs[maxCandidate],
                                  originalMaxCandidate, (double) payoffs[originalMaxCandidate]/(double)totalPayoffs[originalMaxCandidate], payoffs[originalMaxCandidate], totalPayoffs[originalMaxCandidate]);
                */
                relativeCount++;
            }

            return maxCandidate;
        }
    
        public void receiveResults(int[] voteCounts, double result) {}
    }
