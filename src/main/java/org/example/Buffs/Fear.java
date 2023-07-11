package org.example.Buffs;

import org.example.Painter.Process;
import org.example.gpu.Window;
import org.example.gpu.trest;

import java.awt.*;

public class Fear extends BuffParent {

    private final Color color = new Color(91, 0, 161);
    private final Color colorForAnother = new Color(238, 108, 0);

    public Fear(Window window) {
        super(window);
        renderInit("fear", "bufPointer", color);
        setBuffCanExistTime(10);
        setCanExistTime(100);
        setChance(0.1);
    }

    @Override
    public void buffOnn() {
        super.buffOnn();
        trest.enemyScared = true;
    }

    @Override
    public void buffOff() {
        super.buffOff();
        trest.enemyScared = false;
    }
}
