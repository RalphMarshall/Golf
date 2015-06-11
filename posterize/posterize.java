import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.nio.file.*;
import javax.imageio.*;
class M {
    public static void main(String[] a) throws Exception {
        BufferedImage i=ImageIO.read(new File(a[0]));
        int w,h,k,r,g,b,t,z,q;
        w=i.getWidth();
        h=i.getHeight();
        BufferedImage o=new BufferedImage(w,h,2);
        for (int x=0;x<w;x++) {
            for(int y=0;y<h;y++){
                Color v=null;
                k=i.getRGB(x,y);
                r=k>>16&0xff;
                g=k>>8&0xff;
                b=k&0xff;

                double e=999;

                for(String n: Files.readAllLines(Paths.get(a[2]))) {
                    String[]m=n.split(" ");
                    t=Integer.parseInt(m[0]);
                    z=Integer.parseInt(m[1]);
                    q=Integer.parseInt(m[2]);

                    double d=Math.sqrt((t-r)*(t-r)+(z-g)*(z-g)+(q-b)*(q-b));
                    if(d<e){
                        e=d;v=new Color(t,z,q);
                    }
                }
                o.setRGB(x,y,v.getRGB());
            }
        }
        ImageIO.write(o,"png",new File(a[1]));
    }
}
