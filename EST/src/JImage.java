package ImagePackage;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ali
 */

import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import java.awt.*;
import java.awt.geom.*; //for shapes

public class JImage{
    public BufferedImage img = null;
    public Graphics2D graph = null;
    
    // Set pen parameters
    /* g2d.setPaint(Color.black);
     * g2d.setFont(someFont);
     * g2d.scale(...);
     *
     * void drawString(String s, float left, float bottom)
     *
     * // Allocate a shape
Rectangle2D.Double rect = ...;
Ellipse2D.Double ellipse = new Ellipse2D.Double(10, 10, 350, 350);
Polygon poly = ...;
GeneralPath path = ...;
RoundRectangle2D.Double
QuadCurve2D.Double
Line2D.Double
Arc2D.Double
CubicCurve2D.Double
     *
     * // Draw shape
  g2d.draw(shape);  // outline
  g2d.fill(shape);  // solid
     */

    public JImage(){

    }
    public boolean Load(String addr){

        try {
            img = ImageIO.read(new File(addr));
            graph=img.createGraphics();
        }
        catch (Exception e) {
            System.out.println("Error: JImage.Load() Image file not found");
            return false;
        }
        return true;
    }
    public void Save(String addr, String format){//format can be "jpg" "png" bmp" "gif"

        try {
            ImageIO.write(img, format, new File(addr));
        }
        catch (Exception e) {
            System.out.println("Error: JImage.Save() "+e.toString());

        }
    }
    public void Create(int width, int height, Color color){
        this.Create(width, height);

        graph.setColor(color);
        Rectangle2D.Double shape = new Rectangle2D.Double(0, 0, width,height);
        graph.fill(shape);
    }

    public void Create(int width, int height){//image is black
        img=new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        graph=img.createGraphics();
    }


    
}
