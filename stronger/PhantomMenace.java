public class PhantomMenace {
    public static void main(String[] args) {
        if(args.length < 1)
        {
            System.out.print("ok");
            System.exit(0);
        }

           String me = args[0], target="D", secondTarget="D", worstTarget="D";
           int best=0;
           int attackerCount = 0;
           int numBots= 0;
           int secondBest=0;
           int worst=0;

        for(int i=1;i<args.length;i++)
        {
            String[] tokens = args[i].split(",");
            int life = Integer.valueOf(tokens[1]);
            if(life > 0 && life >= best)
            {
                secondBest = best;
                secondTarget = target;
                best = life;
                target = tokens[0];

            }
            else if(life > 0 && life >= secondBest)
            {
                secondBest= life;
                secondTarget = tokens[0];
            }

            if(life > 0 && life <= best)
            {
            worst = life;
            worstTarget = tokens[0];
            }
            // count incoming attacks
            if(tokens[3].equals(me))
            attackerCount++;
            // count living bots
            if(life>0)
            numBots++;
        }

        // activate offensive regime?!
        if(numBots<5)
        {
            if(target.equals(me))
              System.out.print(secondTarget);
            else
              System.out.print(target);
        }
        else
        {
          if(worstTarget.equals(me))
            System.out.print("D");
          if(target.equals(me))
            System.out.print("D");
          else if(attackerCount>0)
            System.out.print("D");
          else
            System.out.print(target);
        }

    }
}
