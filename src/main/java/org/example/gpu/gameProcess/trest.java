package org.example.gpu.gameProcess;


import org.example.Buffs.BuffParent;
import org.example.Buffs.CutTheTail;
import org.example.Buffs.Fear;
import org.example.Buffs.Speed;
import org.example.Enemy.Enemy;
import org.example.Main;
import org.example.Painter.Apple;
import org.example.Player.GluePart;
import org.example.Player.Phantom;
import org.example.Player.Player;
import org.example.Sound.LWJGLSound;
import org.example.Sound.MainSoundsController;
import org.example.time.ShortTimer;
import org.example.time.Timer;
import org.example.gpu.render.Model;
import org.example.gpu.render.ModelRendering;
import org.example.gpu.render.Window;
import org.example.obstructions.WormHole;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWVidMode;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;


public class trest {

    public static boolean mouseControl = false;
    static int width = 500;
    static int height = 500;
    public static int xMouse;
    public static int yMouse;
    public static ArrayList<ModelRendering> background = new ArrayList<>();
    public static ArrayList<ModelRendering> background2 = new ArrayList<>();
    public static ArrayList<ModelRendering> background3 = new ArrayList<>();
    public static ArrayList<BuffParent> buffs = new ArrayList<>();
    public static ArrayList<WormHole> holes = new ArrayList<>();
    public static HashMap<Point, Point> toTargets = new HashMap<>();
    public static ArrayList<Phantom> bosses = new ArrayList<>();

    public static float half;

    static boolean isPaused = false;
    public static boolean isEnd = false;

    public static boolean enemyScared = false;

    public static boolean ringWayIsReady = false;
    public static boolean exit = false;
    public static boolean stageChanging = false;

    public static float eatenPlayerTime = 0;
    public static float eatenPlayerTimelast = 0;
    public static float mainTime = 0;
    static float[] direction = new float[2];

    float[] a = new float[2];
    static double radius1;
    private static int outLeft;
    private static int fromLeft;
    private static int outRight;
    private static int fromRight;
    private static int outUp;
    private static int fromUp;
    private static int outDown;
    private static int fromDown;


    static public Apple apple;
    static public Player player;
    static public Phantom phantom;

    //    static MainSound mainSound = null;
    static MainSoundsController mainSound = null;
    static LWJGLSound deathSound = null;
    static boolean hacks = false;

    public static float scaleGround = 0;
    public static float scaleFear = 0;
    public static float scaleSpeedBuf = 0f;
    public static float scaleCutTheTailBuf = 0f;
    public static float scaleHoles = 0f;
    public static float scalePassiveEnemy = 0f;
    public static float scaleActiveEnemy = 0f;
    public static float scaleGlueParts = 0f;

//    scaleGround = 0.02f;
//    scaleFear = 0.1f;
//    scaleSpeedBuf = 0.5f;
//    scaleCutTheTailBuf = 0.1f;
//    scaleHoles = 0.25f;
//    scalePassiveEnemy = 10f;
//    scaleActiveEnemy = 1f;
//    scaleGlueParts = 2.5f;

    enum Stage {

        FIRST_STAGE {
            public void setStage() {
                clear();
                groundCount = 0f;

                passiveEnemyCount = 100f;

                super.setStage();
            }

            public Stage getNextStage() {
                return FIRST_BOSS;
            }

            @Override
            public void timer() {
                stageTimer = 100;
            }

            @Override
            public boolean isBoss() {
                return false;
            }
        },
        FIRST_BOSS {
            public void setStage() {
                clear();

                gluePartsCount = 300f;

                super.setStage();
            }

            public Stage getNextStage() {
                return SECOND_STAGE;
            }

            @Override
            public void timer() {
                stageTimer = bossTime;
            }

            @Override
            public boolean isBoss() {
                return true;
            }

        },
        SECOND_STAGE {
            public void setStage() {
                clear();
                groundCount = 0f;
                activeEnemyCount = 50;

                super.setStage();
            }

            public Stage getNextStage() {
                return SECOND_BOSS;
            }

            @Override
            public void timer() {
                stageTimer = 100;
            }

            @Override
            public boolean isBoss() {
                return false;
            }
        },
        SECOND_BOSS {
            public void setStage() {
                clear();

                gluePartsCount = 5f;
                super.setStage();
            }

            public Stage getNextStage() {
                return THIRD_STAGE;
            }

            @Override
            public void timer() {
                stageTimer = bossTime;
            }

            @Override
            public boolean isBoss() {
                return true;
            }
        },

        THIRD_STAGE {
            public void setStage() {
                clear();

                groundCount = 0.5f;
                passiveEnemyCount = 80;
                activeEnemyCount = 40;

                super.setStage();
            }

            public Stage getNextStage() {
                return THIRD_BOSS;
            }

            @Override
            public void timer() {
                stageTimer = 100;
            }

            @Override
            public boolean isBoss() {
                return false;
            }
        },
        THIRD_BOSS {
            public void setStage() {
                clear();

                gluePartsCount = 5f;
                super.setStage();
            }

            public Stage getNextStage() {
                return FOURTH_STAGE;
            }

            @Override
            public void timer() {
                stageTimer = bossTime;
            }

            @Override
            public boolean isBoss() {
                return true;
            }
        },
        FOURTH_STAGE {
            public void setStage() {
                clear();

                groundCount = 1f;
                passiveEnemyCount = 100;
                activeEnemyCount = 50;
                gluePartsCount = 20f;

                super.setStage();
            }

            public Stage getNextStage() {
                return FOURTH_BOSS;
            }

            @Override
            public void timer() {
                stageTimer = 100;
            }

            @Override
            public boolean isBoss() {
                return false;
            }
        },
        FOURTH_BOSS {
            public void setStage() {
                clear();
                gluePartsCount = 5f;
                super.setStage();
            }

            public Stage getNextStage() {

                return FIRST_STAGE;
            }

            @Override
            public void timer() {
                stageTimer = bossTime;
            }

            @Override
            public boolean isBoss() {
                return true;
            }


        };
        public static int roundCount = -1;
        public void reset(){
            stage = Stage.FIRST_STAGE;
            roundCount = -1;
            stage.setStage();
        }

        public void setStage() {

            if (!stage.isBoss()){
                roundCount++;
                if (roundCount > 0) {
                    holesCount = roundCount;
                    groundCount = 1f + roundCount * 0.1f;

                    passiveEnemyCount = 100 + roundCount * (10 * 1.4f);
                    activeEnemyCount = roundCount * (5);
                    gluePartsCount =roundCount*5;
                    cutTheTailBufCount = 0;
                    fearCount = 0;
                    speedBufCount = roundCount;
                    if(roundCount>4){
                        cutTheTailBufCount = 3;
                        fearCount = 4;
                    }
                    if(groundCount>2){
                        groundCount=2;
                    }
                    if(speedBufCount>10){
                        speedBufCount=10;
                    }



                }
            }else {
                groundCount = 0f;
            }
            stage.timer();
            stageStopWatch = 0;
            setScale();
            System.out.println(stage.name() + " round "+roundCount+" stated!");
        }

        ;

        public void setScale() {
            scaleFear = fearCount/ stageTimer;
            scaleSpeedBuf = speedBufCount/ stageTimer;
            scaleCutTheTailBuf = cutTheTailBufCount/ stageTimer;
            scaleGround = groundCount / stageTimer;
            scaleHoles = holesCount / stageTimer;
            scalePassiveEnemy = passiveEnemyCount / stageTimer;
            scaleActiveEnemy = activeEnemyCount / stageTimer;
            scaleGlueParts = gluePartsCount / stageTimer;
        }

        public abstract Stage getNextStage();

        public abstract void timer();

        public void clear() {
            groundCount = 0f;
            fearCount = 0f;
            speedBufCount = 0f;
            cutTheTailBufCount = 0f;
            holesCount = 0f;
            passiveEnemyCount = 0f;
            activeEnemyCount = 0f;
            gluePartsCount = 0f;
        }

        public abstract boolean isBoss();

        static float bossTime = 3;//5*60

        static float stageTimer = 0;
        private static float groundCount = 0f;
        private static float fearCount = 0f;
        private static float speedBufCount = 0f;
        private static float cutTheTailBufCount = 0f;
        private static float holesCount = 0f;
        private static float passiveEnemyCount = 0f;
        private static float activeEnemyCount = 0f;
        private static float gluePartsCount = 0f;
    }

    public static Stage stage = Stage.FIRST_STAGE;


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


        org.example.gpu.render.Window window = new org.example.gpu.render.Window(width, height);
        if (Main.fullscreen) {
            window.setFullscreen(true);
        }
        if (Main.hacks) {
            hacks = true;
        }
//        window.setFullscreen(true);

        window.createWindow("what'sup");


        half = (float) window.width / 1.5f;

        for (int j = 0; j < 220; j++) {
            Color color = new Color((int) (Math.random() * 100), (int) (Math.random() * 255), (int) (Math.random() * 255));
            background.add(new ModelRendering(window, null, "background"));
            background.get(j).addModel(new Model(window, (int) (Math.random() * 30 + 20), color));
        }
        for (int j = 0; j < 200; j++) {
            Color color = new Color((int) (Math.random() * 100), (int) (Math.random() * 255), (int) (Math.random() * 255));
            background2.add(new ModelRendering(window, null, "background"));
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


        mainSound = new MainSoundsController();

        deathSound = new LWJGLSound("./sounds/game_over.ogg", false);
        deathSound.setCastomVolume(0.1f);



        loop(window);


        window.destroyWindow();
    }

    static float stageStopWatch = 0;

    public void loop(org.example.gpu.render.Window window) {
        double frame_cap = 1.0 / 60;

        double frameTime = 0;
        int frames = 0;
        double time = Timer.getTime();
        double unprocessed = 0;
        float addingEntity = 0;
        float processTime = 0;
        int processCount = 0;

        float stageTimer = 0;


        stage.setStage();

//        ShortTimer timer = new ShortTimer(5);
        while (!window.shouldClose()) {
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
//            timer.start(false,mainTime);
//            timer.update(mainTime);
            if (!isPaused && !isEnd) {
                if (mainSound != null) {
                    mainSound.play();
                }
                if (addingEntity >= 1) {
                    if(!stage.isBoss()){
                        addSomeEntity(window);
                    }else{

                    }
                    addingEntity = 0;
                }
                if (stageStopWatch < Stage.stageTimer) {
                    stageStopWatch += (float) time2 - (float) time;
                    stageChanging = false;
                } else {
                    if(stage.isBoss()){
                        if(bosses.size()!=0){
                            if (Phantom.phantoms.size() == 0){
                                bosses.clear();
                            }
                        }else {
                            stageChanging = true;
                            stage = stage.getNextStage();
                            stage.setStage();
                            GluePart.clearParts();
                        }

                    }else {

                        if(Phantom.phantoms.size()==0){
                            for (int i = 0; i < Stage.roundCount+1; i++) {
                                bosses.add(new Phantom(window,Player.player.getXy().size()*3));
                            }

                        }
                        stageChanging = true;
//                        clearStage();
                        stage = stage.getNextStage();
                        stage.setStage();
                        GluePart.clearParts();
                        Enemy.resetAngryTimer();
                    }
//                    }
                }

            } else if (isPaused) {
                if (mainSound != null) {
                    mainSound.pause();
                }
            }
            time = time2;

            input(window);

            if (processTime >= 1.0 / 100) {
                processCount++;
                playgroundScale();
                processTime = 0;
                process();
                moveBackground();
            }
            window.update();
            while (unprocessed >= frame_cap) {
                canRender = true;
                unprocessed -= frame_cap;


                if (frameTime >= 1) {
//                    System.out.println(processCount);
                    processCount = 0;
                    frameTime = 0;
//                    System.out.println("FPS: " + frames);
                    frames = 0;
                }
            }


            if (canRender) {
                glClear(GL_COLOR_BUFFER_BIT);

                for (int i = 0; i < ModelRendering.selfList.size(); i++) {
                    ModelRendering.selfList.get(i).renderModels();
                }
                Point mouse = MouseInfo.getPointerInfo().getLocation();
                mouse.x = mouse.x - window.windowPosX - window.width / 2;
                mouse.y = (mouse.y - window.windowPosY - window.height / 2);
//            System.out.println(mouse);
                xMouse = mouse.x;
                yMouse = -mouse.y;
                window.swapBuffer();
                frames++;

            }
            if (exit)
                exit(window);
        }
    }


    public static float playGroundScale = 1;
    public static float playGroundWidth = 1;
    public static float playGroundHeight = 1;
    public static int speedBufCount = 0;
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


    public void scale() {
        playGroundScale = 1 + (int) stageStopWatch * scaleGround;
        if (playGroundScale > 3) {
            playGroundScale = 3;
        }

        fearCount = (int) Math.floor(stageStopWatch * scaleFear);
//        if (fearCount > 2) {
//            fearCount = 2;
//        }
//Player.countOfApples
            speedBufCount = (int) Math.floor(stageStopWatch * scaleSpeedBuf);
        cutTheTailCount = (int) Math.floor(stageStopWatch * scaleCutTheTailBuf);
        holesCount = (int) Math.floor(stageStopWatch * scaleHoles);
        passiveEnemyCount = (int) (scalePassiveEnemy * stageStopWatch);
//            if (passiveEnemyCount > 200) {
//                passiveEnemyCount = 200;
//            }

        activeEnemyCount = (int) Math.floor(stageStopWatch * scaleActiveEnemy);
        if (activeEnemyCount > 50) {
            activeEnemyCount = 50;
        }

        GluePart.maxAmountOfGlueParts = (int) Math.floor(stageStopWatch * scaleGlueParts);
    }

    public void calculateEntity() {
        scale();
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
        currentSpeedCount = speedBufCount - currentSpeedCount;
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

    public void addSomeEntity(org.example.gpu.render.Window window) {
        calculateEntity();

//        if(!stageChanging){
            for (int i = 0; i < currentSpeedCount; i++) {
                buffs.add(new Speed(window));
            }
            for (int i = 0; i < currentFearCount; i++) {
                buffs.add(new Fear(window));
            }
            for (int i = 0; i < currentCutTheTailCount; i++) {
                buffs.add(new CutTheTail(window));
            }
//        }
        if (!stage.isBoss()) {
            for (int i = 0; i < currentPassiveEnemyCount; i++) {             // 200
                new Enemy(window, false);
            }
            for (int i = 0; i < currentActiveEnemyCount; i++) {           //   30
                new Enemy(window, true);
            }
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


        }
    }

    public void clearEnemies() {
        for (int i = 0; i < Enemy.enemies.size(); i++) {
            if (Enemy.enemies.get(i).enemyHead.distance(Player.player.getHeadXY()) > 450) {
                Enemy.enemies.get(i).removeEnemy();
                i--;
                if (Enemy.enemies.size() == 0) {
                    return;
                }

            }

        }

    }
    public void clearStage(){
//        if(buffs.size()==0){
//            return;
//        }
        for (int i = 0; i < buffs.size(); i++) {
            if(buffs.get(i).suddenExpose()){
                buffs.remove(i);
                i--;
            }

        }
        for (int i = 0; i < holes.size(); i++) {
            if(holes.get(i).suddenExpose()){
                holes.remove(i);
                i--;
            }
        }
//        if(buffs.size() ==0 && holes.size() == 0){
//            stage = stage.getNextStage();
//            stage.setStage();
//        }
    }

    public void pushAway() {
        int maxDistance = 600;
        for (Enemy enemy : Enemy.enemies) {

            boolean changed = false;
            for (int i = 0; i < enemy.getXy().size(); i++) {

                Point2D enemyPoint = new Point2D.Float(enemy.getXy().get(i)[0], enemy.getXy().get(i)[1]);
                Point2D enemyPhantomPoint = new Point2D.Float(enemy.getPhantomXY().get(i)[0], enemy.getPhantomXY().get(i)[1]);
                float distance = (float) enemyPoint.distance(0, 0);
                float phantomDistance = (float) enemyPhantomPoint.distance(0, 0);
                if (phantomDistance < maxDistance) {
                    changed = true;
                    float phantomAngle = (float) Math.atan((-enemyPhantomPoint.getY()) / (-enemyPhantomPoint.getX()));
                    float newPosition = phantomDistance + (10);
                    double phantomTranslocationX = newPosition * Math.cos(phantomAngle);
                    double phantomTranslocationY = newPosition * Math.sin(phantomAngle);
                    if (-enemyPhantomPoint.getX() < 0) {
                        enemy.getPhantomXY().set(i, new float[]{+(float) phantomTranslocationX, +(float) phantomTranslocationY});
                        enemy.getXy().set(i, enemy.getPhantomXY().get(i));
                    } else {
                        enemy.getPhantomXY().set(i, new float[]{-(float) phantomTranslocationX, -(float) phantomTranslocationY});
                        enemy.getXy().set(i, enemy.getPhantomXY().get(i));
                    }

                }
            }
            if (changed) {
//                    enemy.setXy();
                enemy.makeChainTogether();
                enemy.setXy();
//                    enemy.setPhantomXY();

            }
        }
        for (WormHole enemy : holes) {
            Point2D point2D = new Point2D.Float(enemy.getXy()[0],enemy.getXy()[1]);
                float phantomDistance = (float) point2D.distance(0, 0);
                if (phantomDistance < maxDistance) {
                    float phantomAngle = (float) Math.atan((-point2D.getY()) / (-point2D.getX()));
                    float newPosition = phantomDistance + (10);
                    double phantomTranslocationX = newPosition * Math.cos(phantomAngle);
                    double phantomTranslocationY = newPosition * Math.sin(phantomAngle);
                    if (-point2D.getX() < 0) {
                        enemy.getXy()[0] = (float) phantomTranslocationX;
                        enemy.getXy()[1] = (float) phantomTranslocationY;
                    } else {
                        enemy.getXy()[0] = -(float) phantomTranslocationX;
                        enemy.getXy()[1] = -(float) phantomTranslocationY;
                    }
                }
        }
        for (BuffParent enemy : buffs) {
            float phantomDistance = (float) enemy.getXy().distance(0, 0);
            if (phantomDistance < maxDistance) {
                float phantomAngle = (float) Math.atan((-enemy.getXy().getY()) / (-enemy.getXy().getX()));
                float newPosition = phantomDistance + (10);
                double phantomTranslocationX = newPosition * Math.cos(phantomAngle);
                double phantomTranslocationY = newPosition * Math.sin(phantomAngle);
                if (-enemy.getXy().getX() < 0) {
                    enemy.getXy().setLocation(+(float) phantomTranslocationX, +(float) phantomTranslocationY);
                } else {
                    enemy.getXy().setLocation(-(float) phantomTranslocationX, -(float) phantomTranslocationY);
                }
            }
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

    public void addSomeHoles(org.example.gpu.render.Window window) {
        if (holes.size() < holesCount) {
            chance += 0.001;
            if (Math.random() < chance) {
                holes.add(new WormHole(window));

            }
        }

    }

    public void input(org.example.gpu.render.Window window) {
        if (glfwGetKey(window.getWindow(), GLFW_KEY_ESCAPE) != 0) {
            exit = true;
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


        if (window.getInput().isKeyPressed(GLFW_KEY_R)) {
            reset(window);
            isPaused = false;
            isEnd = false;
            mouseControl = false;
            System.out.println("restart");
        }
        if (hacks) {
            hacks(window);
        }


    }

    public void hacks(org.example.gpu.render.Window window) {
        if (window.getInput().isKeyPressed(GLFW_KEY_E)) {
            isEnd = !isEnd;
        }
        if (!isEnd && !isPaused) {
            if (window.getInput().isKeyPressed(GLFW_KEY_1)) {
                buffs.add(new Speed(window));

            }
            if (window.getInput().isKeyPressed(GLFW_KEY_2)) {
                buffs.add(new Fear(window));

            }
            if (window.getInput().isKeyPressed(GLFW_KEY_3)) {
                buffs.add(new CutTheTail(window));

            }
            if (window.getInput().isKeyPressed(GLFW_KEY_EQUAL)) {
                Player.player.eatTheApple();

            }
            if (window.getInput().isKeyPressed(GLFW_KEY_MINUS)) {
                Player.player.lostTheApple();

            }
            if (window.getInput().isKeyPressed(GLFW_KEY_MINUS)) {
                Player.player.lostTheApple();

            }
            if (window.getInput().isKeyPressed(GLFW_KEY_SPACE)) {
                immortal = !immortal;

            }
            if (window.getInput().isKeyPressed(GLFW_KEY_TAB)) {
                if(stage.isBoss()){
                    stage = stage.getNextStage();
                    stage.setStage();
                }else {
                    stageStopWatch = Stage.stageTimer;
                }
            }
        }

//        if (window.getInput().isKeyPressed(GLFW_KEY_A)) {
//            sound.play();
//
//        }
//        if (window.getInput().isKeyPressed(GLFW_KEY_S)) {
//            sound.stop();
//
//        }
    }

    public static boolean immortal = false;

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


    public static void reset(org.example.gpu.render.Window window) {
        stage.reset();

        mainSound.reset();
        playgroundScale();
        //Здесь добавить списки
        for (Enemy enemy : Enemy.enemies) {
            enemy.getRendering().clear(true);
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
        if (deathSound != null) {
            deathSound.stop();
        }
        buffs.clear();
        GluePart.clear();
        Phantom.clear();
    }

    public static void playgroundScale() {
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


    public void processInit(org.example.gpu.render.Window window) {
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
        player = new Player(window);
        apple = new Apple(window);





        //Вычисляем пути врагов после проигрыша
        radius1 = Math.pow(width / 5f - 2, 2);
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
                for (int i = 0; i < buffs.size(); i++) {
                    if (buffs.get(i) instanceof Fear) {
                        if (((Fear) buffs.get(i)).isFear()) {
                            Fear.fear();
                            break;
                        }
                    }
                }
                if (stage.isBoss()) {
                    pushAway();
                    clearEnemies();
                    clearStage();
                }
                GluePart.refresh();
                moveEntity();
                a = Apple.getXy();
                apple.update();

                for (int i = 0; i < Phantom.phantoms.size(); i++) {
                    Phantom.phantoms.get(i).update();
                }
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

                Executors.newSingleThreadExecutor().submit(() -> {
                    mainSound.update();
                });


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

        for(Phantom phantom1 : Phantom.phantoms){
            phantom1.moveXy(direction);
        }


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

    public static void end() {
        if (!isEnd && !immortal) {
            if (mainSound != null) {
                mainSound.stop();
            }
            //place for game over sound
            if (deathSound != null) {
                deathSound.play();
            }
            eatenPlayerTime = mainTime;
            isEnd = true;                         // TODO
            mouseControl = false;
            Player.speedBoostTime = 0;
            Player.player.step = Player.player.minStep;
            for (Enemy enemy1 : Enemy.enemies) {
                enemy1.setCurrentDelay(0);
            }

        }

    }

    public void exit(Window window) {
        if (mainSound != null) {
            mainSound.delete();
        }
        for (LWJGLSound sound : LWJGLSound.sounds) {
            sound.delete();
        }
        glfwSetWindowShouldClose(window.getWindow(), true);
    }
}
