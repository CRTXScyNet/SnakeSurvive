package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

public class test extends JFrame {
    private int x1 = 0;
    private int y1 = 0;
    private int x2 = 0;
    private int y2 = 0;
    private boolean mouseMoved = false;
    private  test2  test2= new test2();
    public static BufferedImage image = new BufferedImage(200, 200, 1);
    JLabel label = new JLabel(new ImageIcon(image));
    JLabel label2 = new JLabel("sfsdfhshE");
    JPanel panel = new JPanel();
    JPanel panel2 = new JPanel();
     JLabel coutnOfApples = new JLabel("DFHSDH");

   test() {
       addMouseMotionListener(new MouseMotionListener() {
           @Override
           public void mouseDragged(MouseEvent e) {

           }

           @Override
           public void mouseMoved(MouseEvent e) {
               x1 = e.getX();
               y1 = e.getY();
               mouseMoved = true;
           }
       });
       addMouseListener(new MouseListener() {
           @Override
           public void mouseClicked(MouseEvent e) {

           }

           @Override
           public void mousePressed(MouseEvent e) {


           }

           @Override
           public void mouseReleased(MouseEvent e) {

           }

           @Override
           public void mouseEntered(MouseEvent e) {

           }

           @Override
           public void mouseExited(MouseEvent e) {

           }
       });

this.setSize(500,500);
this.setDefaultCloseOperation(EXIT_ON_CLOSE);
this.setLocationRelativeTo(null);
this.setVisible(true);

panel.setLocation(100,100);

//panel.setLayout();

       JLayeredPane pane = new JLayeredPane();
this.add(pane);

       pane.setBounds(0,0,getWidth(),getHeight());
       coutnOfApples.setForeground(Color.BLUE);
//
//       coutnOfApples.setOpaque(false);
       Font font = new Font("ns",Font.ITALIC,20);
       coutnOfApples.setFont(font);


       coutnOfApples.setLocation(300,300);
       coutnOfApples.setSize(50,50);

       panel.setBounds((int)(getWidth()/2-getWidth()/4),(int)(getHeight()/2-getHeight()/4),getWidth()/2,getHeight()/2);
       panel.add(coutnOfApples);
       label.setLocation(0,0);
       label.setSize(300,300);
//       coutnOfApples.setOpaque(false);



//       panel.setOpaque(false);
       pane.add(panel,3);
       pane.add(label,JLayeredPane.FRAME_CONTENT_LAYER);





   }




    public static void main(String[] args) {
        new test();
    }
}



