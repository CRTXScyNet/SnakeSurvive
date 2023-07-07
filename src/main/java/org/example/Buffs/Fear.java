package org.example.Buffs;

import org.example.Painter.Process;
import org.example.gpu.Window;

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
        Process.enemyScared = true;
    }

    @Override
    public void buffOff() {
        super.buffOff();
        Process.enemyScared = false;
    }
}
