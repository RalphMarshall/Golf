public class Tank{
    public static void main (String[] args){
        if(args.length == 0){
            System.out.print("ok");
            System.exit(0);
        }

        String myId = args[0];
        int life = Integer.MIN_VALUE;
        String[] tokens = {};
        String opposingId = "";

        for(int i=1; i<args.length; i++){
            tokens = args[i].split(",");
            opposingId = tokens[0];
            life = Integer.parseInt(tokens[1]);
            if(life > 0 && !opposingId.equals(myId)){
                System.out.println(opposingId);
                System.exit(0);
            }
        }
        System.out.println("D");
    }
}
