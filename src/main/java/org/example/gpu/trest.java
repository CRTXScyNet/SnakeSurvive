package org.example.gpu;

import org.example.Buffs.BuffParent;
import org.example.Buffs.CutTheTail;
import org.example.Buffs.Fear;
import org.example.Buffs.Speed;
import org.example.Enemy.Enemy;
import org.example.Painter.Apple;
import org.example.Player.Player;
import org.example.Player.PlayerPart;
import org.example.gpu.io.Movement;
import org.example.gpu.render.Model;
import org.example.gpu.render.ModelRendering;
import org.example.obstructions.WormHole;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;


public class trest {

    public static boolean mouseControl = false;
    static int width = 500;
    static int height = 500;
    public static int xMouse;
    public static int yMouse;
    public static int countOfApples = 0;
    public static ArrayList<ModelRendering> background = new ArrayList<>();
    public static ArrayList<ModelRendering> background2 = new ArrayList<>();
    public static ArrayList<ModelRendering> background3 = new ArrayList<>();
    public static ArrayList<BuffParent> buffs = new ArrayList<>();
    public static ArrayList<WormHole> holes = new ArrayList<>();
    public static HashMap<Point, Point> toTargets = new HashMap<>();
    public static ArrayList<Enemy> absorbedEnemies = new ArrayList<>();

    public static float half;
    public static boolean reset = false;
    static boolean isPaused = false;
    public static boolean isEnd = false;
    public static boolean enemyEaten = false;
    public static boolean eaten = false;
    static boolean screenMove = false;
    public static boolean enemyScared = false;
    static boolean appleSpawned = false;
    public static boolean appleVisible = false;
    public static boolean ringWayIsReady = false;

    public static float eatenTime = 0;
    public static float eatenTimelast = 0;
    public static float eatenPlayerTime = 0;
    public static float eatenPlayerTimelast = 0;
    public static float mainTime = 0;
    static float[] direction = new float[2];
    public static float appleDistance = 0;
    float[] a = new float[2];
    static double collisionWithApple = Math.pow(Apple.getAppleSize() + Player.getSize(), 2);
    static double radius1;
    private int outLeft;
    private int fromLeft;

    private int outRight;
    private int fromRight;

    private int outUp;
    private int fromUp;

    private int outDown;
    private int fromDown;
    private int eatenDelayStat = 50;
    private int eatenDelay = eatenDelayStat;
    private static int expansePart = 1;
    static public Apple apple;
    static public Player player;

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
            background.add(new ModelRendering(window,  false, null, "background"));
            background.get(j).addModel(new Model(window, (int) (Math.random() * 30 + 20),color));
        }
        for (int j = 0; j < 200; j++) {
            Color color = new Color((int) (Math.random() * 100), (int) (Math.random() * 255), (int) (Math.random() * 255));
            background2.add(new ModelRendering(window,  false, null, "background"));
            background2.get(j).addModel(new Model(window, (int) (Math.random() * 40 + 30),color));
        }
        for (int j = 0; j < 150; j++) {
            Color color = new Color((int) (Math.random() * 100), (int) (Math.random() * 255), (int) (Math.random() * 255));
            background3.add(new ModelRendering(window,  false, null, "background"));
            background3.get(j).addModel(new Model(window, (int) (Math.random() * 60 + 40),color));
        }

        for (int i = 0; i < ModelRendering.selfList.size(); i++) {
            ModelRendering.selfList.get(i).randomPosition();
        }

        processInit(window);


        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE);
        double frame_cap = 1.0 / 60;

        double frameTime = 0;
        int frames = 0;
        double time = Timer.getTime();
        double unprocessed = 0;
        float randomBuffs = 0;
        float holesTime = 0;
        float timeToSpawnParts = 0;
 expansePart = 1;

        while (!window.shouldClose()) {

            Point mouse = MouseInfo.getPointerInfo().getLocation();

            mouse.x = mouse.x - window.windowPosX - window.width / 2;
            mouse.y = (mouse.y - window.windowPosY - window.height / 2);
//            System.out.println(mouse);
            xMouse = mouse.x;
            yMouse = -mouse.y;
            input(window);
            boolean canRender = false;
            double time2 = Timer.getTime();
            double passed = time2 - time;
            unprocessed += passed;
            frameTime += passed;
            randomBuffs += (float) time2 - (float) time;
            holesTime += (float) time2 - (float) time;
            timeToSpawnParts += (float) time2 - (float) time;
            if (!isPaused) {
                mainTime += (float) time2 - (float) time;
            }
            time = time2;

            if (!isPaused) {
                if (randomBuffs >= 1) {
                    randomBuffs = 0;
                    addBuffs();
                }
                if (holesTime >= 1) {
                    addSomeHoles(window);
                    holesTime = 0;
                }
                if(!isEnd && timeToSpawnParts >= 1) {
                    int count = 0;
                    for(PlayerPart part : PlayerPart.playerParts){
                        count+=part.xy.size();
                    }
                    if(count<500&&PlayerPart.playerParts.size()<PlayerPart.maxAmountOfParts){
                        for (int i = 0; i < 20; i++) {
                            new PlayerPart(window);
                        }

                        expansePart *=2;
                    }

                    timeToSpawnParts = 0;

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
                    System.out.println("FPS: " + frames);
                    frames = 0;
                }
            }

            process();
            moveBackground();
            if (canRender) {
                glClear(GL_COLOR_BUFFER_BIT);

                for (int i = 0; i < ModelRendering.selfList.size(); i++) {
                    ModelRendering.selfList.get(i).renderModels();
                }

                window.swapBuffer();
                frames++;

            }

        }
//        for(ModelRendering rendering : ModelRendering.selfList){
//            rendering.clear();
//        }

        glfwTerminate();
        System.exit(0);
    }
public void addSomeEntity(Window window){

}

    public void input(Window window) {
        if (glfwGetKey(window.getWindow(), GLFW_KEY_ESCAPE) != 0) {
            glfwSetWindowShouldClose(window.getWindow(), true);
        }
        if (window.getInput().isMouseButtonPressed(GLFW_MOUSE_BUTTON_1)) {
            if (!isEnd&&!isPaused) {
                mouseControl = !mouseControl;
            }
        }
        if (window.getInput().isKeyPressed(GLFW_KEY_P)) {
            isPaused = !isPaused;
        }
        if (window.getInput().isKeyPressed(GLFW_KEY_E)) {
            isEnd = !isEnd;
        }

        if (window.getInput().isKeyPressed(GLFW_KEY_R)) {
            reset(window);
            isPaused = false;
            isEnd = false;
            mouseControl = false;
            System.out.println("restart");
        }
//        if (window.getInput().isKeyDown(GLFW_KEY_LEFT)) {
//            Movement.increaseRotation();
//        }
//        if (window.getInput().isKeyDown(GLFW_KEY_RIGHT)) {
//            Movement.decreaseRotation();
//        }
//        if (window.getInput().isKeyPressed(GLFW_KEY_SPACE)) {
//            System.out.println(Movement.getRotation());
//        }

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

        for (BuffParent parent : buffs) {
            if (parent.isExist()) {
                continue;
            } else {
                if (parent.getChance() > Math.random()) {
                    parent.addSome();
                }
            }
        }

    }

    static double chance = 0.001;

    public void addSomeHoles(Window window) {
        if (holes.size() <= 20) {
            chance += 0.001;
            if (Math.random() < chance) {
                holes.add(new WormHole(window));

            }
        }

    }

    public static float getMainTime() {
        return mainTime;
    }


    public static void reset(Window window) {
        expansePart = 1;
        countOfApples = 0;
        //Здесь добавить списки
        for (Enemy snake : Enemy.enemies) {
            snake.reset();
        }
        for (int i = 0; i < Enemy.amountOfAbsorbedEnemies; i++) {
            new Enemy(window, false);
        }
        Enemy.amountOfAbsorbedEnemies = 0;
        for (int i = 0; i < holes.size(); i++) {
            holes.get(i).reset();
        }
        holes.clear();
        chance = 0.1;
        apple.reset();
        player.reset();
        for (BuffParent buffs : buffs) {
            buffs.reset();
        }
        PlayerPart.clear();


    }

    public void processInit(Window window) {
        //Вычисляем границы территории
        width = window.width;
        height = window.height;
        outLeft = (int) (-width * 1.5) / Enemy.step * Enemy.step;
        fromLeft = (int) (-width * 3) / Enemy.step * Enemy.step;

        outRight = (int) (width * 1.5) / Enemy.step * Enemy.step;
        fromRight = (int) (width * 3) / Enemy.step * Enemy.step;

        outUp = (int) (-height * 1.5) / Enemy.step * Enemy.step;
        fromUp = (int) (-height * 3) / Enemy.step * Enemy.step;

        outDown = (int) (height * 1.5) / Enemy.step * Enemy.step;
        fromDown = (int) (height * 3) / Enemy.step * Enemy.step;

        // Создаем сущности
        apple = new Apple(window);
        player = new Player(window);

        for (int i = 0; i < 200; i++) {
            new Enemy(window, false);
        }
        for (int i = 0; i < 30; i++) {
            new Enemy(window, true);
        }
        buffs.add(new Fear(window));
        buffs.add(new CutTheTail(window));
        for (int i = 0; i < 50; i++) {
            buffs.add(new Speed(window));
        }

        //Вычисляем пути врагов после проигрыша
        radius1 = Math.pow(width / 4.5 - 2, 2);
        boolean closer;
        for (int x = -width / 4; x <= width / 4; x++) {
            for (int y = -width / 4; y <= width / 4; y++) {
                Point nearest = new Point();
                double dist = (Math.pow(x, 2)) + (Math.pow(y, 2));
                if (dist < radius1) {
                    nearest.setLocation(2 * x, 2 * y);
                } else {
                    nearest.setLocation(0, 0);
                }
                toTargets.put(new Point(x, y), new Point(nearest.x, nearest.y));
            }
        }
        ringWayIsReady = true;
    }

    public void process() {
        {
            try {
                if (isPaused) {
                    return;
                }

//                pointsToErase.clear();


                for (BuffParent parent : buffs) {
                    if (parent.isExist()) {
                        try {
                            parent.update();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                for (int i = 0; i < holes.size(); i++) {

                    holes.get(i).update();
                }

                moveEntity();

                apple.setTime(mainTime);
                if (!isEnd) {
                    player.setTime(mainTime);
                }
                float[] p = player.getXy().get(0);
                if (appleSpawned && !appleVisible) {
                    if (eatenTimelast > 0) {
                        eatenTimelast -= (mainTime - eatenTime);
                        apple.setTime(-eatenTimelast);
                        eatenTime = mainTime;

                    } else {
                        appleVisible = true;
//                                    appleSpawned = false;
                    }
                }
                if (appleVisible) {
                    appleDistance = (float) Math.sqrt(Math.pow(Math.abs(a[0]), 2) + Math.pow(Math.abs(a[1]), 2));


                    if (Math.pow(Math.abs(p[0] - a[0]), 2) + Math.pow(Math.abs(p[1] - a[1]), 2) <= collisionWithApple) {       //Проверка колизии змеи и яблока
                        appleSpawned = false;
                        appleVisible = false;
                        countOfApples++;
                        if(player.getXy().size()<countOfApples){
                            player.grow();
                        }
                        if(player.getXy().size()>countOfApples){
                            player.minusCell();
                        }

                        player.increaseSpeed();
//                                player.setPhantomXY();
//                                Picture.countOfApples += 1;
                        eaten = true;

                        player.setAppleCount();
                    } else {
                        for (Enemy enemy : Enemy.activeEnemies) {
                            if (enemy.getXy().size() == 0) {
                                continue;
                            }
                            if (Math.pow(Math.abs(enemy.getXy().get(0)[0] - a[0]), 2) + Math.pow(Math.abs(enemy.getXy().get(0)[1] - a[1]), 2) <= collisionWithApple) {       //Проверка колизии змеи и яблока

                                appleSpawned = false;
                                appleVisible = false;
//                                enemy.setDelay();
                                enemy.grow();
                                enemy.setEatAndAngry(true);
                                enemy.setCurrentDelay((int) (enemy.getDelay() / 2));
                                eaten = true;
                            }
                        }
                    }

                    if (eaten) {
                        eatenTime = mainTime;
                        eatenDelay = 0;
                        a = Apple.getXy();
//                            player.grow();
                    }

                }
                a = apple.getXy();
                if (eatenDelay < eatenDelayStat) {
                    eatenDelay++;
                }
                if (eaten) {
                    if (eatenTimelast > 0) {
                        eatenTimelast += mainTime - eatenTime;
                        apple.setTime(-eatenTimelast);
                        eatenTime = mainTime;
                    } else {
                        eatenTimelast = mainTime - eatenTime;
                        apple.setTime(-eatenTimelast);
                    }
                    if (eatenDelay >= eatenDelayStat) {
                        apple.setXy();
                        eaten = false;
                    }
                }
                if (eatenDelay >= eatenDelayStat) {

                    eatenTime = mainTime;
                    appleSpawned = true;
//                                appleVisible = true;


                }


                for (int i = 0; i < Enemy.enemies.size(); i++) {
                    if (Enemy.enemies.get(i).getXy().size() == 0) {
                        continue;
                    }
                    boolean isNearby = false;
                    boolean appleIsNearby = false;
                    float[] nearPoint = new float[2];
                    int rad;
                    rad = 300;
                    float length = 0;
                    nearPoint = new float[2];
                    float appleDest = (float) Math.sqrt(Math.pow(Apple.getXy()[0] - Enemy.enemies.get(i).getXy().get(0)[0], 2) + Math.pow(Apple.getXy()[1] - Enemy.enemies.get(i).getXy().get(0)[1], 2));
                    if (appleDest <= rad) {
                        appleIsNearby = true;
                    }
                    try {
                        boolean isBreak = false;
                        for (float[] ePoint : Enemy.enemies.get(i).getPhantomXY()) {
                            for (int j = 0; j < player.getXy().size(); j++) {
                                float dest = (float) Math.sqrt(Math.pow(player.getXy().get(j)[0] - ePoint[0], 2) + Math.pow(player.getXy().get(j)[1] - ePoint[1], 2));

                                if (dest <= Enemy.getSize() + Player.getSize() - 1) {
                                    if (!isEnd) {
                                        eatenPlayerTime = mainTime;
                                        isEnd = true;                         // TODO
                                        mouseControl = false;
                                        for (Enemy enemy1 : Enemy.enemies) {
                                            enemy1.setCurrentDelay(0);
                                        }
                                        isBreak = true;
                                        break;
                                    }
                                }
                                if (dest <= rad) {
                                    if (length == 0) {
                                        length = dest;
                                        nearPoint = new float[]{player.getXy().get(j)[0], player.getXy().get(j)[1]};
                                    } else if (length > dest) {
                                        length = dest;
                                        nearPoint = new float[]{player.getXy().get(j)[0], player.getXy().get(j)[1]};
                                    }
                                    isNearby = true;
                                }
                            }
                            if (isBreak) {
                                break;
                            }

                        }
                        if (isBreak) {
                            i--;
                            continue;
                        }
                        Enemy.enemies.get(i).unFear();
                        if (isNearby && enemyScared) {
                            Enemy.enemies.get(i).fear();
                            nearPoint = new float[]{Enemy.enemies.get(i).getXy().get(0)[0] * 2, Enemy.enemies.get(i).getXy().get(0)[1] * 2};
                            Enemy.enemies.get(i).moveCheck(nearPoint, appleIsNearby, true);
                        } else if (Enemy.enemies.get(i).isEatAndAngry() || isEnd) {
                            Enemy.enemies.get(i).moveCheck(player.getXy().get(0), appleIsNearby, true);

                        } else if (isNearby) {

                            Enemy.enemies.get(i).moveCheck(nearPoint, appleIsNearby, true);

                        } else {
                            Enemy.enemies.get(i).moveCheck(new float[2], appleIsNearby, false);
                        }

                    } catch (Exception e) {

                    }
                }
                if (isEnd) {
                    eatenPlayerTimelast = mainTime - eatenPlayerTime;
                    player.setTime(-eatenPlayerTimelast);
                }
                player.moveCheck();
                for (int i = 0; i < PlayerPart.playerParts.size(); i++) {
                    PlayerPart.playerParts.get(i).moveCheck();
                }
                PlayerPart.refresh();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void moveEntity() {
        if (player.getDirection() != null) {
            screenMove = true;
            direction = player.getDirection();
        }
        for (BuffParent parent : buffs) {
            if (parent.isExist()) {
                if (parent.getXy().getX() < (outLeft)) {
                    parent.moveXy(new float[]{fromLeft, 0});

                } else if (parent.getXy().getX() > (outRight)) {
                    parent.moveXy(new float[]{fromRight, 0});

                }
                if (parent.getXy().getY() < (outUp)) {
                    parent.moveXy(new float[]{0, fromUp});

                } else if (parent.getXy().getY() > (outDown)) {
                    parent.moveXy(new float[]{0, fromDown});

                }
                parent.moveXy(direction);
            }
        }
        for (int j = 0; j < PlayerPart.playerParts.size(); j++) {



                if (PlayerPart.playerParts.get(j).getXy().get(0)[0] < (outLeft)) {
                    PlayerPart.playerParts.get(j).moveXy(new float[]{fromLeft, 0});

                } else if (PlayerPart.playerParts.get(j).getXy().get(0)[0] > (outRight)) {
                    PlayerPart.playerParts.get(j).moveXy(new float[]{fromRight, 0});

                }
                if (PlayerPart.playerParts.get(j).getXy().get(0)[1] < (outUp)) {
                    PlayerPart.playerParts.get(j).moveXy(new float[]{0, fromUp});

                } else if (PlayerPart.playerParts.get(j).getXy().get(0)[1] > (outDown)) {
                    PlayerPart.playerParts.get(j).moveXy(new float[]{0, fromDown});

                }
            PlayerPart.playerParts.get(j).moveXy((direction));

        }


        for (int i = 0; i < holes.size(); i++) {


            if (holes.get(i).getXy()[0] < (outLeft)) {
                holes.get(i).moveXy(new float[]{fromLeft, 0});

            } else if (holes.get(i).getXy()[0] > (outRight)) {
                holes.get(i).moveXy(new float[]{fromRight, 0});

            }
            if (holes.get(i).getXy()[1] < (outUp)) {
                holes.get(i).moveXy(new float[]{0, fromUp});

            } else if (holes.get(i).getXy()[1] > (outDown)) {
                holes.get(i).moveXy(new float[]{0, fromDown});

            }
            holes.get(i).moveXy(direction);
        }

        for (ModelRendering model : background) {
            model.getModels().get(0).getMovement().addPosition(new Vector3f(-direction[0] / 50, -direction[1] / 50, 0));

        }
        for (ModelRendering model : background2) {
            model.getModels().get(0).getMovement().addPosition(new Vector3f(-direction[0] / 10, -direction[1] / 10, 0));

        }
        for (ModelRendering model : background3) {
            model.getModels().get(0).getMovement().addPosition(new Vector3f(-direction[0] / 5, -direction[1] / 5, 0));
        }
        for (Enemy enemy : Enemy.enemies) {
            enemy.moveXy(direction);
        }
        screenMove = true;
        apple.moveXy(direction);
        player.moveXy(direction);


        for (Enemy enemy : Enemy.enemies) {
            if (enemy.getXy().size() == 0) {
                continue;
            }
            if (enemy.getXy().get(0)[0] < (outLeft)) {
                enemy.moveXy(new float[]{fromLeft, 0});
                enemy.reverse();
            } else if (enemy.getXy().get(0)[0] > (outRight)) {
                enemy.moveXy(new float[]{fromRight, 0});
                enemy.reverse();
            }
            if (enemy.getXy().get(0)[1] < (outUp)) {
                enemy.moveXy(new float[]{0, fromUp});
                enemy.reverse();
            } else if (enemy.getXy().get(0)[1] > (outDown)) {
                enemy.moveXy(new float[]{0, fromDown});
                enemy.reverse();
            }


        }
        if (Apple.getXy()[0] < (outLeft)) {
            apple.moveXy(new float[]{fromLeft, 0});

        } else if (Apple.getXy()[0] > (outRight)) {
            apple.moveXy(new float[]{fromRight, 0});
        }
        if (Apple.getXy()[1] < (outUp)) {
            apple.moveXy(new float[]{0, fromUp});

        } else if (Apple.getXy()[1] > (outDown)) {
            apple.moveXy(new float[]{0, fromDown});
        }
    }
}
