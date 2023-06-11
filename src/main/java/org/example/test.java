package org.example;

import org.example.Painter.Apple;
import org.example.Painter.Picture;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class test extends JFrame {
    private int x1 = 0;
    private int y1 = 0;
    private int x2 = 0;
    private int y2 = 0;
    private boolean mouseMoved = false;

    public static BufferedImage image = new BufferedImage(1000, 1000, 1);
    JLabel label = new JLabel(new ImageIcon(image));


    ArrayList<Rectangle> rectangles = new ArrayList<>();
    Rectangle rectanglUI = new Rectangle(100,100,100,100);
    ArrayList<int[]> coordinates = new ArrayList<>();
    int xMouse = 100;
    int yMouse = 100;

    test() {
        for(int i = 0;i<3;i++){
            for(int i2 = 0;i2<2;i2++){
rectangles.add(new Rectangle(i*150,i2*150,100,100));
coordinates.add(new int[]{i*150,i2*150});
            }
        }

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

        this.setSize(image.getWidth(), image.getHeight());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);


//panel.setLayout();

        JLayeredPane pane = new JLayeredPane();
        this.add(pane);

        pane.setBounds(0, 0, getWidth(), getHeight());


        label.setLocation(0, 0);
        label.setSize(image.getWidth(), image.getHeight());
//       coutnOfApples.setOpaque(false);
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {
xMouse = e.getX();
yMouse = e.getY();
            }
        });

//       panel.setOpaque(false);
//       pane.add(panel,3);
        pane.add(label, JLayeredPane.FRAME_CONTENT_LAYER);
        ArrayList<Double> doubles = new ArrayList<>();


        while (true) {
            for(int i = 0;i< rectangles.size();i++){

                rectangles.get(i).setLocation(xMouse+coordinates.get(i)[0]-200,yMouse+coordinates.get(i)[1]-150);
            }
rectanglUI.setLocation(xMouse-rectanglUI.width/2,yMouse-rectanglUI.height/2);
            image = new BufferedImage(1000, 1000, 1);
            doubles.clear();
            for (int i = 1; i < image.getHeight() / 2; i += 1) {
                for (int x = -i; x <= i; x ++) {
                    for (int y = -i; y <= i; y ++) {
                        boolean inShadow = false;
                        if (Math.pow(x, 2) + Math.pow(y, 2) < Math.pow(i - 1, 2)) {
                            y=y*-1;
                            continue;
                        }
                        if (Math.pow(x, 2) + Math.pow(y, 2) <= Math.pow(i, 2) && Math.pow(x, 2) + Math.pow(y, 2) > Math.pow(i - 1, 2)) {

                            int drawX = x + image.getWidth() / 2;
                            int drawY = y + image.getHeight() / 2;
                            double xTarget = x;
                            double yTarget = y;
                            double TargetRadian = 0;
                            TargetRadian = Math.atan2(xTarget,yTarget);
                            TargetRadian = TargetRadian - TargetRadian % 0.01;

                            if (doubles.size() > 0) {
                                for (double d : doubles) {
                                    try {

                                        if (d == TargetRadian) {
                                            inShadow = true;
                                            image.setRGB((int) x + image.getWidth() / 2, (int) y + image.getHeight() / 2, Color.white.getRGB());
                                            break;
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            if (inShadow) {
                                continue;
                            }else
//                                for(Rectangle rectangle : rectangles) {
                                    if (rectanglUI.contains(new Point(drawX, drawY))) {
                                        try {
                                            doubles.add(TargetRadian);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
//                                }


                        }

                    }
                }


//
            }
System.out.println("finish");
            label.setIcon(new ImageIcon(image));
            repaint();
//
        }


    }


    public static void main(String[] args) {
        new test();
    }
}



