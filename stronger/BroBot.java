public class BroBot {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("ok");
            return;
        }

        int health = 0, id = Integer.parseInt(args[0]);
        for (int k = 1; k < args.length; k++) {
            if (Integer.parseInt(args[k].split(",")[0]) == id) {
                health = Integer.parseInt(args[k].split(",")[1]);
                break;
            }
        }

        String action = "";
        for (String s : args) {
            if (!s.contains(",")) continue;
            String[] botInfo = s.split(",");
            int botId = Integer.parseInt(botInfo[0]);
            if (botId == id) continue;
            if (!botInfo[3].equals("D")) {
                if (Integer.parseInt(botInfo[3]) == id && Integer.parseInt(botInfo[2]) >= health / 9) {
                    action = "D";
                    break;
                }
            }
        }

        if (action.isEmpty()) {
            int max = 0;
            for (String s : args) {
                if (!s.contains(",")) continue;
                String[] botInfo = s.split(",");
                int botId = Integer.parseInt(botInfo[0]);
                if (botId == id) continue;
                int attack = Integer.parseInt(botInfo[2]);
                if (attack > max) {
                    attack = max;
                    action = botInfo[0];
                }
            }
        }
        System.out.println(action);
    }
}
