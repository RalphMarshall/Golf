import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class AboveAverage {

    private static final String FILE_NAME = "state" + File.separator + "AboveAverage";

    private File file = new File(FILE_NAME);
    private List<Bot> bots = new ArrayList<>();
    private Bot me;
    private List<List<Bot>> history = new ArrayList<>();
    private String[] args;

    public AboveAverage(String[] args) {
        this.args = args;
        this.bots = readBotArray(args);
        bots.stream().filter(bot -> bot.isMe).forEach(bot -> me = bot); //Intellij told me to do this...
        if (!file.exists()){
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException ignored) {}
        }
        readHistory();
        updateFile();
    }

    private List<Bot> readBotArray(String[] args){ //First parameter is my id.
        List<Bot> bots = new ArrayList<>();
        String myId = args[0];
        for (String arg : args) {
            if (arg.equals(myId)) {    // `==` not `.equals()`
                continue;
            }
            Bot bot = new Bot(arg, myId);
            bots.add(bot);
        }
        bots.sort(Comparator.comparingDouble((Bot a) -> a.life).reversed());
        return bots;
    }

    private void updateFile() {
        PrintStream out;
        try {
            out = new PrintStream(new FileOutputStream(file, true), true);
            for (String s : args){
                if (!s.matches("\\d+|\\d+,-?\\d+,\\d+,(\\d+|D)")){
                    s.replaceAll("(\\d+,-?\\d+,\\d+,).*", "$1Invalid");
                }
                out.print(s + " ");
            }
            out.println();
        } catch (FileNotFoundException ignored) {}
    }

    private void readHistory() {
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()){
                String line = scanner.nextLine().trim();
                if (line.length() > 0) {
                    history.add(readBotArray(line.split("\\s+")));
                }
            }
        } catch (FileNotFoundException ignored) {}
    }

    public static void main(String[] args) throws FileNotFoundException {
        if(args.length < 1){
            System.out.print("ok");
            System.exit(0);
        }
        try {
            System.out.print(new AboveAverage(args).normalize());
        } catch (Exception e){
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            e.printStackTrace(printWriter);
            String s = writer.toString();
            System.out.print("error: " + s.replace("\n", " | "));
        }
    }

    String normalize() {
        if (history.size() == 0 || me.lastAction.equals("X")){
            return "D";
        }

        if (bots.stream().mapToInt(bot -> (bot.life + me.power - 1)/me.power).sum() < me.life/bots.stream().mapToInt(bot -> bot.power).sum()){
            return bots.stream().max(Comparator.comparingInt(a->a.power)).get().id;
        }

        List<Bot> bestHistory = history.stream().max((a,b) -> {
            int differenceA = 0;
            int differenceB = 0;
            for (int i = 0; i < a.size(); i++){
                if (!a.get(i).equals(bots.get(i))){
                    differenceA++;
                }
                if (!b.get(i).equals(bots.get(i))){
                    differenceB++;
                }
            }
            if (differenceA != differenceB){
                return differenceA - differenceB;
            }
            for (int i = 0; i < a.size(); i++){
                differenceA += Math.abs(bots.get(i).life - a.get(i).life) + Math.abs(bots.get(i).power - a.get(i).power) * 10;
                differenceB += Math.abs(bots.get(i).life - b.get(i).life) + Math.abs(bots.get(i).power - b.get(i).power) * 10;
            }
            return differenceA - differenceB;
        }).get();


        int i = history.indexOf(bestHistory) + 1;
        List<Bot> after = i == history.size() ? bots : history.get(i);

        Map<Bot, String> actions = new HashMap<>();
        for (Bot bot : bots){
            if (bot.equals(me)){
                continue;
            }
            after.stream().filter(future -> future.equals(bot)).forEach(future -> actions.put(bot, future.lastAction));
        }

        List<String> myActions = new ArrayList<>();
        myActions.add("D");
        myActions.add("InvalidChoice");
        myActions.addAll(bots.stream().map(bot -> bot.id).collect(Collectors.toList()));

        Map<String,List<Bot>> scenarios = new HashMap<>();
        for (String action : myActions){
            List<Bot> simulatedBots = bots.stream().map(Bot::copy).collect(Collectors.toList());  //IntelliJ told me to (kind of) golf these lines.
            actions.put(me, action);
            simulatedBots.stream().filter(bot -> actions.get(bot).equals("D")).forEach(Bot::defend);
            simulatedBots.stream().filter(bot -> !actions.get(bot).equals("D")).forEach(bot -> bot.attack(actions.get(bot), simulatedBots));
            scenarios.put(action, simulatedBots);
        }

        return scenarios.keySet().stream().min(Comparator.comparingInt((String action) -> {
            List<Bot> scenario = scenarios.get(action);
            Bot me = scenario.stream().filter(a->a.isMe).findAny().get();
            scenario.removeIf(a->a.life<1||a.equals(me));
            scenario.sort(Comparator.comparingInt(a->a.life));
            int bestLife = scenario.get(scenario.size() * 3 / 4).life;
            scenario.sort(Comparator.comparingInt(a->a.power));
            int bestPower = scenario.get(scenario.size() * 3 / 4).power;
            scenario.add(me);
            return Math.abs(me.power - bestPower)*20 + Math.abs(me.life - bestLife);
        })).get();
    }

    class Bot {
        String id, lastAction;
        int life, power;
        boolean isMe;
        boolean isDefending = false;

        public Bot() {}

        public Bot(String bot, String myId) {
            String[] parts = bot.split(",");
            id = parts[0];
            life = Integer.valueOf(parts[1]);
            power = Integer.valueOf(parts[2]);
            lastAction = parts[3];
            isMe = id.equals(myId);
        }

        Bot copy() {
            Bot bot = new Bot();
            bot.id = id;
            bot.lastAction = lastAction;
            bot.life = life;
            bot.power = power;
            bot.isMe = isMe;
            return bot;
        }

        void defend(){
            this.isDefending = true;
            this.power--;
        }

        void attack(int power){
            if (isDefending) {
                this.power += 2;
                this.life -= power / 2;
            } else {
                this.power++;
                this.life -= power;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Bot bot = (Bot) o;

            return id.equals(bot.id);

        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }

        void attack(String attackId, List<Bot> bots) {
            if (life < 1){
                return;
            }
            Bot attacked = bots.stream().filter((Bot a) -> a.id.equals(attackId)).findFirst().orElse(null);
            if (attacked == null){
                power--;
                return;
            }
            if (attacked.life < 1){
                power--;
                return;
            }
            attacked.attack(power);
        }
    }
}
