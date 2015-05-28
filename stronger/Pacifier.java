public class Pacifier {
    public static void main(String[] args) {

        int ownId=0,ownHp=1,ownAt=1;

        if(args.length==0) {
            System.out.println("ok");
            System.exit(0);
        }

        for(String s : args) {
            if(!s.contains(","))
                ownId=Integer.parseInt(s);
            else {
                String[] tmp = s.split(",");
                if(Integer.parseInt(tmp[0])==ownId) {
                    ownHp=Integer.parseInt(tmp[1]);
                    ownAt=Integer.parseInt(tmp[2]);
                }
            }
        }
        int target=(ownId==0)?1:0;
        float best=-100.f;
        if(ownAt<=8)
            target=-1;
        else
            for(String s:args) {
                if(s.contains(",")) {
                    String[] tmp = s.split(",");
                    float curr=((Float.parseFloat(tmp[1])/(float)ownAt)*Float.parseFloat(tmp[2]))*(0.1f*Float.parseFloat(tmp[2]));
                    target=(curr>=best)?Integer.parseInt(tmp[0]):target;
                    best=(curr>=best)?curr:best;
                }
            }
        System.out.println((target>=0)?target:"D");
    }
}
