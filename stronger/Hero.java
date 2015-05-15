public class Hero {
    public static void main(String[] args) {
        if(args.length < 1){
            System.out.print("ok");
            System.exit(0);
        }
        String me = args[0], target="D";
        int best=0;
        for(int i=1;i<args.length;i++){
            String[] tokens = args[i].split(",");
            int life = Integer.valueOf(tokens[1]);
            if(life > 0 && life >= best && !tokens[0].equals(me)){
                best = life;
                target = tokens[0];
            }
        }
        System.out.print(target);
    }
}
