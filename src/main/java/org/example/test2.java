package org.example;

import javax.swing.*;
import java.awt.*;

public class test2 extends JPanel {

    public void paint(Graphics g,int x1,int y1) {
        super.paint(g);
        g.clearRect(0,0,getWidth(),getHeight());
        g.setColor(Color.black);
        g.fillRoundRect(x1-100,y1-100,200,200,200,200);
//        x2 = x1;
//        y2 = y1;
    }
}
