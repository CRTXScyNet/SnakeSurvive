package org.example.gpu;

import org.example.Buffs.BuffParent;
import org.example.Buffs.CutTheTail;
import org.example.Buffs.Fear;
import org.example.Buffs.Speed;
import org.example.Enemy.Enemy;
import org.example.Painter.Apple;
import org.example.Player.GluePart;
import org.example.Player.Player;
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
    private static int outLeft;
    private static int fromLeft;
    private static int outRight;
    private static int fromRight;
    private static int outUp;
    private static int fromUp;
    private static int outDown;
    private static int fromDown;
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

        window.setFullscreen(true);

        window.createWindow("what'sup");
        half = (float) window.width / 1.5f;
        GL.createCapabilities();

        glEnable(GL_TEXTURE_2D);


        for (int j = 0; j < 220; j++) {
            Color color = new Color((int) (Math.random() * 100), (int) (Math.random() * 255), (int) (Math.random() * 255));
            background.add(new ModelRendering(window, null, "background"));
            background.get(j).addModel(new Model(window, (int) (Math.random() * 30 + 20), color));
        }
        for (int j = 0; j < 200; j++) {
            Color color = new Color((int) (Math.random() * 100), (int) (Math.random() * 255), (int) (Math.random() * 255));
            background2.add(new ModelRendering(window,  null, "background"));
            background2.get(j).addModel(new Model(window, (int) (Math.random() * 40 + 30), color));
        }
        for (int j = 0; j < 150; j++) {
            Color color = new Color((int) (Math.random() * 100), (int) (Math.random() * 255), (int) (Math.random() * 255));
            background3.add(new ModelRendering(window, null, "background"));
            background3.get(j).addModel(new Model(window, (int) (Math.random() * 60 + 40), color));
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
        float addingEntity = 0;
        float processTime = 0;

        expansePart = 1;

        while (!window.shouldClose()) {

            Point mouse = MouseInfo.getPointerInfo().getLocation();

            mouse.x = mouse.x - window.windowPosX - window.width / 2;
            mouse.y = (mouse.y - window.windowPosY - window.height / 2);
//            System.out.println(mouse);
            xMouse = mouse.x;
            yMouse = -mouse.y;

            boolean canRender = false;
            double time2 = Timer.getTime();
            double passed = time2 - time;
            unprocessed += passed;
            frameTime += passed;
            addingEntity += (float) time2 - (float) time;
            processTime += (float) time2 - (float) time;

            if (!isPaused) {
                mainTime += (float) time2 - (float) time;
            }
            time = time2;
            scale();
            input(window);
            if (!isPaused && !isEnd) {
                if (addingEntity >= 1) {

                    addSomeEntity(window);
                    addingEntity = 0;
                }
            }
            if (processTime >= 1.0 / 100) {
                processTime = 0;
                process();
                moveBackground();
            }
            window.update();
            while (unprocessed >= frame_cap) {
                canRender = true;
                unprocessed -= frame_cap;


                if (frameTime >= 1) {

                    frameTime = 0;
                    System.out.println("FPS: " + frames);
                    frames = 0;
                }
            }


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

    public static int speedCount = 0;
    public int currentSpeedCount = 0;
    public static int cutTheTailCount = 0;

    public int currentCutTheTailCount = 0;
    public static int fearCount = 0;

    public int currentFearCount = 0;
    public static int activeEnemyCount = 0;

    public int currentActiveEnemyCount = 0;
    public static int passiveEnemyCount = 0;

    public int currentPassiveEnemyCount = 0;

    public int currentGluePartCount = 0;

    public void stages() {
        if(playGroundScale<2) {
            playGroundScale = 1 + (int) Player.countOfApples * 0.01f;
        }
        if (fearCount < 2) {
            fearCount = (int) Math.floor(Player.countOfApples * 0.1);
        }
        speedCount = (int) Math.floor(Player.countOfApples * 0.5);
        cutTheTailCount = (int) Math.floor(Player.countOfApples * 0.1);
        holesCount = (int) Math.floor(Player.countOfApples * 0.25);
        if (passiveEnemyCount < 200) {
            passiveEnemyCount = Player.countOfApples * 10;
        }
        if (activeEnemyCount < 50) {
            activeEnemyCount = (int) Math.floor(Player.countOfApples * 0.5);
        }
        GluePart.maxAmountOfGlueParts = (int) Math.floor(Player.countOfApples * 2.5);
    }

    public void calculate() {
        stages();
        //вычисления для бафов
        currentSpeedCount = 0;
        currentFearCount = 0;
        currentCutTheTailCount = 0;
        for (BuffParent parent : buffs) {
            if (parent instanceof Speed) {
                currentSpeedCount++;
            } else if (parent instanceof Fear) {
                currentFearCount++;
            } else if (parent instanceof CutTheTail) {
                currentCutTheTailCount++;
            }
        }
        currentSpeedCount = speedCount - currentSpeedCount;
        currentFearCount = fearCount - currentFearCount;
        currentCutTheTailCount = cutTheTailCount - currentCutTheTailCount;
        //Вичисления для врагов

        currentActiveEnemyCount = 0;
        currentPassiveEnemyCount = 0;
        for (Enemy enemy : Enemy.enemies) {
            if (enemy.isActive) {
                currentActiveEnemyCount++;
            }
            if (!enemy.isActive) {
                currentPassiveEnemyCount++;
            }
        }
        currentActiveEnemyCount = activeEnemyCount - currentActiveEnemyCount;
        currentPassiveEnemyCount = passiveEnemyCount - currentPassiveEnemyCount;
    }

    public void addSomeEntity(Window window) {
        calculate();

        for (int i = 0; i < currentSpeedCount; i++) {
            buffs.add(new Speed(window));
        }
        for (int i = 0; i < currentFearCount; i++) {
            buffs.add(new Fear(window));
        }
        for (int i = 0; i < currentCutTheTailCount; i++) {
            buffs.add(new CutTheTail(window));
        }
        for (int i = 0; i < currentPassiveEnemyCount; i++) {             // 200
            new Enemy(window, false);
        }
        for (int i = 0; i < currentActiveEnemyCount; i++) {           //   30
            new Enemy(window, true);
        }

        addBuffs();

        addSomeHoles(window);


        int count = 0;
        for (GluePart part : GluePart.glueParts) {
            count += part.xy.size();
        }
        if (count < GluePart.maxAmountOfGlueParts * 5 && GluePart.glueParts.size() < GluePart.maxAmountOfGlueParts) {
//                        for (int i = 0; i < 20; i++) {
            new GluePart(window);
//                        }

            expansePart *= 2;
        }
    }

    public void addBuffs() {

        for (int i = 0; i < buffs.size(); i++) {
            if (buffs.get(i).isExist()) {
                continue;
            } else {
                if (buffs.get(i).getChance() > Math.random()) {
                    buffs.get(i).addSome();
                }
            }
        }

    }

    static double chance = 0.001;
    public int holesCount = 0;

    public void addSomeHoles(Window window) {
        if (holes.size() < holesCount) {
            chance += 0.001;
            if (Math.random() < chance) {
                holes.add(new WormHole(window));

            }
        }

    }

    public void input(Window window) {
        if (glfwGetKey(window.getWindow(), GLFW_KEY_ESCAPE) != 0) {
            glfwSetWindowShouldClose(window.getWindow(), true);
        }
        if (window.getInput().isMouseButtonPressed(GLFW_MOUSE_BUTTON_1)) {
            if (!isEnd && !isPaused) {
                mouseControl = !mouseControl;
            }
        }
        if (window.getInput().isMouseButtonPressed(GLFW_MOUSE_BUTTON_2)) {
            if (!isEnd && !isPaused) {
                if (!player.getPart().isAlive()) {
                    player.throwPart();
                }/*else {
                    player.takePartBack();
                }*/
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
        hacks(window);


    }

    public void hacks(Window window) {
        if (window.getInput().isKeyPressed(GLFW_KEY_1)) {
            if (!isEnd && !isPaused) {
                buffs.add(new Speed(window));
            }
        }
        if (window.getInput().isKeyPressed(GLFW_KEY_2)) {
            if (!isEnd && !isPaused) {
                buffs.add(new Fear(window));
            }
        }
        if (window.getInput().isKeyPressed(GLFW_KEY_3)) {
            if (!isEnd && !isPaused) {
                buffs.add(new CutTheTail(window));
            }
        }
        if (window.getInput().isKeyPressed(GLFW_KEY_EQUAL)) {
            if (!isEnd && !isPaused) {
                Player.player.eatTheApple();
            }
        }
        if (window.getInput().isKeyPressed(GLFW_KEY_MINUS)) {
            if (!isEnd && !isPaused) {
                Player.player.lostTheApple();
            }
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


    public static float getMainTime() {
        return mainTime;
    }


    public static void reset(Window window) {
GluePart.maxAmountOfGlueParts = 0;
        countOfApples = 0;
        passiveEnemyCount = 0;
        activeEnemyCount = 0;
        fearCount = 0;
        cutTheTailCount = 0;
        speedCount = 0;
        playGroundScale = 1;
        scale();
        //Здесь добавить списки
        for (Enemy enemy : Enemy.enemies) {
            enemy.getRendering().clear();
        }
        Enemy.enemies.clear();
        Enemy.amountOfAbsorbedEnemies = 0;
        for (WormHole hole : holes) {
            hole.reset();
        }
        holes.clear();
        chance = 0.001;
        apple.reset();
        player.reset();
        for (BuffParent buffs : buffs) {
            buffs.reset();
        }
        buffs.clear();
        GluePart.clear();


    }

    public static void scale() {
        outLeft = (int) (-width * (playGroundScale / 2)) / Enemy.step * Enemy.step;
        fromLeft = (int) (-width * playGroundScale) / Enemy.step * Enemy.step;

        outRight = (int) (width * (playGroundScale / 2)) / Enemy.step * Enemy.step;
        fromRight = (int) (width * playGroundScale) / Enemy.step * Enemy.step;
        playGroundWidth = fromRight;
        outUp = (int) (-height * (playGroundScale / 2)) / Enemy.step * Enemy.step;
        fromUp = (int) (-height * playGroundScale) / Enemy.step * Enemy.step;

        outDown = (int) (height * (playGroundScale / 2)) / Enemy.step * Enemy.step;
        fromDown = (int) (height * playGroundScale) / Enemy.step * Enemy.step;
        playGroundHeight = fromDown;
    }

    public static float playGroundScale = 1;
    public static float playGroundWidth = 1;
    public static float playGroundHeight = 1;

    public void processInit(Window window) {
        //Вычисляем границы территории
        width = window.width;
        height = window.height;
        outLeft = (int) (-width * (playGroundScale / 2)) / Enemy.step * Enemy.step;
        fromLeft = (int) (-width * playGroundScale) / Enemy.step * Enemy.step;

        outRight = (int) (width * (playGroundScale / 2)) / Enemy.step * Enemy.step;
        fromRight = (int) (width * playGroundScale) / Enemy.step * Enemy.step;
        playGroundWidth = fromRight;
        outUp = (int) (-height * (playGroundScale / 2)) / Enemy.step * Enemy.step;
        fromUp = (int) (-height * playGroundScale) / Enemy.step * Enemy.step;

        outDown = (int) (height * (playGroundScale / 2)) / Enemy.step * Enemy.step;
        fromDown = (int) (height * playGroundScale) / Enemy.step * Enemy.step;
        playGroundHeight = fromDown;
        // Создаем сущности
        apple = new Apple(window);
        player = new Player(window);

//        for (int i = 0; i < 200; i++) {
//            new Enemy(window, false);
//        }
//        for (int i = 0; i < 30; i++) {
//            new Enemy(window, true);
//        }


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


                moveEntity();
                a = Apple.getXy();
                apple.update();
                player.update();
                for (int i = 0; i < Enemy.enemies.size(); i++) {
                    Enemy.enemies.get(i).update();
                }
                for (int i = 0; i < holes.size(); i++) {
                    holes.get(i).update();
                }
                for (int i = 0; i < GluePart.glueParts.size(); i++) {
                    GluePart.glueParts.get(i).moveCheck();
                }
                GluePart.refresh();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void moveEntity() {
        if (player.getDirection() != null) {
            direction = player.getDirection();
        }
        for (BuffParent parent : buffs) {
            if (parent.isExist()) {
                parent.moveXy(moveGround(new float[]{(float) parent.getXy().getX(), (float) parent.getXy().getY()}));
                parent.moveXy(direction);
            }
        }
        for (int j = 0; j < GluePart.glueParts.size(); j++) {
            GluePart.glueParts.get(j).moveXy(moveGround(GluePart.glueParts.get(j).getXy().get(0)));
            GluePart.glueParts.get(j).moveXy((direction));
        }

        for (int i = 0; i < holes.size(); i++) {
            holes.get(i).moveXy(moveGround(holes.get(i).getXy()));
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
        apple.moveXy(direction);
        player.moveXy(direction);


        for (Enemy enemy : Enemy.enemies) {
            if (enemy.getXy().size() == 0) {
                continue;
            }
            float[] mo = moveGround(enemy.getXy().get(0));
            if (mo[0] != 0 || mo[1] != 0) {
                enemy.moveXy(mo);
                enemy.reverse();
            }
        }
//        apple.moveXy(moveGround(Apple.getXy()));

    }

    static float[] moveGround(float[] xy) {
        if (xy[0] < (outLeft)) {
            return new float[]{fromLeft, 0};
        } else if (xy[0] > (outRight)) {
            return new float[]{fromRight, 0};
        } else if (xy[1] < (outUp)) {
            return new float[]{0, fromUp};
        } else if (xy[1] > (outDown)) {
            return new float[]{0, fromDown};
        }
        return new float[2];
    }
}
