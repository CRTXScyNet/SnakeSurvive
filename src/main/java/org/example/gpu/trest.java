package org.example.gpu;

import org.example.Enemy.Enemy;
import org.example.Painter.Apple;
import org.example.Painter.Process;
import org.example.Player.Player;
import org.example.gpu.render.Model;
import org.example.gpu.render.ModelRendering;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.awt.*;
import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;


public class trest {
    public static boolean mouseControl = false;
    static int width = 500;
    static int height = 500;
    public static int xMouse;
    public static int yMouse;
    public static ArrayList<ModelRendering> background = new ArrayList<>();
    public static ArrayList<ModelRendering> background2 = new ArrayList<>();
    public static ArrayList<ModelRendering> background3 = new ArrayList<>();
    public static float half;
    public static boolean reset = false;
    private ModelRendering foreground;

    public trest() {

        xMouse = 0;
        yMouse = 0;
        if (!glfwInit()) {
            throw new IllegalStateException("Failed to init");
        }
        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        assert vidMode != null;
        width = (int) (vidMode.width() * 0.9);
        height = (int) (vidMode.height() * 0.9);
//        width = 500;
//                height = 500;
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);


        Window window = new Window(width, height);

        window.setFullscreen(true);

        window.createWindow("what'sup");
        half = (float) window.width / 1.5f;
        GL.createCapabilities();

        glEnable(GL_TEXTURE_2D);


        for (int j = 0; j < 220; j++) {
            Color color = new Color((int) (Math.random() * 100), (int) (Math.random() * 255), (int) (Math.random() * 255));
            background.add(new ModelRendering(window, color, false, null,"background"));
            background.get(j).addModel(new Model(window, (int) (Math.random() * 30 + 20)));
        }
        for (int j = 0; j < 200; j++) {
            Color color = new Color((int) (Math.random() * 100), (int) (Math.random() * 255), (int) (Math.random() * 255));
            background2.add(new ModelRendering(window, color, false, null,"background"));
            background2.get(j).addModel(new Model(window, (int) (Math.random() * 40 + 30)));
        }
        for (int j = 0; j < 150; j++) {
            Color color = new Color((int) (Math.random() * 100), (int) (Math.random() * 255), (int) (Math.random() * 255));
            background3.add(new ModelRendering(window, color, false, null,"background"));
            background3.get(j).addModel(new Model(window, (int) (Math.random() * 60 + 40)));
        }

        for (int i = 0; i < ModelRendering.selfList.size(); i++) {
            ModelRendering.selfList.get(i).randomPosition();
        }

        new Process(window);

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE);
        double frame_cap = 1.0 / 60;

        double frameTime = 0;
        int frames = 0;
        double time = Timer.getTime();
        double unprocessed = 0;
        float uTime = Timer.getFloatTime();


        while (!window.shouldClose()) {

            Point mouse = MouseInfo.getPointerInfo().getLocation();

            mouse.x = mouse.x - window.windowPosX - window.width / 2;
            mouse.y = (mouse.y - window.windowPosY - window.height / 2);
//            System.out.println(mouse);
            xMouse = mouse.x;
            yMouse = -mouse.y;
            input(window);

//            if (glfwGetKey(window.getWindow(), GLFW_KEY_SPACE) != 0) {
//                mouseControl = !mouseControl;
//            }

            boolean canRender = false;
            double time2 = Timer.getTime();
            double passed = time2 - time;
            unprocessed += passed;
            frameTime += passed;
            time = time2;
            float uTime2 = Timer.getFloatTime() - uTime;
            window.update();
            while (unprocessed >= frame_cap) {
                canRender = true;
                unprocessed -= frame_cap;
//
//                for (int i = 0; i < ModelRendering.selfList.size(); i++) {
//                    ModelRendering.selfList.get(i).update(window);
//                }


                if (frameTime >= 1) {
                    if(!Process.isGo){

                    }
                    frameTime = 0;
                    System.out.println("FPS: " + frames);
                    frames = 0;
                }
            }
//            if (Process.ringIsReady) {
//                Process.ringIsReady = false;
//                ModelRendering sfdg = new ModelRendering(window, Color.GREEN, false, null);
//                for (int i = 0; i < Process.targets1.size(); i++) {
//                    sfdg.addModel(new Model(window, 50));
//                    sfdg.getModels().get(i).getMovement()
//                            .setPosition(new Vector3f(Process.targets1.get(i).x, Process.targets1.get(i).y, 0));
//                }


//            }

            if (canRender) {
                glClear(GL_COLOR_BUFFER_BIT);

                if (Process.reset) {
                    reset();
                    reset = false;
                }

                for (int i = 0; i < Enemy.enemies.size(); i++) {
                    if (Enemy.enemies.get(i).isGrow) {
                        Enemy.enemies.get(i).grow();
                        Enemy.enemies.get(i).isGrow = false;
                    }
                }
                if (Player.players.size() > 0) {
                    if (Player.players.get(0).isGrow) {
                        Player.players.get(0).grow();
                        Player.players.get(0).isGrow = false;
                    }
                    if (Player.players.get(0).isShorter) {
                        Player.players.get(0).minusCell();
//                        Player.players.get(0).setPhantomXY();
                        Player.players.get(0).isShorter = false;
                    }
                }

                moveBackground();

                for (int i = 0; i < ModelRendering.selfList.size(); i++) {
                    ModelRendering.selfList.get(i).renderModels(uTime2);
                }


                window.swapBuffer();
                frames++;

            }
            Process.ready = true;
        }
//        for(ModelRendering rendering : ModelRendering.selfList){
//            rendering.clear();
//        }

        glfwTerminate();
        System.exit(0);
    }

    public void input(Window window) {
        if (glfwGetKey(window.getWindow(), GLFW_KEY_ESCAPE) != 0) {
            glfwSetWindowShouldClose(window.getWindow(), true);
        }
        if (window.getInput().isMouseButtonPressed(GLFW_MOUSE_BUTTON_1)) {
            mouseControl = !mouseControl;
        }
        if (window.getInput().isKeyPressed(GLFW_KEY_P)) {
            Process.isPaused = !Process.isPaused;
        }
        if (window.getInput().isKeyPressed(GLFW_KEY_R)) {
            reset = true;
            System.out.println("restart");
        }

    }

    public void moveBackground() {
        for (ModelRendering rendering : background3) {
            float x = rendering.getModels().get(0).getMovement().getPosition().x;
            float y = rendering.getModels().get(0).getMovement().getPosition().y;

            if (rendering.getModels().get(0).getMovement().getPosition().x < -half) {
                rendering.getModels().get(0).getMovement().setPosition(new Vector3f(half, y, 0));
            }
            if (rendering.getModels().get(0).getMovement().getPosition().x > half) {
                rendering.getModels().get(0).getMovement().setPosition(new Vector3f(-half, y, 0));
            }
            if (rendering.getModels().get(0).getMovement().getPosition().y < -half) {
                rendering.getModels().get(0).getMovement().setPosition(new Vector3f(x, half, 0));
            }
            if (rendering.getModels().get(0).getMovement().getPosition().y > half) {
                rendering.getModels().get(0).getMovement().setPosition(new Vector3f(x, -half, 0));
            }
        }
        for (ModelRendering rendering : background2) {
            float x = rendering.getModels().get(0).getMovement().getPosition().x;
            float y = rendering.getModels().get(0).getMovement().getPosition().y;

            if (rendering.getModels().get(0).getMovement().getPosition().x < -half) {
                rendering.getModels().get(0).getMovement().setPosition(new Vector3f(half, y, 0));
            }
            if (rendering.getModels().get(0).getMovement().getPosition().x > half) {
                rendering.getModels().get(0).getMovement().setPosition(new Vector3f(-half, y, 0));
            }
            if (rendering.getModels().get(0).getMovement().getPosition().y < -half) {
                rendering.getModels().get(0).getMovement().setPosition(new Vector3f(x, half, 0));
            }
            if (rendering.getModels().get(0).getMovement().getPosition().y > half) {
                rendering.getModels().get(0).getMovement().setPosition(new Vector3f(x, -half, 0));
            }
        }
    }

    public static void reset() {

        //Здесь добавить списки
        for (Enemy snake : Enemy.enemies) {
            snake.reset();
        }
        Apple.apple.reset();
        Player.players.get(0).reset();
//        Apple.reset();
    }
}
