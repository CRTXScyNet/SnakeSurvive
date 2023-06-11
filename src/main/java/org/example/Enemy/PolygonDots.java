package org.example.Enemy;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.Executors;

public class PolygonDots {

    private static ArrayList<Point> points = new ArrayList<>();
    private static ArrayList<Double> radians = new ArrayList<>();

    public static int size(){
        return points.size();
    }

public static void add(Point point , double radian){
    points.add(point);
    radians.add(radian);


    sortPolygonPoints();


}
    public static ArrayList<Point> getPoints() {
        return points;
    }

    public static ArrayList<Double> getRadians() {
        return radians;
    }


    public static void sortPolygonPoints() {

        boolean notEnd = false;
        for (int i = 0; i < radians.size() - 1; i++) {
            if (radians.get(i) <= radians.get(i + 1)) {

            } else {
                notEnd = true;
                Point p = points.get(i);
                points.set(i, points.get(i + 1));
                points.set(i + 1, p);
                double d = radians.get(i);
                radians.set(i, radians.get(i + 1));
                radians.set(i + 1, d);
            }
        }
        if (notEnd) {
            sortPolygonPoints();
        }
    }
    public  static void clear(){
    points.clear();
    radians.clear();
    }

}
