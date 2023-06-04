package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class test extends JFrame {
    private int x1 = 0;
    private int y1 = 0;
    private int x2 = 0;
    private int y2 = 0;
    private boolean mouseMoved = false;
    private ArrayList<Point> points = new ArrayList<>();

    test() {
        for (int i = 0; i < 100; i++) {
//            if(i == 99){
//                points.add(new Point(i*2, i*2));
//            }else {
                points.add(new Point(i*2, i*2));
//            }

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
        this.setBackground(Color.BLACK);
        this.setSize(500, 500);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        while (true) {
            if (mouseMoved) {
                repaint();
                paint(this.getGraphics());
                mouseMoved = false;
            }
        }

    }

    @Override
    public void paint(Graphics g) {
        g.clearRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.white);

        for (int i = points.size()-2; i >= 0; i--) {


            points.set(i+1,new Point(points.get(i).x,points.get(i).y));
//                points.get(i + 1).x = points.get(i).x;
//                points.get(i + 1).y = points.get(i).y;
//                        }


        }
        points.set(0,new Point(x1,y1));
//        points.get(0).x = x1;
//        points.get(0).y = y1;

        for(Point point : points){
            g.fillRoundRect(point.x - 10, point.y - 10, 20, 20, 20, 20);
        }

//        x2 = x1;
//        y2 = y1;
    }

    public static void main(String[] args) {
        new test();
    }
}



