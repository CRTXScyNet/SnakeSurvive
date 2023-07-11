package org.example.Player;

import org.example.gpu.Timer;
import org.example.gpu.Window;
import org.example.gpu.render.Model;
import org.example.gpu.render.ModelRendering;
import org.example.gpu.trest;
import org.joml.Vector3f;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class PlayerPart {
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

    public ArrayList<float[]> xy = new ArrayList<>();

//    public static Point2D playerHeadXY() {
//        return new Point2D.Float(xy.get(0)[0], xy.get(0)[1]);
//    }

    private float[] direction = null;

    private Color color = new Color(Color.white.getRGB());

    boolean perviyNah = false;

    private double chance = Math.random() * 0.8 + 0.2;

    static float size = 8;
    static double stepOfSize = 2;

    public static void setStep() {
        Player.step = (int) (Player.getSize() * stepOfSize);
    }

    public static float maxStepStat = Player.maxStep / 2;
    public float maxStep = maxStepStat;
    public static final float minStep = 0.5f;
    public float step = 0.5f;


    public void setDelay() {
        if (delay < 100) {
            delay += 1;
//            this.delay = (int) delayDouble;
        }
    }



    public static int snakeLength = 5;




    public static ArrayList<PlayerPart> playerParts = new ArrayList<>();
    public static int playerPartForSpawn = 0;


    public boolean reset = false;




    private Window window;
    private ModelRendering rendering;
    private float birthTime = 0;

    public PlayerPart(Window window) {

        this.window = window;
        xy.add(getRandomPoint());
        size = Player.size;
        tMouse = (float) Math.random() * 6.28f;
        color = new Color(0, 200, 200);
        playerParts.add(this);

        rendering = new ModelRendering(window, false, null, "playerPart");
        rendering.addModel(new Model(window, (int) (size * 30), color));
        rendering.getModels().get(0).getMovement().setPosition(new Vector3f((float) xy.get(0)[0], (float) xy.get(0)[1], 0));
        birthTime = Timer.getFloatTime();
        rendering.setTime(birthTime + trest.getMainTime());

//        for (int i = 0; i < snakeLength - 1; i++) {
//            addCircle();
//        }
    }


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
    }
    public void absorbCircle(float []newXY) {
        if (xy.size() < maxSize) {
            float x = newXY[0];
            float y = newXY[1];
            xy.add(new float[]{x, y});

            rendering.addModel(new Model(window, (int) (size * 30), color));
            rendering.getModels().get(xy.size() - 1).getMovement().setPosition(new Vector3f((float) x, (float) y, 0));
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

    public void spawn() {
        xy.set(0, getRandomPoint());
        rendering.getModels().get(0).getMovement().setPosition(new Vector3f((float) xy.get(0)[0], (float) xy.get(0)[1], 0));
    }

    public void setXy(float[] xy) {
        this.xy.set(0, xy);
        rendering.getModels().get(0).getMovement().setPosition(new Vector3f((float) xy[0], (float) xy[1], 0));
    }

    public void reset() {
        speedBoostTime = 0;
        maxStep = maxStepStat;
        step = 0.1f;
        delayDouble = delayStat;
        delay = (int) delayStat;
        delayCount = 0;
        xy.clear();
        xy.add(getRandomPoint());
        rendering.clear();
        rendering.addModel(new Model(window, (int) (size * 30), color));
        rendering.getModels().get(0).getMovement().setPosition(new Vector3f((float) xy.get(0)[0], (float) xy.get(0)[1], 0));
        playerPartForSpawn = 0;
//        move(width/2,height/2);
//        for (int i = 0; i < snakeLength - 1; i++) {
//            addCircle();
//        }

    }

    public float[] getRandomPoint() {
        float x = (int) (Math.random() * window.width * 3 - (window.width * 1.5));
        float y = (int) (Math.random() * window.height * 3 - (window.height * 1.5));
        if (Player.playerHeadXY().distance(x, y) < 500) {
            return getRandomPoint();
        }
        return new float[]{x, y};
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

    void setRadian(Point2D Target) {

        float xTarget = (float) Target.getX() - xy.get(0)[0];
        float yTarget = (float) Target.getY() - xy.get(0)[1];

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
        if (trest.isEnd) {
            stepRad = (float) dif / (xy.size()+1);
        }
        maxCount = (int) (3.14 / stepRad);

        if (playerIsNear || playerPartIsNear || trest.isEnd) {
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
            tMouse += stepRad;
        } else {
            if (Math.random() > 0.5) {
                tMouse += stepRad;
            } else {
                tMouse -= stepRad;
            }
        }


        if (tMouse > 6.28) {
            tMouse -= 6.28;

        } else if (tMouse < 0) {

            tMouse += 6.28;
        }


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

            if (!difDir) {
                if (count < maxCount) {
                    count++;
                    if (step < maxStep) {
                        step += 0.1;
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
    public boolean playerIsNear = false;
    public boolean playerPartIsNear = false;
    public boolean canSee = false;

    public Point2D playerPartHeadXY() {
        return new Point2D.Float(xy.get(0)[0], xy.get(0)[1]);
    }

    public static void addSpeedTime(float time) {
        speedBoostTime += time;
    }

    public void setTime(float time) {
        rendering.setTime(time);
    }

    public void moveCheck() {
        if (!canSee) {
            if (Timer.getFloatTime() - birthTime >= 5) {
                canSee = true;
            }
        }
        rendering.setTime(birthTime + trest.getMainTime());
        maxStep = (float) (Player.maxStep / xy.size()/1.5);

        try {
            playerPartIsNear = false;
            playerIsNear = false;
            Point2D nearest = null;


            if (canSee) {
                if (!trest.isEnd) {
                    for (float[] d : Player.players.get(0).getXy()) {
                        if (!playerIsNear && playerPartHeadXY().distance(new Point2D.Float(d[0], d[1])) < 100) {
                            playerIsNear = true;
                        }
                        if (playerIsNear) {
                            if (nearest == null) {
                                nearest = new Point2D.Float(d[0], d[1]);
                            } else {
                                Point2D newest = new Point2D.Float(d[0], d[1]);
                                if (nearest.distance(xy.get(0)[0], xy.get(0)[1]) > newest.distance(xy.get(0)[0], xy.get(0)[1])) {
                                    nearest = newest;
                                }
                            }
                        }

                    }
                }
//                playerIsNear = false; //TODO
                if (!trest.isEnd) {
                    if (playerIsNear && nearest != null && nearest.distance(playerPartHeadXY()) < Player.size) {
                        if (xy.size() > 1) {
                            Player.players.get(0).grow();
                            xy.remove(0);
                            rendering.getModels().remove(0);

                            return;
                        } else {
                            Player.players.get(0).grow();
                            clearPart();
                            return;
                        }
                    }
                }

                PlayerPart part = null;
                if (!playerIsNear) {
                    for (int i = 0; i < playerParts.size(); i++) {
                        if (playerParts.get(i) == this) {
                            continue;
                        }
                        if ((xy.size() == 1 || playerParts.get(i).getXy().size() > xy.size()) && playerParts.get(i).getXy().size() < 30) {
                            Point2D point2D = new Point2D.Float(playerParts.get(i).getXy().get(playerParts.get(i).getXy().size() - 1)[0], playerParts.get(i).getXy().get(playerParts.get(i).getXy().size() - 1)[1]);
                            if (!playerPartIsNear && playerPartHeadXY().distance(point2D) < 300) {
                                playerPartIsNear = true;
                                part = playerParts.get(i);
                                nearest = point2D;
                            } else {
                                if (nearest != null && part != null) {
                                    if (nearest.distance(playerPartHeadXY()) > point2D.distance(playerPartHeadXY())) {
                                        nearest = point2D;
                                        part = playerParts.get(i);
                                    }
                                }
                            }
                        }
                    }
                }

                if (!trest.isEnd) {
                    if (playerPartIsNear && nearest != null && part != null && nearest.distance(playerPartHeadXY()) < Player.size) {
                        if (xy.size() > 1) {
                            part.absorbCircle(xy.get(0));
                            xy.remove(0);
                            rendering.getModels().remove(0);
                            return;
                        } else {
                            part.absorbCircle(xy.get(0));
                            clearPart();
                            return;
                        }
                    }
                }
            }

            speedBoostTimer = (float) (trest.getMainTime() - timeTo);
            timeTo = trest.getMainTime();


            try {
                float xTarget;
                float yTarget;
                if ((playerIsNear || playerPartIsNear) && nearest != null) {
                    xTarget = (float) nearest.getX();
                    yTarget = (float) nearest.getY();
                } else {
                    xTarget = xy.get(0)[0];
                    yTarget = xy.get(0)[1];
                }
                if (trest.isEnd) {
                    xTarget = 0;
                    yTarget = 0;
                }
                setRadian(new Point2D.Float(xTarget, yTarget));
//            selfStep = Math.random()*(step+ Math.sqrt(Math.pow(Math.abs(xTarget - x), 2) + Math.pow(Math.abs(yTarget - y), 2)) / 50);
                try {
                    if(trest.isEnd){
                        pointWatch[0] = (float) (step*10* Math.sin(tMouse) + xy.get(0)[0]);
                        pointWatch[1] = (float) (step*10 * Math.cos(tMouse) + xy.get(0)[1]);
                    }else{
                        pointWatch[0] = (float) (step * Math.sin(tMouse) + xy.get(0)[0]);
                        pointWatch[1] = (float) (step * Math.cos(tMouse) + xy.get(0)[1]);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                move(pointWatch[0], pointWatch[1]);


            } catch (Exception e) {
                e.printStackTrace();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clear() {
        for (PlayerPart part : playerParts) {
            part.xy.clear();
            part.rendering.clear();
            ModelRendering.selfList.remove(part.rendering);
        }
        playerParts.clear();
    }

    public void clearPart() {
        xy.clear();
        rendering.clear();
        ModelRendering.selfList.remove(rendering);
        playerParts.remove(this);
    }

    public void move(float x, float y) {

        try {
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

            }

            direction = new float[]{x / 50, y / 50};


            //place for physics  TODO


            xy.set(0, new float[]{x, y});
//
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


    public static int maxSize = 160;

}
