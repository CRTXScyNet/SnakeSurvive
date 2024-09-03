package org.example.Buffs;

import org.example.Player.Player;
import org.example.gpu.render.Window;

import java.awt.*;
//Усиление увеличивает скорость игрока в два раза, и на время позволяет скользить не теряя скорости естественным образом.
public class Speed extends BuffParent {

    private final Color color = new Color(238, 108, 0);

    public Speed(Window window) {
        super(window);
        renderInit("speed", "bufPointer", color);

        setCanExistTime(15);
        setBuffCanExistTime(15);
        setChance(0.1);
        soundInit("./sounds/CutTheTailConstSound.ogg", true);
    }

    @Override
    public void buffOnn() {
        super.buffOnn();
        Player.addSpeedTime(buffCanExistTime);
    }

    @Override
    public void buffOff() {
        super.buffOff();


    }
}
