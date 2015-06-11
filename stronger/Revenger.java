public class Revenger {
    public static void main(String[] args) {
        if(args.length < 1){
            System.out.print("ok");
            System.exit(0);
        }

        String me = args[0], target = "D";
        int last_attacker_power = -1;

        for(int i=1; i<args.length; ++i){
            String[] tokens = args[i].split(",");
            int power = Integer.parseInt(tokens[2]);
            int life  = Integer.parseInt(tokens[1]);

            if( tokens[3].equals(me)
             && power>last_attacker_power
             && life>0  ){
                target = tokens[0];
                last_attacker_power = power;
            }
        }

        System.out.print(target);
    }
}
