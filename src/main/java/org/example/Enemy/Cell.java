package org.example.Enemy;

import org.example.Painter.Node;
import org.example.Painter.Process;

import java.awt.*;

public class Cell {
    public static double size = 10;



    private Node node;
    private double[] xyFuture = new double[2];

    boolean isEdge = false;
    private int position;
    private boolean IsLast = false;
    private boolean IsFirst = false;
    private Color color;
    private int posX;
    private int posY;
    private Point p;

    Cell(int x, int y, Color color){
        node = Process.graph[x+y*Process.graphWidth];
        p = node.getPos();
posX = x;
posY = y;

        this.color = color;
    }
public Point getPoint(){
        return  p;
}
    public int getPosX(){
        return posX;
    }
    public int getPosY(){
        return posY;
    }
    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }
    public  void move(int x, int y){
        Process.graph[posX + posY* Process.graphWidth].setCell(null);
        posX = x;
        posY = y;
        Process.graph[posX + posY* Process.graphWidth].setCell(this);
        node = Process.graph[posX + posY* Process.graphWidth];
        p = node.getPos();
    }
//    public  void move(double[] xyNow,double[] xyFuture,boolean IsLast){
//isEdge = false;
//        xyLast = this.xyNow;
//        this.xyNow = xyNow;
//        this.xyFuture = xyFuture;
//        if(xyLast[0]!=xyFuture[0]&&xyLast[1]!=xyFuture[1]){
//            isEdge = true;
//        }else
//        if(IsLast){
//            isEdge = true;;
//        }
//    }
    public boolean getEdge(){
        return isEdge;
    }
    public static double getSize(){
        return size;
    }

}
