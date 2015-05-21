import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Stronger {

    static final String[] defaultPlayers = {
        "java Analyst",
        "python Berserker.py",
        "python Boxer.py",
        "java BroBot",
        "java Bully",
        "/bin/bash copycat.sh",
        "java Coward",
        "java Equaliser",
        "java Guardian",
        "java Hero",
        "java PhantomMenace",
        "python Precog.py",
        "java Psycho",
        "java Revenger",
        "perl santayana.pl",
        "python SemiRandom.py",
        "python snapping_turtle.py",
        "perl tactician.pl",
        "java Tank",
        "python TrevorPhilips.py",
        "java Wiisniper"
    };
    final int turnLimit=300;
    final int timeout = 1000;
    final int startLife = 1000;
    final int startPower = 10;
    final int numRounds = 150;

    boolean log = false;
    List<Player> players;

    public static void main(String[] args){
        new Stronger().run(args);
    }

    void run(String[] args){
        init(args);
        for(int i=0;i<numRounds;i++){
            Collections.shuffle(players);
            runGame(i+1);
        }
        Collections.sort(players);
        for(Player player : players)
            System.out.println(player.toString());
    }

    void runGame(int roundNum){
        log("Player Count: " + players.size());
        for(Player player : players)
            player.reset();
        int turn = 0;
        while(turn++ < turnLimit){
            if(aliveCount() < 2)
                break;
            log("Turn " + turn + ", Round " + roundNum);
            List<Player> clones = new ArrayList<Player>();
            for(Player player : players)
                clones.add(player.copy());
            for(Player player : players){
                if(player.life < 1 || player.timedOut)
                    continue;               
                String[] args = new String[players.size()+1];
                args[0] = "" + player.id;
                for(int i=1;i<args.length;i++)
                    args[i] = players.get(i-1).toArgument();
                String reply = getReply(player, args);
                Player clone = player.findCopyOrMe(clones);
                if(reply.equals("T")){
                    clone.timedOut = true;
                    clone.life = 0;
                }
                clone.lastAction = reply.trim();
            }

            for(Player player : players){
                if(player.life < 1 || player.timedOut)
                    continue;               
                Player clone = player.findCopyOrMe(clones);
                if(clone.lastAction.equals("D")){
                    clone.power--;
                }else{
                    try{
                        int target = Integer.parseInt(clone.lastAction);
                        for(Player t : players)
                            if(t.id == target && t.life < 1)
                                throw new Exception();
                        for(Player tclone : clones){
                            if(tclone.id == target){
                                int atk = player.power; 
                                if(tclone.lastAction.equals("D")){
                                    atk -= player.power / 2;
                                    tclone.power++;
                                }
                                if (tclone.life > 0) {
                                    tclone.life -= atk;
                                    if (tclone.life <1)
                                        tclone.lastDeath += turn;
                                    tclone.power++;
                                }
                            }
                        }
                    } catch (Exception e){
                        // log(player.cmd + " returned an invalid command: (" + clone.lastAction + ")");
                        clone.power--;
                    }
                }
            }
            players = clones;

            int maxLife = 0, sumLife = 0, totLive=0;
            for (Player player : players)
                if (player.life > 0) {
                    totLive++;
                    sumLife += player.life;
                    if (player.life > maxLife) {
                        maxLife = player.life;
                    }
                }

            String marker;
            for(Player player : players){
                if(player.power < 1)
                    player.power = 1;
                marker = player.life == maxLife ? "*" : player.life <= 0 ? "-" : player.life > sumLife/totLive ? ">" : " ";
                log(String.format("%4s %4s (%2s) %30s%s %6s %2s %6s", player.life, player.power, player.id, player.cmd, marker, player.scoreLife, player.scoreRounds, player.lastDeath));
//                log(player.life + "\t\t" + player.power + "\t\t(" + player.id + ")\t" + marker + player.cmd + "\t\t" + player.scoreLife + "\t" + player.scoreRounds + "\t" + player.lastDeath);
            }
            log("\n");
        }

        if(aliveCount() == 1)
            for(Player player : players)
                if(player.life > 0){
                    player.scoreRounds++;
                    player.scoreLife += player.life;
                }
        for (Player player : players)
            if (player.life > 0)
                player.lastDeath += turnLimit;
        
    }

    void log(String msg){if(log)System.out.println(msg);}

    String getReply(Player player, String[] args){
        try{
            List<String> cmd = new ArrayList<String>();
            String[] tokens = player.cmd.split(" ");
            for(String token : tokens)
                cmd.add(token);
            for(String arg : args)
                cmd.add(arg);
            ProcessBuilder builder = new ProcessBuilder(cmd);
            builder.redirectErrorStream();
            long start = System.currentTimeMillis();
            Process process = builder.start();
            Scanner scanner = new Scanner(process.getInputStream());
            process.waitFor();          
            String reply = scanner.nextLine();
            scanner.close();
            process.destroy();
            if(System.currentTimeMillis() - start > timeout)
                return "T";
            return reply;
        }catch(Exception e){
            //            e.printStackTrace();
            return "Exception: " + e.getMessage();
        }
    }

    void init(String[] args){
        players = new ArrayList<Player>();
        for(String arg : args){
            if(arg.toLowerCase().startsWith("-log")){
                log = true;
            }else{
                Player player = createPlayer(arg);
                if(player != null)
                    players.add(player);
            }
        }
        for(String cmd : defaultPlayers){
            Player player = createPlayer(cmd);
            if(player != null)
                players.add(player);
        }
    }

    Player createPlayer(String cmd){
        Player player = new Player(cmd);
        String reply = getReply(player, new String[]{});
        log(player.cmd + " " + reply);
        if(reply != null && reply.equals("ok"))
            return player;
        return null;
    }

    int aliveCount(){
        int alive = 0;;
        for(Player player : players)
            if(player.life > 0)
                alive++;
        return alive;
    }

    static int nextId = 0;  
    class Player implements Comparable<Player>{
        int id, life, power, scoreRounds, scoreLife,lastDeath;
        boolean timedOut;
        String cmd, lastAction;

        Player(String cmd){
            this.cmd = cmd;
            id = nextId++;
            scoreRounds = 0;
            scoreLife = 0;
            reset();
        }

        public Player copy(){
            Player copy = new Player(cmd);
            copy.id = id;
            copy.life = life;
            copy.power = power;
            copy.scoreRounds = scoreRounds;
            copy.scoreLife = scoreLife;
            copy.lastAction = lastAction;
            copy.lastDeath = lastDeath;
            return copy;
        }

        void reset(){
            life = startLife;
            power = startPower;
            lastAction = "X";
            timedOut = false;
        }

        Player findCopyOrMe(List<Player> copies){
            for(Player copy : copies)
                if(copy.id == id)
                    return copy;
            return this;
        }

        public int compareTo(Player other){
            if(scoreRounds == other.scoreRounds)
                return other.scoreLife - scoreLife;
            return other.scoreRounds - scoreRounds;
        }

        public String toArgument(){
            return id + "," + life + "," + power + "," + lastAction;  
        }

        public String toString(){
            String out = "" + scoreRounds + "\t" + scoreLife;
            while(out.length() < 20)
                out += " ";
            return out + "(" + id + ")\t" + cmd;
        }
    }
}
