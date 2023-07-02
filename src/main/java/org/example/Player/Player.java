package org.example.Player;

import org.example.Enemy.Entity;
import org.example.gpu.Timer;
import org.example.gpu.Window;
import org.example.gpu.render.Model;
import org.example.gpu.render.ModelRendering;
import org.example.gpu.trest;
import org.joml.Vector3f;

import java.awt.*;
import java.util.ArrayList;

public class Player extends Entity {
    private int width;
    private int height;
    private static final double innerPlace = 0.9;
    private static final double exteriorBorder = (1 - innerPlace) / 2;
    static final double[] playGround = new double[]{innerPlace, exteriorBorder};

    public void moveXy(float[] direct) {
        for (int i = 0; i < xy.size(); i++) {
            xy.set(i, new float[]{xy.get(i)[0] - direct[0], xy.get(i)[1] - direct[1]});
        }
        for (int i = 0; i < phantomXY.size(); i++) {
            phantomXY.set(i, new float[]{phantomXY.get(i)[0] - direct[0], phantomXY.get(i)[1] - direct[1]});

        }
        for (int i = 0; i < rendering.getModels().size(); i++) {
            rendering.getModels().get(i).getMovement().setPosition(new Vector3f((float) phantomXY.get(i)[0], (float) phantomXY.get(i)[1], 0));
        }
    }

    private ArrayList<float[]> xy = new ArrayList<>();
    private ArrayList<float[]> directionOfPhantomXY = new ArrayList<>();
    private ArrayList<float[]> phantomXY = new ArrayList<>();


    public void setPhantomXY() {
        phantomXY.clear();
        directionOfPhantomXY.clear();
        for (float[] p : xy) {
            directionOfPhantomXY.add(new float[]{0, 0});
            phantomXY.add(new float[]{p[0], p[1]});
        }
//        for (int i = 0; i < xy.size(); i++) {
//            phantomXY.set
//        }
//        if(phantomXY.size() < xy.size()){
//
//        }
//        if(phantomXY.size() < xy.size()){
//
//        }
    }


    private Color color = new Color(Color.white.getRGB());

    boolean perviyNah = false;

    private double chance = Math.random() * 0.8 + 0.2;

    static double size = 5;
    static double stepOfSize = 2;

    public static void setStep() {
        Player.step = (int) (Player.getSize() * stepOfSize);
    }

    public static int step = (int) (Player.getSize() * stepOfSize);


    public void setDelay() {
        if (delay < 100) {
            delay += 1;
//            this.delay = (int) delayDouble;
        }
    }

    double[] tailless = new double[2];
    public double[] taillessCopy = new double[2];
    public double[] taillessPhantomCopy = new double[2];
    private int reverseCount = 100;
    private int reverse = reverseCount;

    private boolean isMove = false;
    public static int snakeLength = 10;


    public float[] getDirection() {
        if (directionOfPhantomXY.size() > 0) {
            return directionOfPhantomXY.get(0);
        } else {
            return null;
        }

    }

    public static ArrayList<Player> players = new ArrayList<>();
    public boolean isGrow = false;
    public boolean isShorter = false;
    public boolean reset = false;
    private Window window;
    private ModelRendering rendering;

    public Player(Window window) {


//        xy.add(new int[]{(int)(Math.random()*imahe.getWidth()*playGround[0])+(int)(imahe.getWidth()*playGround[1]),(int)(Math.random()*imahe.getHeight()*playGround[0])+(int)(imahe.getHeight()*playGround[1])});

//        Point co = getRandomPoint();
//        xy.add(new double[]{co.x,co.y});
        this.window = window;
        xy.add(new float[]{0, 0});
        setPhantomXY();
        color = new Color((int) (Math.random() * 254 + 1), (int) (Math.random() * 254 + 1), (int) (Math.random() * 254 + 1) /*Color.cyan.getRGB()*/);
        players.add(this);

        rendering = new ModelRendering(window, color, false, this,"player");
        rendering.addModel(new Model(window, (int) (size * 30)));
        rendering.getModels().get(0).getMovement().setPosition(new Vector3f((float) 0, (float) 0, 0));


        for (int i = 0; i < snakeLength + 2; i++) {
            addCircle();
        }
    }

    //TODO проверить SetPhantom
    public ArrayList<float[]> getXy() {
        return xy;
    }

    public ArrayList<float[]> getPhantomXY() {
        return phantomXY;
    }

    public void addCircle() {
        if (xy.size() < maxSize) {
            float x = xy.get(xy.size() - 1)[0];
            float y = xy.get(xy.size() - 1)[1];
            xy.add(new float[]{x, y});
            float xp = phantomXY.get(phantomXY.size() - 1)[0];
            float yp = phantomXY.get(phantomXY.size() - 1)[1];
            phantomXY.add(new float[]{xp, yp});
            directionOfPhantomXY.add(new float[]{0,0});
            rendering.addModel(new Model(window, (int) (size * 30)));
            rendering.getModels().get(xy.size() - 1).getMovement().setPosition(new Vector3f((float) xp, (float) yp, 0));
        }
    }


    public void grow() {
        for (int i = 0; i < 1; i++) {
            addCircle();
        }
    }

    public void minusCell() {
        try {
            if (getXy().size() > 3) {
                rendering.getModels().remove(getXy().size() - 1);
                getXy().remove(getXy().size() - 1);
                phantomXY.remove(phantomXY.size() - 1);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reset() {

        delayDouble = delayStat;
        delay = (int) delayStat;
        delayCount = 0;
        xy.clear();
        xy.add(new float[]{0, 0});
        rendering.clear();
        rendering.addModel(new Model(window, (int) (size * 30)));
        rendering.getModels().get(0).getMovement().setPosition(new Vector3f((float) 0, (float) 0, 0));
//        move(width/2,height/2);
        for (int i = 0; i < snakeLength; i++) {
            addCircle();
        }

    }

    public Point getRandomPoint() {
        int x = (int) (Math.random() * (width * playGround[0])) + (int) (width * playGround[1]);
        int y = (int) (Math.random() * (height * playGround[0])) + (int) (height * Player.playGround[1]);
        return new Point(x, y);
    }

    public Color getColor() {
        return color;
    }

    public static double getSize() {
        return size;
    }

    public void setReset(boolean reset) {
        this.reset = reset;
    }

    public boolean isReset() {
        return reset;
    }

    private float tMouse = 1;
    static float stepRad = 0.1f;
    private float[] pointWatch = new float[]{0, 0};

    void setRadian(Point Target) {

        float xTarget = Target.x - xy.get(0)[0];
        float yTarget = Target.y - xy.get(0)[1];

        float TargetRadian = 0;
        // 1143 372 900 600
        TargetRadian = (float) Math.atan2(xTarget, yTarget);
        if (TargetRadian < 0) {
            TargetRadian += 6.28;

        }
        float halfNear = (tMouse + 3.14f) % 6.28f;

        pointWatch[0] = (float) (step * Math.sin(tMouse) + xy.get(0)[0]);
        pointWatch[1] = (float) (step * Math.cos(tMouse) + xy.get(0)[1]);

//        System.out.println("pointWatch is " + pointWatch);
//        System.out.println("halfNear is " + halfNear);
//        System.out.println("tMouse is " + tMouse);
//
//
//        System.out.println("Enemy is " + Enemy + ", rad: " + EnemyRadian);

        tMouse = (float) ((tMouse + 6.28) % 6.28);
        double dif = Math.abs((TargetRadian - tMouse) % 6.28);
        if (dif < 3.14) {

            stepRad = (float) (dif / 2);
        } else {
            dif = Math.abs((Math.max(TargetRadian, tMouse) - 3.14) - (Math.min(TargetRadian, tMouse) + 3.14));

            stepRad = (float) (dif / 2);
        }


        if (TargetRadian > tMouse && TargetRadian < halfNear) {                                // увеличение

            tMouse += stepRad;
//            System.out.print("+");
        } else if (TargetRadian > tMouse && TargetRadian > halfNear && halfNear >= 3.14) {          // уменьшение

            tMouse -= stepRad;
//System.out.println("-");
        } else if (TargetRadian > tMouse && TargetRadian > halfNear && halfNear < 3.14) {          // увеличение

            tMouse += stepRad;
//            System.out.print("+");
        } else if (TargetRadian < tMouse && TargetRadian < halfNear && halfNear >= 3.14) {                          // уменьшение

            tMouse -= stepRad;
//            System.out.print("-");
        } else if (TargetRadian < tMouse && TargetRadian > halfNear) {                          // уменьшение

            tMouse -= stepRad;
//            System.out.print("-");
        } else if (TargetRadian < tMouse && TargetRadian < halfNear) {                          // увеличение

            tMouse += stepRad;
//            System.out.print("+");
        }
//        System.out.println();
        if (tMouse > 6.28) {
            tMouse -= 6.28;
//            System.out.println("tMouse > 6.28");
        } else if (tMouse < 0) {
//            System.out.println("tMouse<0");
            tMouse += 6.28;
        }


    }

    static double delayStat = 15;
    private double delayDouble = delayStat;
    private int delay = (int) delayDouble;
    private int delayCount = 0;

    static double timerStat = 500;
    static double timer = timerStat;
    double timeTo = Timer.getTime();

    public void moveCheck() {
        try {
            double timeToGo = Timer.getTime();
            double dif = timeToGo - timeTo;
//           if(dif <0.01){
//
//               return;
//           }else {
//               System.out.println("OK");
//               timeTo = timeToGo;
//           }


            try {
                if (reset) {
                    reset();
                    reset = false;
                }
                perviyNah = false;
                if (delayCount < delay) {
                    delayCount++;
                    movePhantom();
                    return;
                }

                timer -= 1;
                if (timer <= 0) {
                    isGrow = true;
                    if (delay > 1) {
                        delay -= 1;
                    }
                    timer = timerStat;

                }
                if ((int) timer == timerStat / 2) {
                    isGrow = true;

                }
                movePhantom();
                delayCount = 0;
                int xTarget;
                int yTarget;
                if (trest.mouseControl) {
                    xTarget = trest.xMouse;
                    yTarget = trest.yMouse;
                } else {
                    xTarget = 0;
                    yTarget = 0;
                }

                setRadian(new Point(xTarget, yTarget));
//            selfStep = Math.random()*(step+ Math.sqrt(Math.pow(Math.abs(xTarget - x), 2) + Math.pow(Math.abs(yTarget - y), 2)) / 50);
                try {
                    pointWatch[0] = (float) (step * Math.sin(tMouse) + xy.get(0)[0]);
                    pointWatch[1] = (float) (step * Math.cos(tMouse) + xy.get(0)[1]);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                move(pointWatch[0], pointWatch[1]);


            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void movePhantom() {
        try {

            for (int i = 0; i < phantomXY.size(); i++) {
                try {
                    phantomXY.get(i)[0] +=
                            directionOfPhantomXY.get(i)[0];
                    phantomXY.get(i)[1] +=
                            directionOfPhantomXY.get(i)[1];

                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
            for (int i = 0; i < rendering.getModels().size(); i++) {
                rendering.getModels().get(i).getMovement().setPosition(new Vector3f((float) phantomXY.get(i)[0], (float) phantomXY.get(i)[1], 0));
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

    public void move(float x, float y) {

        setPhantomXY();

        try {

            for (int i = xy.size() - 1; i > 0; i--) {
                try {
//                    if (i == xy.size() - 1) {
//
////                        phantomXY.get(i)[0] = xy.get(i)[0];
////                        phantomXY.get(i)[1] = xy.get(i)[1];
//
//                    }else if(i!=0){
                        xy.get(i)[0] = xy.get(i - 1)[0];
                        xy.get(i)[1] = xy.get(i - 1)[1];
                        directionOfPhantomXY.add(0, new float[]{((float) xy.get(i)[0] - phantomXY.get(i)[0]) / (delay + 1), ((float) xy.get(i)[1] - phantomXY.get(i)[1]) / (delay + 1)});
//                    }
//                    directionOfPhantomXY.add(0, new float[]{((float) xy.get(i)[0] - phantomXY.get(i)[0]) / (delay + 1), ((float) xy.get(i)[1] - phantomXY.get(i)[1]) / (delay + 1)});

                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
//            phantomXY.get(0)[0] = xy.get(0)[0];
//            phantomXY.get(0)[1] = xy.get(0)[1];
            xy.get(0)[0] = x;
            xy.get(0)[1] = y;

            directionOfPhantomXY.add(0, new float[]{((float) xy.get(0)[0] - phantomXY.get(0)[0]) / (delay + 1), ((float) xy.get(0)[1] - phantomXY.get(0)[1]) / (delay + 1)});

        } catch (Exception e) {
            e.printStackTrace();

        }
//xy.add(0,new int[]{x,y});
//xy.remove(xy.size()-1);
    }


    static int maxSize = 50;

}
