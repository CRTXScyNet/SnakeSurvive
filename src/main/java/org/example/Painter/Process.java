package org.example.Painter;

import org.example.Enemy.Enemy;
import org.example.Enemy.Line;
import org.example.Enemy.PolygonDots;
import org.example.Player.Player;
import org.example.gpu.Timer;
import org.example.gpu.Window;
import org.example.gpu.render.Model;
import org.example.gpu.render.ModelRendering;
import org.example.gpu.trest;
import org.joml.Vector3f;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Process {
    private int width;
    private int height;
    public  static float appleDistance = 0;
    public static boolean reset = false;
    public static boolean isPaused = false;
    public static boolean isEnd = false;

    public static boolean ready = false;
    private boolean targetChanged = false;
    public static boolean eaten = false;
    public static float eatenTime = 0;
    public static float eatenTimelast = 0;
    public static float eatenPlayerTime = 0;
    public static float eatenPlayerTimelast = 0;
    private int eatenDelayStat = 500;
    private int eatenDelay = eatenDelayStat;

    public static ArrayList<Point> getPointsOfArrow() {
        return pointsOfArrow;
    }

    //    static int appleIsEatenForSnakeStat = 150;
//    static int appleIsEatenForSnake = appleIsEatenForSnakeStat;
    static ArrayList<Point> pointsOfArrow = new ArrayList<>();
    static ArrayList<Point> points = new ArrayList<>();
    //    static public Rectangle lifeArea = new Rectangle(Picture.image.getWidth() / 2 - 100, Picture.image.getHeight() / 2 - 100, 200, 200);
    static ArrayList<Point> pointsOfRect = new ArrayList<>();
    static ArrayList<Point> pointsToErase = new ArrayList<>();
    //    static ArrayList<Point> pointsToErase = new ArrayList<>();
    static ArrayList<Point> pointsOfApple = new ArrayList<>();
    static ArrayList<Color> colors = new ArrayList<>();

    public static ArrayList<Point> getPointsOfApple() {
        return pointsOfApple;
    }

    public static ArrayList<Point> getPointsToErase() {
        return pointsToErase;
    }

    public static ArrayList<Point> getPoints() {
        return points;
    }


    public static ArrayList<Color> getColors() {
        return colors;
    }

    static double collisionWithApple = Math.pow(Apple.getAppleSize() + Player.getSize(), 2);

    float[] a = new float[2];
    static boolean gameOver = false;
    static boolean screenMove = false;

    public static double[] getDirection() {
        return new double[]{direction[0], direction[1]};
    }

    static float[] direction = new float[2];
    private int outLeft;
    private int fromLeft;

    private int outRight;
    private int fromRight;

    private int outUp;
    private int fromUp;

    private int outDown;
    private int fromDown;
    private int agressiveMode = 300;
    ArrayList<Line> lines = new ArrayList<>();
    Walls walls;
    public static boolean isGo = false;


    private ModelRendering renderingArrow;
    static public Apple apple;
    public static ArrayList<Point> targets1 = new ArrayList<>();
    public static HashMap<Point, Point> toTargets = new HashMap<>();
    public static boolean ringWayIsReady = false;
    public static boolean ringIsReady = false;

    public Process(Window window) {
        this.width = window.width;
        this.height = window.height;

        outLeft = (int) (-width) / Enemy.step * Enemy.step;
        fromLeft = (int) (-width * 2) / Enemy.step * Enemy.step;

        outRight = (int) (width) / Enemy.step * Enemy.step;
        fromRight = (int) (width * 2) / Enemy.step * Enemy.step;

        outUp = (int) (-height) / Enemy.step * Enemy.step;
        fromUp = (int) (-height * 2) / Enemy.step * Enemy.step;

        outDown = (int) (height) / Enemy.step * Enemy.step;
        fromDown = (int) (height * 2) / Enemy.step * Enemy.step;


        renderingArrow = new ModelRendering(window, Color.red, true, null,"applePointer");
        renderingArrow.addModel(new Model(window, 100));

//        walls = new Walls(width, height);
        apple = new Apple(window);
        Player player = new Player(window);
        for (int i = 0; i < 200; i++) {
            new Enemy(window, false);
        }
        for (int i = 0; i < 30; i++) {
            new Enemy(window, true);
        }
        double radius1 = Math.pow(width / 5 - 2, 2);
//        for (int x = -width / 4; x <= width / 4; x++) {
//            for (int y = -width / 4; y <= width / 4; y++) {
//                double dist = (Math.pow(x, 2)) + (Math.pow(y, 2));
//                if (dist >= radius1 &&
//                        dist <= Math.pow(radius1, 2)) {
//                    targets1.add(new Point(x, y));
//                }
//            }
//        }
//        ringIsReady = true;


        Executors.newSingleThreadExecutor().submit(() -> {
            boolean closer;

            for (int x = -width / 4; x <= width / 4; x++) {
                for (int y = -width / 4; y <= width / 4; y++) {
                    Point nearest = new Point();
                    double dist = (Math.pow(x, 2)) + (Math.pow(y, 2));
                    if (dist < radius1) {
                        nearest.setLocation(2 * x, 2 * y);
                    } else {
                        nearest.setLocation(0, 0);
                    }
//                    if (Math.random() > 0.5) {
//                        closer = true;
//                    } else {
//                        closer = false;
//                    }
//
//                    for (Point p : targets1) {
//                        double firstDistance = Math.sqrt(Math.pow(Math.abs(p.x - x), 2) + Math.pow(Math.abs(p.y - y), 2));
//                        double lastDistance = Math.sqrt(Math.pow(Math.abs(x - nearest.x), 2) + Math.pow(Math.abs(y - nearest.y), 2));
//                        if (closer) {
//                            if (firstDistance < lastDistance) {
//                                nearest.setLocation(p.x, p.y);
//                            }
//                        } else {
//                            if (firstDistance <= lastDistance) {
//                                nearest.setLocation(p.x, p.y);
//                            }
//                        }
//                    }
                    toTargets.put(new Point(x, y), new Point(nearest.x, nearest.y));
                }
            }
            ringWayIsReady = true;

        });

        try {


            Executors.newSingleThreadExecutor().submit(() -> {
                while (!ready) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(20);
                    } catch (Exception e) {

                    }
                }
                double cap = 1.0 / 500.0;
                double time = org.example.gpu.Timer.getTime();
                double buf = 0;
                while (true) {
                    try {
                        double time2 = org.example.gpu.Timer.getTime() - time;
                        buf += time2;
                        time = org.example.gpu.Timer.getTime();
                        if (buf >= cap) {

                            buf = 0;
                            if (trest.reset) {
                                if(!reset){
                                    reset = true;
                                }
                                continue;

                            } else if (reset) {
                                reset = false;
                                isPaused = false;
                                isEnd = false;
                                trest.mouseControl = false;
                            }
                            if (isPaused) {
                                continue;
                            }

//                pointsToErase.clear();

                            isGo=true;
                            if (trest.mouseControl) {

                                if (player.getDirection() != null) {
                                    screenMove = true;
                                    direction = player.getDirection();
                                }

                                if (screenMove) {
                                    for (ModelRendering model : trest.background) {
                                        model.getModels().get(0).getMovement().addPosition(new Vector3f(-direction[0] / 50, -direction[1] / 50, 0));

                                    }
                                    for (ModelRendering model : trest.background2) {
                                        model.getModels().get(0).getMovement().addPosition(new Vector3f(-direction[0] / 10, -direction[1] / 10, 0));

                                    }
                                    for (ModelRendering model : trest.background3) {
                                        model.getModels().get(0).getMovement().addPosition(new Vector3f(-direction[0] / 5, -direction[1] / 5, 0));
                                    }
                                    for (Enemy enemy : Enemy.enemies) {
                                        enemy.moveXy(direction);
                                    }
                                    screenMove = true;
                                    apple.moveXy(direction);
                                    player.moveXy(direction);


                                }
                            }
                            for (Enemy enemy : Enemy.enemies) {

                                if (enemy.getXy().get(0)[0] < (outLeft)) {
                                    enemy.moveXy(new float[]{fromLeft, 0});
                                    enemy.reverse();
                                } else if (enemy.getXy().get(0)[0] > (outRight)) {
                                    enemy.moveXy(new float[]{fromRight, 0});
                                    enemy.reverse();
                                }
                                if (enemy.getXy().get(0)[1] < (outUp)) {
                                    enemy.moveXy(new float[]{0, fromUp});
                                    enemy.reverse();
                                } else if (enemy.getXy().get(0)[1] > (outDown)) {
                                    enemy.moveXy(new float[]{0, fromDown});
                                    enemy.reverse();
                                }


                            }
                            if (Apple.getXy()[0] < (outLeft)) {
                                apple.moveXy(new float[]{fromLeft, 0});

                            } else if (Apple.getXy()[0] > (outRight)) {
                                apple.moveXy(new float[]{fromRight, 0});

                            }
                            if (Apple.getXy()[1] < (outUp)) {
                                apple.moveXy(new float[]{0, fromUp});

                            } else if (Apple.getXy()[1] > (outDown)) {
                                apple.moveXy(new float[]{0, fromDown});

                            }
                            float[] p = player.getXy().get(0);
                            if (appleSpawned && !appleVisible) {
                                if (eatenTimelast > 0) {
                                    eatenTimelast -= (org.example.gpu.Timer.getFloatTime() - eatenTime);
                                    eatenTime = org.example.gpu.Timer.getFloatTime();

                                } else {
                                    appleVisible = true;
//                                    appleSpawned = false;
                                }
                            }
                            if (appleVisible) {
                                appleDistance = (float)Math.sqrt(Math.pow(Math.abs(a[0]), 2) + Math.pow(Math.abs(a[1]),2));


                                if (Math.pow(Math.abs(p[0] - a[0]), 2) + Math.pow(Math.abs(p[1] - a[1]),2) <= collisionWithApple) {       //Проверка колизии змеи и яблока
                                    appleSpawned = false;
                                    appleVisible = false;
                                    player.setDelay();
                                    player.minusCell();
//                                player.setPhantomXY();
//                                Picture.countOfApples += 1;
                                    eaten = true;
                                } else {
                                    for (Enemy enemy : Enemy.activeEnemies) {
                                        if (Math.pow(Math.abs(enemy.getXy().get(0)[0] - a[0]), 2) + Math.pow(Math.abs(enemy.getXy().get(0)[1] - a[1]), 2) <= collisionWithApple) {       //Проверка колизии змеи и яблока

                                            appleSpawned = false;
                                            appleVisible = false;
//                                enemy.setDelay();
                                            enemy.isGrow = true;
                                            enemy.setEatAndAngry(true);
                                            enemy.setCurrentDelay((int) (enemy.getDelay() / 2));
                                            eaten = true;
                                        }
                                    }
                                }

                                if (eaten) {
                                    eatenTime = org.example.gpu.Timer.getFloatTime();
                                    eatenDelay = 0;
                                    a = Apple.getXy();
//                            player.grow();
                                }

                            }
                            a = apple.getXy();
                            if (eatenDelay < eatenDelayStat) {
                                eatenDelay++;
                            }
                            if (eaten) {
                                if (eatenTimelast > 0) {
                                    eatenTimelast += org.example.gpu.Timer.getFloatTime() - eatenTime;
                                    eatenTime = org.example.gpu.Timer.getFloatTime();
                                } else {
                                    eatenTimelast = org.example.gpu.Timer.getFloatTime() - eatenTime;
                                }
                                if (eatenDelay >= eatenDelayStat) {
                                    apple.setXy();

                                    a = apple.getXy();


                                    eaten = false;
                                }
                            }
                            if (eatenDelay >= eatenDelayStat) {

                                eatenTime = org.example.gpu.Timer.getFloatTime();
                                appleSpawned = true;
//                                appleVisible = true;
                                try {
                                    double xTarget = Apple.getXy()[0];
                                    double yTarget = Apple.getXy()[1];
                                    double TargetRadian = 0;
                                    // 1143 372 900 600
                                    TargetRadian = Math.atan2(xTarget, yTarget);
                                    if (TargetRadian < 0) {
                                        TargetRadian += 6.28;

                                    }
                                    double pointWatchX = (100 * Math.sin(TargetRadian));
                                    double pointWatchY = (100 * Math.cos(TargetRadian));
                                    renderingArrow.getModels().get(0).getMovement().setPosition(new Vector3f((float) pointWatchX, (float) pointWatchY, 0));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }


                            for (Enemy enemy : Enemy.enemies) {

                                boolean isNearby = false;
                                float[] nearPoint = new float[2];
                                int rad;
                                rad = 300;
                                float length = 0;
                                nearPoint = new float[2];

                                for (float[] ePoint : enemy.getPhantomXY()) {
                                    for (int i = 0; i < player.getPhantomXY().size(); i++) {
                                        float dest = (float) Math.sqrt(Math.pow(player.getPhantomXY().get(i)[0] - ePoint[0], 2) + Math.pow(player.getPhantomXY().get(i)[1] - ePoint[1], 2));
//                                        if (dest <= Enemy.getSize() + Player.getSize() - 1) {
//                                            if (!isEnd) {
//                                                eatenPlayerTime = Timer.getFloatTime();
//                                                isEnd = true;                          TODO
//                                                trest.mouseControl = false;
//                                                for (Enemy enemy1 : Enemy.enemies) {
//                                                    enemy1.setCurrentDelay(0);
//                                                }
//                                            }
//                                        }
                                        if (dest <= rad) {
                                            if (length == 0) {
                                                length = dest;
                                                nearPoint = new float[]{player.getPhantomXY().get(i)[0], player.getPhantomXY().get(i)[1]};
                                            } else if (length > dest) {
                                                length = dest;
                                                nearPoint = new float[]{player.getPhantomXY().get(i)[0], player.getPhantomXY().get(i)[1]};
                                            }
                                            isNearby = true;
                                        }
                                    }
                                    if (isEnd) {
                                        eatenPlayerTimelast = Timer.getFloatTime() - eatenPlayerTime;
                                    }
                                }

                                if (enemy.isEatAndAngry() /*|| isEnd*/) {
                                    enemy.moveCheck(player.getPhantomXY().get(0), a, true);
                                } else if (isNearby) {
                                    enemy.moveCheck(nearPoint, a, true);
                                } else {
                                    enemy.moveCheck(new float[2], a, false);
                                }
                                boolean notSelf = true;
                            }
                            player.moveCheck();
                            isGo = false;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();

        }

    }




    static boolean appleSpawned = false;
    public static boolean appleVisible = false;


    private ArrayList<Point> pointArrayList = new ArrayList<>();
    private ArrayList<Point> pointEnemyArrayList = new ArrayList<>();
    private ArrayList<Point> pointAppleArrayList = new ArrayList<>();
    private ArrayList<Point> pointToEraseArrayList = new ArrayList<>();
    private ArrayList<Point> pointToEraseSquareArrayList = new ArrayList<>();
    ArrayList<Point> pointsOfPoly = new ArrayList<>();
    ArrayList<Double> pointsRad = new ArrayList<>();

//    public static Polygon getPolygon() {
//        return polygon;
//    }

    static Polygon polygon;
    Point center;

    public Polygon getPoligon() {
        center = new Point(width / 2, height / 2);
        for (Point point1 : walls.getPoints()) {
            boolean isCross = false;
            for (Enemy enemy : Enemy.enemies) {
                for (Line line : enemy.lines) {

                    try {
                        Point firstPointOfWall = new Point(line.getF().x, line.getF().y);
                        Point secondPointOfWall = new Point(line.getS().x, line.getS().y);
                        if (firstPointOfWall.x - secondPointOfWall.x == 0) {
                            firstPointOfWall.x += 1;
                        }
                        if (point1.x - center.x == 0) {
                            point1.x += 1;
                        }
                        Point p = getCross(point1, center, firstPointOfWall, secondPointOfWall);
                        if (belongLine(p, center, point1, firstPointOfWall, secondPointOfWall)) {
                            isCross = true;
                        } else {
//                            g.drawLine(point.x, point.y, p.x, p.y);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            if (!isCross) {
//        g.drawLine(point.x, point.y, point1.x, point1.y);
                PolygonDots.add(point1, getShadowRadian(point1, center));
//                pointsOfPoly.add(point1);
//                pointsRad.add(getShadowRadian(point1, center));
            }
        }
        Line lineL = null;
        for (Enemy enemy : Enemy.enemies) {
            for (Line line : enemy.lines) {
                boolean crossedBranch = false;
                boolean crossedBranch2 = false;
                Point branchPoint1 = new Point(line.getF().x, line.getF().y);
                Point branchPoint2 = new Point(line.getS().x, line.getS().y);
                Point crossPoints1 = null;
                Point crossPoints2 = null;

//                ArrayList<Point> crossPointsCopy1 = new ArrayList<>();
//                ArrayList<Point> crossPointsCopy2 = new ArrayList<>();
                for (Enemy enemy1 : Enemy.enemies) {
                    for (Line line1 : enemy1.lines) {
                        if (line.equals(line1)) {
                            continue;
                        }
                        Point branchPoint3 = new Point(line1.getF().x, line1.getF().y);
                        Point branchPoint4 = new Point(line1.getS().x, line1.getS().y);

                        try {

                            Point sos = new Point(branchPoint1.x, branchPoint1.y);
                            if (sos.x - center.x == 0) {
                                sos.x += 1;
                            }
                            if (branchPoint3.x - branchPoint4.x == 0) {
                                branchPoint3.x += 1;
                            }

                            Point p = getCross(sos, center, branchPoint3, branchPoint4);
                            if (belongLine(p, center, sos, branchPoint3, branchPoint4)) {
                                if (crossPoints1 == null) {
                                    lineL = line;
                                    crossPoints1 = p;
                                } else if (center.distance(crossPoints1) > center.distance(p)) {

                                    crossPoints1 = p;
                                }


//                                crossPoints1.add(p);
                                crossedBranch = true;
//                            cross = p;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            Point sos = new Point(branchPoint2.x, branchPoint2.y);
                            if (sos.x - center.x == 0) {
                                sos.x += 1;
                            }
                            if (branchPoint3.x - branchPoint4.x == 0) {
                                branchPoint3.x += 1;
                            }
                            Point p = getCross(sos, center, branchPoint3, branchPoint4);
                            if (belongLine(p, center, sos, branchPoint3, branchPoint4)) {
                                if (crossPoints2 == null) {
                                    crossPoints2 = p;
                                } else if (center.distance(crossPoints2) > center.distance(p)) {
                                    crossPoints2 = p;
                                }
//                                crossPoints2.add(p);
                                crossedBranch2 = true;
//                            cross2 = p;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (crossedBranch) {
//                    Point cross = getNearPoint(crossPoints1, center);
                    PolygonDots.add(crossPoints1, getShadowRadian(crossPoints1, center));
//                    pointsOfPoly.add(crossPoints1);
//                    pointsRad.add(getShadowRadian(crossPoints1, center));
                    getNextCross(line, center, crossPoints1);
                } else {
                    PolygonDots.add(branchPoint1, getShadowRadian(branchPoint1, center));
//                    pointsOfPoly.add(branchPoint1);
//                    pointsRad.add(getShadowRadian(branchPoint1, center));
                    getNextCross(line, center, branchPoint1);
                    //вызов метода проверки следующих совпадений
                }
                if (crossedBranch2) {
//                    Point cross = getNearPoint(crossPoints2, center);
                    PolygonDots.add(crossPoints2, getShadowRadian(crossPoints2, center));
//                    pointsOfPoly.add(crossPoints2);
//                    pointsRad.add(getShadowRadian(crossPoints2, center));
                    getNextCross(line, center, crossPoints2);
                } else {
                    PolygonDots.add(branchPoint2, getShadowRadian(branchPoint2, center));
//                    pointsOfPoly.add(branchPoint2);
//                    pointsRad.add(getShadowRadian(branchPoint2, center));
                    getNextCross(line, center, branchPoint2);

                }

            }
        }


        int c = PolygonDots.size();
        int x[] = new int[c];
        int y[] = new int[c];
        pointsOfPoly = PolygonDots.getPoints();
        for (int i = 0; i < c; i++) {
            x[i] = pointsOfPoly.get(i).x;
            y[i] = pointsOfPoly.get(i).y;
        }
        pointsOfPoly.clear();
        pointsRad.clear();
        PolygonDots.clear();
        return new Polygon(x, y, c);
    }


    public void getNextCross(Line branch, Point center, Point curPoint) {

        double length = center.distance(curPoint) + 2000;
        double TargetRadian = getShadowRadian(curPoint, center);


        double TargetRadian1 = TargetRadian + 0.05/*Math.toRadians(0.5)*/;
        Point newPoint1 = new Point((int) (length * Math.sin(TargetRadian1)) + center.x, (int) (length * Math.cos(TargetRadian1)) + center.y);
        double TargetRadian2 = TargetRadian - 0.05/*Math.toRadians(0.5)*/;
        Point newPoint2 = new Point((int) (length * Math.sin(TargetRadian2)) + center.x, (int) (length * Math.cos(TargetRadian2)) + center.y);
        Point crossPoints1 = null;
        Point crossPoints2 = null;
        boolean radian1Useless = false;
        boolean radian2Useless = false;
        boolean crossedBranch = false;
        boolean crossedBranch2 = false;
        for (Enemy enemy : Enemy.enemies) {
            for (Line line : enemy.lines) {

                if (radian1Useless && radian2Useless) {
                    break;
                }
                Point branchPoint1 = new Point(line.getF());
                Point branchPoint2 = new Point(line.getS());
                if (!radian1Useless) {
                    try {
                        if (branchPoint1.x - branchPoint2.x == 0) {
                            branchPoint1.x += 1;
                        }
                        if (newPoint1.x - center.x == 0) {
                            newPoint1.x += 1;
                        }
                        Point p = getCross(newPoint1, center, branchPoint1, branchPoint2);
                        if (belongLine(p, center, newPoint1, branchPoint1, branchPoint2)) {
                            if (line.equals(branch)) {
                                radian1Useless = true;
//                                crossPoints1.clear();
                            } else {
                                if (crossPoints1 == null) {
                                    crossPoints1 = p;
                                } else if (center.distance(crossPoints1) > center.distance(p)) {
                                    crossPoints1 = p;
                                }
//                                crossPoints1.add(p);
                                crossedBranch = true;
                            }
//                            g2.drawLine(point.x, point.y, p.x, p.y);

//                            cross = p;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (!radian2Useless) {
                    try {
                        if (newPoint2.x - center.x == 0) {
                            newPoint2.x += 1;
                        }
                        if (branchPoint1.x - branchPoint2.x == 0) {
                            branchPoint1.x += 1;
                        }
                        Point p = getCross(newPoint2, center, branchPoint1, branchPoint2);
                        if (belongLine(p, center, newPoint2, branchPoint1, branchPoint2)) {
                            if (line.equals(branch)) {
                                radian2Useless = true;
//                                crossPoints2.clear();
                            } else {
                                if (crossPoints2 == null) {
                                    crossPoints2 = p;
                                } else if (center.distance(crossPoints2) > center.distance(p)) {
                                    crossPoints2 = p;
                                }
//                                crossPoints2.add(p);
                                crossedBranch2 = true;
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            if (radian1Useless && radian2Useless) {
                break;
            }
        }
        if (crossedBranch && !radian1Useless) {
//            Point sssuk = getNearPoint(crossPoints1, center);
            PolygonDots.add(crossPoints1, getShadowRadian(crossPoints1, center));

//            pointsOfPoly.add(crossPoints1);
//            pointsRad.add(getShadowRadian(crossPoints1, center));
//                    g.drawLine(curPoint.x, curPoint.y, newPoint1.x, newPoint1.y);
        } else if (!crossedBranch && !radian1Useless) {
            for (Line wallsPoints : walls.getWalls()) {
                try {
                    Point wall1 = new Point(wallsPoints.getF());
                    Point wall2 = new Point(wallsPoints.getS());
                    if (wall1.x - wall2.x == 0) {
                        wall1.x += 1;
                    }
                    if (newPoint1.x - center.x == 0) {
                        newPoint1.x += 1;
                    }
                    Point p = getCross(newPoint1, center, wall1, wall2);
                    if (belongWall(p, center, newPoint1, wall1, wall2)) {
                        PolygonDots.add(p, getShadowRadian(p, center));
//                        pointsOfPoly.add(p);
//                        pointsRad.add(getShadowRadian(p, center));
//                            g.drawLine(curPoint.x, curPoint.y, p.x, p.y);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
//            g.drawLine(curPoint.x, curPoint.y, newPoint1.x, newPoint1.y);
            //вызов метода проверки следующих совпадений
        }
        if (crossedBranch2 && !radian2Useless) {
//            Point sssuk = getNearPoint(crossPoints2, center);
            PolygonDots.add(crossPoints2, getShadowRadian(crossPoints2, center));
//            pointsOfPoly.add(crossPoints2);
//            pointsRad.add(getShadowRadian(crossPoints2, center));
//                    g.drawLine(curPoint.x, curPoint.y, newPoint2.x, newPoint2.y);
        } else if (!crossedBranch2 && !radian2Useless) {
            for (Line wallsPoints : walls.getWalls()) {
                try {
                    Point wall1 = new Point(wallsPoints.getF());
                    Point wall2 = new Point(wallsPoints.getS());
                    if (newPoint2.x - center.x == 0) {
                        newPoint2.x += 1;
                    }
                    if (wall1.x - wall2.x == 0) {
                        wall1.x += 1;
                    }
                    Point p = getCross(newPoint2, center, wall1, wall2);
                    if (belongWall(p, center, newPoint2, wall1, wall2)) {
                        PolygonDots.add(p, getShadowRadian(p, center));
//                        pointsOfPoly.add(p);
//                        pointsRad.add(getShadowRadian(p, center));
//                        g.drawLine(curPoint.x, curPoint.y, p.x, p.y);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
//            g.drawLine(curPoint.x, curPoint.y, newPoint2.x, newPoint2.y);
            //вызов метода проверки следующих совпадений
        }

    }


    public Point getNearPoint(ArrayList<Point> variants, Point center) {
        Point point1 = new Point(-3000, -3000);
        for (Point points : variants) {
            if (point1.x == -3000) {
                point1 = points;
            } else {
                if (center.distance(point1) > center.distance(points)) {
                    point1 = points;
                }
            }
        }
        return point1;
    }

    public void sortPolygonPoints() {
        boolean notEnd = false;
        for (int i = 0; i < pointsRad.size() - 1; i++) {
            if (pointsRad.get(i) <= pointsRad.get(i + 1)) {

            } else {
                notEnd = true;
                Point p = pointsOfPoly.get(i);
                pointsOfPoly.set(i, pointsOfPoly.get(i + 1));
                pointsOfPoly.set(i + 1, p);
                double d = pointsRad.get(i);
                pointsRad.set(i, pointsRad.get(i + 1));
                pointsRad.set(i + 1, d);
            }
        }
        if (notEnd) {
            sortPolygonPoints();
        }
    }

    public double getShadowRadian(Point curPoint, Point center) {
        double xTarget = curPoint.x - center.x;
        double yTarget = curPoint.y - center.y;
        return Math.atan2(xTarget, yTarget);
    }


    public boolean belongLine(Point crossPoint, Point center, Point wall, Point pointOfLine1, Point pointOfLine2) {
        int lenthX1 = Math.abs(crossPoint.x - pointOfLine1.x) + Math.abs(crossPoint.x - pointOfLine2.x) - Math.abs(pointOfLine1.x - pointOfLine2.x);
        int lenthY1 = Math.abs(crossPoint.y - pointOfLine1.y) + Math.abs(crossPoint.y - pointOfLine2.y) - Math.abs(pointOfLine1.y - pointOfLine2.y);
        int lenthX2 = Math.abs(crossPoint.x - center.x) + Math.abs(crossPoint.x - wall.x) - Math.abs(center.x - wall.x);
        int lenthY2 = Math.abs(crossPoint.y - center.y) + Math.abs(crossPoint.y - wall.y) - Math.abs(center.y - wall.y);
        if (lenthX1 < 5 && lenthY1 < 5 && lenthX2 < 5 && lenthY2 < 5) {
//            if(crossPoint.equals(wall)|crossPoint.equals(pointOfLine1)||crossPoint.equals(pointOfLine2)){
//                return false;
//            }

            return true;
        }
        return false;
    }

    public boolean belongWall(Point crossPoint, Point center, Point wall, Point pointOfLine1, Point pointOfLine2) {
        int lenthX1 = Math.abs(crossPoint.x - pointOfLine1.x) + Math.abs(crossPoint.x - pointOfLine2.x) - Math.abs(pointOfLine1.x - pointOfLine2.x);
        int lenthY1 = Math.abs(crossPoint.y - pointOfLine1.y) + Math.abs(crossPoint.y - pointOfLine2.y) - Math.abs(pointOfLine1.y - pointOfLine2.y);
        int lenthX2 = Math.abs(crossPoint.x - center.x) + Math.abs(crossPoint.x - wall.x) - Math.abs(center.x - wall.x);
        int lenthY2 = Math.abs(crossPoint.y - center.y) + Math.abs(crossPoint.y - wall.y) - Math.abs(center.y - wall.y);
        if (lenthX1 < 10 && lenthY1 < 10 && lenthX2 < 10 && lenthY2 < 10) {
            return true;
        }
        return false;
    }

    public static Point getCross(Point p1, Point p2, Point p3, Point p4) {
        Line line = new Line(new Point(p1.x, p1.y), new Point(p2.x, p2.y));
        Line secLine = new Line(new Point(p3.x, p3.y), new Point(p4.x, p4.y));
        Point p = line.getIntersectionPoint(secLine);

        return new Point((int) (p.getX()), (int) p.getY());

    }
}


