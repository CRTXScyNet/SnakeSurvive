package org.example.Player;

import org.example.Enemy.Entity;
import org.example.gpu.Timer;
import org.example.gpu.Window;
import org.example.gpu.render.Model;
import org.example.gpu.render.ModelRendering;
import org.example.gpu.trest;
import org.joml.Vector3f;

import java.awt.*;
import java.awt.geom.Point2D;
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
        for (int i = 0; i < rendering.getModels().size(); i++) {
            rendering.getModels().get(i).getMovement().setPosition(new Vector3f((float) xy.get(i)[0], (float) xy.get(i)[1], 0));
        }
    }

    public static ArrayList<float[]> xy = new ArrayList<>();

    public static Point2D playerHeadXY() {
        return new Point2D.Float(xy.get(0)[0], xy.get(0)[1]);
    }

    private float[] direction = null;

    private Color color = new Color(Color.white.getRGB());

    boolean perviyNah = false;

    private double chance = Math.random() * 0.8 + 0.2;

    static float size = 8;
    static double stepOfSize = 2;

    public static void setStep() {
        Player.step = (int) (Player.getSize() * stepOfSize);
    }

    public static float maxStepStat = 0.5f;
    public static float maxStep = maxStepStat;
    public static final float minStep = 0.1f;
    public static float step = 0.1f;


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
    public static int snakeLength = 5;


    public float[] getDirection() {
        return direction;
    }

    public static ArrayList<Player> players = new ArrayList<>();
    public boolean isGrow = false;
    public boolean isShorter = false;
    public boolean reset = false;


    static boolean speedBoost = false;
    public static boolean absorb = false;
    public static ArrayList<float[]> absorbArray = new ArrayList<>();
    private Window window;
    private ModelRendering rendering;

    public Player(Window window) {


//        xy.add(new int[]{(int)(Math.random()*imahe.getWidth()*playGround[0])+(int)(imahe.getWidth()*playGround[1]),(int)(Math.random()*imahe.getHeight()*playGround[0])+(int)(imahe.getHeight()*playGround[1])});

//        Point co = getRandomPoint();
//        xy.add(new double[]{co.x,co.y});
        this.window = window;
        xy.add(new float[]{0, 0});

        color = new Color((int) (Math.random() * 200 + 1), (int) (Math.random() * 200 + 1), (int) (Math.random() * 200 + 1) /*Color.cyan.getRGB()*/);
        players.add(this);

        rendering = new ModelRendering(window, color, false, this, "player");
        rendering.addModel(new Model(window, (int) (size * 30)));
        rendering.getModels().get(0).getMovement().setPosition(new Vector3f((float) 0, (float) 0, 0));


        for (int i = 0; i < snakeLength - 1; i++) {
            addCircle();
        }
    }

    //TODO проверить SetPhantom
    public ArrayList<float[]> getXy() {
        return xy;
    }


    public void addCircle() {
        if (xy.size() < maxSize) {
            float x = xy.get(xy.size() - 1)[0];
            float y = xy.get(xy.size() - 1)[1];
            xy.add(new float[]{x, y});

            direction = new float[]{0, 0};
            rendering.addModel(new Model(window, (int) (size * 30)));
            rendering.getModels().get(xy.size() - 1).getMovement().setPosition(new Vector3f((float) x, (float) y, 0));
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


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        speedBoostTime = 0;
        maxStep = maxStepStat;
        step = 0.1f;
        delayDouble = delayStat;
        delay = (int) delayStat;
        delayCount = 0;
        xy.clear();
        xy.add(new float[]{0, 0});
        rendering.clear();
        rendering.addModel(new Model(window, (int) (size * 30)));
        rendering.getModels().get(0).getMovement().setPosition(new Vector3f((float) 0, (float) 0, 0));
//        move(width/2,height/2);
        for (int i = 0; i < snakeLength - 1; i++) {
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
    private float stepRadLast = 0;
    private int count = 0;
    private int maxCount = 500;
    boolean difDir = false;
    boolean canIncreaseSpeed = false;

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

        tMouse = (float) ((tMouse + 6.28) % 6.28);

        double dif = Math.abs((TargetRadian - tMouse) % 6.28);
        if (dif > 3.14) {
            dif = Math.abs((Math.max(TargetRadian, tMouse) - 3.14) - (Math.min(TargetRadian, tMouse) + 3.14));

        }

        stepRad = step / 15;
        if (!trest.mouseControl) {
//            stepRad = step / 30;
            stepRad = (float) dif * (step / 60);
        }


        if (TargetRadian > tMouse && TargetRadian > halfNear && halfNear >= 3.14) {          // уменьшение
            stepRad *= -1;
//            tMouse -= stepRad;
//System.out.println("-");
        } else if (TargetRadian < tMouse && TargetRadian < halfNear && halfNear >= 3.14) {                          // уменьшение
            stepRad *= -1;
//            tMouse -= stepRad;
//            System.out.print("-");
        } else if (TargetRadian < tMouse && TargetRadian > halfNear) {                          // уменьшение
            stepRad *= -1;
//            tMouse -= stepRad;
//            System.out.print("-");
        }

//        if(trest.mouseControl){
        tMouse += stepRad;
//        }
//        System.out.println();
        if (tMouse > 6.28) {
            tMouse -= 6.28;
//            System.out.println("tMouse > 6.28");
        } else if (tMouse < 0) {
//            System.out.println("tMouse<0");
            tMouse += 6.28;
        }
        maxCount = (int) (1000 / (step * 15));
        if (trest.mouseControl) {
            if (stepRadLast != 0) {


                if (difDir && ((stepRadLast > 0 && stepRad > 0) || (stepRadLast < 0 && stepRad < 0))) {
                    canIncreaseSpeed = true;
                } else if (difDir) {
                    canIncreaseSpeed = false;
                    count = 0;
                }

                if ((stepRadLast > 0 && stepRad < 0) || (stepRadLast < 0 && stepRad > 0)) {
                    difDir = true;
                } else {
                    difDir = false;
                }

                if (!difDir && dif > 0.01 || dif < -0.01) {
                    if (count < maxCount) {
                        count++;
                        if (step < maxStep) {
                            step += 0.0012;
                        } else {
//                            System.out.println("Maximum!");
                        }
//                        if (count == maxCount) {
//
//                        }
                    } else {
                        canIncreaseSpeed = false;
                    }

                }
            }

            stepRadLast = stepRad;

        }


    }

    public void increaseSpeed() {
        maxStep += 0.1;
    }


    static double delayStat = 15;
    private double delayDouble = delayStat;
    private int delay = (int) delayDouble;
    private int delayCount = 0;

    static double timerStat = 500;
    static double timer = timerStat;
    double timeTo = trest.getMainTime();
    double dif = 0;
    static float speedBoostTime = 0;
    static float speedBoostTimer = 0;

    public static void addSpeedTime(float time) {
        speedBoostTime += time;
    }
    public void setTime(float time){
        rendering.setTime(time);
    }

    public void moveCheck() {
        try {
            dif += trest.getMainTime() - timeTo;
            speedBoostTimer = (float) (trest.getMainTime() - timeTo);
            timeTo = trest.getMainTime();
            if (dif >= 5) {
                isGrow = true;
                dif = 0;
            }


            try {
                if (reset) {
                    reset();
                    reset = false;
                }
                if (speedBoostTime <= 0|| step<maxStep/2) {
                    speedBoost = false;
                }
                if(speedBoostTime>0&&!speedBoost && step <= maxStep){
                    step*=1.5;
                }

                if (speedBoostTime > 0){
                    speedBoostTime -= speedBoostTimer;
                    speedBoost = true;
                }
                if (step > minStep && !trest.mouseControl && !speedBoost) {
                    step *= 0.9999;
                } else if (step > minStep && !canIncreaseSpeed && !speedBoost) {
                    step *= 0.999;
                }
                if (step < minStep) {
                    step = minStep;
                }


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


    public void move(float x, float y) {

        try {

//            if (!trest.mouseControl) {
////                float headDistance = (float) Math.sqrt(Math.pow(xy.get(0)[0], 2) + Math.pow(xy.get(0)[1], 2));
////
////                if (headDistance < 30) {
////                    xy.set(0, new float[]{xy.get(0)[0] + (xy.get(0)[0] / 10), xy.get(0)[1] + (xy.get(0)[1] / 10)});
////                }
//
////                if (headDistance > 30) {
////                    xy.set(0, new float[]{xy.get(0)[0] - (xy.get(0)[0] / 1000), xy.get(0)[1] - (xy.get(0)[1] / 1000)});
////                }
//
//
//                for (int i = 1; i < xy.size(); i++) {
//                    float distance = (float) Math.sqrt(Math.pow(xy.get(i)[0], 2) + Math.pow(xy.get(i)[1], 2));
//                    if (distance !=0 && distance < 10) {
//                        xy.set(i, new float[]{xy.get(i)[0] - (xy.get(i)[0] * (step / (distance))), xy.get(i)[1] - (xy.get(i)[1] * (step / (distance)))});
//                    }
//                }
//            }
            for (int i =  0; i < xy.size()-1; i++) {
                float distance = (float) Math.sqrt(Math.pow(xy.get(i+1)[0] - xy.get(i)[0], 2) + Math.pow(xy.get(i+1)[1] - xy.get(i)[1], 2));
                float distanceDif = (size-distance)/2;
                float angle;
                if (distance > size) {
                    angle = (float) Math.atan((xy.get(i)[1] - xy.get(i + 1)[1]) / (xy.get(i)[0] - xy.get(i + 1)[0]));
                    if (xy.get(i)[0] - xy.get(i + 1)[0] < 0) {
                        xy.set(i+1, new float[]{xy.get(i)[0] + (float) (size * Math.cos(angle)), xy.get(i)[1] + (float) (size * Math.sin(angle))});
                    } else {
                        xy.set(i+1, new float[]{xy.get(i)[0] - (float) (size * Math.cos(angle)), xy.get(i)[1] - (float) (size * Math.sin(angle))});
                    }
                }

            }

            direction = new float[]{x / 100, y / 100};


            //place for physics  TODO


            xy.set(0, new float[]{x, y});
            for (int i = 0; i < 6; i++) {
                makePhysics();
            }
            for (int i = 0; i < rendering.getModels().size(); i++) {
                rendering.getModels().get(i).getMovement().setPosition(new Vector3f(xy.get(i)[0], xy.get(i)[1], 0));
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
//xy.add(0,new int[]{x,y});
//xy.remove(xy.size()-1);
    }

    public void makePhysics() {
        for (int i = 0; i < xy.size(); i++) {
            for (int j = 1; j < xy.size(); j++) {
                if (j == i) {
                    continue;
                }
                float distance = (float) Math.sqrt(Math.pow(xy.get(i)[0] - xy.get(j)[0], 2) + Math.pow(xy.get(i)[1] - xy.get(j)[1], 2));
                float distanceDif = size - distance;
                float angle;
                if (distance != 0 && distance < size - 0.5) {
                    if (i == 0 && trest.mouseControl && step > minStep) {
                        step *= 0.99;
                    }
                    angle = (float) Math.atan((xy.get(i)[1] - xy.get(j)[1]) / (xy.get(i)[0] - xy.get(j)[0]));
                    if (xy.get(i)[0] - xy.get(j)[0] < 0) {
                        xy.set(i, new float[]{xy.get(j)[0] - (float) ((size - distanceDif) * Math.cos(angle)), xy.get(j)[1] - (float) ((size - distanceDif) * Math.sin(angle))});
                        xy.set(j, new float[]{xy.get(i)[0] + (float) ((size) * Math.cos(angle)), xy.get(i)[1] + (float) ((size) * Math.sin(angle))});
                    } else {
                        xy.set(i, new float[]{xy.get(j)[0] + (float) ((size - distanceDif) * Math.cos(angle)), xy.get(j)[1] + (float) ((size - distanceDif) * Math.sin(angle))});

                        xy.set(j, new float[]{xy.get(i)[0] - (float) ((size) * Math.cos(angle)), xy.get(i)[1] - (float) ((size) * Math.sin(angle))});

                    }
                    deepPhysic(j);
                }

            }

        }
    }

    public void deepPhysic(int i) {
        for (int j = xy.size() - 1; j > 0; j--) {
            if (j == i) {
                continue;
            }
            float distance = (float) Math.sqrt(Math.pow(xy.get(i)[0] - xy.get(j)[0], 2) + Math.pow(xy.get(i)[1] - xy.get(j)[1], 2));
            float distanceDif = size - distance;
            float angle;
            if (distance != 0 && distance < size - 0.5) {
                angle = (float) Math.atan((xy.get(i)[1] - xy.get(j)[1]) / (xy.get(i)[0] - xy.get(j)[0]));
                if (xy.get(i)[0] - xy.get(j)[0] < 0) {
                    xy.set(j, new float[]{xy.get(i)[0] + (float) ((size) * Math.cos(angle)), xy.get(i)[1] + (float) ((size) * Math.sin(angle))});
                } else {
                    xy.set(j, new float[]{xy.get(i)[0] - (float) ((size) * Math.cos(angle)), xy.get(i)[1] - (float) ((size) * Math.sin(angle))});
                }
            }

        }
    }

    public void addAbsorbedSnake(){
        for (int i = 0; i < absorbArray.size(); i++) {

            if (xy.size() < maxSize) {
                float x = absorbArray.get(i)[0];
                float y = absorbArray.get(i)[1];
                xy.add(new float[]{x, y});

                direction = new float[]{0, 0};
                rendering.addModel(new Model(window, (int) (size * 30)));
                rendering.getModels().get(xy.size() - 1).getMovement().setPosition(new Vector3f((float) x, (float) y, 0));
            }
        }

    }


   public static int maxSize = 160;

}
