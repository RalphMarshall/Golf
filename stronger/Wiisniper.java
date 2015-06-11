public class Wiisniper {
    public static void main(String[] args) {
        if(args.length < 1){
            System.out.print("ok");
            System.exit(0);
        }
        String me = args[0], target="D";
        double bestRatio = Integer.MIN_VALUE;

        String[] tokens = args[Integer.valueOf(me)].split(",");
        double myLife = Integer.valueOf(tokens[1]);
        double myPower = Integer.valueOf(tokens[2]);

        for(int i=1;i<args.length;i++){
            tokens = args[i].split(",");
            double life = Integer.valueOf(tokens[1]);
            double power = Integer.valueOf(tokens[2]);
            double ratio = myLife/power - life/myPower;
            if(tokens[3].equals(me)) //attacks my bot
                ratio = ratio * 5;
            if(life > 0 && power > 0 && !tokens[0].equals(me) && myPower > 0 && ratio > bestRatio){
                bestRatio = ratio;
                target = tokens[0];
            }
        }
        System.out.print(target);
    }
}
