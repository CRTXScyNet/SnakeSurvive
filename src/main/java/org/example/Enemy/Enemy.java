package org.example.Enemy;

import org.example.Painter.Picture;
import org.example.Painter.Process;
import org.example.gpu.Window;
import org.example.gpu.render.Model;
import org.example.gpu.render.ModelRendering;
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

    public void moveXy(float[] direct) {

        for (int i = 0; i < xy.size(); i++) {
            xy.set(i, new float[]{xy.get(i)[0] - direct[0], xy.get(i)[1] - direct[1]});
        }
        for (int i = 0; i < phantomXY.size(); i++) {
            phantomXY.set(i, new float[]{phantomXY.get(i)[0] - direct[0], phantomXY.get(i)[1] - direct[1]});
        }
        for (int i = 0; i < rendering.getModels().size(); i++) {
            rendering.getModels().get(i).
                    getMovement().setPosition(new Vector3f((float) phantomXY.get(i)[0], (float) phantomXY.get(i)[1], 0));
        }
    }

    public void teleportXy(float[] direct) {


        xy.set(0, new float[]{direct[0], direct[1]});


        phantomXY.set(0, new float[]{direct[0], direct[1]});


        rendering.getModels().get(0).
                getMovement().setPosition(new Vector3f((float) phantomXY.get(0)[0], (float) phantomXY.get(0)[1], 0));


    }

    private ArrayList<float[]> xy = new ArrayList<>();
    private ArrayList<float[]> directionOfPhantomXY = new ArrayList<>();
    private ArrayList<float[]> phantomXY = new ArrayList<>();

    private void setPhantomXY() {
        phantomXY.clear();
        directionOfPhantomXY.clear();
        for (float[] p : xy) {
            directionOfPhantomXY.add(new float[]{0, 0});
            phantomXY.add(new float[]{p[0], p[1]});
        }
    }


    public static ArrayList<Enemy> enemies = new ArrayList<>();
    public static ArrayList<Enemy> activeEnemies = new ArrayList<>();
    private Color color = new Color(Color.white.getRGB());

    static double size = 5;
    static double stepOfSize = 2;


    public static int step = (int) (Enemy.getSize() * stepOfSize);
    static double delayStat = 200;
    static double delayStatActive = 75;
    private double delayDouble = delayStat;
    private int delay = (int) delayDouble;
    private int delayCount = 0;
    private double timerStat = 10;
    private double timer = timerStat;


    public void setEatAndAngry(boolean eatAndAngry) {
        this.eatAndAngry = eatAndAngry;
    }

    public boolean isEatAndAngry() {
        return eatAndAngry;
    }

    private boolean eatAndAngry = false;
//    public void setRegion(SearchRegion region) {
//        this.region = region;
//    }
//
//    public SearchRegion getRegion() {
//        return region;
//    }
//
//    private SearchRegion region;


    public double getDelay() {
        return delay;
    }

    public void setCurrentDelay(double delay) {
        delayDouble = delay;
        this.delay = (int) delayDouble;

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


    private ModelRendering rendering;

    public Enemy(Window window, boolean isActive) {
        this.window = window;
        Point co = getRandomPoint();
        xy.add(new float[]{co.x, co.y});
        setPhantomXY();

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
        rendering = new ModelRendering(window, color, false, this, "enemy");
        rendering.addModel(new Model(window, (int) (size * 30)));
        rendering.getModels().get(0).getMovement().setPosition(new Vector3f((float) co.x, (float) co.y, 0));
        for (int i = 0; i < snakeLength; i++) {
            addCircle();
        }
    }

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
            rendering.addModel(new Model(window, (int) (size * 30)));
            rendering.getModels().get(xy.size() - 1).getMovement().setPosition(new Vector3f((float) xp, (float) yp, 0));
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
        timer = timerStat;
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
        rendering.clear();
        rendering.addModel(new Model(window, (int) (size * 30)));
        rendering.getModels().get(0).getMovement().setPosition(new Vector3f((float) co.x, (float) co.y, 0));
        move(co.x, co.y);
        for (int i = 0; i < snakeLength; i++) {
            addCircle();
        }

    }

    public Point getRandomPoint() {
        int x = (int) (-(window.width) + ((int) (Math.random() * ((window.width * 2) / (int) (size * stepOfSize))) * (int) (size * stepOfSize)));
        int y = (int) (-(window.height) + ((int) (Math.random() * ((window.height * 2) / (int) (size * stepOfSize))) * (int) (size * stepOfSize)));
        if (Math.pow(Math.abs(x), 2) + Math.pow(Math.abs(y), 2) <= Math.pow(300, 2)) {
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


    public void moveCheck(float[] point, float[] apple, boolean isNearby) {


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
        movePhantom();
        if (eatAndAngry && timer <= 0) {
            timerStat *= 2;
            timer = timerStat;
            eatAndAngry = false;
            setCurrentDelay(getDelay() * 2);
            setDelay();
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
            } else if (!Process.appleVisible) {
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
            } else {
                xTarget = apple[0];
                yTarget = apple[1];
            }

        }
        if (Process.isEnd) {
            hunt = true;
            if (Process.ringWayIsReady && !isActive) {

                Point nearest = new Point(0, 0);
                Point self = new Point((int) xy.get(0)[0], (int) xy.get(0)[1]);
                if (Process.toTargets.get(self) != null) {
                    nearest = Process.toTargets.get(self);
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
            if (!reverse() && isTeleport() && Process.isEnd) {
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

        return Math.pow(xy.get(0)[0], 2) + Math.pow(xy.get(0)[1], 2) < Math.pow(30, 2);
    }

    public void setLines() {
        points.clear();
        lines.clear();
        if (Picture.rect.contains(new Point((int) phantomXY.get(0)[0], (int) phantomXY.get(0)[1]))) {
            points.add(new Point((int) phantomXY.get(0)[0], (int) phantomXY.get(0)[1]));
            for (int i = 1; i < phantomXY.size() - 1; i++) {
                if ((int) phantomXY.get(i - 1)[0] != (int) phantomXY.get(i + 1)[0] && (int) phantomXY.get(i - 1)[1] != (int) phantomXY.get(i + 1)[1]) {
                    points.add(new Point((int) phantomXY.get(i)[0], (int) phantomXY.get(i)[1]));
                }


            }
            points.add(new Point((int) phantomXY.get(phantomXY.size() - 1)[0], (int) phantomXY.get(phantomXY.size() - 1)[1]));
            Point t = null;

            for (int i = 0; i < points.size(); i++) {
                if (t == null) {
                    t = points.get(i);
                } else {
                    lines.add(new Line(t, points.get(i)));
                    t = new Point(points.get(i));
                }
            }

        }
    }

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
                            getMovement().setPosition(new Vector3f((float) phantomXY.get(i)[0], (float) phantomXY.get(i)[1], 0));
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
                    directionOfPhantomXY.set(i, new float[]{((float) xy.get(i)[0] - phantomXY.get(i)[0]) / (delay + 1), ((float) xy.get(i)[1] - phantomXY.get(i)[1]) / (delay + 1)});
//                    }
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
//            phantomXY.get(0)[0] = xy.get(0)[0];
//            phantomXY.get(0)[1] = xy.get(0)[1];
            xy.get(0)[0] = x;
            xy.get(0)[1] = y;

            directionOfPhantomXY.set(0, new float[]{((float) xy.get(0)[0] - phantomXY.get(0)[0]) / (delay + 1), ((float) xy.get(0)[1] - phantomXY.get(0)[1]) / (delay + 1)});

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

    static int maxSize = 60;

}
