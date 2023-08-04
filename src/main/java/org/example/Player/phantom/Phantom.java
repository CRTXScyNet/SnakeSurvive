package org.example.Player.phantom;

import org.example.Painter.Pointer;
import org.example.Player.Player;
import org.example.Player.PlayerParent;
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

    private ArrayList<Point2D> teleportFrom = new ArrayList<>();
    private ArrayList<Point2D> teleportTo = new ArrayList<>();
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

    public Phantom(Window wind, ArrayList<float[]> xy, ArrayList<Head> heads, ArrayList<Point2D> teleportFrom, ArrayList<Point2D> teleportTo) {
        super(wind);
        isTail = true;
        color = agressive;

        maxStep = 1.5f;
        this.teleportTo.addAll(teleportTo);
        this.teleportFrom.addAll(teleportFrom);
        this.xy = xy;
        this.size = 8;
        renderInit(color, "player", null);
        Heads.addAll(heads);
        phantoms.add(this);
        pointer = new Pointer(0.5f, window, "bufPointer", color, 150, 350, 30);

    }

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
        ArrayList<Point2D> newTeleportFrom = new ArrayList<>();
        ArrayList<Point2D> newTeleportTo = new ArrayList<>();
        if (Heads.size() > 0) {
            for (int i = 0; i < Heads.size(); i++) {
                if (Heads.get(i).position > pos) {

                    newHeads.add(Heads.get(i));
                    newTeleportFrom.add(this.teleportFrom.get(i));
                    newTeleportTo.add(this.teleportTo.get(i));
                }
            }
            this.teleportFrom.removeAll(newTeleportFrom);
            this.teleportTo.removeAll(newTeleportTo);
            Heads.removeAll(newHeads);
        }

        for (int i = 0; i < newHeads.size(); i++) {
            newHeads.get(i).setPosition(newHeads.get(i).getPosition() - pos);

        }


        new Phantom(window, newXy, newHeads, newTeleportFrom, newTeleportTo);
    }

    public Point2D getRandomPoint(boolean spawn) {
        double x = (-((double) window.width / 2) + ((Math.random() * ((double) window.width))));
        double y = (-((double) window.width / 2) + ((Math.random() * ((double) window.width))));
        if(spawn) {
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
        for (Point2D point2D : teleportTo) {
            double[] a = new double[]{point2D.getX(), point2D.getY()};
            point2D.setLocation(a[0] - direct[0], a[1] - direct[1]);
        }

        for (Point2D point2D : teleportFrom) {
            float[] a = new float[]{(float) point2D.getX(), (float) point2D.getY()};
            point2D.setLocation(a[0] - direct[0], a[1] - direct[1]);
        }

    }

    public void setStep(float step) {
        this.step = step;
    }

    private final int maxCount = (int) (3.14 / 0.1);
    boolean canIncreaseSpeed = false;
    private float maxRad = 1.5f;
    private float curRad = 0;
    private float distance = 0;
    private boolean changeDir = false;
    private boolean eaten = false;

    public void wiggle() {
        if (distance > 100) {
            if (changeDir) {
                curRad += 0.05;
                if (curRad > maxRad) {
                    changeDir = false;
                }
            } else {
                curRad -= 0.05;
                if (curRad < -maxRad) {
                    changeDir = true;
                }
            }
            canIncreaseSpeed = true;
        } else {
            if (curRad > 0) {
                curRad /= 2;
            } else {
                curRad /= 2;
            }
            canIncreaseSpeed = false;
        }

    }

    @Override
    public void setRadian(Point2D target) {
        if (Heads.size() > 0) {
            for (int i = 0; i < Heads.size(); i++) {
                int pos = Heads.get(i).getPosition();
                float dir = Heads.get(i).getDirection();
                super.setRadian(teleportFrom.get(i), new Point2D.Float(xy.get(pos)[0], xy.get(pos)[1]), dir);

                if (dir > 6.28) {
                    dir -= 6.28;

                } else if (dir < 0) {

                    dir += 6.28;
                }
                dir += 0.3 * radDir;
                Heads.get(i).setDirection(dir);
                Heads.get(i).setNextStep(new float[]{
                        (float) ((step) * Math.sin(dir) + xy.get(pos)[0]),
                        (float) ((step) * Math.cos(dir) + xy.get(pos)[1])
                });

            }
        }
        if (target != null) {
            distance = (float) getHeadXY().distance(target);
        }else {
            distance = (float) getHeadXY().distance(Player.player.getHeadXY());
        }

        wiggle();
        if (target != null) {
            super.setRadian(target);
        }
        float rad = targetRadian + curRad;
        super.setRadian(new Point2D.Double((float) ((step) * Math.sin(rad) + xy.get(0)[0])
                , (float) ((step) * Math.cos(rad) + xy.get(0)[1])));

        tMouse += (stepRad * radDir);

        if (tMouse > 6.28) {
            tMouse -= 6.28;

        } else if (tMouse < 0) {

            tMouse += 6.28;
        }
        if (target != null) {
            if (canIncreaseSpeed && step < maxStep) {
                step += Math.abs(stepRad) / 20;
            }


        }

        stepRadLast = radDir;
        pointWatch[0] = (float) ((step) * Math.sin(tMouse) + xy.get(0)[0]);
        pointWatch[1] = (float) ((step) * Math.cos(tMouse) + xy.get(0)[1]);
    }

    public void die() {
        float[] p = new float[]{(float) Player.player.getHeadXY().getX(), (float) Player.player.getHeadXY().getY()};
        for (int j = 0; j < xy.size(); j++) {

            float distance = (float) Math.sqrt(Math.pow(p[0] - xy.get(j)[0], 2) + Math.pow(p[1] - xy.get(j)[1], 2));

            float angle;
            if (distance < 30) {
                angle = (float) Math.atan((p[1] - xy.get(j)[1]) / (p[0] - xy.get(j)[0]));
                double translocationX = (distance - 5) * Math.cos(angle);
                double translocationY = (distance - 5) * Math.sin(angle);
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
        Point2D nearPlayer = new Point2D.Float(Player.player.getXy().get(position)[0], Player.player.getXy().get(position)[1]);
        if (xy.size() > Player.player.xy.size()) {
            pointer.setColor(agressive);
            rendering.setRGB(agressive);
            if(maxStep != 2){
                maxStep = 2f;
            }
            if(minStep != 1f){
                minStep = 1f;
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
            for (int i = 0; i < Player.player.getXy().size(); i++) {
                if (getHeadXY().distance(Player.player.getXy().get(i)[0], Player.player.getXy().get(i)[1]) < size) {
                    Point2D point = getRandomPoint(false);
                    if (xy.size() > 1) {
                        Point2D point2D = new Point2D.Float(Player.player.getXy().get(i)[0], Player.player.getXy().get(i)[1]);
                        teleportFrom.add(point2D);
                        teleportTo.add(point);
                        Head head = new Head(1);
                        portals.add(new PhantomPortal(point2D,window,head,color,true));
                        portals.add(new PhantomPortal(point,window,head,color,false));
                        head.setDirection(tMouse);
                        Heads.add(head);
                    }
                    xy.set(0, new float[]{(float) point.getX(), (float) point.getY()});
                    Player.player.minusCell();
                    Player.player.lostTheApple();
                    break;
                }
            }


            target.setLocation(nearPlayer);

        } else {
            pointer.setColor(scared);
            rendering.setRGB(scared);
            if(minStep != 1f){
                minStep = 1f;
            }
            if(maxStep != 1.5){
                maxStep = 1.5f;
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
                Point2D from = teleportFrom.get(i);
                Point2D to = teleportTo.get(i);
                if (from.distance(xy.get(pos)[0], xy.get(pos)[1]) < size/3) {
                    xy.set(pos, new float[]{(float) to.getX(), (float) to.getY()});
                    if (pos != xy.size() - 1) {
                        Heads.get(i).setPosition(++pos);
                    } else {
                        Heads.get(i).setAlive(false);
                        Heads.remove(i);
                        teleportTo.remove(i);
                        teleportFrom.remove(i);
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
        int count = xy.size()-1;
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
    public static void updateAll(){
        time = trest.getMainTime();
        for (int i = 0; i < phantoms.size(); i++) {
            phantoms.get(i).update();
        }
        for (int i = 0; i < portals.size(); i++) {
            portals.get(i).update(time);
            if(portals.get(i).closed()){
                portals.get(i).remove();
                portals.remove(i);
//                System.out.println("Portal deleted");
                i--;
            }
        }
    }
    public static void moveXyAll(float[] direct){
        for(Phantom phantom1 : phantoms){
            phantom1.moveXy(direct);
        }
        for (PhantomPortal portal : portals) {
            portal.moveXY(direct);
        }
    }
}
