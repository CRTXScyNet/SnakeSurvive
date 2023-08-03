package org.example.obstructions;

import org.example.Player.Player;
import org.example.Player.GluePart;
import org.example.Sound.MainSoundsController;
import org.example.gpu.render.Window;
import org.example.gpu.render.Model;
import org.example.gpu.render.ModelRendering;
import org.example.gpu.gameProcess.trest;
import org.joml.Vector3f;

import java.awt.*;
import java.awt.geom.Point2D;

public class WormHole {
    private float[] xy = new float[]{};
    static Color color = new Color(28, 150, 232);

    private static int size = 10;
    public static WormHole wormHole;

    public float[] getXy() {
        return xy;
    }


    private boolean isNear = false;
    private boolean partIsNear = false;
    private boolean isPull = false;
    private boolean changePosition = false;
    private float spawn;
    private final float timeToShow = 3;
    private final float timeToClose = 1;
    private float timer = 0;
    private float startToMove;
    private final float whenToMove = 15;
    private Window window;
    private ModelRendering rendering;


    public WormHole(Window window) {
        spawn = trest.getMainTime();
        wormHole = this;
        this.window = window;
        xy = new float[]{(int) (Math.random() * trest.playGroundWidth- (trest.playGroundWidth/ 2)), (int) (Math.random() * trest.playGroundHeight - (trest.playGroundHeight / 2))};
//        System.out.printf("Apple x: %s, y: %s ", xy[0],xy[1]);
        rendering = new ModelRendering(window,   null, "wormHole");
        rendering.addModel(new Model(window, (int) (size * 50),color));
        rendering.getModels().get(0).getMovement().setPosition(new Vector3f((float) xy[0], (float) xy[1], 0));

    }

    public void reset() {
        rendering.clear(true);
        ModelRendering.removeModelRender(rendering);
    }

    public void moveXy(float[] direct) {
        float x = xy[0] - direct[0];
        float y = xy[1] - direct[1];
        xy = new float[]{x, y};
        rendering.getModels().get(0).getMovement().setPosition(new Vector3f((float) x, (float) y, 0));

    }

    public void setXy() {
        spawn = trest.getMainTime();
        try {
            xy = new float[]{(int) (Math.random() * trest.playGroundWidth- (trest.playGroundWidth/ 2)), (int) (Math.random() * trest.playGroundHeight - (trest.playGroundHeight / 2))};
            rendering.getModels().get(0).getMovement().setPosition(new Vector3f((float) xy[0], (float) xy[1], 0));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }

public boolean close = false;
    public void update() {
        if(close){
            return;
        }

        if (!changePosition) {

            if (isPull) {
                if (Player.player.getHeadXY().distance(new Point2D.Float(xy[0], xy[1])) < 200) {
                    isNear = true;
                }
                for (GluePart part: GluePart.glueParts){
                    if(part.gluePartHeadXY().distance(new Point2D.Float(xy[0], xy[1])) < 200){
                        pullPart(part);
                    }

                }

                if (isNear) {
                    pullPlayer();
                }
            } else {
                rendering.setTime(-(timer * 1.1f) / (timeToShow) - 0.2f);

                if (timer >= timeToShow && !isPull) {
                    rendering.setTime(timer);
                    isPull = true;
                } else {
                    isPull = false;
                }
            }
            timer = trest.getMainTime() - spawn;
            if (timer >= whenToMove) {
                isPull = false;
                changePosition = true;
                startToMove = trest.getMainTime();
            }
        } else {
            timer = trest.getMainTime() - startToMove;
            rendering.setTime(-(timeToClose - timer) * 1.1f / timeToClose - 0.1f);
            if (timer >= timeToClose) {
                setXy();
if(suddenExpose){
    close = true;
}
                    changePosition = false;

            }
        }

    }
    private Point2D zero = new Point2D.Float(0,0);
    private boolean suddenExpose = false;
    public boolean suddenExpose(){
        suddenExpose = true;
        if (zero.distance(xy[0],xy[1])>600) {
            reset();
            return true;
        }
        if(isPull){
            isPull = false;
            changePosition = true;
            startToMove = trest.getMainTime();
        }
        if(close){
            reset();
            return true;
        }
        return false;
    }

    public void pullPlayer() {
        boolean stillNear = false;
        for (int j = Player.player.xy.size() - 1; j >= 0; j--) {

            float distance = (float) Math.sqrt(Math.pow(xy[0] - Player.player.xy.get(j)[0], 2) + Math.pow(xy[1] - Player.player.xy.get(j)[1], 2));
            float angle;
            if (distance> 10 && distance < 200) {
                if(j == 0){
                    float pitch = 0.7f + (distance/200*0.3f);
                    MainSoundsController.setPitch(pitch);

                }
                MainSoundsController.setPitchChange(true);
                stillNear = true;
                angle = (float) Math.atan((xy[1] - Player.player.xy.get(j)[1]) / (xy[0] - Player.player.xy.get(j)[0]));
                double translocationX = (distance - ((100 - distance / 10) * 0.012)) * Math.cos(angle);
                double translocationY = (distance - ((100 - distance / 10) * 0.012)) * Math.sin(angle);
                if (xy[0] - Player.player.xy.get(j)[0] < 0) {
                    Player.player.xy.set(j, new float[]{xy[0] + (float) translocationX, xy[1] + (float) translocationY});
                } else {
                    Player.player.xy.set(j, new float[]{xy[0] - (float) translocationX, xy[1] - (float) translocationY});
                }
            }

        }
        if (!stillNear) {
            isNear = false;
        }
    }
    public void pullPart(GluePart part) {

        for (int j = part.xy.size() - 1; j >= 0; j--) {

            float distance = (float) Math.sqrt(Math.pow(xy[0] - part.xy.get(j)[0], 2) + Math.pow(xy[1] - part.xy.get(j)[1], 2));
//                float distanceDif = distance;
            float angle;
            if (distance > 10 && distance < 200) {

                angle = (float) Math.atan((xy[1] - part.xy.get(j)[1]) / (xy[0] - part.xy.get(j)[0]));
                double translocationX = (distance - ((100 - distance / 10) * 0.02)) * Math.cos(angle);
                double translocationY = (distance - ((100 - distance / 10) * 0.02)) * Math.sin(angle);
                if (xy[0] - part.xy.get(j)[0] < 0) {
                    part.xy.set(j, new float[]{xy[0] + (float) translocationX, xy[1] + (float) translocationY});
                } else {
                    part.xy.set(j, new float[]{xy[0] - (float) translocationX, xy[1] - (float) translocationY});
                }
            }
        }
    }
}
