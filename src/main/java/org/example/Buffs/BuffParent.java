package org.example.Buffs;

import org.example.Painter.Apple;
import org.example.Player.Player;
import org.example.gpu.Timer;
import org.example.gpu.Window;
import org.example.gpu.render.Model;
import org.example.gpu.render.ModelRendering;
import org.example.gpu.trest;
import org.joml.Vector3f;

import java.awt.*;
import java.awt.geom.Point2D;

public  class BuffParent {
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
    protected boolean eaten = false;
    protected boolean isExist = false;

    protected static float size = 10;

    protected Window window;

    public BuffParent(Window window) {
        chance = 0.5;
        this.window = window;
    }

    public void renderInit(String buffShader, String buffPointerShader, Color color) {


        renderingBuff = new ModelRendering(window, color, false, null, buffShader);

        renderingBuffPointer = new ModelRendering(window, color, false, null, buffPointerShader);
    }
    public void addSome(){
//        if(renderingBuff.getModels().size()>0){
//            return;
//        }
        beginTime = trest.getMainTime();

        xy = new Point2D.Float((float) (Math.random() * window.width *3 - (window.width * 1.5)), (float) (Math.random() * window.height*3 - (window.height * 1.5)));
        renderingBuff.addModel(new Model(window, (int) (size * 50)));
        renderingBuff.getModels().get(0).getMovement().setPosition(new Vector3f((float) xy.getX(), (float) xy.getY(), 0));

        renderingBuffPointer.addModel(new Model(window, 50));

        isExist = true;
    }
    public boolean isExist(){
        return isExist;
    }
    public void moveXy(float[] direct) {
        if (direct != null && renderingBuff.getModels().size()>0) {
            float x = (float) xy.getX() - direct[0];
            float y = (float) xy.getY() - direct[1];
            xy = new Point2D.Float(x, y);
            renderingBuff.getModels().get(0).getMovement().setPosition(new Vector3f((float) x, (float) y, 0));
        }

    }
    public void update() {

        if(isExist){
            if (!eaten && xy.distance(Player.playerHeadXY()) < size * 2) {

                buffOnn();

            }
            if (!eaten) {
                setPointerPosition();
                existingTimer = trest.getMainTime() - beginTime;
                renderingBuff.setTime(existingTimer);
                renderingBuffPointer.setTime(existingTimer);
                if (existingTimer >= canExistTime) {
                    hide();
                    isExist = false;
                }

            } else {
                buffTimer = trest.getMainTime() - catchTime;
                if (buffTimer >= buffCanExistTime/* && !remove*/) {
                    buffOff();
                }
            }
        }
    }
    public void setPointerPosition(){
        double xTarget = xy.getX();
        double yTarget = xy.getY();
        double TargetRadian = 0;
        // 1143 372 900 600
        TargetRadian = Math.atan2(xTarget, yTarget);
        if (TargetRadian < 0) {
            TargetRadian += 6.28;
        }
        double pointWatchX = (100 * Math.sin(TargetRadian));
        double pointWatchY = (100 * Math.cos(TargetRadian));
        renderingBuffPointer.getModels().get(0).getMovement().setPosition(new Vector3f((float) pointWatchX, (float) pointWatchY, 0));
    }
    public void buffOnn() {
        eaten = true;
        catchTime = trest.getMainTime();
        hide();
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
    public void reset(){
        renderingBuff.clear();
        renderingBuffPointer.clear();
        eaten = false;
        isExist = false;
    }
}
