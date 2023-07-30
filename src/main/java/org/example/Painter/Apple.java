package org.example.Painter;

import org.example.Player.Player;
import org.example.Sound.LWJGLSound;
import org.example.gpu.render.Window;
import org.example.gpu.render.Model;
import org.example.gpu.render.ModelRendering;
import org.example.gpu.gameProcess.trest;
import org.joml.Vector3f;

import java.awt.*;
import java.awt.geom.Point2D;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Apple {

    static Color color = new Color(Color.RED.getRGB());
    private int width;
    private int height;
    private static int size = 10;
    private int eatenDelayStat = 50;
    private int eatenDelay = eatenDelayStat;
    private static float[] xy = new float[]{};



    private  static Point2D point2D = new Point2D.Float();
    public static float eatenTime = 0;
    public static float eatenTimelast = 0;
    static double collisionWithApple = Math.pow(Apple.getAppleSize() * 2, 2);
    public static Apple apple;

    private Window window;
    public static boolean appleSpawned = false;
    public static boolean appleVisible = false;

    public static boolean eaten = false;

    private ModelRendering rendering;
    private ModelRendering renderingPoiner;

    public Apple(Window window) {
        apple = this;
        this.window = window;
        xy = new float[]{(int) (Math.random() * trest.playGroundWidth / 2 - (trest.playGroundWidth / 4)), (int) (Math.random() * trest.playGroundHeight / 2 - (trest.playGroundHeight / 4))};
//        System.out.printf("Apple x: %s, y: %s ", xy[0],xy[1]);
        rendering = new ModelRendering(window, null, "apple");
        rendering.addModel(new Model(window, (int) (size * 50), color));
        rendering.getModels().get(0).getMovement().setPosition(new Vector3f((float) xy[0], (float) xy[1], 0));
        renderingPoiner = new ModelRendering(window, null, "applePointer");
        renderingPoiner.addModel(new Model(window, 30, color));
        point2D.setLocation(xy[0],xy[1]);
        soundInit();
    }

    public static boolean checkCollision(float[] xy) {
        if (!eaten && Math.pow(Math.abs(xy[0] - Apple.xy[0]), 2) + Math.pow(Math.abs(xy[1] - Apple.xy[1]), 2) <= collisionWithApple) {
            int d = (int)Math.round(Math.random()*(ac-1));
            eatSound.get(d).play();
            eaten = true;
            return true;
        } else {
            return false;
        }

    }

    public void update() {
        for(LWJGLSound sound : eatSound){
            sound.update(xy[0],xy[1]);
        }
        if (appleSpawned && !appleVisible) {
            if (eatenTimelast > 0) {
                eatenTimelast -= (trest.mainTime - eatenTime);
                setTime(-eatenTimelast);
                eatenTime = trest.mainTime;

            } else {
                appleVisible = true;
//                                    appleSpawned = false;
            }
        }
        if (eatenDelay < eatenDelayStat) {
            eatenDelay++;
        }

        if (eaten && appleVisible) {

            appleSpawned = false;
            appleVisible = false;
            eatenTime = trest.mainTime;
            eatenDelay = 0;
        }

        if (eaten) {
            if (eatenTimelast > 0) {
                eatenTimelast += trest.mainTime - eatenTime;
                apple.setTime(-eatenTimelast);
                eatenTime = trest.mainTime;
            } else {
                eatenTimelast = trest.mainTime - eatenTime;
                apple.setTime(-eatenTimelast);
            }
            if (eatenDelay >= eatenDelayStat) {
                apple.setXy();
                eaten = false;
            }
        }
        if (eatenDelay >= eatenDelayStat) {
            eatenTime = trest.mainTime;
            appleSpawned = true;
        }
        if (appleVisible) {
            apple.setTime(trest.mainTime);
        }
    }

    private float pointerTime = 0;

    public void setTime(float time) {
        rendering.setTime(time);
        if (appleVisible) {
            float buf = (float) (1 - (Player.playerHeadXY().distance(xy[0], xy[1]) / (window.width)));
            if (Player.playerHeadXY().distance(xy[0], xy[1]) >= window.width) {
                pointerTime += 0.01;
            } else {
                if (buf < 0.8) {
                    pointerTime += 0.01;
                } else if (buf > 0.9) {
                    pointerTime += 0.05;
                } else {
                    pointerTime += (buf-0.8)/2;
                }
            }
            renderingPoiner.setTime(pointerTime/*(float)(Player.playerHeadXY().distance(xy[0],xy[1])/(window.width*3))*/);
        } else {
            renderingPoiner.setTime(time);
        }
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
        point2D.setLocation(xy[0],xy[1]);

    }

    public void setXy() {
        try {
            int x = (int) (Math.random() * trest.playGroundWidth - (trest.playGroundWidth / 2));
            int y = (int) (Math.random() * trest.playGroundHeight - (trest.playGroundHeight / 2));
            xy = new float[]{x, y};
            rendering.getModels().get(0).getMovement().setPosition(new Vector3f(x, y, 0));
            point2D.setLocation(xy[0],xy[1]);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
    }
    private static ArrayList<LWJGLSound> eatSound = new ArrayList<>();
    private static int ac = 0;
    public void soundInit(){
        Path path = Path.of("./sounds/apple");
        ac = 0;
        try(DirectoryStream<Path> files = Files.newDirectoryStream(path)){
            for(Path file : files){
                if(Files.isRegularFile(file)) {
                    if(file.getFileName().toString().contains(".ogg")) {
                        ac++;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println(ac);
        for (int i = 0; i < ac; i++) {
            eatSound.add(new LWJGLSound(path.toString()+"/sample_"+i+".ogg",false));
            eatSound.get(i).setCastomVolume(1f);
        }
    }

    public void reset() {
        try {
            appleSpawned = true;
            appleVisible = false;
            eaten = false;

            int x = (int) (Math.random() * trest.playGroundWidth / 2 - (trest.playGroundWidth / 4));
            int y = (int) (Math.random() * trest.playGroundHeight / 2 - (trest.playGroundHeight / 4));
            xy = new float[]{x, y};
            rendering.getModels().get(0).getMovement().setPosition(new Vector3f(x, y, 0));
            point2D.setLocation(xy[0],xy[1]);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }
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
    public static Point2D getPoint2D() {
        return point2D;
    }


}
