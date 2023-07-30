package org.example.Buffs;

import org.example.Player.Player;
import org.example.Sound.LWJGLSound;
import org.example.gpu.render.Window;
import org.example.gpu.render.Model;
import org.example.gpu.render.ModelRendering;
import org.example.gpu.gameProcess.trest;
import org.joml.Vector3f;

import java.awt.*;
import java.awt.geom.Point2D;

public class BuffParent {
    public Point2D getXy() {
        return xy;
    }

    protected Point2D xy = new Point2D.Float(0,0);
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
    protected boolean soundExist = false;



    protected Window window;
    protected Color color = new Color(0,0,0);
    protected LWJGLSound constantSound = null;
    protected LWJGLSound pickUpSound = null;

    public BuffParent(Window window) {

        chance = 0.5;
        this.window = window;

    }
    public void soundInit(String soundPath,boolean isLoop){
        constantSound = new LWJGLSound(soundPath,isLoop);
        constantSound.setCastomVolume(0.05f);
        pickUpSound = new LWJGLSound("./sounds/shard1.ogg",false);
        pickUpSound.setCastomVolume(0.1f);
        soundExist = true;
    };

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
        if(constantSound != null) {
            constantSound.update((float) xy.getX(), (float) xy.getY());
            if (isExist || isShowing){
                constantSound.play();
            }
        }

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
                        float secTime = -(timerForShow - timer) * 1.1f / timerForShow - 0.1f;
                        renderingBuff.setTime(secTime);
                        renderingBuffPointer.setTime(secTime);
                        if(constantSound != null) {
                            constantSound.fadeOut(timer, timerForShow);
                        }
                        if (timer >= timerForShow) {
                            hide();
                            isExist = false;
                            if(constantSound != null) {
                                constantSound.setFadeOut(false);
                            }
                        }
                    } else {
                        float timer = trest.getMainTime() - catchTime;
                        float secTime = -(timerForShow - timer) * 1.1f / timerForShow - 0.1f;
                        renderingBuff.setTime(secTime);
                        renderingBuffPointer.setTime(secTime);
                        if(constantSound != null) {
                            constantSound.fadeOut(timer, timerForShow);
                        }
                        if (timer >= timerForShow) {
                            hide();
                            if(constantSound != null) {
                                constantSound.setFadeOut(false);
                            }
                        }
                    }
                }
            }
        }else {

            setPointerPosition();
            float timer = trest.getMainTime() - beginTime;
            float secTime = -(timer) * 1.1f / timerForShow - 0.1f;
            renderingBuff.setTime(secTime);
            renderingBuffPointer.setTime(secTime);
            if(constantSound != null) {
                constantSound.appearIn(timer, timerForShow);
            }
            if (timer >= timerForShow) {
                isShowing = false;
                if(constantSound != null) {
                    constantSound.setAppearIn(false);
                }
            }
        }
    }
    public boolean suddenExpose(){
        if (xy.distance(0,0)>450) {
            reset();
            return true;
        }
        return false;
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
        if(pickUpSound != null){
            pickUpSound.play();
        }
        catchTime = trest.getMainTime();


    }

    public void buffOff() {
        eaten = false;
        isExist = false;
    }

    public void hide() {
        if(constantSound != null) {
            constantSound.stop();
        }
        renderingBuff.clear(false);
        renderingBuffPointer.clear(false);

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
        renderingBuff.clear(true);
        renderingBuffPointer.clear(true);
        ModelRendering.removeModelRender(renderingBuff);
        ModelRendering.removeModelRender(renderingBuffPointer);
        if(constantSound != null) {
            constantSound.delete();
        }
        if(pickUpSound != null) {
            pickUpSound.delete();
        }
        eaten = false;
        isExist = false;
    }
}
