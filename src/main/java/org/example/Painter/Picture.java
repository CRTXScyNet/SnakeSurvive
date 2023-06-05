package org.example.Painter;

import org.example.Enemy.Enemy;
import org.example.Player.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.BorderUIResource;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Picture extends JFrame {

static Toolkit toolkit = Toolkit.getDefaultToolkit();
static Dimension dimension = toolkit.getScreenSize();
    static JLabel label = new JLabel();
    public static int countOfApples = 0;
    static JLabel coutnOfApplesLabel = new JLabel(String.valueOf(countOfApples));

    static JLayeredPane pane = new JLayeredPane();
    static JPanel panel = new JPanel();

    public static ExecutorService executorService = Executors.newSingleThreadExecutor();
    public static BufferedImage image = new BufferedImage((int)(dimension.width*0.9), (int)(dimension.height*0.9), 1);


    static boolean isAdd = false;
    static boolean isDelete = false;
    static boolean isPaused = false;
    public static boolean isEnd = false;
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
    static Font secondaryFont = new Font("ns", Font.ITALIC, 25);
    static Font mainFont = new Font("ns", Font.ITALIC, 50);
    static JPanel losePanel = new JPanel();
    static JLabel lose = new JLabel("Вы проNграли!");
    static JLabel res = new JLabel("Начать заново?");
    static JPanel pausePanel = new JPanel();
    static JLabel pauseLabel = new JLabel("Пауза");
    static JLabel cont = new JLabel("Продолжить?");


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
        setSize(image.getWidth(), image.getHeight());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        width = image.getWidth();
        height = image.getHeight();
        pane.setBounds(0, 0, getWidth(), getHeight());

        label.setIcon(new ImageIcon(image));
        label.setBounds(0, 0, width, height);


        BufferedImage image1 = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        setCursor(this.getToolkit().createCustomCursor(image1, new Point(), null));

        add(pane);


        coutnOfApplesLabel.setFont(mainFont);
        coutnOfApplesLabel.setForeground(Color.WHITE);
        panel.setOpaque(false);
        panel.setBounds(width / 2 - 150, 150, 300, 150);
        coutnOfApplesLabel.setBounds(panel.getWidth() / 2 - 50, 0, 100, 100);

        panel.add(coutnOfApplesLabel);
        pane.add(panel, 3);
        pane.add(label, JLayeredPane.FRAME_CONTENT_LAYER);


        losePanel.setLayout(null);
        losePanel.setBounds(width / 2 - width / 4, 0/*height / 2 - height / 4*/, width / 2, height);
        lose.setBounds(losePanel.getWidth() / 2 - 250, 0, 500, 100);
        lose.setBackground(Color.BLACK);
        lose.setFont(mainFont);
        lose.setForeground(Color.WHITE);
        lose.setOpaque(true);
        lose.setHorizontalAlignment(JLabel.CENTER);
        losePanel.add(lose, BorderLayout.NORTH);
        res.setFont(secondaryFont);
        res.setBorder(new BorderUIResource.LineBorderUIResource(Color.WHITE));
        res.setForeground(Color.WHITE);
        res.setBackground(Color.BLACK);
        res.setOpaque(true);
        res.setBounds(losePanel.getWidth() / 2 - losePanel.getWidth() / 4, losePanel.getHeight() - losePanel.getHeight() / 3, losePanel.getWidth() / 2, losePanel.getHeight() / 6);
        res.setHorizontalAlignment(JLabel.CENTER);
        res.setCursor(Cursor.getDefaultCursor());
        losePanel.add(res, BorderLayout.CENTER);
        losePanel.setOpaque(false);
        res.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == 1) {
                    if (isEnd) {
                        reset = true;

                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                res.setBorder(new BorderUIResource.LineBorderUIResource(Color.cyan));
                res.setForeground(Color.cyan);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                res.setBorder(new BorderUIResource.LineBorderUIResource(Color.WHITE));
                res.setForeground(Color.WHITE);
            }
        });


        pausePanel.setLayout(null);
        pausePanel.setBounds(width / 2 - width / 4, 0/*height / 2 - height / 4*/, width / 2, height);
        pauseLabel.setBounds(pausePanel.getWidth() / 2 - 250, 0, 500, 100);
        pauseLabel.setBackground(Color.BLACK);
        pauseLabel.setFont(mainFont);
        pauseLabel.setForeground(Color.WHITE);
        pauseLabel.setOpaque(true);
        pauseLabel.setHorizontalAlignment(JLabel.CENTER);
        pausePanel.add(pauseLabel, BorderLayout.NORTH);
        cont.setFont(secondaryFont);
        cont.setBorder(new BorderUIResource.LineBorderUIResource(Color.WHITE));
        cont.setForeground(Color.WHITE);
        cont.setBackground(Color.BLACK);
        cont.setOpaque(true);
        cont.setBounds(losePanel.getWidth() / 2 - pausePanel.getWidth() / 4, pausePanel.getHeight() - pausePanel.getHeight() / 3, pausePanel.getWidth() / 2, pausePanel.getHeight() / 6);
        cont.setHorizontalAlignment(JLabel.CENTER);
        cont.setCursor(Cursor.getDefaultCursor());
        pausePanel.add(cont, BorderLayout.CENTER);
        pausePanel.setOpaque(false);
        cont.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == 1) {
                    if (isPaused) {
                        isPaused = false;
                        pane.remove(pausePanel);
                        cont.setBorder(new BorderUIResource.LineBorderUIResource(Color.WHITE));
                        cont.setForeground(Color.WHITE);
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                cont.setBorder(new BorderUIResource.LineBorderUIResource(Color.cyan));
                cont.setForeground(Color.cyan);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                cont.setBorder(new BorderUIResource.LineBorderUIResource(Color.WHITE));
                cont.setForeground(Color.WHITE);
            }
        });


//        addMouseMotionListener(new MouseMotionListener() {
//            @Override
//            public void mouseDragged(MouseEvent e) {
//
//            }
//
//            @Override
//            public void mouseMoved(MouseEvent e) {
////                System.out.println(e.getX());
////                xMouse = e.getX();
////                yMouse = e.getY();
////                snake.move(e.getX(),e.getY());
//            }
//        });
        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {

//                System.out.println(e.getButton());
            }

            @Override
            public void mousePressed(MouseEvent e) {
                try {
                    if (!isPaused && !isEnd) {
                        if (e.getButton() == 1) {
                            mouseControl = !mouseControl;
                        }

                    }
                }catch (Exception km){
                    km.printStackTrace();
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
                if (e.getKeyCode() == 82) {

                }
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
                    if (!isEnd) {
                        isPaused = !isPaused;
                        if (isPaused) {
                            pane.add(pausePanel, 5);
                        } else {
                            pane.remove(pausePanel);
                        }
                    } else {
                        System.exit(0);
                    }
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

        this.setVisible(true);


        executorService.submit(() -> {

            Process process = new Process();
        });
        ArrayList<Point> pointsOfApple = new ArrayList<>();
        ArrayList<Point> pointsOfArrow = new ArrayList<>();
        ArrayList<Point> points = new ArrayList<>();
        ArrayList<Color> colors = new ArrayList<>();
        while (Process.ready) {
        }
        try {
            while (true) {
                Point point = MouseInfo.getPointerInfo().getLocation();
                xMouse = point.x - getX();
                yMouse = point.y - getY();
                if (Process.ready) {

                    pointsOfApple = Process.getPointsOfApple();
                    pointsOfArrow = Process.getPointsOfArrow();
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
                    try {
                        for (int i = 0; i < pointsOfArrow.size(); i++) {
                            try {
                                if (pointsOfArrow.get(i).x < image.getWidth() && pointsOfArrow.get(i).x >= 0 && pointsOfArrow.get(i).y < image.getHeight() && pointsOfArrow.get(i).y >= 0) {
//                                try {
                                    image.setRGB(pointsOfArrow.get(i).x, pointsOfArrow.get(i).y, Apple.color.getRGB());
//                                }catch (Exception e){
//                                }
                                }
                            } catch (Exception e) {
//                            e.printStackTrace();

                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

//                for (Point p : targets){
//                    image.setRGB(p.x,p.y,Color.white.getRGB());
//                }
                    boolean snakesAreOnReset = false;

                    if (reset) {


                        reset();
                        pane.remove(losePanel);
                        Picture.countOfApples = 0;
                        mouseControl = true;
                        res.setBorder(new BorderUIResource.LineBorderUIResource(Color.WHITE));
                        res.setForeground(Color.WHITE);
                        reset = false;
                        isEnd = false;
//                        try {
//                    TimeUnit.MILLISECONDS.sleep(500);
//                        } catch (Exception e) {
//
//                        }
                    } else if (isPaused) {

                    } /*else if (isEnd) {



                    }*/ else {
                        Process.ready = false;
                    }

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
//                coutnOfApplesLabel.setLocation(0,(int)(height*0.9));
                label.setIcon(new ImageIcon(image));                                                                 //оставить
                coutnOfApplesLabel.setText(String.valueOf(countOfApples));

                this.repaint();
//                try {
//                    TimeUnit.MICROSECONDS.sleep(1);
//                } catch (Exception e) {
//
//                }
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

    public static void pauseMenuON() {
        isEnd = true;
        mouseControl = false;
        pane.add(losePanel, 5);
    }

    public static void reset() {


        reset = true;
        for (Enemy enemy : Enemy.enemies) {
            enemy.reset();
        }
        for (Player player : Player.players) {
            player.reset();
        }

    }
}
