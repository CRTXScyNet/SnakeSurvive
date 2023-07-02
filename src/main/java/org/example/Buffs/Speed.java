package org.example.Buffs;

import org.example.gpu.Window;
import org.example.gpu.render.Model;
import org.example.gpu.render.ModelRendering;
import org.joml.Vector3f;

import java.awt.*;

public class Speed {
    private static float[] xy = new float[]{};
    static Color color = new Color(Color.RED.getRGB());

    private static int size = 10;


    public static Color getAppleColor() {
        return color;
    }
    public static int getAppleSize() {
        return size;
    }
    public static float[] getXy() {
        return xy;
    }
    public void moveXy(float[] direct) {
        float x = xy[0]-direct[0];
        float y = xy[1]-direct[1];
        xy = new float[]{x,y};
        rendering.getModels().get(0).getMovement().setPosition(new Vector3f((float) x,(float)y,0));

    }
    public void setXy() {
        try{
            int x =(int)(Math.random()*window.width*3-(window.width/1.5));
            int y = (int)(Math.random()*window.height*3-(window.height/1.5));
            xy = new float[]{x, y};
            rendering.getModels().get(0).getMovement().setPosition(new Vector3f(x,y,0));
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e);
        }
    }
    private int width;
    private int height;
    private org.example.gpu.Window window;
    private ModelRendering rendering;
    Speed(Window window){
        this.window = window;
        xy = new float[]{(int)(Math.random()*window.width-(window.width/2)) ,  (int)(Math.random()*window.height-(window.height/2))};
        System.out.printf("Apple x: %s, y: %s ", xy[0],xy[1]);
        rendering = new ModelRendering(window,color,true,null,"apple");
        rendering.addModel(new Model(window, (int)(size*50)));
        rendering.getModels().get(0).getMovement().setPosition(new Vector3f((float) xy[0],(float)xy[1],0));
    }


}
