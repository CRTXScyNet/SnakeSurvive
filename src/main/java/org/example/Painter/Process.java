package org.example.Painter;

import org.example.Player.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Process {

    static boolean ready = false;
    private boolean targetChanged = false;
    private boolean eaten = false;
    private int eatenDelayStat = 50;
    private int eatenDelay = eatenDelayStat;
    static int appleIsEatenForSnakeStat = 150;
    static int appleIsEatenForSnake = appleIsEatenForSnakeStat;

    static ArrayList<Point> points = new ArrayList<>();
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

    int[] p = new int[2];

    Process() {

        Apple apple = new Apple(Picture.image);
        Player player = new Player(Picture.image);


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
        for (double x = -Player.getSize(); x <= Player.getSize(); x++) {
            for (double y = -Player.getSize(); y <= Player.getSize(); y++) {
                if ((int) ((Math.pow((x), 2)) + (Math.pow((y), 2))) < (int) Math.pow(Player.getSize(), 2)) {          //Макет круга змеи
                    pointArrayList.add(new Point((int) x, (int) y));

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

            while (true) {


                ready = false;
                pointsOfApple.clear();
                pointsToErase.clear();
                points.clear();
                colors.clear();

                if (appleIsEatenForSnake < appleIsEatenForSnakeStat) {
                    appleIsEatenForSnake++;
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
                if (eatenDelay >= eatenDelayStat && !appleSpawned) {
                    for (Point point : pointAppleArrayList) {
                        pointsOfApple.add(new Point(point.x + p[0], point.y + p[1]));
                    }
                    appleSpawned = true;
                }

                try {


                    player.moveCheck(Apple.getXy()[0], Apple.getXy()[1], Picture.mouseControl, targetChanged);

                    double[] s = player.getXy().get(0);
                    if (eatenDelay >= eatenDelayStat) {

                        if (Math.pow(Math.abs(s[0] - p[0]), 2) + Math.pow(Math.abs(s[1] - p[1]), 2) <= collisionWithApple) {       //Проверка колизии змеи и яблока
                            try {
                                for (Point point : pointAppleArrayList) {
                                    pointsToErase.add(new Point(point.x + p[0], point.y + p[1]));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            appleSpawned = false;
                            player.setDelay();
                            eaten = true;
                        }

                        if (eaten) {
                            eatenDelay = 0;
                            p = Apple.getXy();
//                            player.grow();
                            if(player.getXy().size()>2){
                                player.getXy().remove(player.getXy().size()-1);
                            }

                        }
                    }
                    boolean notSelf = true;


                    if (player.taillessCopy[0] != 0) {                                                                           //Затирка последней ячейки змеи
                        for (Point point : pointToEraseArrayList) {
                            pointsToErase.add(new Point(point.x + (int) player.taillessCopy[0], point.y + (int) player.taillessCopy[1]));


                        }
                    }
                    if (player.taillessPhantomCopy[0] != 0) {                                                                           //Затирка последней ячейки змеи
                        for (Point point : pointToEraseArrayList) {

                            pointsToErase.add(new Point(point.x + (int) player.taillessPhantomCopy[0], point.y + (int) player.taillessPhantomCopy[1]));

                        }
                    }
                    for (int i = 0; i < player.getPhantomXY().size(); i++) {
                        if (i >= player.getPhantomXY().size() - 1) {
                            continue;
                        }
                        for (Point point : pointArrayList) {
                            points.add(new Point(point.x + (int) player.getPhantomXY().get(i)[0], point.y + (int) player.getPhantomXY().get(i)[1]));
                            colors.add(player.getColor());
                        }
                    }

                } catch (Exception e) {

                }


//                if (Picture.isAdd) {
//                    try {for(int i = 0; i<1;i++){
//                        Snake snake =  new Snake(Picture.image);
//                        for (SearchRegion region : SearchRegion.searchRegions){
//                            region.containsSnake(snake);                                                              //Добавление змей
//                        }
//                    }
//
//                    } catch (Exception exe) {
//
//                    }
//                    Picture.isAdd = false;
//                }
//                if (Picture.isDelete) {
//                    if (Snake.snakes.size() > 1) {
//                        for (Point i : Snake.snakes.get(0).getXy()) {
//                            for (double x = i.x - Snake.step; x <= i.x + Snake.step; x++) {
//                                for (double y = i.y - Snake.step; y <= i.y + Snake.step; y++) {
//                                                                                                                      //Удаление змеи
//                                    pointsToErase.add(new Point((int) x, (int) y));
//
//                                }
//                            }
//                        }
//                        try {
//                            Snake.snakes.get(0).getRegion().removeSnake(Snake.snakes.get(0));
//                            Snake.snakes.remove(0);
//                        } catch (Exception exe) {
//
//                        }
//                        Picture.isDelete = false;
//                    }
//                }


//                for (Snake snake : Snake.snakes) {
//
//                }
                ready = true;
                while (ready) {
                    try {
                        TimeUnit.MICROSECONDS.sleep(1);
                    } catch (Exception e) {

                    }
                }
            }
        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println(e);
        }
    }

    static boolean appleSpawned = false;


    private ArrayList<Point> pointArrayList = new ArrayList<>();
    private ArrayList<Point> pointAppleArrayList = new ArrayList<>();
    private ArrayList<Point> pointToEraseArrayList = new ArrayList<>();
    private ArrayList<Point> pointToEraseSquareArrayList = new ArrayList<>();

}


