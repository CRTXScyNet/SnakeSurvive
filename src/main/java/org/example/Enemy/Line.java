package org.example.Enemy;

import java.awt.*;

public     class Line {
    private double k;
    private double b;
    boolean revert;

    public Point getF() {
        return f;
    }

    public Point getS() {
        return s;
    }

    private Point f;
    private Point s;

    public Line(Point a, Point b) {
        f = a;
        s = b;
        if (a.equals(b))
            throw new IllegalArgumentException("Points are equal. There are endless number of lines through one point.");
        double A = a.getY() - b.getY();
        double B = b.getX() - a.getX();
        if (B == 0) {
//             Line line = new Line(a, new Point(b.x+1,b.y));
//            this.k = line.getK();
//            this.b = line.b;
//            f = line.getF();
//            s = line.getS();
//            revert = true;
//            throw new IllegalArgumentException("Points lay on same vertical line.");
        }else {
//            A = a.getY() - b.getY();
//            B = b.getX() - a.getX();
            double C = a.getX() * b.getY() - b.getX() * a.getY();
            this.k = - A / B;
            this.b = - C / B;
        }

    }

    Line(Double k, Double b) {
        this.k = k;
        this.b = b;
    }

    /**
     * Возвращает угловой коэффициент линии из формулы вида y = k * x + b
     * @return угловой коэфициент
     */
    public double getK() {
        return k;
    }

    /**
     * Возвращает смещение линии из формулы вида y = k * x + b
     * @return смещение линии
     */
    public double getB() {
        return b;
    }

    Line getPerpendicularLine(Point point) {
        return new Line(-1 / k, point.getY() + point.getX() / k);
    }

    public Point getIntersectionPoint(Line other) {
//        if(revert){
//            System.out.println("dfgdfg");
//        }
        if (getK() == other.getK()) {
            this.k += 1;
//            other.k -= 1;
        }
//            throw new IllegalArgumentException("Lines are parallel and do not intersect.");
        double x  = (other.getB() - getB()) / (getK() - other.getK());
        double y = getK() * x + getB();
        return new Point((int)x,(int)y);
    }

    @Override
    public String toString() {
        return "Line{" +
                "y = " + k +
                " * x + " + b +
                '}';
    }
}
