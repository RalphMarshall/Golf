public class Equaliser {
    public static void main (String[] args) {
        if (args.length == 0) {
            System.out.print("ok");
            System.exit(0);
        }

        String myId = args[0];
        String[] tokens;
        int life, power;
        int lowestPower = Integer.MAX_VALUE;
        String lowest = "";
        int lowestLife = 1000;
        int myLife = 0;

        for (int i=1; i<args.length; i++) {
            tokens = args[i].split(",");
            life = Integer.parseInt(tokens[1]);
            power = Integer.parseInt(tokens[2]);
            if (!tokens[0].equals(myId)) {
                if (life > 0 && power < lowestPower && !tokens[3].equals("D")) {
                    lowest = tokens[0];
                    lowestPower = power;
                }
                lowestLife = Math.min(life, lowestLife);
            } else {
                myLife = life;
            }
        }

        if (myLife < lowestLife*5/4) {
            // IT'S TIME TO DEFEND!
            System.out.println("D");
        } else if (lowestPower != Integer.MAX_VALUE) {
            System.out.println(lowest);
        } else {
            // Them buffed up perma-defenders don't need no power
            System.out.println("D");
            // And if you can't beat 'em, join 'em
        }
    }
}
