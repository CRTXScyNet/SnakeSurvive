package org.example.Player.phantom;

import org.example.gpu.render.Model;
import org.example.gpu.render.ModelRendering;
import org.example.gpu.render.Window;
import org.example.time.ShortTimer;
import org.joml.Vector3f;

import java.awt.*;
import java.awt.geom.Point2D;

//Класс портала, в который уходит босс, при попадании по игроку.
public class PhantomPortal {
    private ModelRendering rendering;

    private Point2D xy = new Point2D.Float(0, 0);
    private Window window;
    private Phantom.Head head;
    private ShortTimer timer;
    private float mainTime = 0;
    private boolean closed = false;


    public PhantomPortal(Point2D point2D, Window window, Phantom.Head head, Color color, boolean in) {
        xy = new Point2D.Double(point2D.getX(), point2D.getY());
        this.window = window;
        if (in) {
            rendering = new ModelRendering(window, null, "portal");
            rendering.addModel(new Model(window, 50, color, false));
        } else {
            rendering = new ModelRendering(window, null, "portal");
            rendering.addModel(new Model(window, 50, color, false));
        }
        rendering.getModels().get(0).getMovement().setPosition(new Vector3f((float) xy.getX(), (float) xy.getY(), 0));
        timer = new ShortTimer(0.5f);
        this.head = head;
//        System.out.println("Portal created");
    }

    public void update(float time) {
        if (head.getPosition() != 0) {
            if (head.isAlive()) {
                timer.start(true, time);
                if (mainTime >= 1) {
                    mainTime += 0.01;
                } else {
                    mainTime = timer.update(time);
                }
            } else {
                if (!closed) {
                    if (!timer.isIncrease() && timer.isStopped()) {
                        closed = true;
                        return;
                    }
                    timer.start(false, time);
                    mainTime = timer.update(time);
                }
            }
        } else {
            if (timer.isStopped() && !timer.isIncrease()) {
                closed = true;
                return;
            }
            if (!timer.isStopped() && timer.isIncrease()) {
                timer.start(true, time);
            } else {
                timer.start(false, time);
            }
            mainTime = timer.update(time);
        }


//        System.out.println(mainTime);

        rendering.setTime(-mainTime);
    }

    public boolean closed() {
        return closed;
    }

    public void remove() {
        rendering.clear(true);
    }

    public void moveXY(float[] direct) {
        xy.setLocation(xy.getX() - direct[0], xy.getY() - direct[1]);
        rendering.getModels().get(0).getMovement().setPosition(new Vector3f((float) xy.getX(), (float) xy.getY(), 0));

    }

    public Point2D getXy() {
        return xy;
    }

    public void setXy(Point2D xy) {
        this.xy = xy;
    }
}
