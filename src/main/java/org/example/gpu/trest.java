package org.example.gpu;

import org.example.Buffs.BuffParent;
import org.example.Buffs.Fear;
import org.example.Buffs.Speed;
import org.example.Enemy.Enemy;
import org.example.Painter.Apple;
import org.example.Painter.Process;
import org.example.Player.Player;
import org.example.gpu.render.Model;
import org.example.gpu.render.ModelRendering;
import org.example.obstructions.WormHole;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

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
    static boolean isPaused = false;
    public static float mainTime = 0;
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

        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);


        Window window = new Window(width, height);

//        window.setFullscreen(true);

        window.createWindow("what'sup");
        half = (float) window.width / 1.5f;
        GL.createCapabilities();

        glEnable(GL_TEXTURE_2D);


        for (int j = 0; j < 220; j++) {
            Color color = new Color((int) (Math.random() * 100), (int) (Math.random() * 255), (int) (Math.random() * 255));
            background.add(new ModelRendering(window, color, false, null, "background"));
            background.get(j).addModel(new Model(window, (int) (Math.random() * 30 + 20)));
        }
        for (int j = 0; j < 200; j++) {
            Color color = new Color((int) (Math.random() * 100), (int) (Math.random() * 255), (int) (Math.random() * 255));
            background2.add(new ModelRendering(window, color, false, null, "background"));
            background2.get(j).addModel(new Model(window, (int) (Math.random() * 40 + 30)));
        }
        for (int j = 0; j < 150; j++) {
            Color color = new Color((int) (Math.random() * 100), (int) (Math.random() * 255), (int) (Math.random() * 255));
            background3.add(new ModelRendering(window, color, false, null, "background"));
            background3.get(j).addModel(new Model(window, (int) (Math.random() * 60 + 40)));
        }

        for (int i = 0; i < ModelRendering.selfList.size(); i++) {
            ModelRendering.selfList.get(i).randomPosition();
        }

        new Process(window);
        Process.buffs.add(new Fear(window));
        for (int i = 0; i < 50; i++) {
            Process.buffs.add(new Speed(window));
        }

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE);
        double frame_cap = 1.0 / 60;

        double frameTime = 0;
        int frames = 0;
        double time = Timer.getTime();
        double unprocessed = 0;
        float randomBuffs = 0;
        float holesTime = 0;


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
            randomBuffs += (float) time2 - (float) time;
            holesTime += (float) time2 - (float) time;
            if (!Process.isPaused) {
                mainTime += (float) time2 - (float) time;
            }
            time = time2;

            if (!Process.isPaused) {

                if (randomBuffs >= 1) {
                    randomBuffs = 0;
                    addBuffs();
                }
                if (holesTime >= 1) {
                    addSomeHoles(window);
                    holesTime = 0;
                }
            }
            window.update();
            while (unprocessed >= frame_cap) {
                canRender = true;
                unprocessed -= frame_cap;
//
//                for (int i = 0; i < ModelRendering.selfList.size(); i++) {
//                    ModelRendering.selfList.get(i).update(window);
//                }


                if (frameTime >= 1) {

                    frameTime = 0;
//                    System.out.println("FPS: " + frames);
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
                    reset(window);
                    reset = false;
                }

                // Place for process
                someActions(window);
                moveBackground();


                for (int i = 0; i < ModelRendering.selfList.size(); i++) {
                    ModelRendering.selfList.get(i).renderModels();
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

    public void someActions(Window window) {
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
        if (Process.enemyEaten && !Process.isGo) {
            for (Enemy enemy : Process.absorbedEnemies) {
                enemy.enemyAbsorbed();
            }
            Process.absorbedEnemies.clear();
            if (Player.absorb) {
                Player.players.get(0).addAbsorbedSnake();
                Player.absorbArray.clear();
                Player.absorb = false;
            }
            Process.enemyEaten = false;
        }
    }

    public void input(Window window) {
        if (glfwGetKey(window.getWindow(), GLFW_KEY_ESCAPE) != 0) {
            glfwSetWindowShouldClose(window.getWindow(), true);
        }
        if (window.getInput().isMouseButtonPressed(GLFW_MOUSE_BUTTON_1)) {
            if (!Process.isEnd) {
                mouseControl = !mouseControl;
            }
        }
        if (window.getInput().isKeyPressed(GLFW_KEY_P)) {
            isPaused = isPaused;
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

    public void addBuffs() {

        for (BuffParent parent : Process.buffs) {
            if (parent.isExist()) {
                continue;
            } else {
                if (parent.getChance() > Math.random()) {
                    parent.addSome();
                }
            }
        }

    }

    static double chance = 0.01;

    public void addSomeHoles(Window window) {
        if (Process.holes.size() <= 20) {
            chance += 0.001;
            if (Math.random() < chance) {
                Process.holes.add(new WormHole(window));

            }
        }

    }

    public static float getMainTime() {
        return mainTime;
    }


    public static void reset(Window window) {

        //Здесь добавить списки
        for (Enemy snake : Enemy.enemies) {
            snake.reset();
        }
        for (int i = 0; i < Enemy.amountOfAbsorbedEnemies; i++) {
            new Enemy(window, false);
        }
        Enemy.amountOfAbsorbedEnemies = 0;
        for (int i = 0; i < Process.holes.size(); i++) {
            Process.holes.get(i).reset();
        }
        Process.holes.clear();
        chance = 0.1;
        Apple.apple.reset();
        Player.players.get(0).reset();
        for (BuffParent buffs : Process.buffs) {
            buffs.reset();
        }
//        Apple.reset();
    }

//    public void process() {
//        {
//            try {
//                if (trest.reset) {
//                    if (!reset) {
//                        reset = true;
//                    }
//                    continue;
//
//                } else if (reset) {
//                    reset = false;
//                    isPaused = false;
//                    isEnd = false;
//                    trest.mouseControl = false;
//                }
//                if (isPaused) {
//                    continue;
//                }
//
////                pointsToErase.clear();
//
//                while (enemyEaten) {
//                    try {
//                        TimeUnit.MILLISECONDS.sleep(1);
//                    } catch (Exception e) {
//
//                    }
//                    if (isGo) {
//                        isGo = false;
//                    }
//                }
//                isGo = true;
//                for (BuffParent parent : buffs) {
//                    if (parent.isExist()) {
//                        try {
//                            parent.update();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//                for (int i = 0; i < holes.size(); i++) {
//
//                    holes.get(i).update();
//                }
//
//                if (trest.mouseControl) {
//
//                    if (player.getDirection() != null) {
//                        screenMove = true;
//                        direction = player.getDirection();
//                    }
//                    for (BuffParent parent : buffs) {
//                        if (parent.isExist()) {
//                            if (parent.getXy().getX() < (outLeft)) {
//                                parent.moveXy(new float[]{fromLeft, 0});
//
//                            } else if (parent.getXy().getX() > (outRight)) {
//                                parent.moveXy(new float[]{fromRight, 0});
//
//                            }
//                            if (parent.getXy().getY() < (outUp)) {
//                                parent.moveXy(new float[]{0, fromUp});
//
//                            } else if (parent.getXy().getY() > (outDown)) {
//                                parent.moveXy(new float[]{0, fromDown});
//
//                            }
//                            parent.moveXy(direction);
//                        }
//                    }
//
//                    if (screenMove) {
//                        for (int i = 0; i < holes.size(); i++) {
//
//
//                            if (holes.get(i).getXy()[0] < (outLeft)) {
//                                holes.get(i).moveXy(new float[]{fromLeft, 0});
//
//                            } else if (holes.get(i).getXy()[0] > (outRight)) {
//                                holes.get(i).moveXy(new float[]{fromRight, 0});
//
//                            }
//                            if (holes.get(i).getXy()[1] < (outUp)) {
//                                holes.get(i).moveXy(new float[]{0, fromUp});
//
//                            } else if (holes.get(i).getXy()[1] > (outDown)) {
//                                holes.get(i).moveXy(new float[]{0, fromDown});
//
//                            }
//                            holes.get(i).moveXy(direction);
//                        }
//
//                        for (ModelRendering model : trest.background) {
//                            model.getModels().get(0).getMovement().addPosition(new Vector3f(-direction[0] / 50, -direction[1] / 50, 0));
//
//                        }
//                        for (ModelRendering model : trest.background2) {
//                            model.getModels().get(0).getMovement().addPosition(new Vector3f(-direction[0] / 10, -direction[1] / 10, 0));
//
//                        }
//                        for (ModelRendering model : trest.background3) {
//                            model.getModels().get(0).getMovement().addPosition(new Vector3f(-direction[0] / 5, -direction[1] / 5, 0));
//                        }
//                        for (Enemy enemy : Enemy.enemies) {
//                            enemy.moveXy(direction);
//                        }
//                        screenMove = true;
//                        apple.moveXy(direction);
//                        player.moveXy(direction);
//
//
//                    }
//                }
//                for (Enemy enemy : Enemy.enemies) {
//                    if (enemy.getXy().size() == 0) {
//                        continue;
//                    }
//                    if (enemy.getXy().get(0)[0] < (outLeft)) {
//                        enemy.moveXy(new float[]{fromLeft, 0});
//                        enemy.reverse();
//                    } else if (enemy.getXy().get(0)[0] > (outRight)) {
//                        enemy.moveXy(new float[]{fromRight, 0});
//                        enemy.reverse();
//                    }
//                    if (enemy.getXy().get(0)[1] < (outUp)) {
//                        enemy.moveXy(new float[]{0, fromUp});
//                        enemy.reverse();
//                    } else if (enemy.getXy().get(0)[1] > (outDown)) {
//                        enemy.moveXy(new float[]{0, fromDown});
//                        enemy.reverse();
//                    }
//
//
//                }
//                if (Apple.getXy()[0] < (outLeft)) {
//                    apple.moveXy(new float[]{fromLeft, 0});
//
//                } else if (Apple.getXy()[0] > (outRight)) {
//                    apple.moveXy(new float[]{fromRight, 0});
//                }
//                if (Apple.getXy()[1] < (outUp)) {
//                    apple.moveXy(new float[]{0, fromUp});
//
//                } else if (Apple.getXy()[1] > (outDown)) {
//                    apple.moveXy(new float[]{0, fromDown});
//                }
//                apple.setTime(trest.getMainTime());
//                if (!isEnd) {
//                    player.setTime(trest.getMainTime());
//                }
//                float[] p = player.getXy().get(0);
//                if (appleSpawned && !appleVisible) {
//                    if (eatenTimelast > 0) {
//                        eatenTimelast -= (trest.getMainTime() - eatenTime);
//                        apple.setTime(-eatenTimelast);
//                        eatenTime = trest.getMainTime();
//
//                    } else {
//                        appleVisible = true;
////                                    appleSpawned = false;
//                    }
//                }
//                if (appleVisible) {
//                    appleDistance = (float) Math.sqrt(Math.pow(Math.abs(a[0]), 2) + Math.pow(Math.abs(a[1]), 2));
//
//
//                    if (Math.pow(Math.abs(p[0] - a[0]), 2) + Math.pow(Math.abs(p[1] - a[1]), 2) <= collisionWithApple) {       //Проверка колизии змеи и яблока
//                        appleSpawned = false;
//                        appleVisible = false;
//                        player.setDelay();
//                        player.minusCell();
//                        player.increaseSpeed();
////                                player.setPhantomXY();
////                                Picture.countOfApples += 1;
//                        eaten = true;
//                    } else {
//                        for (Enemy enemy : Enemy.activeEnemies) {
//                            if (enemy.getXy().size() == 0) {
//                                continue;
//                            }
//                            if (Math.pow(Math.abs(enemy.getXy().get(0)[0] - a[0]), 2) + Math.pow(Math.abs(enemy.getXy().get(0)[1] - a[1]), 2) <= collisionWithApple) {       //Проверка колизии змеи и яблока
//
//                                appleSpawned = false;
//                                appleVisible = false;
////                                enemy.setDelay();
//                                enemy.isGrow = true;
//                                enemy.setEatAndAngry(true);
//                                enemy.setCurrentDelay((int) (enemy.getDelay() / 2));
//                                eaten = true;
//                            }
//                        }
//                    }
//
//                    if (eaten) {
//                        eatenTime = trest.getMainTime();
//                        eatenDelay = 0;
//                        a = Apple.getXy();
////                            player.grow();
//                    }
//
//                }
//                a = apple.getXy();
//                if (eatenDelay < eatenDelayStat) {
//                    eatenDelay++;
//                }
//                if (eaten) {
//                    if (eatenTimelast > 0) {
//                        eatenTimelast += trest.getMainTime() - eatenTime;
//                        apple.setTime(-eatenTimelast);
//                        eatenTime = trest.getMainTime();
//                    } else {
//                        eatenTimelast = trest.getMainTime() - eatenTime;
//                        apple.setTime(-eatenTimelast);
//                    }
//                    if (eatenDelay >= eatenDelayStat) {
//                        apple.setXy();
//
//                        a = apple.getXy();
//
//
//                        eaten = false;
//                    }
//                }
//                if (eatenDelay >= eatenDelayStat) {
//
//                    eatenTime = trest.getMainTime();
//                    appleSpawned = true;
////                                appleVisible = true;
//
//
//                }
//
//
//                for (Enemy enemy : Enemy.enemies) {
//                    if (enemy.getXy().size() == 0) {
//                        continue;
//                    }
//                    boolean isNearby = false;
//                    boolean appleIsNearby = false;
//                    float[] nearPoint = new float[2];
//                    int rad;
//                    rad = 300;
//                    float length = 0;
//                    nearPoint = new float[2];
//                    float appleDest = (float) Math.sqrt(Math.pow(Apple.getXy()[0] - enemy.getXy().get(0)[0], 2) + Math.pow(Apple.getXy()[1] - enemy.getXy().get(0)[1], 2));
//                    if (appleDest <= rad) {
//                        appleIsNearby = true;
//                    }
//                    try {
//                        boolean isBreak = false;
//                        for (float[] ePoint : enemy.getPhantomXY()) {
//                            for (int i = 0; i < player.getXy().size(); i++) {
//                                float dest = (float) Math.sqrt(Math.pow(player.getXy().get(i)[0] - ePoint[0], 2) + Math.pow(player.getXy().get(i)[1] - ePoint[1], 2));
//
//                                if (dest <= Enemy.getSize() + Player.getSize() - 1) {
//                                    if (!enemy.isActive /*&& player.getXy().size() < Player.maxSize*/) {
//                                        Player.absorbArray.addAll(enemy.getXy());
//                                        Player.absorb = true;
//                                        absorbedEnemies.add(enemy);
//                                        enemyEaten = true;
//                                        isBreak = true;
//                                        break;
//                                    } else {
//                                        if (!isEnd) {
//                                            eatenPlayerTime = trest.getMainTime();
//                                            isEnd = true;                         // TODO
//                                            trest.mouseControl = false;
//                                            for (Enemy enemy1 : Enemy.enemies) {
//                                                enemy1.setCurrentDelay(0);
//                                            }
//                                            isBreak = true;
//                                            break;
//                                        }
//                                    }
//                                }
//                                if (dest <= rad) {
//                                    if (length == 0) {
//                                        length = dest;
//                                        nearPoint = new float[]{player.getXy().get(i)[0], player.getXy().get(i)[1]};
//                                    } else if (length > dest) {
//                                        length = dest;
//                                        nearPoint = new float[]{player.getXy().get(i)[0], player.getXy().get(i)[1]};
//                                    }
//                                    isNearby = true;
//                                }
//                            }
//                            if (isBreak) {
//                                break;
//                            }
//
//                        }
//
//                        enemy.unFear();
//                        if (isNearby && enemyScared) {
//                            enemy.fear();
//                            nearPoint = new float[]{enemy.getXy().get(0)[0] * 2, enemy.getXy().get(0)[1] * 2};
//                            enemy.moveCheck(nearPoint, appleIsNearby, true);
//                        } else if (enemy.isEatAndAngry() || isEnd) {
//                            enemy.moveCheck(player.getXy().get(0), appleIsNearby, true);
//
//                        } else if (isNearby) {
//
//                            enemy.moveCheck(nearPoint, appleIsNearby, true);
//
//                        } else {
//                            enemy.moveCheck(new float[2], appleIsNearby, false);
//                        }
//
//                    } catch (Exception e) {
//
//                    }
//                }
//                if (isEnd) {
//                    eatenPlayerTimelast = trest.getMainTime() - eatenPlayerTime;
//                    player.setTime(-eatenPlayerTimelast);
//                }
//                player.moveCheck();
//
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
