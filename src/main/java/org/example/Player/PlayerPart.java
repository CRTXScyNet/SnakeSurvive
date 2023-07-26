package org.example.Player;

import org.example.Painter.Apple;
import org.example.gpu.Timer;
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


    public void moveXy(float[] direct) {

        for (int i = 0; i < xyArray.size(); i++) {
            xyArray.set(i, new float[]{xyArray.get(i)[0] - direct[0], xyArray.get(i)[1] - direct[1]});
        }
        for (int i = 0; i < rendering.getModels().size(); i++) {
            rendering.getModels().get(i).getMovement().setPosition(new Vector3f((float) xyArray.get(i)[0], (float) xyArray.get(i)[1], 0));
        }
        xy = new Point2D.Float((float) xy.getX() - direct[0], (float) xy.getY() - direct[1]);



        rendering.getModels().get(0).getMovement().setPosition(new Vector3f((float) xy.getX(), (float) xy.getY(), 0));

    }

    public Point2D xy = new Point2D.Float();
    public ArrayList<float[]> xyArray = new ArrayList<>();

    private Color color = new Color(Color.white.getRGB());
    public Color mainColor = new Color(0, 100, 100);
    public Color takenAppleColor = new Color(200, 0, 0);
    static float size = 8;

    public float step = 1.1f * Player.step;

    public void setDelay() {
        if (delay < 100) {
            delay += 1;
//            this.delay = (int) delayDouble;
        }
    }





    public boolean reset = false;


    private org.example.gpu.Window window;
    private ModelRendering rendering;

    private float birthTime = 0;

    public PlayerPart(org.example.gpu.Window window) {

        this.window = window;
        xy.setLocation(Player.playerHeadXY());
        size = Player.size;
        tMouse = Player.player.gettMouse();
        color = new Color(0, 200, 200);
        rendering = new ModelRendering(window,  null, "gluePart");
        birthTime = Timer.getFloatTime();
        rendering.setTime(birthTime + trest.getMainTime());
    }

    public void addCircle() {

            float x = xyArray.get(xyArray.size() - 1)[0];
            float y = xyArray.get(xyArray.size() - 1)[1];
            xyArray.add(new float[]{x, y});

            rendering.addModel(new Model(window, (int) (size * 30), color));
            rendering.getModels().get(xyArray.size() - 1).getMovement().setPosition(new Vector3f((float) x, (float) y, 0));

    }
    public Point2D getXy() {
        return xy;
    }


    public boolean isAlive() {
        return isAlive;
    }



    public void spawn() {
        birthTime = trest.getMainTime();
        isAlive = true;
        xyArray.add(new float[]{(float) xy.getX(),(float) xy.getX()});
        rendering.addModel(new Model(window, (int) (size * 30), color));
        rendering.getModels().get(0).getMovement().setPosition(new Vector3f((float) xy.getX(), (float) xy.getY(), 0));
    }

    public void remove() {
        rendering.getModels().clear();
        xyArray.clear();
    }

    public void setXy(Point2D xy) {
        this.xy.setLocation(xy);
    }

    public void reset() {
        callBack = false;
        isEmpty = true;
        isAlive = false;
        appleIsNear = false;
        isLost = false;
        remove();
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


    void setRadian() {
        float xTarget;
        float yTarget;
if(callBack){
    xTarget = (float) (Player.playerHeadXY().getX() - xy.getX());
    yTarget = (float) (Player.playerHeadXY().getY() - xy.getY());
}else if(appleIsNear){
    xTarget = (float) (Apple.getXy()[0] - xy.getX());
    yTarget = (float) (Apple.getXy()[1] - xy.getY());
}else {
    xTarget = (float) (pointWatch[0] - xy.getX());
    yTarget = (float) (pointWatch[1] - xy.getY());
}


        float TargetRadian = 0;
        // 1143 372 900 600
        TargetRadian = (float) Math.atan2(xTarget, yTarget);
        if (TargetRadian < 0) {
            TargetRadian += 6.28;

        }
        float halfNear = (tMouse + 3.14f) % 6.28f;

        tMouse = (float) ((tMouse + 6.28) % 6.28);

        double dif = Math.abs((TargetRadian - tMouse) % 6.28);
        if (dif > 3.14) {
            dif = Math.abs((Math.max(TargetRadian, tMouse) - 3.14) - (Math.min(TargetRadian, tMouse) + 3.14));

        }


        stepRad = (float) dif / (15/(step/2));


        if (appleIsNear ||callBack) {
            if (TargetRadian > tMouse && TargetRadian > halfNear && halfNear >= 3.14) {          // уменьшение
                stepRad *= -1;
            } else if (TargetRadian < tMouse && TargetRadian < halfNear && halfNear >= 3.14) {                          // уменьшение
                stepRad *= -1;
            } else if (TargetRadian < tMouse && TargetRadian > halfNear) {                          // уменьшение
                stepRad *= -1;
            }
            tMouse += stepRad;
        } else {
            if (Math.random() > 0.5) {
                if (isLost){
                    tMouse += 0.1;
                }else {
                    tMouse += 0.01;
                }
            } else {
                if (isLost) {
                    tMouse -= 0.1;
                } else {
                    tMouse -= 0.01;

                }
            }
        }
        if (tMouse > 6.28) {
            tMouse -= 6.28;

        } else if (tMouse < 0) {

            tMouse += 6.28;
        }
        pointWatch[0] = (float) (step * Math.sin(tMouse) + xy.getX());
        pointWatch[1] = (float) (step * Math.cos(tMouse) + xy.getY());

    }

    static double delayStat = 15;
    private double delayDouble = delayStat;
    private int delay = (int) delayDouble;
    public boolean appleIsNear = false;
    public boolean isEmpty = true;

    public void setCallBack(boolean callBack) {
        this.callBack = callBack;
    }

    public boolean callBack = false;
    public boolean isAlive = false;
    public boolean isLost = false;

    public void setTime(float time) {
        rendering.setTime(time);
    }
    public void setAppleCount() {
                rendering.getModels().get(0).setRGB(takenAppleColor);
        }
    public void resetAppleCount() {
        rendering.getModels().get(0).setRGB(mainColor);
    }


    public void update() {

        if(isAlive){
            timer = trest.getMainTime() - birthTime;
            if(Player.playerHeadXY().distance(xy)>300){
                isLost = true;
            }
            if(timer >=5){
                timer = 0;
                callBack = true;
            }
            moveXy(Player.player.getDirection());
            moveCheck();
        }else {
            timer = 0;
            tMouse = Player.player.gettMouse();
            setXy(Player.playerHeadXY());
        }
    }
    private float timer = 0;

    public void moveCheck() {

        if(isEmpty && !callBack){
            if(!isLost) {
                step = 2 * Player.step;
                if (xy.distance(Apple.getXy()[0], Apple.getXy()[1]) < 100) {
                    if (Apple.checkCollision(new float[]{(float) xy.getX(), (float) xy.getY()})) {
                        setAppleCount();
                        callBack = true;
                        appleIsNear = false;
                        isEmpty = false;
                    } else {
                        appleIsNear = true;
                    }

                }else {
                    appleIsNear = false;
                }
            }else {
                if(xy.distance(Player.playerHeadXY())<100){
                    callBack = true;
                }
                step = Player.maxStep/2;
            }
            setRadian();
        }else{
            step = 2 * Player.step;
            if(xy.distance(Player.playerHeadXY())<size){
                if(xyArray.size()>1){
                    xyArray.remove(0);
                    rendering.getModels().remove(0);
                    xy.setLocation(xyArray.get(0)[0],xyArray.get(0)[1]);
                    Player.player.grow();
                }else {

                    if (!isEmpty) {
                        Player.player.eatTheApple();
                    }else {
                        Player.player.grow();
                    }

                    reset();
                    return;
                }
            }
            setRadian();
        }
        move(pointWatch[0],pointWatch[1]);
        checkForAbsorb();
    }


    public void move(float x, float y) {

        try {
            xy.setLocation(x, y);
            xyArray.set(0,new float[]{(float)xy.getX(),(float)xy.getY()});
            for (int i = 0; i < xyArray.size() - 1; i++) {
                float distance = (float) Math.sqrt(Math.pow(xyArray.get(i + 1)[0] - xyArray.get(i)[0], 2) + Math.pow(xyArray.get(i + 1)[1] - xyArray.get(i)[1], 2));
                float distanceDif = (size - distance) / 2;
                float angle;
                if (distance > size) {
                    angle = (float) Math.atan((xyArray.get(i)[1] - xyArray.get(i + 1)[1]) / (xyArray.get(i)[0] - xyArray.get(i + 1)[0]));
                    if (xyArray.get(i)[0] - xyArray.get(i + 1)[0] < 0) {
                        xyArray.set(i + 1, new float[]{xyArray.get(i)[0] + (float) (size * Math.cos(angle)), xyArray.get(i)[1] + (float) (size * Math.sin(angle))});
                    } else {
                        xyArray.set(i + 1, new float[]{xyArray.get(i)[0] - (float) (size * Math.cos(angle)), xyArray.get(i)[1] - (float) (size * Math.sin(angle))});
                    }
                }

            }



            for (int i = 0; i < rendering.getModels().size(); i++) {
                rendering.getModels().get(i).getMovement().setPosition(new Vector3f(xyArray.get(i)[0], xyArray.get(i)[1], 0));
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
//xy.add(0,new int[]{x,y});
//xy.remove(xy.size()-1);
    }
    public void checkForAbsorb() {

        for (GluePart part : GluePart.glueParts) {
            if (part.gluePartHeadXY().distance(xy) < 200 && part.getXy().size() > 1) {
                boolean eat = false;
                for (int i = 1; i < part.xy.size(); i++) {
                    Point2D partPoint = new Point2D.Float(part.xy.get(i)[0], part.xy.get(i)[1]);

                    if (xy.distance(partPoint) < size) {
                        //Добавляем ячейку игроку
                        //Создаем новую часть игрока с координатами откушеной части

                        addCircle();
                        ArrayList<float[]> newPart = new ArrayList<>();
                        if(part.xy.size()-1 != i) {
                            for (int j = i; j < part.xy.size(); j++) {
                                newPart.add(new float[]{part.xy.get(j)[0], part.xy.get(j)[1]});
                                part.minusCell(j);

                                j--;
                            }
                            newPart.remove(0);
                            new GluePart(window, newPart);
                        }else{
                            part.minusCell(part.xy.size()-1);

                        }

                        return;
                    }

                }

            }
        }

    }


}
