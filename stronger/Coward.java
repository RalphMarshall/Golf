public class Coward {
    public static void main(String[] args) {
        if(args.length < 1){
            System.out.print("ok");
            System.exit(0);
        }
        String me = args[0], action="D";
        int best=Integer.MAX_VALUE;
        for(int i=1;i<args.length;i++){
            String[] tokens = args[i].split(",");
            if(tokens[0].equals(me)){
                if(Integer.valueOf(tokens[1]) > 500){
                    System.out.print("D");
                    System.exit(0);
                }
            }
        }
        for(int i=1;i<args.length;i++){
            String[] tokens = args[i].split(",");
            int power = Integer.valueOf(tokens[2]);
            if(power <= best && !tokens[0].equals(me) && Integer.valueOf(tokens[1]) > 0){
                best = power;
                action = tokens[0];
            }
        }
        System.out.print(action);
    }
}
