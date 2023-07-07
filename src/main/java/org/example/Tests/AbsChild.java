package org.example.Tests;

public class AbsChild extends AbsTest{
    @Override
    public void Say() {
        super.Say();
        System.out.println("YOYOYO! I'M SHILDREN!");
    }

    public AbsChild() {
        super();
    }
}
