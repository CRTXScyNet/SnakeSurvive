package org.example.Painter;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Apple {
    private static double[] xy = new double[]{};
    static Color color = new Color(Color.RED.getRGB());

    private static int sizeStat = 10;
    private static int size = sizeStat;
    private static double innerPlace = 0.9;

    private static  double exteriorBorder = (1-innerPlace)/2;
    private static  double[] playGround = new double[]{innerPlace,exteriorBorder};

    public static Color getAppleColor() {
        return color;
    }
    public static int getAppleSize() {
        return size;
    }
    public static double[] getXy() {
        return xy;
    }
    public void moveXy(double[] direct) {
        xy = new double[]{xy[0]-direct[0],xy[1]-direct[1]};

    }
    public void setXy() {
        try{
            int x = (int) (Math.random() * (width * playGround[0])) + (int) (width * playGround[1]);
            int y = (int) (Math.random() * (height * playGround[0])) + (int) (height * playGround[1]);
            xy = new double[]{x, y};
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e);
        }
    }
    private int width;
    private int height;
    Apple(int width,int height){
        this.width=width;
                this.height=height;
        xy = new double[]{(Math.random()*width*playGround[0])+(width*playGround[1]),(Math.random()*height*playGround[0])+(height*playGround[1])};
    }


}
