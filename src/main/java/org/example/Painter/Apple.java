package org.example.Painter;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Apple {
    private static double[] xy = new double[]{};
    static Color color = new Color(Color.RED.getRGB());

    public static void setSize(int size) {
        Apple.size = size;
    }


    private static int sizeStat = 10;
    private static int size = sizeStat;
    private static double innerPlace = 0.9;

    public static double getExteriorBorder() {
        return exteriorBorder;
    }

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
            int x = (int) (Math.random() * (Picture.width * playGround[0])) + (int) (Picture.width * playGround[1]);
            int y = (int) (Math.random() * (Picture.height * playGround[0])) + (int) (Picture.height * playGround[1]);
            xy = new double[]{x, y};
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e);
        }
    }
    public static void reset(){
        size = sizeStat;
    }
    Apple(){
        xy = new double[]{(Math.random()*Picture.width*playGround[0])+(Picture.width*playGround[1]),(Math.random()*Picture.height*playGround[0])+(Picture.height*playGround[1])};
    }
    public static void plusRegion() {
        if(innerPlace<0.9){
            innerPlace+=0.1;
            exteriorBorder = (1-innerPlace)/2;
            playGround = new double[]{innerPlace,exteriorBorder};
        }
    }
    public static void minusRegion() {
        if(innerPlace>0.2){
            innerPlace-=0.1;
            exteriorBorder = (1-innerPlace)/2;
            playGround = new double[]{innerPlace,exteriorBorder};
        }
    }
}
