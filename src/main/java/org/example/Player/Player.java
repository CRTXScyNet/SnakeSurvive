package org.example.Player;

import org.example.Enemy.Entity;
import org.example.Painter.Apple;
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

    private float[] direction = new float[2];

    private Color color = new Color(Color.white.getRGB());

    boolean perviyNah = false;

    private double chance = Math.random() * 0.8 + 0.2;

    static float size = 8;
    static double stepOfSize = 2;

    public static void setStep() {
        Player.step = (int) (Player.getSize() * stepOfSize);
    }

    public static float maxStepStat = 2f;
    public static float maxStep = maxStepStat;
    public static final float minStep = 1f;
    public static float step = 1f;


    public void setDelay() {
        if (delay < 100) {
            delay += 1;
//            this.delay = (int) delayDouble;
        }
    }

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
    public Color mainColor = new Color(0, 100, 100);
    public Color takenAppleColor = new Color(200, 0, 0);

    static boolean speedBoost = false;
    public static boolean absorb = false;
    public static ArrayList<float[]> absorbArray = new ArrayList<>();
    private static Window window;
    private ModelRendering rendering;
    private ModelRendering maxSpeedRender;
    public static Player player;
    public PlayerPart part;

    public Player(Window window) {


//        xy.add(new int[]{(int)(Math.random()*imahe.getWidth()*playGround[0])+(int)(imahe.getWidth()*playGround[1]),(int)(Math.random()*imahe.getHeight()*playGround[0])+(int)(imahe.getHeight()*playGround[1])});

//        Point co = getRandomPoint();
//        xy.add(new double[]{co.x,co.y});
        Player.window = window;
        xy.add(new float[]{0, 0});

        color = mainColor;
        players.add(this);

        rendering = new ModelRendering(window, this, "player");
        rendering.addModel(new Model(window, (int) (size * 30), color));
        rendering.getModels().get(0).getMovement().setPosition(new Vector3f((float) 0, (float) 0, 0));
        maxSpeedRender = new ModelRendering(window, null, "speedIndicate");
        maxSpeedRender.addModel(new Model(window, 30, color));
        maxSpeedRender.getModels().get(0).getMovement().setPosition(new Vector3f((float) xy.get(0)[0], (float) xy.get(0)[1], 0));
        maxSpeedRender.getModels().get(0).getMovement().setRotation(-tMouse);


        for (int i = 0; i < snakeLength - 1; i++) {
            addCircle();
        }
        player = this;
        part = new PlayerPart(window);
    }

    public void throwPart() {
//        if(countOfApples>0) {
        part.spawn();
        minusCell();
//        }
    }

    public void takePartBack() {
        if (/*countOfApples>0 &&*/ part.isAlive()) {
            part.setCallBack(true);
        }
    }

    public PlayerPart getPart() {
        return this.part;
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

            rendering.addModel(new Model(window, (int) (size * 30), color));
            rendering.getModels().get(xy.size() - 1).getMovement().setPosition(new Vector3f((float) x, (float) y, 0));
        }
    }


    public void grow() {
        for (int i = 0; i < 1; i++) {
            addCircle();
        }
        setAppleCount();
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
        countOfApples = 0;
        speedBoostTime = 0;
        maxStep = maxStepStat;
        step = 0.1f;
        delayDouble = delayStat;
        delay = (int) delayStat;
        delayCount = 0;
        xy.clear();
        xy.add(new float[]{0, 0});
        rendering.clear();
        rendering.addModel(new Model(window, (int) (size * 30), color));
        rendering.getModels().get(0).getMovement().setPosition(new Vector3f((float) 0, (float) 0, 0));
//        move(width/2,height/2);
        for (int i = 0; i < snakeLength - 1; i++) {
            addCircle();
        }
        part.reset();

    }

    public void setAppleCount() {
        int i = countOfApples;
        for (Model model : rendering.getModels()) {
            if (i > 0) {
                model.setRGB(takenAppleColor);
                i--;
            } else {
                model.setRGB(mainColor);
            }
        }
    }

    public void cutTheTail() {
        int i = Player.countOfApples;
        int s = xy.size() / 2;
        for (int j = xy.size() - 1; j >= 0; j--) {
            if (xy.size() > 5) {
                if (j > i - 1 && j > s) {
                    GluePart part = new GluePart(window);
                    part.setXy(xy.get(j));
                    xy.remove(j);
                    rendering.getModels().remove(j);

                }
            }
        }
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

    public float gettMouse() {
        return tMouse;
    }

    private float tMouse = 1;
    static float stepRad = 0.1f;
    private float[] pointWatch = new float[]{0, 0};
    private float stepRadLast = 0;
    private int count = 0;
    private int maxCount = 500;
    public static int countOfApples = 0;
    boolean difDir = false;
    boolean canIncreaseSpeed = false;

    public void eatTheApple() {
        countOfApples++;
        if (xy.size() < countOfApples) {
            grow();
        }
        if (xy.size() > countOfApples) {
            minusCell();
        }
        increaseSpeed();
        setAppleCount();
    }

    public void lostTheApple() {
        countOfApples--;

        grow();

        decreaseSpeed();
        setAppleCount();
    }

    public void update() {

        if (Apple.appleVisible) {
            if (Apple.checkCollision(xy.get(0))) {
                eatTheApple();
            }
        }
        if (trest.isEnd) {
            trest.eatenPlayerTimelast = trest.mainTime - trest.eatenPlayerTime;
            setTime(-trest.eatenPlayerTimelast);
        } else {
            setTime(trest.mainTime);
        }
        moveCheck();
        part.update();
    }

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

//        pointWatch[0] = (float) (step * Math.sin(tMouse) + xy.get(0)[0]);
//        pointWatch[1] = (float) (step * Math.cos(tMouse) + xy.get(0)[1]);

        tMouse = (float) ((tMouse + 6.28) % 6.28);

        double dif = Math.abs((TargetRadian - tMouse) % 6.28);
        if (dif > 3.14) {
            dif = Math.abs((Math.max(TargetRadian, tMouse) - 3.14) - (Math.min(TargetRadian, tMouse) + 3.14));

        }


        if (!trest.mouseControl) {
//            stepRad = step / 30;
            stepRad = (float) Math.abs(dif) / (30 / (step / 2));
        } else {
            stepRad = (float) Math.abs(dif) / (15 / (step / 2));
        }
        maxCount = (int) (3.14 / stepRad);

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
        if (trest.mouseControl) {


            if ((stepRadLast > 0 && stepRad < 0) || (stepRadLast < 0 && stepRad > 0)) {
                count = 0;
            }


            if (dif > 0.05) {
                if (count < maxCount) {
                    canIncreaseSpeed = true;
                    count++;
                    if (step < maxStep) {
                        step += Math.abs(stepRad) / 10;
                    } else {
//                            System.out.println("Maximum!");
                    }
                    if (count == maxCount) {
                        System.out.println("Maximum!");
                    }
                }
            } else {
                canIncreaseSpeed = false;
            }
        }

        stepRadLast = stepRad;


    }

    public void increaseSpeed() {
        maxStep += 0.05;

    }

    public void decreaseSpeed() {
        maxStep -= 0.05;

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

    public void setTime(float time) {
        rendering.setTime(time);
    }

    public void moveCheck() {
        try {
            dif += trest.getMainTime() - timeTo;
            speedBoostTimer = (float) (trest.getMainTime() - timeTo);
            timeTo = trest.getMainTime();
            if (dif >= 100.0 / countOfApples) {
                grow();
                dif = 0;
            }


            try {
                if (speedBoostTime <= 0 || step < maxStep / 2) {
                    speedBoost = false;
                }
                if (speedBoostTime > 0 && !speedBoost && step <= maxStep + minStep) {
                    step *= 1.5;
                }

                if (speedBoostTime > 0) {
                    speedBoostTime -= speedBoostTimer;
                    speedBoost = true;
                }
                if (step > minStep && !trest.mouseControl && !speedBoost) {
                    step *= 0.9999;
                } else if (step > minStep && !speedBoost) {// && !canIncreaseSpeed
                    if (!canIncreaseSpeed) {
                        step *= 0.99;                                              //TODO
                    } else {
                        step *= 0.999;
                    }
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
            if (!trest.isEnd) {
                checkForAbsorb();
            }
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
            boolean stop = false;
            if (step <= size / 2) {
                xy.set(0, new float[]{x, y});
                for (int i = 0; i < xy.size() - 1; i++) {
                    float distance = (float) Math.sqrt(Math.pow(xy.get(i + 1)[0] - xy.get(i)[0], 2) + Math.pow(xy.get(i + 1)[1] - xy.get(i)[1], 2));
                    float distanceDif = (size - distance) / 2;
                    float angle;
                    if (distance > size) {
                        angle = (float) Math.atan((xy.get(i)[1] - xy.get(i + 1)[1]) / (xy.get(i)[0] - xy.get(i + 1)[0]));
                        if (xy.get(i)[0] - xy.get(i + 1)[0] < 0) {
                            xy.set(i + 1, new float[]{xy.get(i)[0] + (float) (size * Math.cos(angle)), xy.get(i)[1] + (float) (size * Math.sin(angle))});
                        } else {
                            xy.set(i + 1, new float[]{xy.get(i)[0] - (float) (size * Math.cos(angle)), xy.get(i)[1] - (float) (size * Math.sin(angle))});
                        }
                    }
                    if (!stop) {
                        stop = deepPhysic();
                    } else {
                        deepPhysic();
                    }
                }
            } else {
                float[] xyBuff = new float[]{xy.get(0)[0], xy.get(0)[1]};

                int stepPartCount = (int) Math.ceil(((step / (size / 2))));
                float stepPart = step / stepPartCount;

                float xyAngle = (float) Math.atan((xy.get(0)[1] - y) / (xy.get(0)[0] - x));

                for (int j = 1; j < stepPartCount + 1; j++) {


                    double changeX = (stepPart * j) * Math.cos(xyAngle);
                    double changeY = (stepPart * j) * Math.sin(xyAngle);
                    if (x - xy.get(0)[0] < 0) {
                        xy.set(0, new float[]{xyBuff[0] - (float) changeX, xyBuff[1] - (float) changeY});
                    } else {
                        xy.set(0, new float[]{xyBuff[0] + (float) changeX, xyBuff[1] + (float) changeY});
                    }

                    for (int i = 0; i < xy.size() - 1; i++) {
                        float distance = (float) Math.sqrt(Math.pow(xy.get(i + 1)[0] - xy.get(i)[0], 2) + Math.pow(xy.get(i + 1)[1] - xy.get(i)[1], 2));
                        float distanceDif = (size - distance) / 2;
                        float angle;
                        if (distance > size) {
                            angle = (float) Math.atan((xy.get(i)[1] - xy.get(i + 1)[1]) / (xy.get(i)[0] - xy.get(i + 1)[0]));

                            if (xy.get(i)[0] - xy.get(i + 1)[0] < 0) {
                                xy.set(i + 1, new float[]{xy.get(i)[0] + (float) (size * Math.cos(angle)), xy.get(i)[1] + (float) (size * Math.sin(angle))});
                            } else {
                                xy.set(i + 1, new float[]{xy.get(i)[0] - (float) (size * Math.cos(angle)), xy.get(i)[1] - (float) (size * Math.sin(angle))});
                            }
                        }
                        if (!stop) {
                            stop = deepPhysic();
                        } else {
                            deepPhysic();
                        }
                    }

                }
            }
            if (stop) {
                step *= 0.99;
            }
            if (trest.mouseControl) {
                float maxDistance = 80;
                float distance = (float) playerHeadXY().distance(0, 0);
                if (distance > maxDistance) {
                    float angle = (float) Math.atan((-y) / (-x));
                    double translocationX = Player.step * Math.cos(angle);
                    double translocationY = Player.step * Math.sin(angle);
                    if (-x < 0) {
                        direction = new float[]{(float) +translocationX, (float) +translocationY};
                    } else {
                        direction = new float[]{(float) -translocationX, (float) -translocationY};
                    }
//                    direction = new float[]{direction[0]/2, direction[1]/2};
                } else {
                    direction = new float[]{x / 20, y / 20};
                }
            } else {
                direction = new float[]{0, 0};
            }

            //place for physics  TODO

//            for (int i = 0; i < 1; i++) {
//                makePhysics();
//            }


            for (int i = 0; i < rendering.getModels().size(); i++) {
                rendering.getModels().get(i).getMovement().setPosition(new Vector3f(xy.get(i)[0], xy.get(i)[1], 0));
            }
            maxSpeedRender.getModels().get(0).getMovement().setPosition(new Vector3f((float) xy.get(0)[0], (float) xy.get(0)[1], 0));
            maxSpeedRender.getModels().get(0).getMovement().setRotation(-tMouse);
            speedScale = ((step - (maxStep - 0.2f)) / 0.2f);
            if(speedScale<0){
                speedScale = 0;
            }
            if (speedScale>1){
                curSpeed+=1*0.01;
            }else {
                curSpeed += speedScale * 0.01;
            }
            maxSpeedRender.setTime(trest.getMainTime());
            maxSpeedRender.setSpeedScale(speedScale);
            maxSpeedRender.setSpeed(curSpeed);
            if (step == maxStep) {
                System.out.println("MAXXX");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
//xy.add(0,new int[]{x,y});
//xy.remove(xy.size()-1);
    }

    float speedScale = ((step - (maxStep - minStep)) / minStep);
    float curSpeed = 0;

    public void makePhysics() {
        for (int i = 0; i < xy.size(); i++) {

            for (int j = 1; j < xy.size(); j++) {
                if (j == i) {
                    continue;
                }
                float distance = (float) Math.sqrt(Math.pow(xy.get(i)[0] - xy.get(j)[0], 2) + Math.pow(xy.get(i)[1] - xy.get(j)[1], 2));
                float distanceDif = size - distance;
                float angle;
                if (distance != 0 && distance < size * 1.5) {
                    if (i == 0 && trest.mouseControl && step > minStep) {
                        step *= 0.99;
                    }
                    angle = (float) Math.atan((xy.get(i)[1] - xy.get(j)[1]) / (xy.get(i)[0] - xy.get(j)[0]));
                    if (xy.get(i)[0] - xy.get(j)[0] < 0) {
//                        xy.set(i, new float[]{xy.get(j)[0] - (float) ((size - distanceDif) * Math.cos(angle)), xy.get(j)[1] - (float) ((size - distanceDif) * Math.sin(angle))});
                        xy.set(j, new float[]{xy.get(i)[0] + (float) ((size) * Math.cos(angle)), xy.get(i)[1] + (float) ((size) * Math.sin(angle))});

                    } else {
//                        xy.set(i, new float[]{xy.get(j)[0] + (float) ((size - distanceDif) * Math.cos(angle)), xy.get(j)[1] + (float) ((size - distanceDif) * Math.sin(angle))});

                        xy.set(j, new float[]{xy.get(i)[0] - (float) ((size) * Math.cos(angle)), xy.get(i)[1] - (float) ((size) * Math.sin(angle))});
                        ;
                    }

                }
                deepPhysic();
            }

        }
    }

    public boolean deepPhysic() {
        boolean stop = false;
        for (int j = xy.size() - 1; j > 0; j--) {

            float distance = (float) Math.sqrt(Math.pow(xy.get(0)[0] - xy.get(j)[0], 2) + Math.pow(xy.get(0)[1] - xy.get(j)[1], 2));
            float distanceDif = size - distance;
            float angle;
            if (distance != 0 && distance < size) {
                if (trest.mouseControl && step > minStep && j != 1) {

                    stop = true;
                }
                angle = (float) Math.atan((xy.get(0)[1] - xy.get(j)[1]) / (xy.get(0)[0] - xy.get(j)[0]));
//                if(Math.sqrt(Math.pow(xy.get(1)[0] - xy.get(j)[0], 2) + Math.pow(xy.get(1)[1] - xy.get(j)[1], 2))>size*2) {
                if (xy.get(0)[0] - xy.get(j)[0] < 0) {
                    xy.set(j, new float[]{xy.get(0)[0] + (float) ((size) * Math.cos(angle)), xy.get(0)[1] + (float) ((size) * Math.sin(angle))});
                } else {
                    xy.set(j, new float[]{xy.get(0)[0] - (float) ((size) * Math.cos(angle)), xy.get(0)[1] - (float) ((size) * Math.sin(angle))});
                }
//                }else if(Math.sqrt(Math.pow(xy.get(1)[0] - xy.get(j)[0], 2) + Math.pow(xy.get(1)[1] - xy.get(j)[1], 2))<=size*2&& j!=1) {
//                    if (xy.get(0)[0] - xy.get(j)[0] < 0) {
//                        xy.set(j, new float[]{xy.get(0)[0] - (float) ((size) * Math.cos(angle)), xy.get(0)[1] - (float) ((size*2) * Math.sin(angle))});
//                    } else {
//                        xy.set(j, new float[]{xy.get(0)[0] - (float) ((size) + Math.cos(angle)), xy.get(0)[1] + (float) ((size*2) * Math.sin(angle))});
//                    }
//                }
            }

        }
        return stop;
    }

    public void checkForAbsorb() {

        for (GluePart part : GluePart.glueParts) {
            if (part.gluePartHeadXY().distance(playerHeadXY()) < 200 && part.getXy().size() > 1) {
                boolean eat = false;
                for (int i = 1; i < part.xy.size(); i++) {
                    Point2D partPoint = new Point2D.Float(part.xy.get(i)[0], part.xy.get(i)[1]);

                    if (playerHeadXY().distance(partPoint) < size) {
                        //Добавляем ячейку игроку
                        //Создаем новую часть игрока с координатами откушеной части

                        grow();
                        ArrayList<float[]> newPart = new ArrayList<>();
                        if (part.xy.size() - 1 != i) {
                            for (int j = i; j < part.xy.size(); j++) {
                                newPart.add(new float[]{part.xy.get(j)[0], part.xy.get(j)[1]});
                                part.minusCell(j);

                                j--;
                            }
                            newPart.remove(0);
                            new GluePart(window, newPart);
                        } else {
                            part.minusCell(part.xy.size() - 1);
                        }

                        return;
                    }

                }

            }
        }

    }


    public static int maxSize = 160;

}
