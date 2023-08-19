package org.example.Buffs;

import org.example.Player.Player;
import org.example.gpu.gameProcess.trest;
import org.example.gpu.render.Model;
import org.example.gpu.render.Window;
import org.joml.Vector3f;

import java.awt.*;
import java.awt.geom.Point2D;

public class CutTheTail extends BuffParent{
    public CutTheTail(Window window) {
        super(window);
        color = new Color(0,255,255);
        renderInit("speed", "bufPointer", color);
        setBuffCanExistTime(1);
        setCanExistTime(5000);
        setChance(0.1);
        soundInit("./sounds/CutTheTailConstSound.ogg",true);

    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void buffOnn() {
        super.buffOnn();
        Player.player.cutTheTail();
    }

    @Override
    public void buffOff() {
        super.buffOff();
    }
    public void addSomeFinal() {
//        if(renderingBuff.getModels().size()>0){
//            return;
//        }
        beginTime = trest.getMainTime();
        isShowing = true;

        xy = new Point2D.Float((float) (Math.random() * window.width/4 - (window.width/8)), (float) (Math.random() *window.width/4 - (window.width/8)));
        renderingBuff.addModel(new Model(window, (int) (size * 50),color,false));
        renderingBuff.getModels().get(0).getMovement().setPosition(new Vector3f((float) xy.getX(), (float) xy.getY(), 0));


        closing = false;
        isExist = true;
    }
}
