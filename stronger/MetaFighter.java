package folder;

public class MetaFighter {
    public static void main(String[] args) {
        if(args.length < 1){
            System.out.print("ok");
            System.exit(0);
        }
        String me = args[0], target = "D";
        int weakestAmount = 2000, weakest = 0, totalLife = 0, totalPower = 0, threat = 0, enemies = 0;
        int life = 0,power = 0;
        for(int i = 1; i < args.length; i++){
            String[] data = args[i].split(",");
            if(me.equals(data[0])){
                life = Integer.parseInt(data[1]);
                power = Integer.parseInt(data[2]);
            }else{
                if(Integer.parseInt(data[1]) > 0){
                    if(Integer.parseInt(data[1]) < weakestAmount){
                        weakest = Integer.parseInt(data[0]);
                        weakestAmount = Integer.parseInt(data[1]);
                    }
                    totalLife += Integer.parseInt(data[1]);
                    totalPower += Integer.parseInt(data[2]);
                    enemies++;
                }
            }
        }
        int powerV,totalPowerV,lifeV,totalLifeV,Defend = 0,AttackSelf = 0;
        powerV = power;
        totalPowerV = totalPower;
        lifeV = life;
        totalLifeV = totalLife;
        MetaFighter m = new MetaFighter();

        threat = m.calculateThreat(0, 0,totalLifeV,lifeV,powerV,totalPowerV, enemies);
        if (threat < 0){
            target = Integer.toString(weakest);
        }else{
            lifeV = lifeV - powerV;
            powerV++;
            AttackSelf = m.calculateThreat(0, 1,totalLifeV,lifeV,powerV,totalPowerV, enemies);  
            powerV = power;
            totalPowerV = totalPower;
            lifeV = life;
            totalLifeV = totalLife;
            Defend = m.calculateThreat(0, 2,totalLifeV,lifeV,powerV,totalPowerV, enemies);
            if(threat > AttackSelf && threat > Defend){
                target = Integer.toString(weakest);
            }
            if(Defend > AttackSelf && Defend > threat){
                target = "D";
            }
            if(AttackSelf > threat && AttackSelf > Defend){
                target = me;
            }
            if (Defend == threat){
                target = Integer.toString(weakest);
            }
            if (enemies < 3){
                target = Integer.toString(weakest);
            }
        }
        System.out.print(target);
        System.exit(0);
    }
    private int calculateThreat(int i, int s,int totalLifeV,int lifeV,int powerV, int totalPowerV, int enemies){
        if(totalLifeV > 0 && lifeV > 0){
            if(s == 0){
                totalLifeV += (0-powerV);
            }
            if (s != 2){
                lifeV += (0-totalPowerV);
            }else{
                lifeV += 0.5*(0-totalPowerV);
                powerV--;
            }
            powerV += enemies;
            totalPowerV++;
            i++;
            return calculateThreat(i, 0,totalLifeV,lifeV,powerV,totalPowerV, enemies);
        }
        if(lifeV > 0){
            return -1;
        }
        return i;
    }
}
