package org.example.gpu.render;


import org.example.Enemy.Enemy;
import org.example.Enemy.Entity;
import org.example.gpu.gameProcess.trest;
import org.joml.Vector3f;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;

//Класс отрисовывающий объекты класса Models
public class ModelRendering {


    public static HashSet<ModelRendering> selfList = new HashSet<>();

    public ArrayList<Model> getModels() {
        return models;
    }

    private ArrayList<Model> models = new ArrayList<>();
    private Window window;
    private Shader shader;
    private float red;
    private float green;
    private float blue;
    private Point2D mousePos;


    float time;
    boolean isApple;

    private Entity entity = null;

    public ModelRendering(Window window, Entity entity, String shaderName) {
        this.entity = entity;
        this.isApple = isApple;

        this.window = window;
        selfList.add(this);
        shader = new Shader(shaderName);
    }


    public float getTime() {
        return time;
    }

    public void setRGB(Color color) {
        for (int i = 0; i < models.size(); i++) {
            models.get(i).setRGB(color);
        }

    }

    public void setTime(float time) {
        for (Model model : models) {
            model.setTime(time);
        }
        this.time = time;
    }

    public void addModel(Model model) {
        models.add(model);
    }

    public void setPosOnShader(Point2D pos) {
        mousePos = new Point2D.Double(pos.getX(), pos.getY());
    }

    public void renderModels() {
        shader.bind();
        if (mousePos != null) {
            shader.setUniform("mousePos", (float) mousePos.getX(), (float) mousePos.getY());
        }
        shader.setUniform("curSpeed", speed);
        shader.setUniform("speedScale", speedScale);
        shader.setUniform("u_resolution", window.width, window.height);
        if (models.size() == 0) {
            return;
        }
        if (models.size() > 1) {
            for (int i = models.size() - 1; i >= 0; i--) {
                try {
                    shader.setUniform("time", models.get(i).getTime() + (models.size() - i) * 0.1f);
                    float redA = (float) models.get(i).color.getRed() / 255;
                    float greenA = (float) models.get(i).color.getGreen() / 255;
                    float blueA = (float) models.get(i).color.getBlue() / 255;
                    if (entity instanceof Enemy) {
                        if (((Enemy) entity).hunt) {
                            redA *= 3;

                        }
                    }
                    shader.setUniform("rgb", redA, greenA, blueA);
                    shader.setUniform("projection", models.get(i).getMovement().projection().scale(models.get(i).getScale()));
                    models.get(i).render();
                } catch (Exception e) {
                    continue;
                }
            }
        } else {
            shader.setUniform("time", models.get(0).getTime());
            float redA = (float) models.get(0).color.getRed() / 255;
            float greenA = (float) models.get(0).color.getGreen() / 255;
            float blueA = (float) models.get(0).color.getBlue() / 255;
            if (entity instanceof Enemy) {
                if (((Enemy) entity).hunt) {
                    redA *= 3;

                }
            }
            shader.setUniform("rgb", redA, greenA, blueA);
            shader.setUniform("projection", models.get(0).getMovement().projection().scale(models.get(0).getScale()));
            models.get(0).render();
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

    public void clear(boolean shader) {
        for (int i = 0; i < models.size(); i++) {
            models.get(i).clean();
        }
//        if(shader) {
//            this.shader.clean();
//        }
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
            float x = (float) (Math.random() * trest.half * 2 - trest.half);
            float y = (float) (Math.random() * trest.half * 2 - trest.half);
            models.get(i).getMovement().setPosition(new Vector3f(x, y, 0));
        }
    }

    public static void removeModelRender(ModelRendering mr) {
        selfList.remove(mr);

    }

    private float speedScale = 0;

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    private float speed = 0;

    public void setSpeedScale(float scale) {
        speedScale = scale;
    }
}
