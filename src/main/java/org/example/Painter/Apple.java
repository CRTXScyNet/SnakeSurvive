package org.example.Painter;

import org.example.gpu.Window;
import org.example.gpu.render.Model;
import org.example.gpu.render.ModelRendering;
import org.joml.Vector3f;

import java.awt.*;

public class Apple {
    private static float[] xy = new float[]{};
    static Color color = new Color(Color.RED.getRGB());
    private static int size = 10;
    public static Apple apple;
    private int width;
    private int height;
    private Window window;

    private ModelRendering rendering;
    private ModelRendering renderingPoiner;

    public Apple(Window window) {
        apple = this;
        this.window = window;
        xy = new float[]{(int) (Math.random() * window.width * 3 - (window.width / 1.5)), (int) (Math.random() * window.height * 3 - (window.height / 1.5))};
//        System.out.printf("Apple x: %s, y: %s ", xy[0],xy[1]);
        rendering = new ModelRendering(window,  true, null, "apple");
        rendering.addModel(new Model(window, (int) (size * 50),color));
        rendering.getModels().get(0).getMovement().setPosition(new Vector3f((float) xy[0], (float) xy[1], 0));
        renderingPoiner = new ModelRendering(window,  true, null,"applePointer");
        renderingPoiner.addModel(new Model(window, 30,color));
    }
    public void setTime(float time){
        rendering.setTime(time);
        renderingPoiner.setTime(time);
    }
    public void moveXy(float[] direct) {
        float x = xy[0] - direct[0];
        float y = xy[1] - direct[1];
        xy = new float[]{x, y};
        rendering.getModels().get(0).getMovement().setPosition(new Vector3f((float) x, (float) y, 0));
        double xTarget = Apple.getXy()[0];
        double yTarget = Apple.getXy()[1];
        double TargetRadian = 0;
        // 1143 372 900 600
        TargetRadian = Math.atan2(xTarget, yTarget);
        if (TargetRadian < 0) {
            TargetRadian += 6.28;

        }
        double pointWatchX = (100 * Math.sin(TargetRadian));
        double pointWatchY = (100 * Math.cos(TargetRadian));
        renderingPoiner.getModels().get(0).getMovement().setPosition(new Vector3f((float) pointWatchX, (float) pointWatchY, 0));
        renderingPoiner.getModels().get(0).getMovement().setRotation((float) -TargetRadian);

    }

    public void setXy() {
        try {
            int x = (int) (Math.random() * window.width * 3 - (window.width / 1.5));
            int y = (int) (Math.random() * window.height * 3 - (window.height / 1.5));
            xy = new float[]{x, y};
            rendering.getModels().get(0).getMovement().setPosition(new Vector3f(x, y, 0));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }
    public void reset() {
        setXy();
    }

    public static Color getAppleColor() {
        return color;
    }

    public static int getAppleSize() {
        return size;
    }

    public static float[] getXy() {
        return xy;
    }

    public ModelRendering getRendering() {
        return rendering;
    }


}
