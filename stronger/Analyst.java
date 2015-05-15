public class Analyst {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("ok");
            System.exit(0);
        }
        String me = args[0], that = "D";
        int maxThreat = 200;
        for (int i = 1; i < args.length; i++) {
            String[] player = args[i].split(",");
            int threat = Integer.parseInt(player[2]) * 100
                         * (!player[3].equals("D") ? 5 : 1)
                         * (player[3].equals(me) ? 5 : 1)
                         - Integer.parseInt(player[1]);
            if (threat > maxThreat && Integer.parseInt(player[1]) > 0 && !player[0].equals(me)) {
                maxThreat = threat;
                that = player[0];
            }
        }
        System.out.println(that);
    }
}
