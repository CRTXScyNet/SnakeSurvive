package org.example.Buffs;

import org.example.Player.Player;
import org.example.gpu.Window;
import org.example.gpu.render.Model;
import org.example.gpu.render.ModelRendering;
import org.example.gpu.trest;
import org.joml.Vector3f;

import java.awt.*;
import java.awt.geom.Point2D;

public class BuffParent {
    public Point2D getXy() {
        return xy;
    }

    protected Point2D xy;
    protected ModelRendering renderingBuff;
    protected ModelRendering renderingBuffPointer;

    protected float beginTime;
    protected float existingTimer = 0;

    protected float canExistTime = 10f;

    protected double chance;

    protected float catchTime;

    protected float buffTimer = 0;
    protected float buffCanExistTime = 5f;


    protected static float size = 10;
    protected float timerForShow = 0.5f;
    protected float timeOfEnd = 0;
    protected boolean eaten = false;
    protected boolean isExist = false;
    protected boolean closing = false;
    protected boolean isShowing = false;

    protected Window window;
    protected Color color = new Color(0,0,0);

    public BuffParent(Window window) {
        chance = 0.5;
        this.window = window;
    }

    public void renderInit(String buffShader, String buffPointerShader, Color color) {
this.color = color;

        renderingBuff = new ModelRendering(window,   null, buffShader);

        renderingBuffPointer = new ModelRendering(window,  null, buffPointerShader);
    }

    public void addSome() {
//        if(renderingBuff.getModels().size()>0){
//            return;
//        }
        beginTime = trest.getMainTime();
        isShowing = true;

        xy = new Point2D.Float((float) (Math.random() * trest.playGroundWidth - (trest.playGroundWidth/2)), (float) (Math.random() *trest.playGroundHeight - (trest.playGroundHeight/2)));
        renderingBuff.addModel(new Model(window, (int) (size * 50),color));
        renderingBuff.getModels().get(0).getMovement().setPosition(new Vector3f((float) xy.getX(), (float) xy.getY(), 0));

        renderingBuffPointer.addModel(new Model(window, 15,color));
        closing = false;
        isExist = true;
    }

    public boolean isExist() {
        return isExist;
    }

    public void moveXy(float[] direct) {
        if (direct != null && renderingBuff.getModels().size() > 0) {
            float x = (float) xy.getX() - direct[0];
            float y = (float) xy.getY() - direct[1];
            xy = new Point2D.Float(x, y);
            renderingBuff.getModels().get(0).getMovement().setPosition(new Vector3f((float) x, (float) y, 0));
        }

    }

    public void update() {
        if (!isShowing) {
            if (isExist) {
                if (!eaten && xy.distance(Player.playerHeadXY()) < size * 2) {
                    timeOfEnd = trest.getMainTime();
                    closing = true;
                    buffOnn();

                }
                if (!eaten) {
                    setPointerPosition();
                    existingTimer = trest.getMainTime() - beginTime;
                    renderingBuff.setTime(existingTimer);
                    renderingBuffPointer.setTime(existingTimer);
                    if (existingTimer >= canExistTime && !closing) {
                        timeOfEnd = trest.getMainTime();
                        closing = true;
                    }

                } else {
                    buffTimer = trest.getMainTime() - catchTime;
                    if (buffTimer >= buffCanExistTime/* && !remove*/) {
                        buffOff();
                    }
                }
                if (closing) {
                    if (!eaten) {
                        float timer = trest.getMainTime() - timeOfEnd;
                        renderingBuff.setTime(-(timerForShow - timer) * 1.1f / timerForShow - 0.1f);
                        renderingBuffPointer.setTime(-(timerForShow - timer) * 1.1f / timerForShow - 0.1f);
                        if (timer >= timerForShow) {
                            hide();
                            isExist = false;
                        }
                    } else {
                        float timer = trest.getMainTime() - timeOfEnd;
                        renderingBuff.setTime(-(timerForShow - timer) * 1.1f / timerForShow - 0.1f);
                        renderingBuffPointer.setTime(-(timerForShow - timer) * 1.1f / timerForShow - 0.1f);
                        if (timer >= timerForShow) {
                            hide();
                        }
                    }
                }
            }
        }else {
            setPointerPosition();
            float timer = trest.getMainTime() - beginTime;
            renderingBuff.setTime(-(timer) * 1.1f / timerForShow - 0.1f);
            renderingBuffPointer.setTime(-(timer) * 1.1f / timerForShow - 0.1f);
            if (timer >= timerForShow) {
                isShowing = false;
            }
        }
    }

    public void setPointerPosition() {
        double xTarget = xy.getX();
        double yTarget = xy.getY();
        double TargetRadian = 0;
        // 1143 372 900 600
        TargetRadian = Math.atan2(xTarget, yTarget);
        if (TargetRadian < 0) {
            TargetRadian += 6.28;
        }
        double pointWatchX = (80 * Math.sin(TargetRadian));
        double pointWatchY = (80 * Math.cos(TargetRadian));
        renderingBuffPointer.getModels().get(0).getMovement().setPosition(new Vector3f((float) pointWatchX, (float) pointWatchY, 0));
        renderingBuffPointer.getModels().get(0).getMovement().setRotation((float) -TargetRadian);

    }

    public void buffOnn() {
        eaten = true;
        catchTime = trest.getMainTime();

    }

    public void buffOff() {
        eaten = false;
        isExist = false;
    }

    public void hide() {
        renderingBuff.clear();
        renderingBuffPointer.clear();
    }

    public void setCanExistTime(float canExistTime) {
        this.canExistTime = canExistTime;
    }

    public void setBuffCanExistTime(float buffCanExistTime) {
        this.buffCanExistTime = buffCanExistTime;
    }

    public double getChance() {
        return chance;
    }

    public void setChance(double chance) {
        this.chance = chance;
    }

    public void reset() {
        renderingBuff.clear();
        renderingBuffPointer.clear();
        eaten = false;
        isExist = false;
    }
}
