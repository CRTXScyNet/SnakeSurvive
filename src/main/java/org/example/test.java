package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class test extends JFrame {
    private int x1 = 0;
    private int y1 = 0;
    private int x2 = 0;
    private int y2 = 0;
    private boolean mouseMoved = false;

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
this.setBackground(Color.BLACK);
this.setSize(500,500);
this.setDefaultCloseOperation(EXIT_ON_CLOSE);
this.setLocationRelativeTo(null);
this.setVisible(true);
while (true){
    if(mouseMoved) {

        paint(this.getGraphics());
        mouseMoved = false;
    }
}

   }

    @Override
    public void paint(Graphics g) {
g.clearRect(0,0,getWidth(),getHeight());
       g.setColor(Color.white);
        g.fillRoundRect(x1-100,y1-100,200,200,200,200);
//        x2 = x1;
//        y2 = y1;
    }

    public static void main(String[] args) {
        new test();
    }
}



