import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SisBot {
    private static final Path directoryPath = Paths.get("state");
    private static final Path filePath = Paths.get("state" + java.io.File.separator + "SisBot.txt");
    private List<Bot> botList;
    private List<String> fileContents;
    private int id, health, power, turn;

    public SisBot(String[] args) throws IOException {
        if (args.length == 0) {
            createSaveFile();
            System.out.println("ok");
            System.exit(0);
        }
        fileContents = Files.readAllLines(filePath, Charset.defaultCharset());
        for (int k = 1; k < args.length; k++) {
            if (args[k].split(",")[3].equals("X")) {
                Files.write(filePath, "".getBytes());
                fileContents.clear();
                break;
            }
        }
        getBots(args);
        makeMove();
        writeBots();
    }

    private void createSaveFile() throws IOException {
        if (!Files.exists(filePath)) {
            if (!Files.exists(directoryPath))
                Files.createDirectory(directoryPath);
            Files.createFile(filePath);
        }
        else
            Files.write(filePath, "".getBytes());
    }

    private void getBots(String[] args) {
        id = Integer.parseInt(args[0]);
        botList = new ArrayList<Bot>();
        for (int k = 1; k < args.length; k++) {
            String[] botInfo = args[k].split(",");
            if (Integer.parseInt(botInfo[0]) != id && Integer.parseInt(botInfo[1]) > 0)
                botList.add(new Bot(args[k]));
            else if (Integer.parseInt(botInfo[0]) == id) {
                health = Integer.parseInt(botInfo[1]);
                power = Integer.parseInt(botInfo[2]);
            }
        }
    }

    private void makeMove() throws IOException {
        if (fileContents.isEmpty()) {
            System.out.println(botList.get((int)(Math.random()*botList.size())).getId());
            return;
        }
        getLastAction();
        String action = "";
        int maxHealth = 0, maxPower = 0;
        for (Bot b : botList) {
            if (power >= 40 && health >= 250) {
                if (b.getPower() > maxPower && (!b.getAction().equals("D") || botList.size() == 1)) {
                    maxPower = b.getPower();
                    action = String.valueOf(b.getId());
                }
            }
            else if (b.getAction().equals(String.valueOf(id)) && b.getLastAction() != null && b.getLastAction().equals(String.valueOf(id))) {
                System.out.println(health % 2 == 0 ? b.getId() : "D");
                return;
            }
            else if (b.getAction().equals(String.valueOf(id)) && b.getPower() > 50) {
                System.out.println("D");
                return;
            }
            else {
                if (b.getHealth() > maxHealth) {
                    maxHealth = b.getHealth();
                    action = String.valueOf(b.getId());
                }
            }
        }
        System.out.println(action);
    }

    private void getLastAction() {
        boolean endNext = false;
        for (String s : fileContents) {
            if (s.startsWith("Turn")) {
                turn = Integer.parseInt(s.split(" ")[1]);
                if (endNext)
                    break;
                else {
                    endNext = true;
                    continue;
                }
            }
            if (s.isEmpty()) break;
            String[] botInfo = s.split(",");
            for (Bot b : botList) {
                if (b.getId() == Integer.parseInt(botInfo[0]))
                    b.setLastAction(botInfo[4]);
            }
        }
    }

    private void writeBots() throws IOException {
        StringBuilder sb = new StringBuilder();
        String s = System.lineSeparator();
        sb.append("Turn " + ++turn + s);
        for (Bot b : botList) {
            b.setLastAction(b.getAction());
            sb.append(b.toString() + s);
        }
        sb.append(s);
        for (String str : fileContents)
            sb.append(str + s);
        Files.write(filePath, sb.toString().getBytes());
    }

    public static void main(String[] args) throws IOException {
        new SisBot(args);
    }

    private class Bot {
        private int id, health, power;
        private String action, lastAction;

        public Bot(String info) {
            String[] botInfo = info.split(",");
            id = Integer.parseInt(botInfo[0]);
            health = Integer.parseInt(botInfo[1]);
            power = Integer.parseInt(botInfo[2]);
            action = botInfo[3];
        }

        public int getId() {
            return id;
        }

        public int getHealth() {
            return health;
        }

        public int getPower() {
            return power;
        }

        public String getAction() {
            return action;
        }

        public void setLastAction(String lastAction) {
            this.lastAction = lastAction;
        }

        public String getLastAction() {
            return lastAction;
        }

        @Override
        public String toString() {
            return new StringBuilder()
                    .append(id).append(",")
                    .append(health).append(",")
                    .append(power).append(",")
                    .append(action).append(",")
                    .append(lastAction).toString();
        }
    }
}
