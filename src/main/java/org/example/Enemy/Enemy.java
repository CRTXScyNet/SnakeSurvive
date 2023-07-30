package org.example.Enemy;

import org.example.Painter.Apple;

import org.example.Player.Player;
import org.example.Sound.MainSoundsController;
import org.example.gpu.render.Window;
import org.example.gpu.render.Model;
import org.example.gpu.render.ModelRendering;
import org.example.gpu.gameProcess.trest;
import org.joml.Vector3f;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;

public class Enemy extends Entity {
    private int width;
    private int height;
    private static final double innerPlace = 1;
    private static final double exteriorBorder = (1 - innerPlace) / 2;
    static final double[] playGround = new double[]{innerPlace, exteriorBorder};
    private boolean isScared = false;
    public Enemy(Window window, boolean isActive) {
        this.window = window;
        Point co = getRandomPoint();
        xy.add(new float[]{co.x, co.y});
        setPhantomXY();
        enemyHead.setLocation(xy.get(0)[0],xy.get(0)[1]);
        enemies.add(this);


        this.isActive = isActive;
        if (!isActive) {
            delay = (int) (delayStat - Math.random() * (delayStat / 4));
            delayCount = delay;
            color = new Color((int) (Math.random() * 50 + 200), (int) (Math.random() * 50 + 200), (int) (Math.random() * 50 + 200));
        } else {
            delay = (int) delayStatActive;
            delayDouble = delayStatActive;
            delayCount = delay;
            activeEnemies.add(this);
            color = new Color(150, 150, 255);
        }
        rendering = new ModelRendering(window,   this, "enemy");
        rendering.addModel(new Model(window, (int) (size * 30),color));
        rendering.getModels().get(0).getMovement().setPosition(new Vector3f((float) co.x, (float) co.y, 0));
        for (int i = 0; i < snakeLength; i++) {
            addCircle();
        }
    }
    public void moveXy() {
float halfX = -window.width/2f;
        float halfY = -window.height/2f;
        float x = (float)enemyHead.getX();
        float y = (float)enemyHead.getY();
        if (x < -halfX) {
            moveXy(new float[]{halfX*2, 0});
        }
        if (x > halfX) {
            moveXy(new float[]{-halfX*2, 0});
        }
        if (y < -halfY) {
            moveXy(new float[]{0, halfY*2});
        }
        if (y > halfY) {
            moveXy(new float[]{0, -halfY*2});
        }
    }

    public void moveXy(float[] direct) {

        for (int i = 0; i < xy.size(); i++) {
            xy.set(i, new float[]{xy.get(i)[0] - direct[0], xy.get(i)[1] - direct[1]});
        }
        enemyHead.setLocation(xy.get(0)[0],xy.get(0)[1]);
        for (int i = 0; i < phantomXY.size(); i++) {
            phantomXY.set(i, new float[]{phantomXY.get(i)[0] - direct[0], phantomXY.get(i)[1] - direct[1]});
        }
        for (int i = 0; i < rendering.getModels().size(); i++) {
            rendering.getModels().get(i).
                    getMovement().setPosition(new Vector3f(phantomXY.get(i)[0], phantomXY.get(i)[1], 0));
        }
    }
    public void removeEnemy(){
        rendering.clear(true);
        phantomXY.clear();
        enemies.remove(this);

    }

    public void teleportXy(float[] direct) {


        xy.set(0, new float[]{direct[0], direct[1]});
        enemyHead.setLocation(xy.get(0)[0],xy.get(0)[1]);

        phantomXY.set(0, new float[]{direct[0], direct[1]});


        rendering.getModels().get(0).
                getMovement().setPosition(new Vector3f(phantomXY.get(0)[0], phantomXY.get(0)[1], 0));


    }
public Point2D enemyHead = new Point2D.Float();
    private ArrayList<float[]> xy = new ArrayList<>();
    private ArrayList<float[]> directionOfPhantomXY = new ArrayList<>();
    private ArrayList<float[]> phantomXY = new ArrayList<>();

    public void setPhantomXY() {
        phantomXY.clear();
        directionOfPhantomXY.clear();
        for (float[] p : xy) {
            directionOfPhantomXY.add(new float[]{0, 0});
            phantomXY.add(new float[]{p[0], p[1]});
        }

    }
    public void setXy(){
        for (int i = 0; i < phantomXY.size(); i++) {
            xy.set(i, phantomXY.get(i));
        }

    }


    public static ArrayList<Enemy> enemies = new ArrayList<>();
    public static ArrayList<Enemy> activeEnemies = new ArrayList<>();
    private Color color = new Color(Color.white.getRGB());

    static float size = 5;
    static double stepOfSize = 2;


    public static int step = (int) (Enemy.getSize() * stepOfSize);
   public static double delayStat = 30;
    static double delayStatActive = 20; //TODO
    private double delayDouble = delayStat;
    private int delay = (int) delayDouble;
    private int delayCount = 0;
    private static double timerForRestart = 10;
    private static double timerStat = timerForRestart;
    private double timer = timerStat;


    public void setEatAndAngry(boolean eatAndAngry) {
        this.eatAndAngry = eatAndAngry;
        timer = timerStat;
    }

    public boolean isEatAndAngry() {
        return eatAndAngry;
    }

    private boolean eatAndAngry = false;

    public double getDelay() {
        return delay;
    }

    public void setCurrentDelay(double delay) {
        delayDouble = delay;
        this.delay = (int) delayDouble;

    }
public void fear(){

        isScared = true;

}
    public void unFear(){

            isScared = false;

    }
    public void setDelay() {
        if (delay < 1000) {
            if (!isActive) {
                delayDouble += 1;
                this.delay = (int) delayDouble;
            } else {
                if (delay > 3) {
                    delayDouble -= delayDouble * 0.2;
                    this.delay = (int) delayDouble;
                }
            }

        }
    }

    private int reverseCount = 10;
    private int reverse = reverseCount;

    public static ArrayList<Boolean> snakeIsReady = new ArrayList<>();


    private boolean isMove = false;


    public static int snakeLength = 10;

    public boolean isActive = false;
    public boolean isGrow = false;
    public boolean reset = false;
    private Window window;


    public ModelRendering getRendering() {
        return rendering;
    }

    private ModelRendering rendering;



    public ArrayList<float[]> getXy() {
        return xy;
    }

    public ArrayList<float[]> getPhantomXY() {
        return phantomXY;
    }

    public void addCircle() {
        if (xy.size() < maxSize) {
            float x = xy.get(xy.size() - 1)[0];
            float y = xy.get(xy.size() - 1)[1];
            xy.add(new float[]{x, y});
            float xp = phantomXY.get(phantomXY.size() - 1)[0];
            float yp = phantomXY.get(phantomXY.size() - 1)[1];
            phantomXY.add(new float[]{xp, yp});
            directionOfPhantomXY.add(new float[]{0, 0});
            rendering.addModel(new Model(window, (int) (size * 30),color));
            rendering.getModels().get(xy.size() - 1).getMovement().setPosition(new Vector3f(xp, yp, 0));
        }
    }

    public void grow() {
        for (int i = 0; i < 1; i++) {
            addCircle();
        }
    }

    public void reset() {
        Point co = getRandomPoint();
        hunt = false;
        eatAndAngry = false;
        timer = timerForRestart;
        if (isActive) {
            delayDouble = delayStatActive;
            delay = (int) delayStatActive;
        } else {
            delayDouble = delayStat;
            delay = (int) delayStat;
        }

        xy.clear();
        xy.add(new float[]{co.x, co.y});
        setPhantomXY();
        enemyHead.setLocation(xy.get(0)[0],xy.get(0)[1]);
        rendering.clear(true);
        rendering.addModel(new Model(window, (int) (size * 30),color));
        rendering.getModels().get(0).getMovement().setPosition(new Vector3f((float) co.x, (float) co.y, 0));
        move(co.x, co.y);
        for (int i = 0; i < snakeLength; i++) {
            addCircle();
        }

    }

    public Point getRandomPoint() {
        int x = (int) (-(window.width/2) + ((int) (Math.random() * ((window.width) / (int) (size * stepOfSize))) * (int) (size * stepOfSize)));
        int y = (int) (-(window.height/2) + ((int) (Math.random() * ((window.height) / (int) (size * stepOfSize))) * (int) (size * stepOfSize)));
        if (Math.pow(Math.abs(x), 2) + Math.pow(Math.abs(y), 2) <= Math.pow(500, 2)) {
            return getRandomPoint();
        }
        return new Point(x, y);
    }

    public Color getColor() {
        return color;
    }

    public static double getSize() {
        return size;
    }

    public void setReset(boolean reset) {
        this.reset = reset;
    }

    public boolean isReset() {
        return reset;
    }

    public boolean hunt = false;
private double bufDelay = 0;
private Point2D apple = new Point2D.Float();
public void update(){

    if (xy.size() == 0) {
        return;
    }
    if(Math.sqrt(Math.pow(enemyHead.getX(),2)+Math.pow(enemyHead.getY(),2))<300){
        MainSoundsController.white_snakes_bool = true;
    }else {
        MainSoundsController.empty_space_bool = true;
    }
    boolean isNearby = false;
    boolean appleIsNearby = false;
    float[] nearPoint = new float[2];
    int rad;
    if(isActive){
        rad = 300;
    }else {
        rad = 500;
    }

    int playerRad = 150;
    float length = 0;
    nearPoint = new float[2];
    float appleDest = (float) Math.sqrt(Math.pow(Apple.getXy()[0] - xy.get(0)[0], 2) + Math.pow(Apple.getXy()[1] - xy.get(0)[1], 2));
    if (appleDest <= rad) {
        appleIsNearby = true;
        apple.setLocation(Apple.getXy()[0],Apple.getXy()[1]);
    }else {

        Point2D phantomApple = new Point2D.Float(Apple.getXy()[0],Apple.getXy()[1]);
        if (Math.abs(enemyHead.getX()- Apple.getXy()[0])>trest.playGroundWidth/2) {
            if (enemyHead.getX() < Apple.getXy()[0]) {
                phantomApple.setLocation(Apple.getXy()[0] - trest.playGroundWidth, phantomApple.getY());
            } else if (enemyHead.getX() > Apple.getXy()[0]) {
                phantomApple.setLocation(Apple.getXy()[0] + trest.playGroundWidth, phantomApple.getY());
            }
        }
        if (Math.abs(enemyHead.getY()- Apple.getXy()[1])>trest.playGroundHeight/2) {
            if (enemyHead.getY() < Apple.getXy()[1]) {
                phantomApple.setLocation(phantomApple.getX(), Apple.getXy()[1] - trest.playGroundHeight);
            } else if (enemyHead.getY() > Apple.getXy()[1]) {
                phantomApple.setLocation(phantomApple.getX(), Apple.getXy()[1] + trest.playGroundHeight);
            }
        }
        if(phantomApple.distance(enemyHead)<=rad){
            appleIsNearby = true;
            apple.setLocation(phantomApple);
        }
    }
    try {

        if(!trest.isEnd){
            boolean isBreak = false;
            for (float[] ePoint : getPhantomXY()) {
                for (int j = 0; j < Player.player.getXy().size(); j++) {
                    float dest = (float) Math.sqrt(Math.pow(Player.player.getXy().get(j)[0] - ePoint[0], 2) + Math.pow(Player.player.getXy().get(j)[1] - ePoint[1], 2));

                    if (dest <= getSize() + Player.getSize() - 1) {
                        if (!trest.isEnd && !trest.immortal) {
trest.end();
                            isBreak = true;
                            break;
                        }

                    }
                    if (dest <= playerRad) {

                        if (length == 0) {
                            length = dest;
                            nearPoint = new float[]{Player.player.getXy().get(j)[0], Player.player.getXy().get(j)[1]};
                        } else if (length > dest) {
                            length = dest;
                            nearPoint = new float[]{Player.player.getXy().get(j)[0], Player.player.getXy().get(j)[1]};
                        }
                        isNearby = true;
                    }
                }
                if (isBreak) {
                    break;
                }
            }
        }
        unFear();
        if (isNearby && trest.enemyScared) {
            fear();
            nearPoint = new float[]{xy.get(0)[0] * 2, xy.get(0)[1] * 2};
            moveCheck(nearPoint, appleIsNearby, true);
        } else if (isEatAndAngry() || trest.isEnd) {
            MainSoundsController.purple_snakes_hunting_bool = true;
            moveCheck(Player.player.getXy().get(0), appleIsNearby, true);

        } else if (isNearby) {
            MainSoundsController.white_snakes_hunting_bool = true;
            moveCheck(nearPoint, appleIsNearby, true);

        } else {
            moveCheck(new float[2], appleIsNearby, false);
        }
        if(!eatAndAngry) {
            if (Apple.appleVisible && isActive && Apple.checkCollision(xy.get(0))) {
                grow();
                setEatAndAngry(true);
                setCurrentDelay((int) (getDelay() / 2));
            }
        }
    } catch (Exception e) {

    }
}
public static void resetAngryTimer(){
    timerStat = timerForRestart;
}

    public void moveCheck(float[] point,boolean appleIsNearby, boolean isNearby) {


        if (delayCount < delay) {                    //проверка прошло ли достаточно времени, чтобы делать новый шаг
            delayCount++;
            movePhantom();
//                setLines();
            if (reverse < reverseCount) {                    //проверка прошло ли достаточно времени с прошлого разворота
                reverse++;
            }
            return;
        }
        if (eatAndAngry) {
            timer -= 1;
        }
        setPhantomXY();
        if(delay == 0){
            movePhantom();
        }
        if(!isActive&&!trest.isEnd){
            if (isNearby&&isScared) {
                delay = (int)(size*stepOfSize/(Player.step/2));
            } else {
                delay = (int) delayStat;
            }
        }else if(isActive && !trest.isEnd){
            if (isNearby&&isScared) {
                if(bufDelay == 0){
                    bufDelay = delayDouble;
                    delayDouble = (int)(size*stepOfSize/(Player.step/2));
                    delay = (int)delayDouble;
                }
            } else {
                if(bufDelay!= 0) {
                    delayDouble = bufDelay;
                    delay = (int)delayDouble;
                    bufDelay = 0;
                }
            }
        }
        if (eatAndAngry && timer <= 0) {
            timerStat += timerForRestart;
            timer = timerStat;
            eatAndAngry = false;
            setCurrentDelay(getDelay() * 2);
//            setDelay();
            hunt = false;
        }

        if (reverse < reverseCount) {                                       //проверка прошло ли достаточно времени с прошлого разворота
            reverse++;
        }

        delayCount = 0;
        float xTarget;
        float yTarget;
        if (!isActive) {
            if (isNearby) {

                xTarget = point[0];
                yTarget = point[1];
            } else if (Apple.appleVisible&&appleIsNearby) {
                if(enemyHead.distance(apple)>150){
                    xTarget = (float) apple.getX();
                    yTarget = (float) apple.getY();
                }else {
                    xTarget = xy.get(0)[0] + (xy.get(0)[0] - (float) apple.getX());
                    yTarget = xy.get(0)[1] + (xy.get(0)[1] - (float) apple.getY());
                }

            } else {

                xTarget = xy.get(0)[0];
                yTarget = xy.get(0)[1];
                int course = (int) (Math.random() * 4);
                switch (course) {
                    case 0:
                        xTarget += step;
                        break;
                    case 1:
                        xTarget -= step;
                        break;
                    case 2:
                        yTarget += step;
                        break;
                    case 3:
                        yTarget -= step;
                        break;
                }
            }
        } else {
            if (eatAndAngry) {
                hunt = true;
                xTarget = point[0];
                yTarget = point[1];
            } else if (Apple.appleVisible&&appleIsNearby) {
                xTarget = (float) apple.getX();
                yTarget = (float) apple.getY();
            } else {
                xTarget = xy.get(0)[0];
                yTarget = xy.get(0)[1];
                int course = (int) (Math.random() * 4);
                switch (course) {
                    case 0:
                        xTarget += step;
                        break;
                    case 1:
                        xTarget -= step;
                        break;
                    case 2:
                        yTarget += step;
                        break;
                    case 3:
                        yTarget -= step;
                        break;
                }

            }

        }
        if(isScared && isNearby){
            xTarget = point[0];
            yTarget = point[1];
        }
        if (trest.isEnd) {
            hunt = true;
            if (trest.ringWayIsReady && !isActive) {

                Point nearest = new Point(0, 0);
                Point self = new Point((int) xy.get(0)[0], (int) xy.get(0)[1]);
                if (trest.toTargets.get(self) != null) {
                    nearest = trest.toTargets.get(self);
                }
                xTarget = nearest.x;
                yTarget = nearest.y;
            } else {
                xTarget = point[0];
                yTarget = point[1];
            }
        } else {

        }

        float x = getXy().get(0)[0];
        float y = getXy().get(0)[1];
Point2D target = new Point2D.Double(xTarget,yTarget);
        downNotSelf = true;
        upNotSelf = true;
        leftNotSelf = true;
        rightNotSelf = true;


          ArrayList<Point2D> doubles =  notSelf(x, y);

        if (!downNotSelf && !upNotSelf && !rightNotSelf && !leftNotSelf) {
            if (!reverse() && isTeleport() && trest.isEnd) {
                Point p = getRandomPoint();
                teleportXy(new float[]{p.x, p.y});

            }
            return;
        }
        Collections.shuffle(doubles);
        Point2D next = null;
        if(doubles.size()>0) {
            for (Point2D d : doubles) {
                if (next == null){
                    next = d;
                }else if (next.distance(target)> d.distance(target)){
                    next = d;
                }
            }
        }
        if(next != null){
move((float)next.getX(),(float)next.getY());
        }else {
            reverse();
        }

//            setLines();


    }

    public boolean isTeleport() {

        return Math.pow(xy.get(0)[0]- Player.playerHeadXY().getX(), 2) + Math.pow(xy.get(0)[1] - Player.playerHeadXY().getY(), 2) < Math.pow(30, 2);
    }

//    public void setLines() {
//        points.clear();
//        lines.clear();
//        if (Picture.rect.contains(new Point((int) phantomXY.get(0)[0], (int) phantomXY.get(0)[1]))) {
//            points.add(new Point((int) phantomXY.get(0)[0], (int) phantomXY.get(0)[1]));
//            for (int i = 1; i < phantomXY.size() - 1; i++) {
//                if ((int) phantomXY.get(i - 1)[0] != (int) phantomXY.get(i + 1)[0] && (int) phantomXY.get(i - 1)[1] != (int) phantomXY.get(i + 1)[1]) {
//                    points.add(new Point((int) phantomXY.get(i)[0], (int) phantomXY.get(i)[1]));
//                }
//
//
//            }
//            points.add(new Point((int) phantomXY.get(phantomXY.size() - 1)[0], (int) phantomXY.get(phantomXY.size() - 1)[1]));
//            Point t = null;
//
//            for (int i = 0; i < points.size(); i++) {
//                if (t == null) {
//                    t = points.get(i);
//                } else {
//                    lines.add(new Line(t, points.get(i)));
//                    t = new Point(points.get(i));
//                }
//            }
//
//        }
//    }

    private boolean downNotSelf = false;
    private boolean upNotSelf = false;
    private boolean leftNotSelf = false;
    private boolean rightNotSelf = false;

    private void lastWay(float x, float y, boolean up, boolean down, boolean right, boolean left) {

        try {
            ArrayList<float[]> variants = new ArrayList<>(/*Arrays.asList(new int[]{x, y - step},new int[]{x, y + step},new int[]{x + step, y},new int[]{x - step, y})*/);
            if (up) {
                variants.add(new float[]{x, y - step});
            }
            if (down) {
                variants.add(new float[]{x, y + step});
            }
            if (right) {
                variants.add(new float[]{x + step, y});
            }
            if (left) {
                variants.add(new float[]{x - step, y});
            }
            Collections.shuffle(variants);
            if (xy.size() > 1) {
                for (float[] v : variants) {
                    boolean isClear = false;
                    for (float[] i : xy) {
                        if (i[0] == v[0] && i[1] == v[1]) {
                            isClear = false;
                            break;
                        } else {
                            isClear = true;
                        }

                    }
                    if (isClear) {
//TODO

                        for (Enemy snake1 : Enemy.enemies) {
                            if (!this.equals(snake1)) {
                                for (float[] i : snake1.getXy()) {
                                    if (i[0] == v[0] && i[1] == v[1]) {
                                        isClear = false;
                                        break;
                                    } else {
                                        isClear = true;
                                    }
                                }
                                if (!isClear) {
                                    break;
                                }
                            }
                        }
                    }
                    if (isClear) {
                        move(v[0], v[1]);
                        return;
                    }
                }
            }

            if (!reverse() && isTeleport()) {

                Point xy = getRandomPoint();
                move(xy.x, xy.y);
            }
            for (int i = 0; i < directionOfPhantomXY.size(); i++) {
                directionOfPhantomXY.set(i, new float[]{0, 0});

            }


        } catch (Exception e) {
            e.printStackTrace();
//            System.out.println(e);
        }
    }

    //    downNotSelf = notSelf(x, y + step);
//    upNotSelf = notSelf(x, y - step);
//    leftNotSelf = notSelf(x - step, y);
//    rightNotSelf = notSelf(x + step, y);
    private ArrayList<Point2D> notSelf(float x, float y) {
        ArrayList<Point2D> doubles = new ArrayList<>();
        for (float[] i : getXy()) {
            if (Math.abs(i[0] - (x + step)) < step * 0.8 && Math.abs(i[1] - y) < step * 0.8) {
                rightNotSelf = false;
            } else if (Math.abs(i[0] - x) < step * 0.8 && Math.abs(i[1] - (y + step)) < step * 0.8) {
                downNotSelf = false;
            } else if (Math.abs(i[0] - (x - step)) < step * 0.8 && Math.abs(i[1] - y) < step * 0.8) {
                leftNotSelf = false;
            } else if (Math.abs(i[0] - x) < step * 0.8 && Math.abs(i[1] - (y - step)) < step * 0.8) {
                upNotSelf = false;
            }
        }
        for (Enemy snake1 : Enemy.enemies) {
            if (!this.equals(snake1)) {
                for (float[] i : snake1.getXy()) {
                    if (Math.abs(i[0] - (x + step)) < step * 0.8 && Math.abs(i[1] - y) < step * 0.8) {
                        rightNotSelf = false;
                    } else if (Math.abs(i[0] - x) < step * 0.8 && Math.abs(i[1] - (y + step)) < step * 0.8) {
                        downNotSelf = false;
                    } else if (Math.abs(i[0] - (x - step)) < step * 0.8 && Math.abs(i[1] - y) < step * 0.8) {
                        leftNotSelf = false;
                    } else if (Math.abs(i[0] - x) < step * 0.8 && Math.abs(i[1] - (y - step)) < step * 0.8) {
                        upNotSelf = false;
                    }
                }
            }
        }

        if (rightNotSelf) {
            doubles.add(new Point2D.Float(x + step,y));
        }
        if (leftNotSelf) {
            doubles.add(new Point2D.Float(x - step,y));
        }
        if (upNotSelf) {
            doubles.add(new Point2D.Float(x,y - step));
        }
        if (downNotSelf) {
            doubles.add(new Point2D.Float(x,y + step));
        }
        return doubles;
    }
    public void makeChainTogether(){
        for (int i = 0; i < phantomXY.size() - 1; i++) {
            float distance = (float) Math.sqrt(Math.pow(phantomXY.get(i + 1)[0] - phantomXY.get(i)[0], 2) + Math.pow(phantomXY.get(i + 1)[1] - phantomXY.get(i)[1], 2));
            float angle;
            if (distance > size*2) {
                angle = (float) Math.atan((phantomXY.get(i)[1] - phantomXY.get(i + 1)[1]) / (phantomXY.get(i)[0] - phantomXY.get(i + 1)[0]));
                if (phantomXY.get(i)[0] - phantomXY.get(i + 1)[0] < 0) {
                    phantomXY.set(i + 1, new float[]{phantomXY.get(i)[0] + (float) (size*2 * Math.cos(angle)), phantomXY.get(i)[1] + (float) (size*2 * Math.sin(angle))});
                } else {
                    phantomXY.set(i + 1, new float[]{phantomXY.get(i)[0] - (float) (size*2 * Math.cos(angle)), phantomXY.get(i)[1] - (float) (size*2 * Math.sin(angle))});
                }
            }

        }
//        for (int i = 0; i < phantomXY.size(); i++) {
//            phantomXY.set(i,xy.get(i));
//        }
    }

    public void movePhantom() {
        try {
//            tailless = new int[]{phantomXY.get(phantomXY.size() - 1).x, phantomXY.get(phantomXY.size() - 1).y};
            for (int i = 0; i < phantomXY.size(); i++) {
                try {
                    phantomXY.get(i)[0] +=
                            directionOfPhantomXY.get(i)[0];
                    phantomXY.get(i)[1] +=
                            directionOfPhantomXY.get(i)[1];
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    System.out.println("phantom size: " + phantomXY.size() + ",direction size: " + directionOfPhantomXY.size());
                }
            }
            for (int i = 0; i < rendering.getModels().size(); i++) {
                try {
                    rendering.getModels().get(i).
                            getMovement().setPosition(new Vector3f(phantomXY.get(i)[0], phantomXY.get(i)[1], 0));
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

    }

    public void move(float x, float y) {
        setPhantomXY();
        try {

            for (int i = xy.size() - 1; i > 0; i--) {
                try {
//                    if (i == xy.size() - 1) {
//                        if (Math.abs(((double) xy.get(i)[0] - phantomXY.get(i)[0])) > step || Math.abs(((double) xy.get(i)[1] - phantomXY.get(i)[1])) > step) {
////                            directionOfPhantomXY.add(0, new double[]{0, 0});
////                            phantomXY.get(i)[0] = xy.get(i).x;
////                            phantomXY.get(i)[1] = xy.get(i).y;
//                        } else {
//
////                            phantomXY.get(i)[0] = xy.get(i)[0];
////                            phantomXY.get(i)[1] = xy.get(i)[1];
//                        }
//                    }else if(i!=0){
                    xy.get(i)[0] = xy.get(i - 1)[0];
                    xy.get(i)[1] = xy.get(i - 1)[1];
                    directionOfPhantomXY.set(i, new float[]{(xy.get(i)[0] - phantomXY.get(i)[0]) / (delay + 1), (xy.get(i)[1] - phantomXY.get(i)[1]) / (delay + 1)});
//                    }
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
//            phantomXY.get(0)[0] = xy.get(0)[0];
//            phantomXY.get(0)[1] = xy.get(0)[1];
            xy.get(0)[0] = x;
            xy.get(0)[1] = y;

            directionOfPhantomXY.set(0, new float[]{(xy.get(0)[0] - phantomXY.get(0)[0]) / (delay + 1), (xy.get(0)[1] - phantomXY.get(0)[1]) / (delay + 1)});
            enemyHead.setLocation(xy.get(0)[0],xy.get(0)[1]);
        } catch (Exception e) {
            e.printStackTrace();

        }
//xy.add(0,new int[]{x,y});
//xy.remove(xy.size()-1);
    }

    ArrayList<Point> points = new ArrayList<>();
    public ArrayList<Line> lines = new ArrayList<>();

    public boolean reverse() {
        try {
            if (reverse < reverseCount) {

                return false;
            } else {
                reverse = 0;

                setPhantomXY();
                Collections.reverse(xy);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }
    public void enemyAbsorbed(){
xy.clear();
rendering.clear(true);
phantomXY.clear();
directionOfPhantomXY.clear();
amountOfAbsorbedEnemies++;
enemies.remove(this);
    }
public static int amountOfAbsorbedEnemies = 0;
    static int maxSize = 60;

}
