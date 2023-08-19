package org.example.Player;

import org.example.Painter.Apple;
import org.example.Player.phantom.Phantom;
import org.example.gpu.gameProcess.trest;
import org.example.gpu.io.Movement;
import org.example.gpu.render.Model;
import org.example.gpu.render.ModelRendering;
import org.example.gpu.render.Window;
import org.example.time.ShortTimer;
import org.joml.Vector3f;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1;

public class Player extends PlayerParent {

    private float[] direction = new float[2];
    public static ArrayList<Player> selfList = new ArrayList<>();


    public float[] getDirection() {
        return direction;
    }


    public Color mainColor = new Color(0, 100, 100);
    public Color takenAppleColor = new Color(200, 0, 0);

    static boolean speedBoost = false;
    public static ArrayList<float[]> absorbArray = new ArrayList<>();


    private ModelRendering maxSpeedRender;
    public static Player player;
    public PlayerPart part;
    private DyingCell dyingCell;
    private float mainTime = 0;


    private class DyingCell {
        private ArrayList<Integer> pos = new ArrayList<>();
        private ArrayList<ShortTimer> timers = new ArrayList<>();
        private ModelRendering rendering;

        DyingCell(Window window) {
            this.pos = pos;
            rendering = new ModelRendering(window, null, "apple");
        }

        private void cellDying(int pos) {
            this.pos.add(pos);
            rendering.addModel(new Model(window, 250, takenAppleColor, false));
            ShortTimer timer = new ShortTimer(0.01f, 0.5f);
            timer.start(true, mainTime);
            timers.add(timer);

        }

        private void update() {
            for (int i = 0; i < pos.size(); i++) {
                if (xy.size() - 1 < pos.get(i)) {
                    rendering.removeModel(rendering.getModels().get(i));
                    timers.remove(i);
                    pos.remove(i);
                    i--;
                    continue;
                }
                float[] point = xy.get(pos.get(i));
                rendering.getModels().get(i).getMovement().setPosition(new Vector3f(point[0], point[1], 0));
                timers.get(i).start(false, mainTime);
                float time = timers.get(i).update(mainTime);
                rendering.getModels().get(i).setTime(time * 2f);
                if (time <= 0) {
                    rendering.removeModel(rendering.getModels().get(i));
                    timers.remove(i);
                    pos.remove(i);
                    i--;
                }
            }
        }

    }

    public Player(Window window) {
        super(window);
        dyingCell = new DyingCell(window);
        color = mainColor;
        xy.add(new float[]{0, 0});
        selfList.add(this);
        renderInit(color, "player", null);
        maxSpeedRender = new ModelRendering(window, null, "speedIndicate");
        maxSpeedRender.addModel(new Model(window, 30, color, false));
        maxSpeedRender.getModels().get(0).getMovement().setPosition(new Vector3f((float) xy.get(0)[0], (float) xy.get(0)[1], 0));
        maxSpeedRender.getModels().get(0).getMovement().setRotation(-tMouse);
        setMaxLength(100);
        setLength(5);


        player = this;
        part = new PlayerPart(window);
    }


    public void throwPart() {
        if (!getPart().isAlive()) {
            if (countOfApples < xy.size() && xy.size() > 1) {
                part.spawn();
                minusCell();
            }
        }
    }

    public void takePartBack() {
        if (/*countOfApples>0 &&*/ part.isAlive()) {
            part.setCallBack(true);
        }
    }

    public PlayerPart getPart() {
        return this.part;
    }

    public void addCircle() {
        if (xy.size() < maxLength) {
            super.addCircle();
        }
    }

    public void grow() {
        if (xy.size() >= maxLength) {
            return;
        }

        for (int i = 0; i < 1; i++) {
            addCircle();
        }
        setAppleCount();
    }

    public void minusCell() {
        if (xy.size() <= countOfApples) {
            return;
        }
        try {
            if (getXy().size() > 1) {
                rendering.getModels().remove(getXy().size() - 1);
                getXy().remove(getXy().size() - 1);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reset() {
        super.reset();
        xy.add(new float[]{0, 0});
        if (rendering != null) {
            rendering.addModel(new Model(window, (int) (size * 30), color, false));
        }
        countOfApples = 0;
        speedBoostTime = 0;
        maxStep = maxStepStat;
        step = 0.1f;
//        move(width/2,height/2);
        for (int i = 0; i < length - 1; i++) {
            addCircle();
        }
        part.reset();
    }

    public void setAppleCount() {
        int i = countOfApples;
        for (Model model : rendering.getModels()) {
            if (i > 0) {
                model.setRGB(takenAppleColor);
                i--;
            } else {
                model.setRGB(mainColor);
            }
        }
    }

    public void cutTheTail() {
        int i = Player.countOfApples;
        int s = xy.size() - (xy.size() - Player.countOfApples) / 2 - 1;
        for (int j = xy.size() - 1; j >= 0; j--) {
            if (xy.size() > 5) {
                if (j > i - 1 && j > s) {
                    GluePart part = new GluePart(window);
                    part.setXy(xy.get(j));
                    xy.remove(j);
                    rendering.getModels().remove(j);

                }
            }
        }
    }

    public void cutOneCell() {
        if (Player.getCountOfApples() >= xy.size()) {
            return;
        }

        GluePart part = new GluePart(window);
        part.setXy(xy.get(xy.size()-1));
        minusCell();


    }

    public float getMouse() {
        return tMouse;
    }


    private float stepRadLast = 0;
    private int count = 0;
    private int maxCount = 500;


    public static int countOfApples = 0;
    boolean canIncreaseSpeed = false;

    public static int getCountOfApples() {
        return countOfApples;
    }

    public void eatTheApple() {
        if (countOfApples >= maxLength - 1) {
            return;
        }

        countOfApples++;
        if (xy.size() < countOfApples) {
            grow();
        }
        if (xy.size() > countOfApples) {
            minusCell();
        }
        increaseSpeed();
        setAppleCount();
    }

    public void lostTheApple() {
        if (countOfApples < 5 && trest.immortal) {
            return;
        }

        if (countOfApples == 0) {
            trest.end();
            return;
        }

        if (countOfApples > 0) {
            countOfApples--;
            decreaseSpeed();
            dyingCell.cellDying(countOfApples);
//            grow();
            setAppleCount();
        }
    }

    public void twist() {
        trest.mouseControl = false;
        if (step < maxStep * 2) {
            step += 0.05;
        }
    }

    public void update() {

        mainTime = trest.mainTime;

        if (Apple.appleVisible) {
            if (Apple.checkCollision(xy.get(0))) {
                eatTheApple();

            }
        }
        if (trest.isEnd) {
            trest.eatenPlayerTimelast = mainTime - trest.eatenPlayerTime;
            setTime(-trest.eatenPlayerTimelast);
        } else {

            setTime(mainTime);
            hold();
        }
        dyingCell.update();
        moveCheck();
        part.update();
    }

    private float maxRad = 0.15f;
    private float curRad = 0;
    private float distance = 0;
    private boolean changeDir = false;
    static boolean isAttack = false;
    static boolean timeToRun = false;
    static boolean fightIsOver = true;

    public void wiggle() {
        if (distance > 50) {
            if (changeDir) {
                curRad += 0.01;
                if (curRad > maxRad) {
                    changeDir = false;
//                    System.out.println("Change Dir");
                }
            } else {
                curRad -= 0.01;
                if (curRad < -maxRad) {
                    changeDir = true;
//                    System.out.println("Change Dir");
                }
            }
//            canIncreaseSpeed = true;
        } else {
            curRad /= 2;
//            canIncreaseSpeed = false;
        }

    }

    protected void setRadian(Point2D target) {
//        if (target != null) {
//            target.setLocation(target.getX() + xy.get(0)[0],
//                    target.getY() + xy.get(0)[1]);
//        }
        if (trest.AIM) {
            trest.mouseControl = true;
            fightIsOver = true;
            Point2D phantomPoint = null;
            if (trest.stage.isBoss()) {
                int position = 0;
                int phantomNumber = 0;
                float distance = 0;
                float secDistance = 0;
                Point2D head = null;
                float headDistance = 0;
                int weakPosition = 0;
                int weakPhantomNumber = 0;
                float weakDistance = 0;
                float weakSecDistance = 0;

                for (int j = 0; j < Phantom.phantoms.size(); j++) {
                    if (Phantom.phantoms.get(j).getXy().size() <= countOfApples) {
                        for (int i = 0; i < Phantom.phantoms.get(j).getXy().size(); i++) {
                            if (weakDistance == 0) {
                                weakDistance = (float) getHeadXY().distance(Phantom.phantoms.get(j).getXy().get(i)[0], Phantom.phantoms.get(j).getXy().get(i)[1]);
                                weakPosition = i;
                                weakPhantomNumber = j;
                            } else {
                                weakSecDistance = (float) getHeadXY().distance(Phantom.phantoms.get(j).getXy().get(i)[0], Phantom.phantoms.get(j).getXy().get(i)[1]);
                                if (weakSecDistance < weakDistance) {
                                    weakDistance = weakSecDistance;
                                    weakPosition = i;
                                    weakPhantomNumber = j;
                                }
                            }
                        }
                        continue;
                    }
                    if (head == null) {
                        head = Phantom.phantoms.get(j).getHeadXY();
                        headDistance = (float) head.distance(getHeadXY());
                    } else if (head.distance(getHeadXY()) > Phantom.phantoms.get(j).getHeadXY().distance(getHeadXY())) {
                        head = Phantom.phantoms.get(j).getHeadXY();
                        headDistance = (float) head.distance(getHeadXY());
                    }

                    for (int i = 0; i < Phantom.phantoms.get(j).getXy().size(); i++) {
                        if((countOfApples<i+countOfApples/2) && (Phantom.phantoms.get(j).getXy().size() - i - countOfApples/2 > countOfApples || Phantom.phantoms.get(j).getXy().size() - i - countOfApples/2 <= 0)){
                            continue;
                        }
                        if (distance == 0) {
                            fightIsOver = false;
                            distance = (float) getHeadXY().distance(Phantom.phantoms.get(j).getXy().get(i)[0], Phantom.phantoms.get(j).getXy().get(i)[1]);
                            position = i;
                            phantomNumber = j;
                        } else {
                            secDistance = (float) getHeadXY().distance(Phantom.phantoms.get(j).getXy().get(i)[0], Phantom.phantoms.get(j).getXy().get(i)[1]);
                            if (secDistance < distance) {
                                distance = secDistance;
                                position = i;
                                phantomNumber = j;
                            }
                        }

                    }
                }
                if (headDistance != 0) {
                    if (headDistance < size * xy.size() / 2 && headDistance < 300) {
                        isAttack = false;
//                    target = new Point2D.Float(xy.get(0)[0] - Phantom.phantoms.get(phantomNumber).getXy().get(position)[0] * 2, xy.get(0)[1] - Phantom.phantoms.get(phantomNumber).getXy().get(position)[1] * 2);

                    } else if (headDistance > size * xy.size()+20) {

                        isAttack = true;
//                    target = new Point2D.Float(Phantom.phantoms.get(phantomNumber).getXy().get(position)[0], Phantom.phantoms.get(phantomNumber).getXy().get(position)[1]);

                    }
                    if ((part.isAlive() ||getCountOfApples()==xy.size())&& headDistance < size * xy.size()+20) {
                        isAttack = false;
                    }
                    if (isAttack) {
                        phantomPoint = new Point2D.Float(Phantom.phantoms.get(phantomNumber).getXy().get(position)[0], Phantom.phantoms.get(phantomNumber).getXy().get(position)[1]);
                    } else {
                        phantomPoint = new Point2D.Float(xy.get(0)[0] - Phantom.phantoms.get(phantomNumber).getXy().get(position)[0] * 2, xy.get(0)[1] - Phantom.phantoms.get(phantomNumber).getXy().get(position)[1] * 2);

                    }
                    if (part.isAlive() || xy.size()==countOfApples) {

                        if (headDistance < size * xy.size() / 2) {
                            timeToRun = true;

                        } else if (headDistance > size * xy.size()+20) {
                            timeToRun = false;
                        }
                        if (!timeToRun/* && isAttack*/) {
                            phantomPoint = null;
                        }
                    }

                    if (weakDistance != 0 && weakDistance < headDistance && (part.isAlive()||getCountOfApples()==xy.size()) && headDistance > size * xy.size() / 2) {
                        phantomPoint = new Point2D.Float(Phantom.phantoms.get(weakPhantomNumber).getXy().get(weakPosition)[0], Phantom.phantoms.get(weakPhantomNumber).getXy().get(weakPosition)[1]);
                    }
                } else if (weakDistance != 0 && fightIsOver) {

                    phantomPoint = new Point2D.Float(Phantom.phantoms.get(weakPhantomNumber).getXy().get(weakPosition)[0], Phantom.phantoms.get(weakPhantomNumber).getXy().get(weakPosition)[1]);
                } else {
                    if (trest.cutTheTail.isExist() && !trest.cutTheTail.isEaten()) {
                        phantomPoint = trest.cutTheTail.getXy();
                    }
                }

            } else {
                if (Apple.appleVisible) {
                    phantomPoint = Apple.getPoint2D();
                } else {
//                    target = null;
                    twist();
                }
            }

            if (phantomPoint != null) {
                target = phantomPoint;
                distance = (float) getHeadXY().distance(target);
                wiggle();
            } else {

                twist();
            }
            if (target != null && trest.mouseControl) {

            } else {


            }

        }

        super.setRadian(target);

        if (target == null) {
            stepRad = (float) Math.abs(difRad) / (20 / (step / 2));
        } else {
            stepRad = (float) Math.abs(difRad) / (15 / (step / 2));
        }

        maxCount = (int) (3.14 / stepRad);
        stepRad *= radDir;
        if (trest.AIM) {
//if(changeDir) {

            if (trest.mouseControl) {
                stepRad += curRad;
            }
            if (((trest.stage.isBoss() && !fightIsOver) || !trest.stage.isBoss()) && !part.isAlive()) {
//                if ((isAttack && timeToRun) || !trest.stage.isBoss()) {
                    if (distance < 350 && difRad < 0.1 && (!trest.stage.isBoss()||(trest.stage.isBoss() && isAttack))) {
                        part.setMouse((float) (tMouse + difRad * radDir));
                        throwPart();
                    }else {
//
                        for (int i = 0; i < Phantom.phantoms.size(); i++) {
                            if (Phantom.phantoms.get(i).getXy().size() > countOfApples) {
                                for (int j = 0; j < Phantom.phantoms.get(i).getXy().size(); j++) {
                                    if ((countOfApples<j+countOfApples/2) && (Phantom.phantoms.get(i).getXy().size() - j - countOfApples/2 > countOfApples || Phantom.phantoms.get(i).getXy().size() - j - countOfApples/2 <= 0)) {
                                        continue;
                                    }
                                    float xTarget = Phantom.phantoms.get(i).getXy().get(j)[0] - (float) getHeadXY().getX();
                                    float yTarget = Phantom.phantoms.get(i).getXy().get(j)[1] - (float) getHeadXY().getY();
                                    float Radian = (float) Math.atan2(xTarget, yTarget);
                                    if (Radian < 0) {
                                        Radian += 6.28;

                                    }
                                    double distance = getHeadXY().distance(Phantom.phantoms.get(i).getXy().get(j)[0], Phantom.phantoms.get(i).getXy().get(j)[1]);
                                    if (distance < 350 && Math.abs(Radian - tMouse) < 0.01) {

                                        part.setMouse((float) (Radian));
                                        throwPart();
//                                    System.out.println("SUCKS " + Phantom.phantoms.get(i).getXy().size() + " with number " + i );
//                                    System.out.println("Target radian: " + Radian + ", with number: " + tMouse );


                                    }
                                }
                            }
                        }
                    }
//
            }
//}else {
//    stepRad -= curRad;
//}

        }

        tMouse += stepRad;

        if (tMouse > 6.28) {
            tMouse -= 6.28;

        } else if (tMouse < 0) {

            tMouse += 6.28;
        }
        if (target != null) {
            if ((stepRadLast > 0 && radDir < 0) || (stepRadLast < 0 && radDir > 0)) {
                count = 0;
            }
            if (difRad > 0.05) {
                if (count < maxCount) {
                    canIncreaseSpeed = true;
                    count++;
                    if (step < maxStep) {
                        step += Math.abs(stepRad) / 10;
                    } else {
//                            System.out.println("Maximum!");
                    }
                    if (count == maxCount) {
//                        System.out.println("Maximum!");
                    }
                }
            } else {
                canIncreaseSpeed = false;
            }
        }

        stepRadLast = radDir;
    }

    public void increaseSpeed() {
        maxStep += 0.05;
    }

    public void decreaseSpeed() {
        maxStep -= 0.05;
    }

    double timeTo = trest.getMainTime();
    double timeToGrow = 0;
    public static float speedBoostTime = 0;
    static float speedBoostTimer = 0;

    public static void addSpeedTime(float time) {
        speedBoostTime += time;
    }

    public void moveCheck() {
        try {

            Movement.setZoom(1 - (float) xy.size() / 70);
            timeToGrow += trest.getMainTime() - timeTo;
            speedBoostTimer = (float) (trest.getMainTime() - timeTo);
            timeTo = trest.getMainTime();
            if (/*countOfApples<30 && */timeToGrow >= 10/*100.0 / countOfApples*/) {

                if (!trest.isEnd && !trest.stage.isBoss()) {
                    grow();
                }
                timeToGrow = 0;
            }


            try {
                if (speedBoostTime <= 0 || step < maxStep) {
                    speedBoost = false;
                }
                if (speedBoostTime > 0 && !speedBoost && step <= maxStep * 1.5) {
                    step *= 1.2f;
                }

                if (speedBoostTime > 0) {
                    speedBoostTime -= speedBoostTimer;
                    speedBoost = true;
                }
                if (step > minStep && !trest.mouseControl && !speedBoost) {
                    step *= 0.9999;
                } else if (step > minStep && !speedBoost) {// && !canIncreaseSpeed
                    if (!canIncreaseSpeed) {
                        step *= 0.99;                                              //TODO
                    } else {
                        step *= 0.999;
                    }
                }
                if (step < minStep) {
                    step = minStep;
                }


                double xTarget;
                double yTarget;
                if (trest.mouseControl) {
                    xTarget = trest.xMouse;
                    yTarget = trest.yMouse;
                } else {
                    xTarget = 0;
                    yTarget = 0;
                }
                if (trest.mouseControl) {
                    setRadian(new Point2D.Double(xTarget, yTarget));
                } else {
                    setRadian(null);
                }

//            selfStep = Math.random()*(step+ Math.sqrt(Math.pow(Math.abs(xTarget - x), 2) + Math.pow(Math.abs(yTarget - y), 2)) / 50);
                try {
                    pointWatch[0] = (float) ((step + secStep) * Math.sin(tMouse) + xy.get(0)[0]);
                    pointWatch[1] = (float) ((step + secStep) * Math.cos(tMouse) + xy.get(0)[1]);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                moveWithPhysics(pointWatch[0], pointWatch[1]);


            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void moveWithPhysics(float x, float y) {

        try {
            super.moveWithPhysics(x, y);
            if (!trest.isEnd) {
                checkForAbsorb();
            }
            if (trest.mouseControl) {
                float maxDistance = 80;
                float distance = (float) getHeadXY().distance(0, 0);
//                if (distance > maxDistance) {
                float angle = (float) Math.atan((-y) / (-x));
                double translocationX = distance / 5 * Math.cos(angle);
                double translocationY = distance / 5 * Math.sin(angle);
                if (-x < 0) {
                    direction = new float[]{(float) +translocationX, (float) +translocationY};
                } else {
                    direction = new float[]{(float) -translocationX, (float) -translocationY};
                }
//                } else {
//                    direction = new float[]{x / 20, y / 20};
//                }
            } else {
                direction = new float[]{0, 0};
            }


            maxSpeedRender.getModels().get(0).getMovement().setPosition(new Vector3f((float) xy.get(0)[0], (float) xy.get(0)[1], 0));
            maxSpeedRender.getModels().get(0).getMovement().setRotation(-tMouse);
            speedScale = ((step - (maxStep - maxStep * 0.1f)) / 0.2f);
            if (speedScale < 0) {
                speedScale = 0;
            }
            if (speedScale > 1) {
                curSpeed += 1 * 0.01;
            } else {
                curSpeed += speedScale * 0.01;
            }
            if (trest.isEnd) {
                speedScale = 0;
            }
            maxSpeedRender.setTime(trest.getMainTime());
            maxSpeedRender.setSpeedScale(speedScale);
            maxSpeedRender.setSpeed(curSpeed);

        } catch (Exception e) {
            e.printStackTrace();

        }
//xy.add(0,new int[]{x,y});
//xy.remove(xy.size()-1);
    }

    float speedScale = ((step - (maxStep - minStep)) / minStep);
    float curSpeed = 0;

    public void makePhysics() {
        for (int i = 0; i < xy.size(); i++) {

            for (int j = 1; j < xy.size(); j++) {
                if (j == i) {
                    continue;
                }
                float distance = (float) Math.sqrt(Math.pow(xy.get(i)[0] - xy.get(j)[0], 2) + Math.pow(xy.get(i)[1] - xy.get(j)[1], 2));
                float distanceDif = size - distance;
                float angle;
                if (distance != 0 && distance < size * 1.5) {
                    if (i == 0 && trest.mouseControl && step > minStep) {
                        step *= 0.99;
                    }
                    angle = (float) Math.atan((xy.get(i)[1] - xy.get(j)[1]) / (xy.get(i)[0] - xy.get(j)[0]));
                    if (xy.get(i)[0] - xy.get(j)[0] < 0) {
                        xy.set(j, new float[]{xy.get(i)[0] + (float) ((size) * Math.cos(angle)), xy.get(i)[1] + (float) ((size) * Math.sin(angle))});

                    } else {

                        xy.set(j, new float[]{xy.get(i)[0] - (float) ((size) * Math.cos(angle)), xy.get(i)[1] - (float) ((size) * Math.sin(angle))});

                    }

                }
                deepPhysic();
            }

        }
    }

    public void checkForAbsorb() {

        for (GluePart part : GluePart.glueParts) {
            if (part.gluePartHeadXY().distance(getHeadXY()) < 200 && part.getXy().size() > 1) {
                boolean eat = false;
                for (int i = 1; i < part.xy.size(); i++) {
                    Point2D partPoint = new Point2D.Float(part.xy.get(i)[0], part.xy.get(i)[1]);

                    if (getHeadXY().distance(partPoint) < size) {
                        //Добавляем ячейку игроку
                        //Создаем новую часть игрока с координатами откушеной части

                        grow();
                        ArrayList<float[]> newPart = new ArrayList<>();
                        if (part.xy.size() - 1 != i) {
                            for (int j = i; j < part.xy.size(); j++) {
                                newPart.add(new float[]{part.xy.get(j)[0], part.xy.get(j)[1]});
                                part.minusCell(j);

                                j--;
                            }
                            newPart.remove(0);
                            new GluePart(window, newPart);
                        } else {
                            part.minusCell(part.xy.size() - 1);
                        }

                        return;
                    }

                }

            }
        }

    }

    private float secStep = 0;

    public void hold() {
        if (!trest.mouseControl) {
            if (window.getInput().isMouseButtonDown(GLFW_MOUSE_BUTTON_1)) {
                if (step < maxStep * 2) {
                    step += 0.01;
//                    secStep+=0.01;
                }
            }
        } else {
//            if(secStep>0){
//                step-=0.1;
//                secStep-=0.01;
//            }
        }

    }


}
