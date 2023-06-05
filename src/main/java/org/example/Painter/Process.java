package org.example.Painter;

import org.example.Enemy.Enemy;
import org.example.Player.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
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
    static public Rectangle lifeArea = new Rectangle(Picture.image.getWidth() / 2 - 100, Picture.image.getHeight() / 2 - 100, 200, 200);
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


    Process() {

        Apple apple = new Apple(Picture.image);
        Player player = new Player(Picture.image);
        for (int i = 0; i < 100; i++) {
            new Enemy(Picture.image, false);
        }
        for (int i = 0; i < 20; i++) {
            new Enemy(Picture.image, true);
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


                ready = false;
                pointsOfApple.clear();
//                pointsToErase.clear();
                points.clear();
                colors.clear();
                pointsOfArrow.clear();
                for (Point point : pointsOfRect) {
                    points.add(new Point(point));

//                        if (Math.pow(player.getXy().get(0)[0] - halfWidth, 2) + Math.pow(player.getXy().get(0)[1] - halfHeight, 2) <= Math.pow(1, 2)) {
//
////                    }
////                    if (lifeArea.contains(player.getXy().get(0)[0], player.getXy().get(0)[1])) {
                    colors.add(Color.green);
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

                    apple.setXy(Picture.image);
                    p = apple.getXy();


                    eaten = false;
                }
                if (eatenDelay >= eatenDelayStat) {
                    for (Point point : pointAppleArrayList) {
                        pointsOfApple.add(new Point(point.x + (int) p[0], point.y + (int) p[1]));
                    }
                    appleSpawned = true;
                    try {
                        double xTarget = Apple.getXy()[0] - Picture.width/2;
                        double yTarget = Apple.getXy()[1] - Picture.height/2;
                        double TargetRadian = 0;
                        // 1143 372 900 600
                        double a = Math.pow((yTarget), 2) + Math.pow((xTarget), 2);
                        if (xTarget > 0) {
                            TargetRadian = Math.acos((yTarget) / Math.sqrt(a));
//                            System.out.println(TargetRadian);
                        } else if (xTarget < 0 && yTarget < 0) {
                            TargetRadian = (Math.acos((xTarget) / Math.sqrt(a))) + 1.57;
//                            System.out.println(TargetRadian);
                        } else {
                            TargetRadian = (Math.asin((xTarget) / Math.sqrt(a))) + 6.28;
//                            System.out.println(TargetRadian);
                        }
                        double pointWatchX = (100 * Math.sin(TargetRadian) + Picture.width/2);
                        double pointWatchY = (100 * Math.cos(TargetRadian) + Picture.height/2);
                        for (Point point : pointArrayList) {
                            pointsOfArrow.add(new Point(point.x + (int) pointWatchX, point.y + (int) pointWatchY));
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }


                playerProcess(player);


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
                    for (double[] ePoint : enemy.getPhantomXY()) {


                        for (double[] point : player.getPhantomXY()) {
                            double dest = Math.pow(Math.abs(point[0] - ePoint[0]), 2) + Math.pow(Math.abs(point[1] - ePoint[1]), 2);
                            if (dest <= Math.pow(Enemy.getSize() + Player.getSize() - 1, 2)) {
                                if (!Picture.isEnd) {
                                    Picture.pauseMenuON();
                                    for(Enemy enemy1 :Enemy.enemies){
                                        enemy1.setCurrentDelay(0);
                                    }
                                }

                            }
                            if (dest <= Math.pow(rad, 2)) {
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
                    }
//if(Picture.isEnd){
//    enemy.moveCheck(new double[]{Picture.xMouse,Picture.yMouse}, p, true);
//}else
                    if (enemy.isEatAndAngry()||Picture.isEnd) {
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
                    for (double[] i : enemy.getPhantomXY()) {

                        for (Point point : pointEnemyArrayList) {
                            points.add(new Point(point.x + (int) i[0], point.y + (int) i[1]));
                            colors.add(enemy.getColor());
                        }
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

                ready = true;
                while (ready) {
//                    try {
//                        TimeUnit.MICROSECONDS.sleep(1);
//                    } catch (Exception e) {
//
//                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

    }

    public void playerProcess(Player player) {


        player.moveCheck((int) Apple.getXy()[0], (int) Apple.getXy()[1], Picture.mouseControl, targetChanged);


        boolean notSelf = true;


//    if (player.taillessCopy[0] != 0) {                                                                           //Затирка последней ячейки змеи
//        for (Point point : pointToEraseArrayList) {
//            pointsToErase.add(new Point(point.x + (int) player.taillessCopy[0], point.y + (int) player.taillessCopy[1]));
//
//
//        }
//    }
//        if (player.taillessPhantomCopy[0] != 0) {                                                                           //Затирка последней ячейки змеи
//            for (Point point : pointToEraseArrayList) {
//
//                pointsToErase.add(new Point(point.x + (int) player.taillessPhantomCopy[0], point.y + (int) player.taillessPhantomCopy[1]));
//
//            }
//        }
        for (int i = 0; i < player.getPhantomXY().size(); i++) {
//            if (i >= player.getPhantomXY().size() - 2) {
//                continue;
//            }
            for (Point point : pointArrayList) {
                points.add(new Point(point.x + (int) player.getPhantomXY().get(i)[0], point.y + (int) player.getPhantomXY().get(i)[1]));
                colors.add(player.getColor());
            }
        }


    }

    static boolean appleSpawned = false;


    private ArrayList<Point> pointArrayList = new ArrayList<>();
    private ArrayList<Point> pointEnemyArrayList = new ArrayList<>();
    private ArrayList<Point> pointAppleArrayList = new ArrayList<>();
    private ArrayList<Point> pointToEraseArrayList = new ArrayList<>();
    private ArrayList<Point> pointToEraseSquareArrayList = new ArrayList<>();

}


