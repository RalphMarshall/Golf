import java.util.Arrays;
import java.util.Scanner;

public class Pirate extends Player{
    public Pirate() {
    }
    private String bid(int[] t,int tol){
        int[]z=t.clone();
        Arrays.sort(z);
        int j=0;
        for(int i=0;i<6;i++){
            if(t[i]==z[5]){j=i;break ;}
        }
        return (tol+t[j])+" "+(j+1);
    }
    @Override
    public String bid(int yourId, int[] diceEachPlayerHas, int[] yourDice,
            String[] bids) {
        int[] t=new int[6];
        for(int i=0;i<yourDice.length;i++){
            t[yourDice[i]-1]++;
        }
        for(int i=1;i<t.length;i++)t[i]+=t[0];
        int tol=(Controller.diceInPlay()-yourDice.length)/4;
        if(bids.length==0)return bid(t,1);
        Scanner i=new Scanner(bids[bids.length-1]);
        int x=i.nextInt(),y=i.nextInt();
        i.close();
        if(t[y-1]>x)return (t[y-1]+2)+" "+y;
        int nd=Controller.diceInPlay();
        if(x>nd+t[y-1]-yourDice.length)return "Liar!";
        if(Controller.isGreaterThan(bid(t,tol), bids[bids.length-1])){
            int z=Controller.valueOf(bids[bids.length-1]);
            for(int j=1;j<=tol;j++)if(Controller.valueOf(bid(t,j))>z)return bid(t,j);
        }
        return "Liar!";
    }
}
