package org.example.Painter;

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

public class Picture extends JFrame{





    static JLabel label = new JLabel();

    public static ExecutorService executorService = Executors.newSingleThreadExecutor();
    public static BufferedImage image = new BufferedImage(1858, 1080, 1);


    static boolean isAdd = false;
    static boolean isDelete = false;
    public static int xMouse = 0;
    public static int yMouse = 0;
    public static boolean mouseControl = true;
    static int[] mouseShelf = new int[2];

    static int snakeAmount = 400;


    static int width = 0;
    static int height = 0;
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
        label.setIcon(new ImageIcon(image));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        BufferedImage image1 = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        setCursor(this.getToolkit().createCustomCursor(image1, new Point(), null));
        add(label);


//        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(image.getWidth(), image.getHeight());
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
        width = image.getWidth();
        height = image.getHeight();
//        for (int x = 0; x < width; x++) {
//            for (int y = 0; y < height; y++) {
//                for (int i = 0; i < 5; i++) {
//                    if ((Math.pow((x - (double) width / 2), 2)) + (Math.pow((y - (double) height / 2), 2)) >= Math.pow(radius1 + 200 * i - 1, 2) &&
//                            (Math.pow((x - (double) width / 2), 2)) + (Math.pow((y - (double) height / 2), 2)) <= Math.pow(radius1 + 200 * i, 2)) {
//                        targets1.add(new Point(x, y));
//                    }
//                }
//            }
//        }

//C:\Users\CRTXScyNet\Documents\ShareX\Screenshots\2023-05\Telegram_LGB2e5bUqm.png
        // https://png.pngtree.com/png-vector/20190830/ourlarge/pngtree-black-cannabis-leaf-logo-inspiration-isolated-on-white-backgroun-png-image_1715899.jpg
//        try{
//            BufferedImage picture = ImageIO.read(new File("C:\\Users\\CRTXScyNet\\Documents\\ShareX\\Screenshots\\2023-05\\Telegram_LGB2e5bUqm.png"));
//            int zeroX = image.getWidth()/2-picture.getWidth()/2;
//            int zeroY = image.getHeight()/2-picture.getHeight()/2;
//            for(int i = 0;i<picture.getWidth();i+=2){
//                for (int y = 0;y<picture.getHeight();y+=2){
//                    Color color = new Color(picture.getRGB(i,y));
//                    if(color.getRed() < 150&&color.getBlue() <150&&color.getGreen() <150){
//                        targets1.add(new Point(zeroX+i,zeroY+y));
////                        targets1.add(new Point(zeroX+i*2,zeroY+y*2));
////                        targets1.add(new Point(zeroX+i*2,zeroY+y*2+1));
////                        targets1.add(new Point(zeroX+i*2+1,zeroY+y*2));
////                        targets1.add(new Point(zeroX+i*2+1,zeroY+y*2+1));
//                    }
//                }
//            }
//
//        }catch (Exception e){
//            e.printStackTrace();
//            System.exit(0);
//        }
//        int zeroX = image.getWidth()/4;
//        int zeroY = image.getHeight()/4;
//        for(int i = zeroX;i<image.getWidth()-zeroX;i+=2){
//            targets.add(new Point(i,image.getHeight()/2));
//        }
//        for(int i = zeroY;i<image.getHeight()-zeroY;i+=2){
//            targets.add(new Point(image.getWidth()/2,i));
//        }
//        targets.add(new Point(image.getWidth()/2,image.getHeight()/2));
//        countX = (int) ((width - ((width * Snake.playGround[1]) * 2)) / (int) (Snake.size * Snake.stepOfSize));
//        countY = (int) ((height - ((height * Snake.playGround[1]) * 2)) / (int) (Snake.size * Snake.stepOfSize));


//        Executors.newSingleThreadExecutor().submit(() -> {
//            new CheckProgress();
//        });
//        System.out.println("Кольуо готово");
//        boolean closer;
//        for (int x = (int) (image.getWidth() * Snake.playGround[1]); x < width - (int) (image.getWidth() * Snake.playGround[1]); x += (int) (Snake.size * Snake.stepOfSize)) {
//            for (int y = (int) (image.getHeight() * Snake.playGround[1]); y < height - (int) (image.getHeight() * Snake.playGround[1]); y += (int) (Snake.size * Snake.stepOfSize)) {
//                Point nearest = targets1.get(0);
//                if (Math.random() > 0.5) {
//                    closer = true;
//                } else {
//                    closer = false;
//                }
//                for (Point p : targets1) {
//                    long firstDistance = (long) Math.sqrt(Math.pow(Math.abs(p.x - x), 2) + Math.pow(Math.abs(p.y - y), 2));
//                    long lastDistance = (long) Math.sqrt(Math.pow(Math.abs(x - nearest.x), 2) + Math.pow(Math.abs(y - nearest.y), 2));
//                    if (closer) {
//                        if (firstDistance < lastDistance) {
//                            nearest.setLocation(p.x, p.y);
//                        }
//                    } else {
//                        if (firstDistance <= lastDistance) {
//                            nearest.setLocation(p.x, p.y);
//                        }
//                    }
//
//                }
//                toTargets.put(new Point(x, y), new Point(nearest.x, nearest.y));
//
//
//            }
//        }

//        System.out.println("Путь к кольцу готов");
//        System.out.println(toTargets.size());


//        int lenth = Snake.step * Snake.maxSize;
//        if (lenth > 500) {
//            lenth = 500;
//        }
//        for (int i = 0; i < Picture.image.getWidth() + lenth; i += lenth) {
//            for (int y = 0; y < Picture.image.getHeight() + lenth; y += lenth) {       //Отрисовка регионов поиска
//                try {
//                    image.setRGB(i, y, Color.white.getRGB());
//                } catch (Exception e) {
//
//                }
//            }
//        }
//        System.out.println("Регионы поиска готовы");
        this.setVisible(true);


        executorService.submit(() -> {
//            for (int i = 0; i < snakeAmount; i++) {                                    //Добавление змей
//                new Snake(image);
//            }
            Process process = new Process();
        });
        ArrayList<Point> pointsOfApple = new ArrayList<>();
        ArrayList<Point> pointsToErase = new ArrayList<>();
        ArrayList<Point> points = new ArrayList<>();
        ArrayList<Color> colors = new ArrayList<>();
        while (Process.ready) {
        }
        try {
            while (true) {
//                Point point = MouseInfo.getPointerInfo().getLocation();
//xMouse = point.x;
//yMouse = point.y;
                if (Process.ready) {

                    pointsOfApple = Process.getPointsOfApple();
                    pointsToErase = Process.getPointsToErase();
                    points = Process.getPoints();
                    colors = Process.getColors();
//                    direction = Process.getDirection();
                    image = new BufferedImage(1858, 1080, 1);
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
                    for (int i = 0; i < pointsOfApple.size(); i++) {
                        try {
                            if (pointsOfApple.get(i).x < image.getWidth() && pointsOfApple.get(i).x >= 0 && pointsOfApple.get(i).y < image.getHeight() && pointsOfApple.get(i).y >= 0) {
//                                try {
                                image.setRGB(pointsOfApple.get(i).x, pointsOfApple.get(i).y, Apple.color.getRGB());
//                                }catch (Exception e){
//                                }
                            }
                        } catch (Exception e) {
//                            e.printStackTrace();

                        }
                    }
                    for (int i = 0; i < points.size(); i++) {
                        try {
                            if (points.get(i).x < image.getWidth() && points.get(i).x >= 0 && points.get(i).y < image.getHeight() && points.get(i).y >= 0) {
//                                try {
                                image.setRGB(points.get(i).x, points.get(i).y, colors.get(i).getRGB());
//                                }catch (Exception e){
//                                }
                            }
                        } catch (Exception e) {
//                            e.printStackTrace();

                        }
                    }

//                for (Point p : targets){
//                    image.setRGB(p.x,p.y,Color.white.getRGB());
//                }

                    Process.ready = false;
                }
                boolean snakesAreOnReset = false;
                if (reset) {

//                    for (Snake snake : Snake.snakes) {
//                        if (snake.isReset()) {
//                            snakesAreOnReset = true;
//                        }
//
//                    }
//                    if (!snakesAreOnReset) {
//                        image = new BufferedImage(1858, 1080, 1);
//                        reset = false;
//                    }
                }

                if (xMouse > 10 && xMouse < image.getWidth() - 10 && yMouse > 10 && yMouse < image.getHeight() - 10) {
                    for (int i = 0; i < mouseCursor.size(); i++) {
                        try {
                            image.setRGB(mouseCursor.get(i).x + mouseShelf[0], mouseCursor.get(i).y + mouseShelf[1], Color.black.getRGB());
                        } catch (Exception e) {

                        }
                    }
                    mouseShelf = new int[]{xMouse, yMouse};                                                                  //оставить
                    mouseCursorColor.clear();
                    for (int i = 0; i < mouseCursor.size(); i++) {
                        try {
//                            mouseCursorColor.add(new Color(image.getRGB(mouseCursor.get(i).x + mouseShelf[0], mouseCursor.get(i).y + mouseShelf[1])));
                            image.setRGB(mouseCursor.get(i).x + mouseShelf[0], mouseCursor.get(i).y + mouseShelf[1], Color.WHITE.getRGB());
                        } catch (Exception e) {

                        }
                    }


                }


                label.setIcon(new ImageIcon(image));                                                                 //оставить

                this.repaint();
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
            newlistist.add(new Point((int)(list.get(i).x +direction[0]),(int)(list.get(i).y +direction[1])));
        }
        return newlistist;
    }
}
