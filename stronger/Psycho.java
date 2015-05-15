import java.util.Random;

public class Psycho {
    public static void main(String[] args) {
        if(args.length < 1){
            System.out.print("ok");
            System.exit(0);
        }

        Random rnd = new Random();
        rnd.setSeed( System.currentTimeMillis() );

        String[] tokens = args[ rnd.nextInt(args.length) ].split(",");
        String target = tokens[0];

        System.out.print(target);
    }
}
