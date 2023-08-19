package org.example.Painter;

import org.example.gpu.render.Model;
import org.example.gpu.render.ModelRendering;
import org.example.gpu.render.Window;
import org.example.time.ShortTimer;
import org.joml.Vector3f;

import java.awt.*;
import java.awt.geom.Point2D;

public class Pointer {
    private ShortTimer timer;
    private ModelRendering rendering;
    private Window window;
    private Point2D position = new Point2D.Float(0, 0);

    private double distance = 0;
    private float posDistance = 0;
    private float mainTime = 0;
    private float visibility = 0;




    private Color color;

    public Pointer(float period, Window window, String shaderName, Color color, float posDistance, float visibility,float scale) {
        this.visibility = visibility;
        this.color = color;
        timer = new ShortTimer(period);
        this.window = window;
        rendering = new ModelRendering(window, null, shaderName);
        rendering.addModel(new Model(window, scale, color,true));

        this.posDistance = posDistance;
    }

    public void update(Point2D target, boolean visible, float time) {
        float result = 0;
        setPointerPosition(target);
        mainTime = time;
        distance = target.distance(0, 0);

        if (visible) {
            if (distance > visibility) {   //Visible

                timer.start(true, mainTime);
                result = -timer.update(mainTime);
            } else { // Invisible

                timer.start(false, mainTime);
                result = -timer.update(mainTime);
            }
        } else {   //Invisible

            timer.start(false, mainTime);
            result = -timer.update(mainTime);
        }

        if (timer.isStopped()) {
            if (timer.isIncrease()) {
                result = -1;
            } else {
                result = -0;
            }
        }
//System.out.println(result);
        rendering.setTime(result);
    }

    public void setPointerPosition(Point2D target) {
        double xTarget = target.getX();
        double yTarget = target.getY();
        double TargetRadian = 0;
        // 1143 372 900 600
        TargetRadian = Math.atan2(xTarget, yTarget);
        if (TargetRadian < 0) {
            TargetRadian += 6.28;

        }
        double pointWatchX = (posDistance * Math.sin(TargetRadian));
        double pointWatchY = (posDistance * Math.cos(TargetRadian));
        position.setLocation(pointWatchX, pointWatchY);
        rendering.getModels().get(0).getMovement().setPosition(new Vector3f((float) pointWatchX, (float) pointWatchY, 0));
        rendering.getModels().get(0).getMovement().setRotation(-(float)TargetRadian);
    }

    public void setColor(Color color) {
        if(this.color.getRGB()!=color.getRGB()) {
            this.color = color;
            rendering.getModels().get(0).setRGB(color);
        }
    }
    public void remove() {
        rendering.clear(true);
    }

}
