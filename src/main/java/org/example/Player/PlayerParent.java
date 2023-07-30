package org.example.Player;

import org.example.Enemy.Entity;
import org.example.gpu.gameProcess.trest;
import org.example.gpu.render.Model;
import org.example.gpu.render.ModelRendering;
import org.example.gpu.render.Window;
import org.joml.Vector3f;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class PlayerParent {
    public static ArrayList<float[]> xy = new ArrayList<>();
    public static Point2D playerHeadXY() {
        return new Point2D.Float(xy.get(0)[0], xy.get(0)[1]);
    }
    private static Window window;
    public static ArrayList<PlayerParent> playerParents = new ArrayList<>();
    private Color color = new Color(Color.white.getRGB());
    private ModelRendering rendering = null;

    static float size = 6;
    protected float tMouse = 1;
    protected float stepRad = 0.1f;
    public static float maxStepStat = 2f;
    public static float maxStep = maxStepStat;
    public static final float minStep = 1f;
    public static float step = 1f;
    protected float[] pointWatch = new float[]{0, 0};
    protected float stepRadLast = 0;
    protected int radDir = 0;
    protected float TargetRadian = 0;
    protected float targetOpposite = 0;
    protected double difRad = 0;
    protected int length = 0;
    protected int maxSize = 0;

    boolean canIncreaseSpeed = false;
    public PlayerParent(Window window) {
        PlayerParent.window = window;
        xy.add(new float[]{0, 0});
        playerParents.add(this);





    }
    protected void renderInit(Color color, String shaderName, Entity entity){
        this.color = color;
        rendering = new ModelRendering(window, entity, shaderName);
        rendering.addModel(new Model(window, (int) (size * 30), color));
        rendering.getModels().get(0).getMovement().setPosition(new Vector3f((float) 0, (float) 0, 0));
    }
    private void setLength(int i){
        length = i;
        for (int j = 0; i < length - 1; j++) {
            addCircle();
        }
    }
    public void addCircle() {
        if (xy.size() < maxSize) {
            float x = xy.get(xy.size() - 1)[0];
            float y = xy.get(xy.size() - 1)[1];
            xy.add(new float[]{x, y});

            if (rendering != null){
                rendering.addModel(new Model(window, (int) (size * 30), color));
                rendering.getModels().get(xy.size() - 1).getMovement().setPosition(new Vector3f((float) x, (float) y, 0));
            }
        }
    }
    private void setMaxSize(int i){
        maxSize = i;
    }
    public void reset() {
        xy.clear();
        if (rendering != null){
            rendering.clear(true);
            rendering.addModel(new Model(window, (int) (size * 30), color));
        }
    }
    public static double getSize() {
        return size;
    }
    void setRadian(Point2D Target) {
        float xTarget;
        float yTarget;
        if (Target != null) {
            xTarget = (float) Target.getX() - xy.get(0)[0];
            yTarget = (float) Target.getY() - xy.get(0)[1];
        } else {
            xTarget = (float) 0 - xy.get(0)[0];
            yTarget = (float) 0 - xy.get(0)[1];
        }
        TargetRadian = (float) Math.atan2(xTarget, yTarget);
        if (TargetRadian < 0) {
            TargetRadian += 6.28;

        }
        targetOpposite = (tMouse + 3.14f) % 6.28f;

        tMouse = (float) ((tMouse + 6.28) % 6.28);
        difRad = Math.abs((TargetRadian - tMouse) % 6.28);
        if (difRad > 3.14) {
            difRad = Math.abs((Math.max(TargetRadian, tMouse) - 3.14) - (Math.min(TargetRadian, tMouse) + 3.14));
        }

        if (TargetRadian > tMouse && TargetRadian > targetOpposite && targetOpposite >= 3.14) {          // уменьшение
            radDir = -1;
        } else if (TargetRadian < tMouse && TargetRadian < targetOpposite && targetOpposite >= 3.14) {                          // уменьшение
            radDir = -1;
        } else if (TargetRadian < tMouse && TargetRadian > targetOpposite) {                          // уменьшение
            radDir = -1;
        }else {
            radDir = 1;
        }
        //здесь нужно дописать что ты хочешь сделать
    }
    public void moveCheck() {
        //логика проверки перемещения
    }
    public void moveWithPhysics(float x, float y) {

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
        if(rendering != null){
            for (int i = 0; i < rendering.getModels().size(); i++) {
                rendering.getModels().get(i).getMovement().setPosition(new Vector3f(xy.get(i)[0], xy.get(i)[1], 0));
            }
        }
        //Допиши свое...
    }
    public void move(float x, float y) {
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

            }
            if(rendering != null){
                for (int i = 0; i < rendering.getModels().size(); i++) {
                    rendering.getModels().get(i).getMovement().setPosition(new Vector3f(xy.get(i)[0], xy.get(i)[1], 0));
                }
            }
        //Допиши свое...
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


}
