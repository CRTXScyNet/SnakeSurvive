package org.example.Painter;

import org.example.Enemy.Line;

import java.awt.*;
import java.util.ArrayList;

public class Walls{
   private Rectangle rectangle;
  private ArrayList<Point> points = new ArrayList<>();

   private int[]x = new int[4];
   private int[]y = new int[4];
   private int count = 4;
   public boolean contains(Point point){
       return point.x > rectangle.getX() && point.y > rectangle.getY() && point.x < rectangle.getX() + rectangle.width && point.y < rectangle.getY() + rectangle.height;


   }
    public Walls(int width, int height){

        rectangle = new Rectangle(0,0,width,height);
        points.add(rectangle.getLocation());
        points.add(new Point(points.get(0).x+rectangle.width,points.get(0).y));
        points.add(new Point(points.get(0).x+rectangle.width,points.get(0).y+rectangle.height));
        points.add(new Point(points.get(0).x,points.get(0).y+rectangle.height));
        for (int i = 0; i<points.size();i++){
            x[i] = points.get(i).x;
            y[i] = points.get(i).y;
        }
    }

    public int[] getXs(){
        return x;
    }
    public int[] getYs(){
        return y;
    }
    public int getCount(){
        return count;
    }
    public ArrayList<Line> getWalls(){
        ArrayList<Line> walles = new ArrayList<>();
        for(int i = 0; i<getCount();i++){
            if(i<getCount()-1){
                walles.add(new Line(points.get(i),points.get(i+1)));

            }else{
                walles.add(new Line(points.get(i),points.get(0)));
            }
        }
        return walles;
    }


    public ArrayList<Point> getPoints() {
        return points;
    }
}
