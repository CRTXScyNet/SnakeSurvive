package org.example.Player.phantom;

import org.example.Painter.Pointer;
import org.example.Player.Player;
import org.example.Player.PlayerParent;
import org.example.Sound.MainSoundsController;
import org.example.gpu.gameProcess.trest;
import org.example.gpu.render.ModelRendering;
import org.example.gpu.render.Window;
import org.joml.Vector3f;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;

public class Phantom extends PlayerParent {
    public static ArrayList<Phantom> phantoms = new ArrayList<>();
    private Color mainColor = new Color(0, 100, 100);


    private ArrayList<Head> Heads = new ArrayList<>();
    private static ArrayList<PhantomPortal> portals = new ArrayList<>();
    private Pointer pointer;
    public boolean isTail = false;

    private Color agressive = new Color(150, 100, 0);
    private Color scared = new Color(102, 162, 107);
    static float time = 0;

    public class Head {


        private int position = 1;
        private float radian = 0;


        private PhantomPortal portalIn;
        private PhantomPortal portalOut;
        private boolean isAlive = false;
        private float[] nextStep = new float[2];


        private float direction = 0;


        Head(int position) {
            isAlive = true;
            this.position = position;


        }

        public void setPosition(int position) {
            this.position = position;
        }

        public int getPosition() {
            return position;
        }

        public float getDirection() {
            return direction;
        }

        public void setDirection(float direction) {
            this.direction = direction;
        }

        public float[] getNextStep() {
            return nextStep;
        }

        public void setNextStep(float[] nextStep) {
            this.nextStep = nextStep;
        }

        public void setAlive(boolean alive) {
            isAlive = alive;
        }

        public boolean isAlive() {
            return isAlive;
        }

        public PhantomPortal getPortalIn() {
            return portalIn;
        }

        public void setPortalIn(PhantomPortal portalIn) {
            this.portalIn = portalIn;
        }

        public PhantomPortal getPortalOut() {
            return portalOut;
        }

        public void setPortalOut(PhantomPortal portalOut) {
            this.portalOut = portalOut;
        }

    }


    public Phantom(Window wind, int length) {

        super(wind);

        minStep = 1.5f;
        maxStep = 2f;
        PlayerParent.playerParents.add(this);
        color = agressive;
        Point2D point = getRandomPoint(true);
        xy.add(new float[]{(float) point.getX(), (float) point.getY()});
        renderInit(color, "player", null);
        this.size = 8;
        setMaxLength(70);
        setLength(length);
        phantoms.add(this);
        pointer = new Pointer(0.5f, window, "bufPointer", color, 150, 350, 30);

    }

    public Phantom(Window wind, ArrayList<float[]> xy, ArrayList<Head> heads) {
        super(wind);
        isTail = true;
        color = agressive;

        maxStep = 1.5f;
        this.xy = xy;
        this.size = 8;
        renderInit(color, "player", null);
        Heads.addAll(heads);
        phantoms.add(this);
        pointer = new Pointer(0.5f, window, "bufPointer", color, 150, 350, 30);

    }

    private final float constStep = maxStepStat;

    public Phantom(Window wind, ArrayList<float[]> xy) {
        super(wind);
        eaten = true;
        reversed = true;
        color = agressive;
        maxStep = 1.5f;
        this.xy = xy;
        this.size = 8;
        renderInit(color, "player", null);

        phantoms.add(this);
        pointer = new Pointer(0.5f, window, "bufPointer", color, 150, 350, 30);
        maxStepStat = constStep + trest.stage.ordinal()/**0.1f*/;

    }

    public void growNewPhantom() {

    }

    public Head getNextTo(int pos) {
        for (int i = 0; i < Heads.size(); i++) {
            if (Heads.get(i).getPosition() >= pos) {
                return Heads.get(i);

            }
        }
        return null;
    }

    public Head getSecond() {
        Head headLess = null;
        for (Head head : Heads) {
            if (headLess == null) {
                headLess = head;
            } else if (head.getPosition() < headLess.getPosition()) {
                headLess = head;
            }
        }
        return headLess;
    }

    public Head getHead(int position) {
        for (Head head : Heads) {
            if (head.position == position) {
                return head;
            }
        }
        return null;
    }

    boolean reversed = false;

    public void prepareToEat(int pos) {
        if (Player.player.step < Player.player.maxStep * 2) {
            Player.player.step += 0.1;
        }
        if (Heads.size() > 0) {
            for (Head head : Heads) {
                ArrayList<float[]> anotherXY = new ArrayList<>();
                int headPos = head.getPosition();
                for (int i = headPos; i < this.xy.size(); i++) {
                    anotherXY.add(this.xy.get(i));
                    this.xy.remove(i);
                    rendering.getModels().remove(i);
                    i--;
                }


                if (anotherXY.size() > 0) {
                    if (Player.player.getHeadXY().distance(anotherXY.get(0)[0], anotherXY.get(0)[1]) > Player.player.getHeadXY().distance(anotherXY.get(anotherXY.size() - 1)[0], anotherXY.get(anotherXY.size() - 1)[1])) {
                        Collections.reverse(anotherXY);
                    }
                    new Phantom(window, anotherXY);
                }
            }
            if (xy.size() == 0) {
                reset();
            } else if (Player.player.getHeadXY().distance(xy.get(0)[0], xy.get(0)[1]) > Player.player.getHeadXY().distance(xy.get(xy.size() - 1)[0], xy.get(xy.size() - 1)[1])) {
                Collections.reverse(xy);
            }
            if(!reversed){
                Player.player.cutOneCell();
            }
            reversed = true;
            Heads.forEach(r -> r.setAlive(false));
            Heads.clear();

        } else {

            ArrayList<float[]> newXy = new ArrayList<>();
            if (pos != 0 && pos != xy.size() - 1) {
                for (int i = pos + 1; i < this.xy.size(); i++) {
                    newXy.add(this.xy.get(i));
                    this.xy.remove(i);
                    rendering.getModels().remove(i);
                    i--;
                }
            }

            xy.remove(pos);
            rendering.getModels().remove(pos);
            if (xy.size() == 0) {
                reset();
            } else if (Player.player.getHeadXY().distance(xy.get(0)[0], xy.get(0)[1]) > Player.player.getHeadXY().distance(xy.get(xy.size() - 1)[0], xy.get(xy.size() - 1)[1])) {
                Collections.reverse(xy);
            }

//    newXy.remove(0);

            if (newXy.size() > 0) {
                new Phantom(window, newXy);
            }
            if(!reversed){
                Player.player.cutOneCell();
            }
            reversed = true;

        }
    }

    public void cutTheTail(int pos) {
        ArrayList<float[]> newXy = new ArrayList<>();
        for (int i = pos + 1; i < this.xy.size(); i++) {
            newXy.add(this.xy.get(i));
            this.xy.remove(i);
            rendering.getModels().remove(i);
            i--;
        }
//        if(getHead(pos)!=null ){
//            Heads.remove(getHead(pos));
//        }
        ArrayList<Head> newHeads = new ArrayList<>();

        if (Heads.size() > 0) {
            for (int i = 0; i < Heads.size(); i++) {
                if (Heads.get(i).position > pos) {

                    newHeads.add(Heads.get(i));
                }
            }

            Heads.removeAll(newHeads);
        }
        if (getHead(xy.size() - 1) != null) {
            Heads.get(Heads.size() - 1).setAlive(false);
            Heads.remove(Heads.size() - 1);
        }
        rendering.getModels().remove(pos);
        xy.remove(pos);

        for (int i = 0; i < newHeads.size(); i++) {
            newHeads.get(i).setPosition(newHeads.get(i).getPosition() - (pos + 1));
        }


        new Phantom(window, newXy, newHeads);
    }

    public Point2D getRandomPoint(boolean spawn) {
        double x = (-((double) window.width / 2) + ((Math.random() * ((double) window.width))));
        double y = (-((double) window.width / 2) + ((Math.random() * ((double) window.width))));
        if (spawn) {
            if (Math.pow(Math.abs(x), 2) + Math.pow(Math.abs(y), 2) <= Math.pow(450, 2)) {
                return getRandomPoint(true);
            }                    //TODO
        }
        return new Point2D.Double(x, y);
    }

    public void update() {
        if (xy.size() == 0) {
            reset();
            return;
        }
        pointer.update(getHeadXY(), true, trest.mainTime);
        moveCheck();

    }

    @Override
    public void moveXy(float[] direct) {
        super.moveXy(direct);

    }

    public void setStep(float step) {
        this.step = step;
    }

    private int maxCount = (int) (3.14 / 0.1);
    private int count = 0;

    boolean canIncreaseSpeed = false;
    private final float maxRad = 0.15f;
    private float curRad = 0;
    private float distance = 0;
    private boolean changeDir = false;
    private boolean eaten = false;

    public void wiggle() {
        if (distance > 100) {
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
        } else {
            curRad /= 2;

        }

    }


    public void setRadian(Point2D target) {
        try {
            if (Heads.size() > 0) {
                for (int i = 0; i < Heads.size(); i++) {
                    int pos = Heads.get(i).getPosition();
                    float dir = Heads.get(i).getDirection();
                    super.setRadian(Heads.get(i).getPortalIn().getXy(), new Point2D.Float(xy.get(pos)[0], xy.get(pos)[1]), dir);

                    if (dir > 6.28) {
                        dir -= 6.28;

                    } else if (dir < 0) {

                        dir += 6.28;
                    }
                    dir += difRad * radDir / 2;
                    Heads.get(i).setDirection(dir);
                    Heads.get(i).setNextStep(new float[]{
                            (float) ((step) * Math.sin(dir) + xy.get(pos)[0]),
                            (float) ((step) * Math.cos(dir) + xy.get(pos)[1])
                    });
                }

            }
//0.732612
//0.732612
//




            wiggle();
            if (target != null) {
                super.setRadian(target);
            }
            stepRad = (float) Math.abs(difRad) / (15 / (step / 2));//0.014808406
            maxCount = (int) (3.14 / stepRad);
            stepRad *= radDir;
            stepRad += curRad;
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
                if (difRad > 0.05) {//1.3612639904022217
                    if (count < maxCount) {
                        canIncreaseSpeed = true;
                        count++;
                        if (step < maxStep) {//0.9998657
                            step += Math.abs(stepRad);
                        } else {
//                            System.out.println("Maximum!");
                        }
                        if (count == maxCount) {
//                            System.out.println("Maximum!");
                        }
                    }
                } else {
                    canIncreaseSpeed = false;
                }
            }

            stepRadLast = radDir;
            pointWatch[0] = (float) ((step) * Math.sin(tMouse) + xy.get(0)[0]);
            pointWatch[1] = (float) ((step) * Math.cos(tMouse) + xy.get(0)[1]);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("SUCKS");
        }
    }

    public void die() {
        float[] p = new float[]{(float) Player.player.getHeadXY().getX(), (float) Player.player.getHeadXY().getY()};
        for (int j = 0; j < xy.size(); j++) {

            float distance = (float) Math.sqrt(Math.pow(p[0] - xy.get(j)[0], 2) + Math.pow(p[1] - xy.get(j)[1], 2));

            float angle;
            if (distance < 30) {
                angle = (float) Math.atan((p[1] - xy.get(j)[1]) / (p[0] - xy.get(j)[0]));
                double translocationX = (distance - 5 - Player.player.step) * Math.cos(angle);
                double translocationY = (distance - 5 - Player.player.step) * Math.sin(angle);
                if (p[0] - xy.get(j)[0] < 0) {
                    xy.set(j, new float[]{p[0] + (float) translocationX, p[1] + (float) translocationY});
                } else {
                    xy.set(j, new float[]{p[0] - (float) translocationX, p[1] - (float) translocationY});
                }
            }
        }
    }

    @Override
    public void moveCheck() {
        if (step > minStep) {
            if (!canIncreaseSpeed) {
                step *= 0.999;                                              //TODO
            } else {
                step *= 0.999;
            }
        }
        Point2D target = new Point2D.Float();

        int position = 0;
        float distance = 0;
        float secDistance = 0;
        for (int i = 0; i < Player.player.getXy().size(); i++) {
            if (distance == 0) {
                distance = (float) getHeadXY().distance(Player.player.getXy().get(i)[0], Player.player.getXy().get(i)[1]);
                position = i;
            } else {
                secDistance = (float) getHeadXY().distance(Player.player.getXy().get(i)[0], Player.player.getXy().get(i)[1]);
                if (secDistance < distance) {
                    distance = secDistance;
                    position = i;
                }
            }
        }
        this.distance = distance;
        Point2D nearPlayer = new Point2D.Float(Player.player.getXy().get(position)[0], Player.player.getXy().get(position)[1]);
        if (xy.size() > (trest.isEnd ? Player.player.getXy().size() / 2 : Player.countOfApples)) {
            MainSoundsController.white_snakes_hunting_bool = true;
            pointer.setColor(agressive);
            rendering.setRGB(agressive);
            if (maxStep != Player.player.maxStep*0.7f) {
                maxStep = Player.player.maxStep*0.7f;
            }
            if (minStep != Player.player.minStep/2) {
                minStep = Player.player.minStep/2;
            }
            if (Player.player.getPart().readyTuCut) {
                for (int j = 1; j < xy.size() - 1; j++) {

                    if (Player.player.part.getPartHead().distance(xy.get(j)[0], xy.get(j)[1]) < size) {
                        Player.player.part.callBack = true;
                        cutTheTail(j);
                        Player.player.getPart().readyTuCut = false;
                        break;
                    }
                }
            }
            if (distance < size) {
                Point2D point = getRandomPoint(false);
                Point2D point2D = new Point2D.Float(Player.player.getXy().get(position)[0], Player.player.getXy().get(position)[1]);
                if (xy.size() > 1) {
                    Head head = new Head(1);
                    head.setPortalIn(new PhantomPortal(point2D, window, head, color, true));
                    head.setPortalOut(new PhantomPortal(point, window, head, color, false));
                    portals.add(head.getPortalIn());
                    portals.add(head.getPortalOut());
                    head.setDirection(tMouse);
                    Heads.add(head);
                } else {
                    Head head = new Head(0);
                    head.setPortalIn(new PhantomPortal(point2D, window, head, color, true));
                    head.setPortalOut(new PhantomPortal(point, window, head, color, false));
                    portals.add(head.getPortalIn());
                    portals.add(head.getPortalOut());
                    head.setAlive(false);
                }
                xy.set(0, new float[]{(float) point.getX(), (float) point.getY()});
//                    Player.player.minusCell();
                Player.player.lostTheApple();

            }


            target.setLocation(nearPlayer);

        } else {
            pointer.setColor(scared);
            rendering.setRGB(scared);
            if (maxStep != Player.player.maxStep/2f) {
                maxStep = Player.player.maxStep/2f;
            }
            if (minStep != Player.player.minStep/2) {
                minStep = Player.player.minStep/2;
            }
            eaten = false;
            for (int i = 0; i < xy.size(); i++) {
                if (Player.player.getHeadXY().distance(xy.get(i)[0], xy.get(i)[1]) < size) {
                    eaten = true;
                    prepareToEat(i);
                }
            }

            if (!eaten) {

                if (getHeadXY().
                        distance(nearPlayer) > 200) {
                    target.setLocation(nearPlayer);
                } else {

                    target.setLocation((xy.get(0)[0] - nearPlayer.getX()) * 2, (xy.get(0)[1] - nearPlayer.getY()) * 2);
                }
            }

            die();

        }
        if (Heads.size() != 0) {
            for (int i = 0; i < Heads.size(); i++) {
                int pos = Heads.get(i).getPosition();
                Point2D from = Heads.get(i).getPortalIn().getXy();
                Point2D to = Heads.get(i).getPortalOut().getXy();
                if (xy.size() - 1 < pos) {
                    Heads.get(i).setAlive(false);
                    Heads.remove(i);
                    i--;
                    continue;
                }
                if (from.distance(xy.get(pos)[0], xy.get(pos)[1]) < size / 3) {
                    xy.set(pos, new float[]{(float) to.getX(), (float) to.getY()});
                    if (pos != xy.size() - 1) {
                        Heads.get(i).setPosition(++pos);
                    } else {
                        Heads.get(i).setAlive(false);
                        Heads.remove(i);

                        i--;
                    }

                }

            }
        }
        if (!eaten) {
            setRadian(target);

        }
        if (Heads.size() > 0) {
            move();
        }
        move(pointWatch[0], pointWatch[1]);
    }

    public void move(float x, float y) {
        int count = xy.size() - 1;
        if (Heads.size() > 0) {
            count = getSecond().getPosition() - 1;
        }
        if (!eaten) {
            xy.set(0, new float[]{x, y});
        }
        for (int i = 0; i < count; i++) {
            float distance = (float) Math.sqrt(Math.pow(xy.get(i + 1)[0] - xy.get(i)[0], 2) + Math.pow(xy.get(i + 1)[1] - xy.get(i)[1], 2));
            float distanceDif = (size - distance) / 2;
            float angle;
            if (distance > size) {
                angle = (float) Math.atan((xy.get(i)[1] - xy.get(i + 1)[1]) / (xy.get(i)[0] - xy.get(i + 1)[0]));
                if (xy.get(i)[0] - xy.get(i + 1)[0] < 0) {
                    xy.set(i + 1, new float[]{xy.get(i)[0] + (float) (size * Math.cos(angle)), xy.get(i)[1] + (float) (size * Math.sin(angle))});
                } else {
                    xy.set(i + 1, new float[]{xy.get(i)[0] - (float) (size * Math.cos(angle)), xy.get(i)[1] - (float) (size * Math.sin(angle))});
                }
            }

        }
        if (rendering != null) {
            for (int i = 0; i < rendering.getModels().size(); i++) {
                rendering.getModels().get(i).getMovement().setPosition(new Vector3f(xy.get(i)[0], xy.get(i)[1], 0));
            }
        }
        //Допиши свое...
    }

    public void move() {
        ArrayList<Integer> heads = new ArrayList<>();
        for (Head head : Heads) {
            heads.add(head.getPosition());
        }
        Collections.sort(heads);
        for (int j = 0; j < heads.size(); j++) {
            Head head = getHead(heads.get(j));
            assert head != null;

            boolean stop = false;

            xy.set(heads.get(j), head.getNextStep());
            for (int i = heads.get(j); i < (j < heads.size() - 1 ? heads.get(j + 1) : xy.size()) - 1; i++) {
                float distance = (float) Math.sqrt(Math.pow(xy.get(i + 1)[0] - xy.get(i)[0], 2) + Math.pow(xy.get(i + 1)[1] - xy.get(i)[1], 2));
                float distanceDif = (size - distance) / 2;
                float angle;
                if (distance > size) {
                    angle = (float) Math.atan((xy.get(i)[1] - xy.get(i + 1)[1]) / (xy.get(i)[0] - xy.get(i + 1)[0]));
                    if (xy.get(i)[0] - xy.get(i + 1)[0] < 0) {
                        xy.set(i + 1, new float[]{xy.get(i)[0] + (float) (size * Math.cos(angle)), xy.get(i)[1] + (float) (size * Math.sin(angle))});
                    } else {
                        xy.set(i + 1, new float[]{xy.get(i)[0] - (float) (size * Math.cos(angle)), xy.get(i)[1] - (float) (size * Math.sin(angle))});
                    }
                }
                if (!stop) {
//                    stop = deepPhysic();
                } else {
//                    deepPhysic();
                }
            }
//            if (stop) {
//                step *= 0.9;
//            }
        }
    }

    public static void clear() {
        for (int i = 0; i < phantoms.size(); i++) {
            phantoms.get(i).xy.clear();
            phantoms.get(i).rendering.clear(true);
            ModelRendering.selfList.remove(phantoms.get(i).rendering);
            phantoms.get(i).reset();
            i--;
        }
        phantoms.clear();
        for (int i = 0; i < portals.size(); i++) {
            portals.get(i).remove();
            portals.remove(i);
            i--;
        }
    }

    public void reset() {
        xy.clear();
        if (rendering != null) {
            rendering.clear(true);
        }
        ModelRendering.selfList.remove(rendering);
        phantoms.remove(this);
        pointer.remove();
    }

    public static void updateAll() {
        time = trest.getMainTime();
        for (int i = 0; i < phantoms.size(); i++) {
            phantoms.get(i).update();
        }
        for (int i = 0; i < portals.size(); i++) {
            portals.get(i).update(time);
            if (portals.get(i).closed()) {
                portals.get(i).remove();
                portals.remove(i);
//                System.out.println("Portal deleted");
                i--;
            }
        }
    }

    public static void moveXyAll(float[] direct) {
        for (Phantom phantom1 : phantoms) {
            phantom1.moveXy(direct);
        }
        for (PhantomPortal portal : portals) {
            portal.moveXY(direct);
        }
    }
}
