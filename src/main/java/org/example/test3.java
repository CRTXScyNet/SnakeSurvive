package org.example;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class test3 {
    ArrayList<Integer> integers = new ArrayList<>();


    test3(){
        for(int i = -10; i<=10; i++) {
            integers.add(i);
        }
        for(int i = integers.size()-1; i>=0; i--) {
            if(integers.get(i) < 5){
                integers.remove(i);
            }
        }



    }

    public static void main(String[] args) {
        new test3();
    }

}
