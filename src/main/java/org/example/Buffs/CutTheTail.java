package org.example.Buffs;

import org.example.Player.Player;
import org.example.gpu.Window;

import java.awt.*;

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
        Player.players.get(0).cutTheTail();
    }

    @Override
    public void buffOff() {
        super.buffOff();
    }
}
