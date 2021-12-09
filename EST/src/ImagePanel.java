package ImagePackage;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ali
 */
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.*;

public class ImagePanel extends JPanel {

    private Image img;
    private JFrame frame;

    public ImagePanel(String frameTitle){
        this.frame=new JFrame();
        this.frame.setBounds(100, 100, 0, 0);
        this.frame.getContentPane().add(this);
        this.frame.setTitle(frameTitle);
    }
    public ImagePanel(){
        this.frame=new JFrame();
        this.frame.setBounds(100, 100, 0, 0);
        this.frame.getContentPane().add(this);
    }
    public void ShowImage(Image img) {
        this.img = img;
        Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
        setPreferredSize(size);
        //setMinimumSize(size);
        //setMaximumSize(size);
        //setSize(size);
        //setLayout(null);
        this.frame.pack();
        
        this.frame.setVisible(true);
        this.paintImmediately(0, 0, img.getWidth(null), img.getHeight(null));
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(img, 0, 0, null);
    }
}
