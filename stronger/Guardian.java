public class Guardian{
    public static void main (String[] args){
        if(args.length == 0){
            System.out.print("ok");
            System.exit(0);
        }

        String myId = args[0];
        int lowestLife = Integer.MAX_VALUE;
        int life = Integer.MIN_VALUE;
        String[] tokens = {};
        String opposingId = "";
        String weakestOpponent = "";
        String lastTarget = "";

        for(int i=1; i<args.length; i++){
            tokens = args[i].split(",");
            opposingId = tokens[0];
            life = Integer.parseInt(tokens[1]);
            lastTarget = tokens[3];
            if(life < lowestLife && life > 0 &&
                !opposingId.equals(myId) &&
                !opposingId.equals(lastTarget)){
                weakestOpponent = opposingId;
            }
        }

        for(int i=1; i<args.length; i++){
            tokens = args[i].split(",");
            opposingId = tokens[0];
            life = Integer.parseInt(tokens[1]);
            lastTarget = tokens[3];
            if (lastTarget.equals(weakestOpponent) &&
                life > 10){
                System.out.println(opposingId);
                System.exit(0);
            }
        }

        System.out.println("D");
    }
}
