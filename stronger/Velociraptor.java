import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Velociraptor {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.print("ok");
            System.exit(0);
        }
        Velociraptor raptor = new Velociraptor(args);
        System.out.print(raptor.determineVictim());
    }

    private final String observationsFilePath = "state/velociraptor.txt";
    private final File observationsFile = new File(observationsFilePath);

    private int id, life, power, round;
    private List<Bot> preyList;
    private Map<Integer, Integer> preyDefendCounts;

    public Velociraptor(String[] preyStates) {
        loadObservations();
        observePrey(preyStates);
        saveObservations();
    }

    private void observePrey(String[] preyStates) {
        this.id = Integer.valueOf(preyStates[0]);
        preyList = new ArrayList<>();
        for (int i = 1; i < preyStates.length; i++) {
            String[] tokens = preyStates[i].split(",");
            int preyId = Integer.valueOf(tokens[0]);
            int preyLife = Integer.valueOf(tokens[1]);
            int preyPower = Integer.valueOf(tokens[2]);
            String lastAction = tokens[3];

            if (preyId == this.id) {
                this.life = preyLife;
                this.power = preyPower;
            } else if (preyLife > 0) {
                Bot prey = new Bot();
                prey.id = preyId;
                prey.life = preyLife;
                prey.power = preyPower;
                prey.lastAction = lastAction;
                preyList.add(prey);
                //Clever girl!
                if (prey.lastAction.equals("D")) {
                    int preyDefendCount = preyDefendCounts.getOrDefault(prey.id, 0);
                    preyDefendCount++;
                    preyDefendCounts.put(prey.id, preyDefendCount);
                }
            }
        }
    }

    public String determineVictim() {
        if (this.life == 1000) { //lay in wait until attacked
            return "D";
        }
        double fastestKill = 1000; //max game rounds
        Bot victim = null;
        for (Bot prey : preyList) {
            int preyDefendCount = preyDefendCounts.getOrDefault(prey.id, 0);
            double effectiveMultiplier = 1 - (.5 * preyDefendCount / round);
            double turnsToKill = prey.life / (this.power * effectiveMultiplier);
            //target whoever can be killed fastest
            //in case of tie, kill the prey with more power first
            if (turnsToKill < fastestKill || (victim != null && turnsToKill == fastestKill && prey.power > victim.power)) {
                fastestKill = turnsToKill;
                victim = prey;
            }
        }
        return String.valueOf(victim.id);
    }

    private void loadObservations() {
        preyDefendCounts = new HashMap<>();
        if (observationsFile.exists()) {
            try (Scanner scanner = new Scanner(observationsFile)) {
                round = Integer.valueOf(scanner.nextLine());
                while (scanner.hasNext()) {
                    String line = scanner.nextLine();
                    String[] tokens = line.split(",");
                    int preyId = Integer.valueOf(tokens[0]);
                    int preyDefendCount = Integer.valueOf(tokens[1]);
                    preyDefendCounts.put(preyId, preyDefendCount);
                }

            } catch (FileNotFoundException ex) {
                System.out.println(ex.getMessage());
            }
        }
        round++;
    }

    private void saveObservations() {
        if (!observationsFile.exists()) {
            try {
                observationsFile.createNewFile();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        try (PrintWriter writer = new PrintWriter(observationsFile)) {
            writer.println(round);
            if (preyDefendCounts != null) {
                preyDefendCounts.entrySet().stream().forEach(entry -> writer.println(entry.getKey() + "," + entry.getValue()));
            }
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private class Bot {

        public int id, life, power;
        public String lastAction;
    }
}
