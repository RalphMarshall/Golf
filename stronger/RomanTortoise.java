import java.util.Random;

public class RomanTortoise {

    public static void main(String[] args) {
        Random r = new Random();
        if(args.length < 1){
            System.out.print("ok");
            System.exit(0);
        }
        String me = args[0];

        if(args[1].split(",")[4].equals("X"))
        {
             System.out.print("D");
             System.exit(0);
        }
        for(int i = 1; i<args.length; i++)
        {
            if((args[i].split(",")[4]==me) && (Integer.parseInt(args[i].split(",")[2])>0))
            {
                //probably attack that bot
                if(r.nextInt()%50 != 0)
                {
                    System.out.print(args[i].split(",")[1]);
                    System.exit(0);
                }
            }
        }
        if(r.nextInt()%4 == 0)
        {
            for(int i = 0; i<100; i++)
            {
                int j = (r.nextInt() % (args.length-1)) + 1;
                if(Integer.parseInt(args[j].split(",")[2])>0)
                {
                    System.out.print(args[j].split(",")[1]);
                    System.exit(0);
                }
            }
        }
        System.out.print("D");
        System.exit(0);
    }

}
