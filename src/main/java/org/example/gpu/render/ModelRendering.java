package org.example.gpu.render;


import org.example.Enemy.Enemy;
import org.example.Enemy.Entity;
import org.example.Painter.Process;
import org.example.Player.Player;
import org.example.gpu.Window;
import org.example.gpu.trest;
import org.joml.Vector3f;

import java.awt.*;
import java.util.ArrayList;

public class ModelRendering {


    public static ArrayList<ModelRendering> selfList = new ArrayList<>();

    public ArrayList<Model> getModels() {
        return models;
    }

    private ArrayList<Model> models = new ArrayList<>();
    private Window window;
    private Shader shader;
    private float red;
    private float green;
    private float blue;
    private Color color;
    boolean isApple;

    private Entity entity = null;

    public ModelRendering(Window window, Color color, boolean isApple , Entity entity,String shaderName) {
        this.entity = entity;
        this.isApple = isApple;
        this.color = color;
        this.window = window;
        selfList.add(this);
        shader = new Shader(shaderName);
//        red = (float)Math.random()*0.7f;
//        green = (float)Math.random()*0.7f;
//        blue = (float)Math.random()*0.7f;
        red = (float) color.getRed() / 255;
        green = (float) color.getGreen() / 255;
        blue = (float) color.getBlue() / 255;
    }




    public void addModel(Model model) {
        models.add(model);
    }

    public void renderModels(float time) {
        shader.bind();
        float redA = red;
        float greenA = green;
        float blueA = blue;
        if(entity instanceof Enemy){
            if(((Enemy) entity).hunt){
                redA *= 3;

            }
        }
        if(isApple){
//           redA = 0;
//           greenA = 0;
//           blueA = 0;
            time = -Process.eatenTimelast;
        }
        if(Process.isEnd && entity instanceof Player){
            time = -Process.eatenPlayerTimelast;
        }
        shader.setUniform("time", time);
        if (models.size() == 0) {
            return;
        }
        for (int i = models.size() - 1; i >= 0; i--) {
            try {
                shader.setUniform("projection", models.get(i).getMovement().projection().scale(models.get(i).getScale()));
                shader.setUniform("rgb", redA, greenA, blueA);
                shader.setUniform("u_resolution", window.width, window.height);
                models.get(i).render(time);
            } catch (Exception e) {
e.printStackTrace();
            }
        }
    }


    public void update(Window window) {
        if (models.size() == 0) {
            return;
        }
//        for (int i = 0; i < models.size(); i++) {
//            models.get(i).update(window);
//        }
//        models.get(0).update(window);
    }

    public void clear() {
        for (int i = 0; i < models.size(); i++) {
            models.get(i).clean();
        }
        models.clear();
    }

    public void removeModel(Model model) {
        model.clean();
        models.remove(model);
    }

    public void randomPosition() {
        if (models.size() <= 0) {
            return;
        }
        for (int i = 0; i < models.size(); i++) {
            float x = (float) (Math.random() * trest.half*2 - trest.half);
            float y = (float) (Math.random() * trest.half*2- trest.half);
            models.get(i).getMovement().setPosition(new Vector3f(x, y, 0));
        }
    }

    public static void removeModelRender(ModelRendering mr) {
        selfList.remove(mr);

    }
}
