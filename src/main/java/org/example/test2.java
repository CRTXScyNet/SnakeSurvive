package org.example;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;


public class test2{
    File file = new File("C:\\Projects\\SnakeSurvive\\src\\back.jpg");
    static BufferedImage image = new BufferedImage(500,500,1);
    static JLabel label = new JLabel(new ImageIcon(image));
    static Point pointTarg = new Point(100, 100);
    static Point pointCent = new Point(image.getWidth()/2,image.getHeight()/2);
    static JFrame frame = new JFrame();

    test2(){
        try{
            image = ImageIO.read(file);
        }catch (Exception e){

        }
        frame.setSize(image.getWidth(),image.getHeight());
        frame.setLocationRelativeTo(null);

        frame.add(label);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                setTarg(e.getX(),e.getY());
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



    }

public void setTarg(int x,int y){
    try{
        image = ImageIO.read(file);
    }catch (Exception e){

    }
        pointTarg.setLocation(x,y);
    image.setRGB(x,y,Color.white.getRGB());
    image.setRGB(pointCent.x,pointCent.y,Color.white.getRGB());
    try {
        double xTarget = pointTarg.x - pointCent.x;
        double yTarget = pointTarg.y - pointCent.y;
        double TargetRadian = 0;
        Date date = new Date();
        TargetRadian = Math.atan2(xTarget,yTarget);
            if(TargetRadian<0){
                TargetRadian += 6.28;

            }
        System.out.println(TargetRadian);
        Date date1 = new Date();
        System.out.println(date1.getTime()-date.getTime());
    } catch (Exception e) {
        e.printStackTrace();
    }
    label.setIcon(new ImageIcon(image));
    frame.repaint();

}
    public static void main(String[] args) {
        new test2();
    }
}
