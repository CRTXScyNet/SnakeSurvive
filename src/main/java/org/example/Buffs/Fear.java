package org.example.Buffs;

import org.example.Enemy.Enemy;
import org.example.Player.GluePart;
import org.example.Player.Player;
import org.example.gpu.Window;
import org.example.gpu.trest;

import java.awt.*;
import java.awt.geom.Point2D;

public class Fear extends BuffParent {

    private final Color color = new Color(91, 0, 161);
    private final Color colorForAnother = new Color(238, 108, 0);



    boolean fear = false;

    public Fear(Window window) {
        super(window);
        renderInit("fear", "bufPointer", color);
        setBuffCanExistTime(10);
        setCanExistTime(100);
        setChance(0.1);
        soundInit("./sounds/CutTheTailConstSound.ogg",true);
    }


public static void fear(){

        int maxDistance = 300;
        for (Enemy enemy : Enemy.enemies) {

            boolean changed = false;
            for (int i = 0; i < enemy.getXy().size(); i++) {

                Point2D enemyPoint = new Point2D.Float(enemy.getXy().get(i)[0], enemy.getXy().get(i)[1]);
                Point2D enemyPhantomPoint = new Point2D.Float(enemy.getPhantomXY().get(i)[0], enemy.getPhantomXY().get(i)[1]);
                float distance = (float) enemyPoint.distance(0, 0);
                float phantomDistance = (float) enemyPhantomPoint.distance(0, 0);
//                    if (distance < maxDistance) {
//                        changed = true;
//                        float angle = (float) Math.atan((-enemyPoint.getY()) / (-enemyPoint.getX()));
//                        double translocationX = (distance + (Player.step* 2)) * Math.cos(angle);
//                        double translocationY = (distance + (Player.step* 2)) * Math.sin(angle);
//                        if (-enemyPoint.getX() < 0) {
//                            enemy.getXy().set(i, new float[]{+(float) translocationX, +(float) translocationY});
//                        } else {
//                            enemy.getXy().set(i, new float[]{-(float) translocationX, -(float) translocationY});
//                        }
//
//                    }

                if (phantomDistance < maxDistance) {
                    changed = true;
                    float phantomAngle = (float) Math.atan((-enemyPhantomPoint.getY()) / (-enemyPhantomPoint.getX()));
                    double phantomTranslocationX = (phantomDistance + (Player.step*3*(1-distance/maxDistance))) * Math.cos(phantomAngle);
                    double phantomTranslocationY = (phantomDistance + (Player.step*3*(1-distance/maxDistance))) * Math.sin(phantomAngle);
                    if (-enemyPhantomPoint.getX() < 0) {
                        enemy.getPhantomXY().set(i, new float[]{+(float) phantomTranslocationX, +(float) phantomTranslocationY});
                        enemy.getXy().set(i,enemy.getPhantomXY().get(i));
                    } else {
                        enemy.getPhantomXY().set(i, new float[]{-(float) phantomTranslocationX, -(float) phantomTranslocationY});
                        enemy.getXy().set(i,enemy.getPhantomXY().get(i));
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
        for (GluePart glue : GluePart.glueParts) {
            for (int i = 0; i < glue.getXy().size(); i++) {

                Point2D enemyPoint = new Point2D.Float(glue.getXy().get(i)[0], glue.getXy().get(i)[1]);
                float distance = (float) enemyPoint.distance(0, 0);
                if (distance < maxDistance) {

                    float angle = (float) Math.atan((-enemyPoint.getY()) / (-enemyPoint.getX()));
                    double translocationX = (distance + (Player.step*3*(1-distance/maxDistance))) * Math.cos(angle);
                    double translocationY = (distance + (Player.step*3*(1-distance/maxDistance))) * Math.sin(angle);
                    if (-enemyPoint.getX() < 0) {
                        glue.getXy().set(i, new float[]{+(float) translocationX, +(float) translocationY});
                    } else {
                        glue.getXy().set(i, new float[]{-(float) translocationX, -(float) translocationY});
                    }
                }
            }


        }


}
    public boolean isFear() {
        return fear;
    }

    @Override
    public void buffOnn() {
        super.buffOnn();
        fear = true;
//        trest.enemyScared = true;
    }

    @Override
    public void buffOff() {
        super.buffOff();
        fear = false;
//        trest.enemyScared = false;
    }
}
