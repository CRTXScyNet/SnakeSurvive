package org.example.Enemy;

import org.example.Painter.Picture;
import org.example.Painter.Process;

import java.awt.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class Enemy {
    private int width = Picture.width;
    private int height =Picture.height;
    private static final double innerPlace = 1;
    private static final double exteriorBorder = (1 - innerPlace) / 2;
    static final double[] playGround = new double[]{innerPlace, exteriorBorder};

    public void moveXy(double[] direct) {
        for (int i = 0; i < xy.size(); i++) {
            xy.set(i, new double[]{xy.get(i)[0] - direct[0], xy.get(i)[1] - direct[1]});
        }
        for (int i = 0; i < phantomXY.size(); i++) {
            phantomXY.set(i, new double[]{phantomXY.get(i)[0] - direct[0], phantomXY.get(i)[1] - direct[1]});
        }


    }

    private ArrayList<double[]> xy = new ArrayList<>();
    private ArrayList<double[]> directionOfPhantomXY = new ArrayList<>();
    private ArrayList<double[]> phantomXY = new ArrayList<>();

    private void setPhantomXY() {
        phantomXY.clear();
        for (double[] p : xy) {
            phantomXY.add(new double[]{p[0], p[1]});
        }
    }


    public static ArrayList<Enemy> enemies = new ArrayList<>();
    public static ArrayList<Enemy> activeEnemies = new ArrayList<>();
    private Color color = new Color(Color.white.getRGB());

    static double size = 5;
    static double stepOfSize = 2;


    public static int step = (int) (Enemy.getSize() * stepOfSize);
    static double delayStat = 40;
    static double delayStatActive = 15;
    private double delayDouble = delayStat;
    private int delay = (int) delayDouble;
    private int delayCount = 0;
    private double timerStat = 1000;
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
        if (delay < 100) {
            if (!isActive) {
                delayDouble += 1;
                this.delay = (int) delayDouble;
            } else {
                if (delay > 3) {
                    delayDouble -= 1;
                    this.delay = (int) delayDouble;
                }
            }

        }
    }

    private int reverseCount = 10;
    private int reverse = reverseCount;

    public static ArrayList<Boolean> snakeIsReady = new ArrayList<>();


    static boolean isTeleport = false;


    private boolean isMove = false;


    public static int snakeLength = 10;

    public boolean isActive = false;


    public Enemy(boolean isActive) {
        Point co = getRandomPoint();
        xy.add(new double[]{co.x, co.y});
        setPhantomXY();
        enemies.add(this);
        this.isActive = isActive;
        if (!isActive) {
            delay = (int) (delayStat - Math.random() * (delayStat / 4));
            color = new Color((int) (Math.random() * 50 + 200), (int) (Math.random() * 50 + 200), (int) (Math.random() * 50 + 200));
        } else {
            delay = (int) delayStatActive;
            delayDouble = delayStatActive;
            activeEnemies.add(this);
            color = new Color(150, 150, 255);
        }
        for (int i = 0; i < snakeLength + 2; i++) {
            addCircle();
        }
    }

    public ArrayList<double[]> getXy() {
        return xy;
    }

    public ArrayList<double[]> getPhantomXY() {
        return phantomXY;
    }

    public void addCircle() {
        if (xy.size() < maxSize) {
            xy.add(new double[]{xy.get(xy.size() - 1)[0], xy.get(xy.size() - 1)[1]});
        }
    }

    public void grow() {
        for (int i = 0; i < 1; i++) {
            addCircle();
        }
    }

    public void reset() {
        Point co = getRandomPoint();
        eatAndAngry = false;
        if (isActive) {
            delayDouble = delayStatActive;
            delay = (int) delayStatActive;
        } else {
            delayDouble = delayStat;
            delay = (int) delayStat;
        }

        xy.clear();
        xy.add(new double[]{co.x, co.y});
        move(co.x, co.y);
        for (int i = 0; i < snakeLength; i++) {
            addCircle();
        }

    }

    public Point getRandomPoint() {
        int x = (int) (Math.random() * (width * playGround[0] / (int) (size * stepOfSize))) * (int) (size * stepOfSize) + (int) (width * playGround[1]);
        int y = (int) (Math.random() * (height * playGround[0] / (int) (size * stepOfSize))) * (int) (size * stepOfSize) + (int) (height * Enemy.playGround[1]);

        if (Math.pow(Math.abs(width / 2 - x), 2) + Math.pow(Math.abs(height / 2 - y), 2) <= Math.pow(300, 2)) {
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

    private boolean reset = false;


    public void moveCheck(double[] point, double[] apple, boolean isNearby) {
        try {


            if (eatAndAngry) {


                timer -= 1;


            }

            if (delayCount < delay) {                    //проверка прошло ли достаточно времени, чтобы делать новый шаг
                delayCount++;
                movePhantom();
                setLines();
                if (reverse < reverseCount) {                    //проверка прошло ли достаточно времени с прошлого разворота
                    reverse++;
                }
                return;
            }
            if (eatAndAngry && timer <= 0) {
                timer = timerStat;
                eatAndAngry = false;
                setCurrentDelay(getDelay() * 2);
                setDelay();
            }

            if (reverse < reverseCount) {                                       //проверка прошло ли достаточно времени с прошлого разворота
                reverse++;
            }

            delayCount = 0;
            double xTarget;
            double yTarget;
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
                if (eatAndAngry || Picture.isEnd) {
                    xTarget = point[0];
                    yTarget = point[1];
                } else {

                    xTarget = apple[0];
                    yTarget = apple[1];
                }
            }
            if(Picture.isEnd){
                xTarget = Picture.xMouse;
                yTarget = Picture.yMouse;
            }
            double x = getXy().get(0)[0];
            double y = getXy().get(0)[1];
            boolean dX = Math.abs(xTarget - x) > Math.abs(yTarget - y);

            boolean down = yTarget > y;
            boolean up = yTarget < y;
            boolean left = xTarget < x;
            boolean right = xTarget > x;
            downNotSelf = true;
            upNotSelf = true;
            leftNotSelf = true;
            rightNotSelf = true;

            try {
                notSelf(x, y);
            } catch (Exception e) {

            }
            if (!downNotSelf && !upNotSelf && !rightNotSelf && !leftNotSelf) {
                if (!reverse() && isTeleport) {

                    Point xy = getRandomPoint();
                    move(xy.x, xy.y);
                }
                directionOfPhantomXY.clear();
                return;
            }

            if (left && dX && leftNotSelf) {
                move(x - step, y);

            } else if (right && dX && rightNotSelf) {
                move(x + step, y);

            } else if (up && upNotSelf) {
                move(x, y - step);

            } else if (down && downNotSelf) {
                move(x, y + step);

            } else if (right && rightNotSelf) {
                move(x + step, y);

            } else if (left && leftNotSelf) {
                move(x - step, y);

            } else {
                lastWay(x, y, upNotSelf, downNotSelf, rightNotSelf, leftNotSelf);
            }
            setLines();


        } catch (Exception e) {

        }
    }
    public void setLines(){
        points.clear();
        lines.clear();
        if (Picture.rect.contains(new Point((int) phantomXY.get(0)[0], (int) phantomXY.get(0)[1]))) {
            points.add(new Point((int) phantomXY.get(0)[0], (int) phantomXY.get(0)[1]));
            for (int i = 1; i < phantomXY.size()-1; i++) {
                if ((int) phantomXY.get(i-1)[0] != (int) phantomXY.get(i+1)[0] && (int) phantomXY.get(i-1)[1] != (int) phantomXY.get(i+1)[1]) {
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

    private void lastWay(double x, double y, boolean up, boolean down, boolean right, boolean left) {

        try {
            ArrayList<double[]> variants = new ArrayList<>(/*Arrays.asList(new int[]{x, y - step},new int[]{x, y + step},new int[]{x + step, y},new int[]{x - step, y})*/);
            if (up) {
                variants.add(new double[]{x, y - step});
            }
            if (down) {
                variants.add(new double[]{x, y + step});
            }
            if (right) {
                variants.add(new double[]{x + step, y});
            }
            if (left) {
                variants.add(new double[]{x - step, y});
            }
            Collections.shuffle(variants);
            if (xy.size() > 1) {
                for (double[] v : variants) {
                    boolean isClear = false;
                    for (double[] i : xy) {
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
                                for (double[] i : snake1.getXy()) {
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

            if (!reverse() && isTeleport) {

                Point xy = getRandomPoint();
                move(xy.x, xy.y);
            }
            directionOfPhantomXY.clear();
        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println(e);
        }
    }

    //    downNotSelf = notSelf(x, y + step);
//    upNotSelf = notSelf(x, y - step);
//    leftNotSelf = notSelf(x - step, y);
//    rightNotSelf = notSelf(x + step, y);
    private void notSelf(double x, double y) {

        for (double[] i : getXy()) {
            if ((int) i[0] == (int) x + step && (int) i[1] == (int) y) {
                rightNotSelf = false;
            } else if ((int) i[0] == (int) x && (int) i[1] == (int) y + step) {
                downNotSelf = false;
            } else if ((int) i[0] == (int) x - step && (int) i[1] == (int) y) {
                leftNotSelf = false;
            } else if ((int) i[0] == (int) x && (int) i[1] == (int) y - step) {
                upNotSelf = false;
            }
        }
        for (Enemy snake1 : Enemy.enemies) {
            if (!this.equals(snake1)) {
                for (double[] i : snake1.getXy()) {
                    if ((int) i[0] == (int) x + step && (int) i[1] == (int) y) {
                        rightNotSelf = false;
                    } else if ((int) i[0] == (int) x && (int) i[1] == (int) y + step) {
                        downNotSelf = false;
                    } else if ((int) i[0] == (int) x - step && (int) i[1] == (int) y) {
                        leftNotSelf = false;
                    } else if ((int) i[0] == (int) x && (int) i[1] == (int) y - step) {
                        upNotSelf = false;
                    }
                }
            }
        }


    }

    public void movePhantom() {
        try {
//            tailless = new int[]{phantomXY.get(phantomXY.size() - 1).x, phantomXY.get(phantomXY.size() - 1).y};
            for (int i = 0; i < phantomXY.size(); i++) {
                try {
                    phantomXY.get(i)[0] += directionOfPhantomXY.get(i)[0];
                    phantomXY.get(i)[1] += directionOfPhantomXY.get(i)[1];
                } catch (IndexOutOfBoundsException e) {

                }
            }


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

    }

    public void move(double x, double y) {
        directionOfPhantomXY.clear();
        setPhantomXY();
        try {

            for (int i = xy.size() - 1; i >= 0; i--) {
                try {
                    if (i == xy.size() - 1) {
                        if (Math.abs(((double) xy.get(i)[0] - phantomXY.get(i)[0])) > step || Math.abs(((double) xy.get(i)[1] - phantomXY.get(i)[1])) > step) {
//                            directionOfPhantomXY.add(0, new double[]{0, 0});
//                            phantomXY.get(i)[0] = xy.get(i).x;
//                            phantomXY.get(i)[1] = xy.get(i).y;
                        } else {

                            phantomXY.get(i)[0] = xy.get(i)[0];
                            phantomXY.get(i)[1] = xy.get(i)[1];
                        }
                    }
                    xy.get(i)[0] = xy.get(i - 1)[0];
                    xy.get(i)[1] = xy.get(i - 1)[1];
                    directionOfPhantomXY.add(0, new double[]{((double) xy.get(i)[0] - phantomXY.get(i)[0]) / (delay + 1), ((double) xy.get(i)[1] - phantomXY.get(i)[1]) / (delay + 1)});

                } catch (IndexOutOfBoundsException e) {

                }
            }
            phantomXY.get(0)[0] = xy.get(0)[0];
            phantomXY.get(0)[1] = xy.get(0)[1];
            xy.get(0)[0] = x;
            xy.get(0)[1] = y;

            directionOfPhantomXY.add(0, new double[]{((double) xy.get(0)[0] - phantomXY.get(0)[0]) / (delay + 1), ((double) xy.get(0)[1] - phantomXY.get(0)[1]) / (delay + 1)});

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
/*//                taillessCopy[0] = phantomXY.get(0)[0];
//                taillessCopy[1] = phantomXY.get(0)[1];
                double directionX = directionOfPhantomXY.get(directionOfPhantomXY.size()-1)[0];
                double directionY = directionOfPhantomXY.get(directionOfPhantomXY.size()-1)[1];
                double taillessX  = Math.abs((xy.get(xy.size()-1).x -  taillessPhantomCopy.get(taillessPhantomCopy.size()-1)[0])/directionX);
                double taillessY  = Math.abs((xy.get(xy.size()-1).y -  taillessPhantomCopy.get(taillessPhantomCopy.size()-1)[1])/directionY);
                if(Double.isNaN(taillessX)){
                    taillessX = 0;
                }
                if(Double.isNaN(taillessY)){
                    taillessY = 0;
                }
                int count = (int)Math.max(taillessX,taillessY);
                while (count>0){
                    taillessPhantomCopy.add(new double[]{taillessPhantomCopy.get(taillessPhantomCopy.size()-1)[0]+=directionX,
                            taillessPhantomCopy.get(taillessPhantomCopy.size()-1)[1]+=directionY});
                    count--;
                }*/
//                movePhantom();
                reverse = 0;
                directionOfPhantomXY.clear();
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
