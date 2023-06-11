package org.example.Painter;

import org.example.Enemy.Enemy;
import org.example.Enemy.Line;
import org.example.Enemy.PolygonDots;
import org.example.Player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Process {

    static boolean ready = false;
    private boolean targetChanged = false;
    public static boolean eaten = false;
    private int eatenDelayStat = 50;
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

    double[] p = new double[2];
    static boolean gameOver = false;
    static boolean screenMove = false;

    public static double[] getDirection() {
        return new double[]{direction[0], direction[1]};
    }

    static double[] direction = new double[2];
    private int outLeft = (int) (Picture.image.getWidth() * 0.1 * -1) / Enemy.step * Enemy.step;
    private int fromLeft = (int) (Picture.image.getWidth() * 1.2 * -1) / Enemy.step * Enemy.step;

    private int outRight = (int) (Picture.image.getWidth() + Picture.image.getWidth() * 0.1) / Enemy.step * Enemy.step;
    private int fromRight = (int) (Picture.image.getWidth() * 1.2) / Enemy.step * Enemy.step;

    private int outUp = (int) (Picture.image.getHeight() * 0.2 * -1) / Enemy.step * Enemy.step;
    private int fromUp = (int) (Picture.image.getHeight() * 1.3 * -1) / Enemy.step * Enemy.step;

    private int outDown = (int) (Picture.image.getHeight() + Picture.image.getHeight() * 0.2) / Enemy.step * Enemy.step;
    private int fromDown = (int) (Picture.image.getHeight() * 1.3) / Enemy.step * Enemy.step;
    private int agressiveMode = 300;
    ArrayList<Line> lines = new ArrayList<>();
    Walls walls;
    static boolean timeRunUp = false;
    static Timer timer = new Timer(20, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
//            System.out.println("start");
            timeRunUp = true;
            timer.stop();
        }
    });

    private int width;
    private int height;

    Process(int width, int height) {
        this.width = width;
        this.height = height;
// width = Picture.image.getWidth();
//        height = Picture.image.getHeight();
        walls = new Walls(width, height);
        Apple apple = new Apple(width, height);
        Player player = new Player(width, height);
        for (int i = 0; i < 20; i++) {
            new Enemy(width, height, false);
        }
        for (int i = 0; i < 10; i++) {
            new Enemy(width, height, true);
        }


//        int lenth = Snake.step*Snake.maxSize;
//        for (int i = 0;i<Picture.image.getWidth()+lenth;i+=lenth){
//            for (int y = 0;y<Picture.image.getHeight()+lenth;y+=lenth){        //Добавление регионов поиска
//                new SearchRegion(new Point(i,y),lenth,lenth);
//            }
//        }
//        SearchRegion.addNearbyRegions();


//        for(Snake snake : Snake.snakes){
//            SearchRegion region = SearchRegion.getRegion(snake.getXy().get(0).x,snake.getXy().get(0).y);
//            region.addOrRemoveSnake(snake);
//            snake.setRegion(region);
//        }
//        Executors.newSingleThreadExecutor().submit(()->{
//
//            while (true){
//                for (Snake snake : Snake.snakes){
//                    for (SearchRegion region : SearchRegion.searchRegions){
//                        region.addOrRemoveSnake(snake);
//                    }
//                }
//                try {
//                    TimeUnit.MICROSECONDS.sleep(1);
//                }catch (Exception e){
//
//                }
//            }
//        });
//        for (double x = lifeArea.getX(); x < lifeArea.getWidth() + lifeArea.getX(); x++) {
//            for (double y = lifeArea.getY(); y < lifeArea.getHeight() + lifeArea.getY(); y++) {
//                if (x == lifeArea.getX() || y == lifeArea.getY() || x == lifeArea.getWidth() + lifeArea.getX() - 1 || y == lifeArea.getHeight() + lifeArea.getY() - 1) {          //Макет региона обитания
//                    pointsOfRect.add(new Point((int) x, (int) y));
//
//                }
//            }
//        }
        int halfWidth = Picture.image.getWidth() / 2;
        int halfHeight = Picture.image.getHeight() / 2;
        for (int x = -agressiveMode; x <= agressiveMode; x++) {
            for (int y = -agressiveMode; y <= +agressiveMode; y++) {
                if ((Math.pow(x, 2)) + (Math.pow(y, 2)) >= Math.pow(agressiveMode - 1, 2) &&
                        (Math.pow(x, 2)) + (Math.pow(y, 2)) <= Math.pow(agressiveMode, 2)) {
                    pointsOfRect.add(new Point(x + halfWidth, y + halfHeight));
                }
//                if ((Math.pow(x, 2)) + (Math.pow(y, 2)) >= Math.pow(200 - 1, 2) &&
//                        (Math.pow(x, 2)) + (Math.pow(y, 2)) <= Math.pow(200, 2)) {
//                    pointsOfRect.add(new Point(x + halfWidth, y + halfHeight));
//                }

            }
        }
        for (double x = -Player.getSize(); x <= Player.getSize(); x++) {
            for (double y = -Player.getSize(); y <= Player.getSize(); y++) {
                if ((int) ((Math.pow((x), 2)) + (Math.pow((y), 2))) < (int) Math.pow(Player.getSize(), 2)) {          //Макет круга змеи
                    pointArrayList.add(new Point((int) x, (int) y));

                }
            }
        }
        for (double x = -Enemy.getSize(); x <= Enemy.getSize(); x++) {
            for (double y = -Enemy.getSize(); y <= Enemy.getSize(); y++) {
                if ((int) ((Math.pow((x), 2)) + (Math.pow((y), 2))) < (int) Math.pow(Enemy.getSize(), 2)) {          //Макет круга змеи
                    pointEnemyArrayList.add(new Point((int) x, (int) y));

                }
            }
        }
        for (int x = -Apple.getAppleSize(); x < +Apple.getAppleSize(); x++) {
            for (int y = -Apple.getAppleSize(); y < +Apple.getAppleSize(); y++) {
                if ((Math.pow((x), 2)) + (Math.pow((y), 2)) <= Math.pow(Apple.getAppleSize(), 2) && (Math.pow((x), 2)) + (Math.pow((y), 2)) >= Math.pow(Apple.getAppleSize() * 0.7, 2)) {          //Макет apple
                    pointAppleArrayList.add(new Point((int) x, (int) y));

                }
            }
        }

        for (double x = -(Player.getSize() * 1.5); x <= Player.getSize() * 1.5; x++) {
            for (double y = -(Player.getSize() * 1.5); y <= Player.getSize() * 1.5; y++) {
                if ((int) ((Math.pow((x), 2)) + (Math.pow((y), 2))) <= (int) Math.pow(Player.getSize() * 1.5, 2)) {          //Макет затирки вокруг круга змеи
                    pointToEraseArrayList.add(new Point((int) x, (int) y));

                }
            }
        }
//        int in =/*(int) Snake.getSize()*/(int)(Math.abs(Player.step-Player.getSize())/2+Math.min(Player.getSize(),Player.step))/2;
//        for (double x =- in; x <= in; x++) {
//            for (double y = - in; y <=in ; y++) {
//                //Макет затирки вокруг круга змеи
//                pointToEraseSquareArrayList.add(new Point((int) x, (int) y));
//
//
//            }
//        }
        try {
            int I = 0;
            while (true) {
                timeRunUp = false;
                timer.start();
                Date date = new Date();

                ready = false;
                lines.clear();
                pointsOfApple.clear();
//                pointsToErase.clear();
                points.clear();
                colors.clear();
                pointsOfArrow.clear();
                for (Point point : pointsOfRect) {
                    pointsOfArrow.add(new Point(point));

//                        if (Math.pow(player.getXy().get(0)[0] - halfWidth, 2) + Math.pow(player.getXy().get(0)[1] - halfHeight, 2) <= Math.pow(1, 2)) {
//
////                    }
////                    if (lifeArea.contains(player.getXy().get(0)[0], player.getXy().get(0)[1])) {
//                    colors.add(Color.green);
//                            screenMove = false;
//                        } else {
//                            colors.add(Color.red);

//                        }
                }
                if (Picture.mouseControl) {

                    if (player.getDirection() != null) {
                        screenMove = true;
                        direction = player.getDirection();
                    }

                    if (screenMove) {
                        for (Enemy enemy : Enemy.enemies) {
                            enemy.moveXy(direction);
//                            if (enemy.getXy().get(0)[0] < (outLeft)) {
//                                enemy.moveXy(new double[]{fromLeft, 0});
//                            } else if (enemy.getXy().get(0)[0] > (outRight)) {
//                                enemy.moveXy(new double[]{fromRight, 0});
//                            }
//                            if (enemy.getXy().get(0)[1] < (outUp)) {
//                                enemy.moveXy(new double[]{0, fromUp});
//                            } else if (enemy.getXy().get(0)[1] > (outDown)) {
//                                enemy.moveXy(new double[]{0, fromDown});
//                            }

                        }

                        screenMove = true;
                        apple.moveXy(direction);
                        player.moveXy(direction);


                    }
                }
                for (Enemy enemy : Enemy.enemies) {

                    if (enemy.getXy().get(0)[0] < (outLeft)) {
                        enemy.moveXy(new double[]{fromLeft, 0});
                        enemy.reverse();
                    } else if (enemy.getXy().get(0)[0] > (outRight)) {
                        enemy.moveXy(new double[]{fromRight, 0});
                        enemy.reverse();
                    }
                    if (enemy.getXy().get(0)[1] < (outUp)) {
                        enemy.moveXy(new double[]{0, fromUp});
                        enemy.reverse();
                    } else if (enemy.getXy().get(0)[1] > (outDown)) {
                        enemy.moveXy(new double[]{0, fromDown});
                        enemy.reverse();
                    }


                }
                if (Apple.getXy()[0] < (outLeft)) {
                    apple.moveXy(new double[]{fromLeft, 0});

                } else if (Apple.getXy()[0] > (outRight)) {
                    apple.moveXy(new double[]{fromRight, 0});

                }
                if (Apple.getXy()[1] < (outUp)) {
                    apple.moveXy(new double[]{0, fromUp});

                } else if (Apple.getXy()[1] > (outDown)) {
                    apple.moveXy(new double[]{0, fromDown});

                }
                double[] s = player.getXy().get(0);
                if (appleSpawned) {
                    if (Math.pow(Math.abs(s[0] - p[0]), 2) + Math.pow(Math.abs(s[1] - p[1]), 2) <= collisionWithApple) {       //Проверка колизии змеи и яблока
//                        try {
//                            for (Point point : pointAppleArrayList) {
//                                pointsToErase.add(new Point(point.x + (int) p[0], point.y + (int) p[1]));
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
                        appleSpawned = false;
                        player.setDelay();
                        if (player.getXy().size() > 3) {
                            player.getXy().remove(player.getXy().size() - 1);
                        }
                        Picture.countOfApples += 1;
                        eaten = true;
                    } else {
                        for (Enemy enemy : Enemy.activeEnemies) {
                            if (Math.pow(Math.abs(enemy.getXy().get(0)[0] - p[0]), 2) + Math.pow(Math.abs(enemy.getXy().get(0)[1] - p[1]), 2) <= collisionWithApple) {       //Проверка колизии змеи и яблока
//                                try {
//                                    for (Point point : pointAppleArrayList) {
//                                        pointsToErase.add(new Point(point.x + (int) p[0], point.y + (int) p[1]));
//                                    }
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
                                appleSpawned = false;
//                                enemy.setDelay();
                                enemy.grow();
                                enemy.setEatAndAngry(true);
                                enemy.setCurrentDelay((int) (enemy.getDelay() / 2));
                                eaten = true;
                            }
                        }
                    }

                    if (eaten) {
                        eatenDelay = 0;
                        p = Apple.getXy();
//                            player.grow();
                    }

                }
                p = apple.getXy();
                if (eatenDelay < eatenDelayStat) {
                    eatenDelay++;
                }
                if (eaten && eatenDelay >= eatenDelayStat) {

                    apple.setXy();
                    p = apple.getXy();


                    eaten = false;
                }
                if (eatenDelay >= eatenDelayStat) {
                    for (Point point : pointAppleArrayList) {
                        pointsOfApple.add(new Point(point.x + (int) p[0], point.y + (int) p[1]));
                    }
                    appleSpawned = true;
                    try {
                        double xTarget = Apple.getXy()[0] - Picture.width / 2;
                        double yTarget = Apple.getXy()[1] - Picture.height / 2;
                        double TargetRadian = 0;
                        // 1143 372 900 600
                        TargetRadian = Math.atan2(xTarget, yTarget);
                        if (TargetRadian < 0) {
                            TargetRadian += 6.28;

                        }
                        double pointWatchX = (100 * Math.sin(TargetRadian) + Picture.width / 2);
                        double pointWatchY = (100 * Math.cos(TargetRadian) + Picture.height / 2);
                        for (Point point : pointArrayList) {
                            pointsOfArrow.add(new Point(point.x + (int) pointWatchX, point.y + (int) pointWatchY));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }


                for (Enemy enemy : Enemy.enemies) {

                    boolean isNearby = false;
                    double[] nearPoint = new double[2];
                    int rad;
//                    if(enemy.isActive){
//                        rad=200;
//                    }else {
                    rad = 300;
//                    }
                    double length = 0;
                    nearPoint = new double[2];
                    double[] ePoint = enemy.getPhantomXY().get(0);


                    for (double[] point : player.getPhantomXY()) {
                        double dest = Math.sqrt(Math.pow(point[0] - ePoint[0], 2) + Math.pow(point[1] - ePoint[1], 2));
                        if (dest <= Enemy.getSize() + Player.getSize() - 1) {
                            if (!Picture.isEnd) {
                                Picture.pauseMenuON();
                                for (Enemy enemy1 : Enemy.enemies) {
                                    enemy1.setCurrentDelay(0);
                                }
                            }

                        }
                        if (dest <= rad) {
                            if (length == 0) {
                                length = dest;
                                nearPoint = new double[]{point[0], point[1]};
                            } else if (length > dest) {
                                length = dest;
                                nearPoint = new double[]{point[0], point[1]};
                            }
                            isNearby = true;
                        }
                    }

                    ;
                    ;

//if(Picture.isEnd){
//    enemy.moveCheck(new double[]{Picture.xMouse,Picture.yMouse}, p, true);
//}else
                    if (enemy.isEatAndAngry() || Picture.isEnd) {
                        enemy.moveCheck(player.getPhantomXY().get(0), p, true);
                    } else if (isNearby) {
                        enemy.moveCheck(nearPoint, p, true);
                    } else {
                        enemy.moveCheck(new double[2], p, false);
                    }
                    boolean notSelf = true;

//                    for (Enemy enemy1 : Enemy.enemies) {
////            if (snake.equals(snake1)) {
////                continue;
////            } else {
//                        for (double[] i : enemy1.getXy()) {
//                            if (i[0] == enemy.taillessCopy[0] && i[1] == enemy.taillessCopy[1]) {
//                                notSelf = false;
//                            }
//                        }
////            }
//                    }
//                    if (notSelf) {
////                        if (enemy.taillessCopy[0] != 0) {                                                                           //Затирка последней ячейки змеи
////                            for (Point point : pointEnemyArrayList) {
////                                pointsToErase.add(new Point(point.x + (int) enemy.taillessCopy[0], point.y + (int) enemy.taillessCopy[1]));
////
////
////                            }
////                        }
//                        if (enemy.taillessPhantomCopy[0] != 0) {                                                                           //Затирка последней ячейки змеи
//                            for (Point point : pointEnemyArrayList) {
//
//                                pointsToErase.add(new Point(point.x + (int) enemy.taillessPhantomCopy[0], point.y + (int) enemy.taillessPhantomCopy[1]));
//
//                            }
//                        }
//                    }

                    //TODO
                    for (Line line : enemy.lines) {
                        lines.add(line);
                    }
                }

//                for (Point point : pointsOfRect) {
//                    points.add(new Point(point));
//                    if (lifeArea.contains(player.getXy().get(0)[0], player.getXy().get(0)[1])) {
//                        colors.add(Color.green);
//                        screenMove = false;
//                    } else {
//                        colors.add(Color.red);
//                        screenMove = true;
//                        direction = player.getDirection();
//
//                    }
//                }
//                if (!lifeArea.contains(player.getXy().get(0)[0], player.getXy().get(0)[1])) {
//                    for (Enemy enemy : Enemy.enemies) {
//                        enemy.moveXy(direction);
//                    }
//                    screenMove = true;
//                    apple.moveXy(direction);
//
//
//
//                }
                Date date1 = new Date();
                polygon = getPoligon();
                System.out.println(new Date().getTime() - date1.getTime());
                playerProcess(player);
                while (!timeRunUp) {
                    try {
                        TimeUnit.MICROSECONDS.sleep(1);
                    } catch (Exception e) {

                    }
                }
//                if ((timeRunUp)) {
                    ready = true;
//                }
//                System.out.println(new Date().getTime() - date.getTime());
                while (ready) {
                    try {
                        TimeUnit.MILLISECONDS.sleep(1);
                    } catch (Exception e) {

                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

    }

    public void playerProcess(Player player) {
        player.moveCheck((int) Apple.getXy()[0], (int) Apple.getXy()[1], Picture.mouseControl, targetChanged);
        for (int i = 0; i < player.getPhantomXY().size(); i++) {
            for (Point point : pointArrayList) {
                points.add(new Point(point.x + (int) player.getPhantomXY().get(i)[0], point.y + (int) player.getPhantomXY().get(i)[1]));
                colors.add(player.getColor());
            }
        }
        for (Enemy enemy : Enemy.enemies) {
            for (double[] i : enemy.getPhantomXY()) {
//        if(polygon.cont

                for (Point point : pointEnemyArrayList) {

                    if (Math.pow(i[0] - (double) (Picture.width / 2), 2) + Math.pow(i[1] - (double) Picture.height / 2, 2) >= Math.pow(300, 2)) {
                        continue;
                    }

                    points.add(new Point(point.x + (int) i[0], point.y + (int) i[1]));
                    colors.add(enemy.getColor());
                }
            }
        }

    }

    static boolean appleSpawned = false;


    private ArrayList<Point> pointArrayList = new ArrayList<>();
    private ArrayList<Point> pointEnemyArrayList = new ArrayList<>();
    private ArrayList<Point> pointAppleArrayList = new ArrayList<>();
    private ArrayList<Point> pointToEraseArrayList = new ArrayList<>();
    private ArrayList<Point> pointToEraseSquareArrayList = new ArrayList<>();
    ArrayList<Point> pointsOfPoly = new ArrayList<>();
    ArrayList<Double> pointsRad = new ArrayList<>();

    public static Polygon getPolygon() {
        return polygon;
    }

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
//e.printStackTrace();
                    }
                }
            }

            if (!isCross) {
//        g.drawLine(point.x, point.y, point1.x, point1.y);
                PolygonDots.add(point1,getShadowRadian(point1, center));
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
                                if(crossPoints1 == null){
                                    lineL = line;
                                    crossPoints1 = p;
                                }else if (center.distance(crossPoints1) > center.distance(p)) {

                                    crossPoints1 = p;
                                }


//                                crossPoints1.add(p);
                                crossedBranch = true;
//                            cross = p;
                            }
                        } catch (Exception e) {

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
                                if(crossPoints2 == null){
                                    crossPoints2 = p;
                                }else if (center.distance(crossPoints2) > center.distance(p)) {
                                    crossPoints2 = p;
                                }
//                                crossPoints2.add(p);
                                crossedBranch2 = true;
//                            cross2 = p;
                            }
                        } catch (Exception e) {

                        }
                    }
                }
                if (crossedBranch) {
//                    Point cross = getNearPoint(crossPoints1, center);
                    PolygonDots.add(crossPoints1,getShadowRadian(crossPoints1, center));
//                    pointsOfPoly.add(crossPoints1);
//                    pointsRad.add(getShadowRadian(crossPoints1, center));
                    getNextCross(line, center, crossPoints1);
                } else {
                    PolygonDots.add(branchPoint1,getShadowRadian(branchPoint1, center));
//                    pointsOfPoly.add(branchPoint1);
//                    pointsRad.add(getShadowRadian(branchPoint1, center));
                    getNextCross(line, center, branchPoint1);
                    //вызов метода проверки следующих совпадений
                }
                if (crossedBranch2) {
//                    Point cross = getNearPoint(crossPoints2, center);
                    PolygonDots.add(crossPoints2,getShadowRadian(crossPoints2, center));
//                    pointsOfPoly.add(crossPoints2);
//                    pointsRad.add(getShadowRadian(crossPoints2, center));
                    getNextCross(line, center, crossPoints2);
                } else {
                    PolygonDots.add(branchPoint2,getShadowRadian(branchPoint2, center));
//                    pointsOfPoly.add(branchPoint2);
//                    pointsRad.add(getShadowRadian(branchPoint2, center));
                    getNextCross(line, center, branchPoint2);

                }

            }
        }


        int  c =PolygonDots.size();
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


        double TargetRadian1 = TargetRadian + 0.06/*Math.toRadians(0.5)*/;
        Point newPoint1 = new Point((int) (length * Math.sin(TargetRadian1)) + center.x, (int) (length * Math.cos(TargetRadian1)) + center.y);
        double TargetRadian2 = TargetRadian - 0.06/*Math.toRadians(0.5)*/;
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
                                if(crossPoints1 == null){
                                    crossPoints1 = p;
                                }else if (center.distance(crossPoints1) > center.distance(p)) {
                                    crossPoints1 = p;
                                }
//                                crossPoints1.add(p);
                                crossedBranch = true;
                            }
//                            g2.drawLine(point.x, point.y, p.x, p.y);

//                            cross = p;
                        }
                    } catch (Exception e) {

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
                                if(crossPoints2 == null){
                                    crossPoints2 = p;
                                }else if (center.distance(crossPoints2) > center.distance(p)) {
                                    crossPoints2 = p;
                                }
//                                crossPoints2.add(p);
                                crossedBranch2 = true;
                            }

                        }
                    } catch (Exception e) {

                    }
                }
            }
            if (radian1Useless && radian2Useless) {
                break;
            }
        }
        if (crossedBranch && !radian1Useless) {
//            Point sssuk = getNearPoint(crossPoints1, center);
            PolygonDots.add(crossPoints1,getShadowRadian(crossPoints1, center));
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
                        PolygonDots.add(p,getShadowRadian(p, center));
//                        pointsOfPoly.add(p);
//                        pointsRad.add(getShadowRadian(p, center));
//                            g.drawLine(curPoint.x, curPoint.y, p.x, p.y);
                    }
                } catch (Exception e) {
//e.printStackTrace();
                }

            }
//            g.drawLine(curPoint.x, curPoint.y, newPoint1.x, newPoint1.y);
            //вызов метода проверки следующих совпадений
        }
        if (crossedBranch2 && !radian2Useless) {
//            Point sssuk = getNearPoint(crossPoints2, center);
            PolygonDots.add(crossPoints2,getShadowRadian(crossPoints2, center));
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
                        PolygonDots.add(p,getShadowRadian(p, center));
//                        pointsOfPoly.add(p);
//                        pointsRad.add(getShadowRadian(p, center));
//                        g.drawLine(curPoint.x, curPoint.y, p.x, p.y);
                    }
                } catch (Exception e) {

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


