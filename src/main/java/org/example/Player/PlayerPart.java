package org.example.Player;

import org.example.Painter.Apple;
import org.example.gpu.gameProcess.trest;
import org.example.gpu.render.Model;
import org.example.gpu.render.Window;
import org.example.time.Timer;
import org.joml.Vector3f;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class PlayerPart extends PlayerParent {


    public void moveXy(float[] direct) {

        for (int i = 0; i < xy.size(); i++) {
            xy.set(i, new float[]{xy.get(i)[0] - direct[0], xy.get(i)[1] - direct[1]});
        }
        for (int i = 0; i < rendering.getModels().size(); i++) {
            rendering.getModels().get(i).getMovement().setPosition(new Vector3f((float) xy.get(i)[0], (float) xy.get(i)[1], 0));
        }
        partHead = new Point2D.Float((float) partHead.getX() - direct[0], (float) partHead.getY() - direct[1]);


        rendering.getModels().get(0).getMovement().setPosition(new Vector3f((float) partHead.getX(), (float) partHead.getY(), 0));

    }

    public Point2D partHead = new Point2D.Float();


    public Color mainColor = new Color(0, 100, 100);
    public Color takenAppleColor = new Color(200, 0, 0);

public boolean readyTuCut = false;
    public void setDelay() {
        if (delay < 100) {
            delay += 1;
//            this.delay = (int) delayDouble;
        }
    }


    private float birthTime = 0;

    public PlayerPart(Window window) {
        super(window);
        this.window = window;
        partHead.setLocation(Player.player.getHeadXY());
        size = Player.player.size;
        tMouse = Player.player.getMouse();
        color = new Color(0, 200, 200);
        renderInit(color, "gluePart", null);
        birthTime = Timer.getFloatTime();
        rendering.setTime(10);
    }

    public Point2D getPartHead() {
        return partHead;
    }


    public boolean isAlive() {
        return isAlive;
    }


    public void spawn() {
        readyTuCut = true;
        birthTime = trest.getMainTime();
        isAlive = true;
        xy.add(new float[]{(float) partHead.getX(), (float) partHead.getX()});
        rendering.addModel(new Model(window, (int) (size*30), color,false));
        rendering.getModels().get(0).getMovement().setPosition(new Vector3f((float) partHead.getX(), (float) partHead.getY(), 0));
        rendering.setTime(1);
    }

    public void remove() {
        rendering.getModels().clear();
        xy.clear();
    }

    public void setPartHead(Point2D partHead) {
        this.partHead.setLocation(partHead);
    }

    public void reset() {
        readyTuCut = false;
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


    protected void setRadian(Point2D point2D) {
        super.setRadian(point2D);

        stepRad = (float) difRad / (15 / (step / 2));
        if (point2D != null) {
            tMouse += stepRad * radDir;
        } else {
            if (Math.random() > 0.5) {
                if (isLost) {
                    tMouse += 0.1;
                } else {
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
        pointWatch[0] = (float) (step * Math.sin(tMouse) + partHead.getX());
        pointWatch[1] = (float) (step * Math.cos(tMouse) + partHead.getY());

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

        if (isAlive) {
            timer = trest.getMainTime() - birthTime;
            if (Player.player.getHeadXY().distance(partHead) > 300) {
                isLost = true;
            }
            if (timer >= 5) {
                timer = 0;
                callBack = true;
            }
            moveXy(Player.player.getDirection());
            moveCheck();
        } else {
            timer = 0;
            tMouse = Player.player.getMouse();
            setPartHead(Player.player.getHeadXY());
        }

    }

    private float timer = 0;

    public void moveCheck() {

        if (isEmpty && !callBack) {
            if (!isLost) {
                step = 2 * Player.player.step;
                if (partHead.distance(Apple.getXy()[0], Apple.getXy()[1]) < 100&&!Apple.eaten) {
                    if (Apple.checkCollision(new float[]{(float) partHead.getX(), (float) partHead.getY()})) {
                        setAppleCount();
                        callBack = true;
                        appleIsNear = false;
                        isEmpty = false;
                        setRadian(null);
                    } else {
                        appleIsNear = true;
                        setRadian(Apple.getPoint2D());
                    }

                } else {
                    setRadian(null);
                    appleIsNear = false;
                }
            } else {
                if (partHead.distance(Player.player.getHeadXY()) < 100) {
                    callBack = true;
                    setRadian(Player.player.getHeadXY());
                } else {
                    setRadian(null);
                }
                step = Player.player.maxStep / 2;

            }

        } else {
            step = 2 * Player.player.step;
            if (partHead.distance(Player.player.getHeadXY()) < size) {
                if (xy.size() > 1) {
                    xy.remove(0);
                    rendering.getModels().remove(0);
                    partHead.setLocation(xy.get(0)[0], xy.get(0)[1]);
                    Player.player.grow();
                    if (!isEmpty) {
                        Player.player.grow();
                        Player.player.eatTheApple();
                        isEmpty = true;
                    }
                } else {

                    if (!isEmpty) {
                        Player.player.grow();
                        Player.player.eatTheApple();
                    } else {
                        Player.player.grow();
                    }

                    reset();
                    return;
                }
            }

            setRadian(Player.player.getHeadXY());
        }
        move(pointWatch[0], pointWatch[1]);
        checkForAbsorb();
    }


    public void move(float x, float y) {

        try {
            partHead.setLocation(x, y);
            xy.set(0, new float[]{(float) partHead.getX(), (float) partHead.getY()});
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


            for (int i = 0; i < rendering.getModels().size(); i++) {
                rendering.getModels().get(i).getMovement().setPosition(new Vector3f(xy.get(i)[0], xy.get(i)[1], 0));
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
//xy.add(0,new int[]{x,y});
//xy.remove(xy.size()-1);
    }

    public void checkForAbsorb() {

        for (GluePart part : GluePart.glueParts) {
            if (part.gluePartHeadXY().distance(partHead) < 200 && part.getXy().size() > 1) {
                boolean eat = false;
                for (int i = 1; i < part.xy.size(); i++) {
                    Point2D partPoint = new Point2D.Float(part.xy.get(i)[0], part.xy.get(i)[1]);

                    if (partHead.distance(partPoint) < size) {
                        //Добавляем ячейку игроку
                        //Создаем новую часть игрока с координатами откушеной части

                        addCircle();
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
    public void setMouse(float t){
        tMouse = t;
    }

}
