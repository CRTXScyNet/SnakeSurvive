package org.example.Painter;

import org.example.Enemy.Enemy;
import org.example.Player.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Picture extends JFrame {


    public static ExecutorService executorService = Executors.newSingleThreadExecutor();


    static boolean isAdd = false;
    static boolean isDelete = false;
    public static int xMouse = 0;
    public static int yMouse = 0;
    public static boolean mouseControl = true;
    static int[] mouseShelf = new int[2];

    static int snakeAmount = 400;


    public static int width = 0;
    public static int height = 0;
    static boolean draw = false;
    static boolean reset = false;
    private double[] direction = new double[2];


    //    static public double getProgress() {
//
//        double i = ((double) toTargets.size() / ((countX * countY))) * 100;
//        return i;
//    }
    ArrayList<Point> mouseCursor = new ArrayList<>();
    ArrayList<Color> mouseCursorColor = new ArrayList<>();

    public Picture() {
        for (int x = -10; x < 10; x++) {
            for (int y = -10; y < 10; y++) {
                if (x == 0 || y == 0) {
                    mouseCursor.add(new Point(x, y));
                }

            }
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BufferedImage image1 = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        setCursor(this.getToolkit().createCustomCursor(image1, new Point(), null));


//        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(1858, 1080);
        setLocationByPlatform(false);

        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {
//                System.out.println(e.getX());
                xMouse = e.getX();
                yMouse = e.getY();
//                snake.move(e.getX(),e.getY());
            }
        });
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

//                System.out.println(e.getButton());
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == 1) {
                    mouseControl = !mouseControl;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == 16) {
                    draw = !draw;
                }
//                if (e.getKeyCode() == 32) {
//                    mouseControl = !mouseControl;
//                }
//                if (e.getKeyCode() == 17) {
//                    Snake.isTeleport = !Snake.isTeleport;
//                }
//                if (e.getKeyCode() == 82) {
//                    reset();
//                }
                if (e.getKeyCode() == 61) {
                    isAdd = true;
                }
                if (e.getKeyCode() == 45) {
                    isDelete = true;
                }
//                if (e.getKeyCode() == 10) {
//                    Apple.plusRegion();
//                    System.out.println(e.getKeyCode());
//                }
//                if (e.getKeyCode() == 8) {
//                    Apple.minusRegion();
//                }
                if (e.getKeyCode() == 27) {
                    System.exit(0);
                }
                System.out.println(e.getKeyCode());

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        try {
            BufferedImage picture = ImageIO.read(new File("C:\\Users\\CRTXScyNet\\Documents\\ShareX\\Screenshots\\2023-05\\Telegram_LGB2e5bUqm.png"));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        width = getWidth();
        height = getHeight();
        this.setBackground(Color.black);

        this.setVisible(true);


        executorService.submit(() -> {
            Process process = new Process();
        });

        while (Process.ready) {
        }
        pointsOfApple = Process.getPointsOfApple();
        pointsToErase = Process.getPointsToErase();
        points = Process.getPoints();
        colors = Process.getColors();
        try {
            while (true) {
//                Point point = MouseInfo.getPointerInfo().getLocation();
//xMouse = point.x;
//yMouse = point.y;
                if (Process.ready) {
//                    ArrayList<Point>  pointsOfApple = Process.getPointsOfApple();
//                    ArrayList<Point>  pointsToErase = Process.getPointsToErase();
//                    ArrayList<Point>  points = Process.getPoints();
//                    ArrayList<Color>  colors = Process.getColors();

//                    direction = Process.getDirection();
                    paint(this.getGraphics());
                    TimeUnit.MILLISECONDS.sleep(100);


                    if (Process.screenMove) {
//                        image = new BufferedImage(1858, 1080, 1);
//                        pointsOfApple = moveSceen(pointsOfApple);
//                        pointsToErase = moveSceen(pointsToErase);
//                        points = moveSceen(points);


                    }

//                    for (int i = 0; i < pointsToErase.size(); i++) {
//                        try {
////                            if (pointsToErase.get(i).x < image.getWidth() && pointsToErase.get(i).x >= 0 && pointsToErase.get(i).y < image.getHeight() && pointsToErase.get(i).y >= 0) {
//                            try {
//                                image.setRGB(pointsToErase.get(i).x, pointsToErase.get(i).y, Color.black.getRGB());
//                            } catch (Exception e) {
//                            }
////                            }
//                        } catch (Exception e) {
////                            e.printStackTrace();
//
//                        }
//                    }


//                    for (int i = 0; i < pointsOfApple.size(); i++) {
//                        try {
//                            if (pointsOfApple.get(i).x < image.getWidth() && pointsOfApple.get(i).x >= 0 && pointsOfApple.get(i).y < image.getHeight() && pointsOfApple.get(i).y >= 0) {
////                                try {
//                                image.setRGB(pointsOfApple.get(i).x, pointsOfApple.get(i).y, Apple.color.getRGB());
////                                }catch (Exception e){
////                                }
//                            }
//                        } catch (Exception e) {
////                            e.printStackTrace();
//
//                        }
//                    }


//                    for (int i = 0; i < points.size(); i++) {
//                        try {
//                            if (points.get(i).x < image.getWidth() && points.get(i).x >= 0 && points.get(i).y < image.getHeight() && points.get(i).y >= 0) {
////                                try {
//                                image.setRGB(points.get(i).x, points.get(i).y, colors.get(i).getRGB());
////                                }catch (Exception e){
////                                }
//                            }
//                        } catch (Exception e) {
////                            e.printStackTrace();
//
//                        }
//                    }


//                for (Point p : targets){
//                    image.setRGB(p.x,p.y,Color.white.getRGB());
//                }

                    Process.ready = false;
                }
//                boolean snakesAreOnReset = false;
//                if (reset) {
//
////                    for (Snake snake : Snake.snakes) {
////                        if (snake.isReset()) {
////                            snakesAreOnReset = true;
////                        }
////
////                    }
////                    if (!snakesAreOnReset) {
////                        image = new BufferedImage(1858, 1080, 1);
////                        reset = false;
////                    }
//                }

//                if (xMouse > 10 && xMouse < image.getWidth() - 10 && yMouse > 10 && yMouse < image.getHeight() - 10) {
//                    for (int i = 0; i < mouseCursor.size(); i++) {
//                        try {
//                            image.setRGB(mouseCursor.get(i).x + mouseShelf[0], mouseCursor.get(i).y + mouseShelf[1], Color.black.getRGB());
//                        } catch (Exception e) {
//
//                        }
//                    }
//                    mouseShelf = new int[]{xMouse, yMouse};                                                                  //оставить
//                    mouseCursorColor.clear();
//                    for (int i = 0; i < mouseCursor.size(); i++) {
//                        try {
////                            mouseCursorColor.add(new Color(image.getRGB(mouseCursor.get(i).x + mouseShelf[0], mouseCursor.get(i).y + mouseShelf[1])));
//                            image.setRGB(mouseCursor.get(i).x + mouseShelf[0], mouseCursor.get(i).y + mouseShelf[1], Color.WHITE.getRGB());                          //отрисовка мыши
//                        } catch (Exception e) {
//
//                        }
//                    }
//
//
//                }


                //оставить

//                this.repaint();
                try {
                    TimeUnit.MICROSECONDS.sleep(1);
                } catch (Exception e) {

                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

    }

    private ArrayList<Point> moveSceen(ArrayList<Point> list) {

        ArrayList<Point> newlistist = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            newlistist.add(new Point((int) (list.get(i).x + direction[0]), (int) (list.get(i).y + direction[1])));
        }
        return newlistist;
    }

    static ArrayList<Point> pointsOfApple;
    static ArrayList<Point> pointsToErase;
    static ArrayList<Point> points;
    static ArrayList<Color> colors;

    @Override
    public void paint(Graphics g) {
        try {

//            for (int i = 0; i < pointsOfApple.size(); i++) {
//
//                g.clearRect(pointsOfApple.get(i).x - Apple.getAppleSize()*2, pointsOfApple.get(i).y - Apple.getAppleSize()*2, Apple.getAppleSize() * 2*2, Apple.getAppleSize() * 2*2);
//
//            }
//
//            for (int i = 0; i < points.size(); i++) {
//                g.clearRect(points.get(i).x - (int) Enemy.getSize()*2, points.get(i).y - (int) Enemy.getSize()*2, (int) Enemy.getSize() * 2*2, (int) Enemy.getSize() * 2*2);
//
//            }
//g.clearRect(0,0,width,height);

Graphics2D g2 = (Graphics2D) g;
g2.clearRect(0,0,width,height);
//            pointsOfApple = Process.getPointsOfApple();
//            pointsToErase = Process.getPointsToErase();
//            points = Process.getPoints();
//            colors = Process.getColors();

            g2.setColor(Apple.getAppleColor());
            for (int i = 0; i < pointsOfApple.size(); i++) {

                g2.fillRoundRect(pointsOfApple.get(i).x - Apple.getAppleSize(), pointsOfApple.get(i).y - Apple.getAppleSize(), Apple.getAppleSize() * 2, Apple.getAppleSize() * 2, Apple.getAppleSize() * 2, Apple.getAppleSize() * 2);

            }
            g2.setStroke(new BasicStroke(4));
            for(Player player : Player.players){
                g2.setColor(player.getColor());
                for (int i = 0; i<player.getPhantomXY().size()-1;i++){

                    g2.drawLine((int)player.getPhantomXY().get(i)[0],(int)player.getPhantomXY().get(i)[1],(int)player.getPhantomXY().get(i+1)[0],(int)player.getPhantomXY().get(i+1)[1]);
                }
            }
            for(Enemy enemy : Enemy.enemies){
                g2.setColor(enemy.getColor());
                for (int i = 0; i<enemy.getPhantomXY().size()-1;i++){

                    g2.drawLine((int)enemy.getPhantomXY().get(i)[0],(int)enemy.getPhantomXY().get(i)[1],(int)enemy.getPhantomXY().get(i+1)[0],(int)enemy.getPhantomXY().get(i+1)[1]);
                }
            }
            g2.setStroke(new BasicStroke(1));
            g2.drawLine(xMouse-10,yMouse,xMouse+10,yMouse);
            g2.drawLine(xMouse,yMouse-10,xMouse,yMouse+10);
//            for (int i = 0; i < points.size()-1; i++) {
//                g.setColor(colors.get(i));
//                g.fillRoundRect(points.get(i).x - (int) Enemy.getSize(), points.get(i).y - (int) Enemy.getSize(), (int) Enemy.getSize() * 2, (int) Enemy.getSize() * 2, (int) Enemy.getSize() * 2, (int) Enemy.getSize() * 2);
//g.drawLine(points.get(i).x,points.get(i).y,points.get(i+1).x,points.get(i+1).y);
//            }
        } catch (Exception e) {
                            e.printStackTrace();

        }

    }
}
