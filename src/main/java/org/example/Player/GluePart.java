package org.example.Player;

import org.example.Sound.MainSoundsController;
import org.example.time.Timer;
import org.example.gpu.render.Model;
import org.example.gpu.render.ModelRendering;
import org.example.gpu.render.Window;
import org.example.gpu.gameProcess.trest;
import org.joml.Vector3f;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class GluePart  extends PlayerParent{
    private int width;
    private int height;
    public static int maxAmountOfGlueParts = 100;
    public static int maxSize = 50;
    private int maxSelf = 0;

    private static boolean thertyn = false;
    private static boolean twentyn = false;
    private static boolean ten = false;


    public static void calcMaxSize() {

        thertyn = false;
        twentyn = false;
        int three = 2;
        int two = 3;
        int one = 5;

int count = 0;
            for (GluePart gluePart : glueParts) {
                if (gluePart.getXy().size() >= 30) {
                    count++;
                    if(count>= three) {
                        thertyn = true;
                        break;
                    }
                }
            }
            count = 0;
        if (thertyn) {
            for (GluePart gluePart : glueParts) {
                if (gluePart.getXy().size() >= 20 && gluePart.getXy().size() < 30) {
                    count++;
                    if(count>= two) {
                        twentyn = true;
                        break;
                    }
                }
            }
            count = 0;
        }else if (twentyn){
            for (GluePart gluePart : glueParts) {
                if (gluePart.getXy().size() >= 10 && gluePart.getXy().size() < 20) {
                    count++;
                    if(count>= one) {
                        ten = true;
                        break;
                    }
                }
            }
        }
    }
    public static void clearParts() {
            for (int i = glueParts.size() - 1; i >= 0; i--) {
                if (glueParts.get(i).gluePartHeadXY().distance(Player.player.getHeadXY()) > 450) {
                    glueParts.get(i).clearPart();
                    if (glueParts.size() <= maxAmountOfGlueParts) {
                        return;
                    }

                }

            }
    }
    public static void refresh() {
        if (glueParts.size() > maxAmountOfGlueParts) {
            for (int i = glueParts.size() - 1; i >= 0; i--) {
                if (glueParts.get(i).gluePartHeadXY().distance(Player.player.getHeadXY()) > 600) {
                    glueParts.get(i).clearPart();
                    if (glueParts.size() <= maxAmountOfGlueParts) {
                        return;
                    }

                }

            }
        }
        calcMaxSize();
    }


    public static float maxStepStat = Player.player.maxStep / 2;
    public float maxStep = maxStepStat;
    public static final float minStep = 1f;
    public float step = 1f;


    public void setDelay() {
        if (delay < 100) {
            delay += 1;
//            this.delay = (int) delayDouble;
        }
    }





    public static ArrayList<GluePart> glueParts = new ArrayList<>();
    public static int gluePartForSpawn = 0;



    private float ignoreTime = 5;
    private float birthTime = 0;


    public GluePart(Window window) {
super(window);
        xy.add(getRandomPoint());
        size = Player.player.size;
        tMouse = (float) Math.random() * 6.28f;
        color = new Color(0, 200, 200);
        glueParts.add(this);
renderInit(color,"gluePart",null);
        birthTime = Timer.getFloatTime();
        rendering.setTime(birthTime + trest.getMainTime());

//        for (int i = 0; i < snakeLength - 1; i++) {
//            addCircle();
//        }
    }

    public GluePart(Window window, ArrayList<float[]> xy) {

        this.window = window;
        this.xy.add(xy.get(0));
        size = Player.player.size;
        tMouse = (float) Math.random() * 6.28f;
        color = new Color(0, 200, 200);
        glueParts.add(this);
        ignoreTime = 0;
        rendering = new ModelRendering(window, null, "gluePart");
        rendering.addModel(new Model(window, (int) (size*30), color,false));
        rendering.getModels().get(0).getMovement().setPosition(new Vector3f((float) this.xy.get(0)[0], (float) this.xy.get(0)[1], 0));
        birthTime = Timer.getFloatTime();
        rendering.setTime(birthTime + trest.getMainTime());

        for (int i = 1; i < xy.size(); i++) {
            absorbCircle(xy.get(i));
        }
    }

    public ArrayList<float[]> getXy() {
        return xy;
    }

    public void absorbCircle(float[] newXY) {

        float x = newXY[0];
        float y = newXY[1];
        xy.add(new float[]{x, y});
        rendering.addModel(new Model(window, (int) (size*30), color,false));
        rendering.getModels().get(xy.size() - 1).getMovement().setPosition(new Vector3f((float) x, (float) y, 0));

    }

    public void minusCell(int count) {
        try {
            rendering.getModels().remove(count);
            getXy().remove(count);

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

        maxStep = maxStepStat;
        step = 0.1f;
        delayDouble = delayStat;
        delay = (int) delayStat;

        xy.clear();
        xy.add(getRandomPoint());
        rendering.clear(true);
        rendering.addModel(new Model(window, (int) (size*30), color,false));
        rendering.getModels().get(0).getMovement().setPosition(new Vector3f((float) xy.get(0)[0], (float) xy.get(0)[1], 0));
        gluePartForSpawn = 0;
//        move(width/2,height/2);
//        for (int i = 0; i < snakeLength - 1; i++) {
//            addCircle();
//        }

    }

    public float[] getRandomPoint() {

        float x = (int) (Math.random() * trest.enemySpawnArea - (trest.enemySpawnArea/2));
        float y = (int) (Math.random() * trest.enemySpawnArea - (trest.enemySpawnArea/2));
        try{
            double dist = 0.3 * (trest.enemySpawnArea/2);
            if (Player.player.getHeadXY().distance(x, y) < dist) {
                return getRandomPoint();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return new float[]{x, y};
    }

    public Color getColor() {
        return color;
    }








    private float stepRadLast = 0;
    private int count = 0;
    private int maxCount = 500;
    boolean difDir = false;
    boolean canIncreaseSpeed = false;


    protected  void setRadian(Point2D target) {

        super.setRadian(target);


        if (trest.isEnd) {
            stepRad = (float) difRad / (30 / (step * 10 / 2) * (xy.size() + 2) / 3 + 5);
//            stepRad = (float) dif / (((xy.size()+1f)/2)*maxStep);
        } else {
            stepRad = step / 15;
        }
        maxCount = (int) (3.14 / stepRad);

        if (playerIsNear || gluePartIsNear || trest.isEnd || playerPartIsNear) {

            tMouse += stepRad*radDir;
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


            if (difDir && ((stepRadLast > 0 && radDir > 0) || (stepRadLast < 0 && radDir < 0))) {
                canIncreaseSpeed = true;
            } else if (difDir) {
                canIncreaseSpeed = false;
                count = 0;
            }

            if ((stepRadLast > 0 && radDir < 0) || (stepRadLast < 0 && radDir > 0)) {
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

        stepRadLast = radDir;


    }

    static double delayStat = 15;
    private double delayDouble = delayStat;
    private int delay = (int) delayDouble;
    public boolean playerIsNear = false;
    public boolean playerPartIsNear = false;
    public boolean gluePartIsNear = false;
    public boolean canSee = false;

    public Point2D gluePartHeadXY() {
        return new Point2D.Float(xy.get(0)[0], xy.get(0)[1]);
    }


    public void setTime(float time) {
        rendering.setTime(time);
    }

    public void moveCheck() {
        if (!canSee) {
            if (Timer.getFloatTime() - birthTime >= ignoreTime) {
                canSee = true;
            }
        }
        rendering.setTime(birthTime + trest.getMainTime());
        maxStep = (float) (Player.player.maxStep / xy.size() / 1.5);

        try {
            gluePartIsNear = false;
            playerIsNear = false;
            playerPartIsNear = false;
            Point2D nearest = null;


            if (canSee) {
                if (!trest.isEnd) {
                    for (float[] d : Player.selfList.get(0).getXy()) {
                        if (!playerIsNear && gluePartHeadXY().distance(new Point2D.Float(d[0], d[1])) < 100) {
                            playerIsNear = true;
                            MainSoundsController.glue_part_bool = true;
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

                    if (playerIsNear && nearest != null && nearest.distance(gluePartHeadXY()) < Player.player.size) {
                        if (xy.size() > 1) {
                            Player.selfList.get(0).grow();
                            xy.remove(0);
                            rendering.getModels().remove(0);

                            return;
                        } else {
                            Player.selfList.get(0).grow();
                            clearPart();
                            return;
                        }
                    }


                    if (!playerIsNear && Player.player.part.isAlive) {
                        for (float[] d : Player.player.part.xy) {
                            if (!playerPartIsNear && gluePartHeadXY().distance(new Point2D.Float(d[0], d[1])) < 300) {
                                playerPartIsNear = true;
                            }
                            if (playerPartIsNear) {
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

                        if (playerPartIsNear && nearest != null && nearest.distance(gluePartHeadXY()) < Player.player.size) {
                            if (xy.size() > 1) {
                                Player.player.part.addCircle();
                                xy.remove(0);
                                rendering.getModels().remove(0);

                                return;
                            } else {
                                Player.player.part.addCircle();
                                clearPart();
                                return;
                            }
                        }
                    }
                }
            }

            GluePart part = null;
            if (!playerIsNear && !playerPartIsNear) {
                for (int i = 0; i < glueParts.size(); i++) {
                    if (glueParts.get(i) == this) {
                        continue;
                    }
                    if ((xy.size() == 1 || glueParts.get(i).getXy().size() > xy.size()) && glueParts.get(i).getXy().size() < canGrow()) {
                        Point2D point2D = new Point2D.Float(glueParts.get(i).getXy().get(glueParts.get(i).getXy().size() - 1)[0], glueParts.get(i).getXy().get(glueParts.get(i).getXy().size() - 1)[1]);
                        if (!gluePartIsNear && gluePartHeadXY().distance(point2D) < 300) {
                            gluePartIsNear = true;
                            part = glueParts.get(i);
                            nearest = point2D;
                        } else {
                            if (nearest != null && part != null) {
                                if (nearest.distance(gluePartHeadXY()) > point2D.distance(gluePartHeadXY())) {
                                    nearest = point2D;
                                    part = glueParts.get(i);
                                }
                            }
                        }
                    }
                }
            }

            if (!trest.isEnd) {
                if (gluePartIsNear && nearest != null && part != null && nearest.distance(gluePartHeadXY()) < Player.player.size) {
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


            try {
                float xTarget;
                float yTarget;
                if ((playerIsNear || gluePartIsNear || playerPartIsNear) && nearest != null) {

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
                    if (trest.isEnd) {

                        pointWatch[0] = (float) (step * 10 * Math.sin(tMouse) + xy.get(0)[0]);
                        pointWatch[1] = (float) (step * 10 * Math.cos(tMouse) + xy.get(0)[1]);
                    } else {
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

    public int canGrow() {
        if (!thertyn) {
            return 30;
        } else if (!twentyn) {
            return 20;
        } else if (!ten) {
            return 10;
        }
        return 5;
    }

    public static void clear() {
        for (GluePart part : glueParts) {
            part.xy.clear();
            part.rendering.clear(true);
            ModelRendering.selfList.remove(part.rendering);
        }
        glueParts.clear();
    }

    public void clearPart() {
        xy.clear();
        rendering.clear(true);
        ModelRendering.selfList.remove(rendering);
        glueParts.remove(this);
    }








}
