package org.example;

public class test {
   double TargetRadian = 1;
   double tMouse = 6;
   test() {
       double stepRad = 0;
       double dif = Math.abs((TargetRadian - tMouse) % 6.28);
       if (dif < 3.14) {
           if (dif < 1) {
               stepRad = 0.001;
           } else {
               stepRad = 0.3;
           }
       } else {
           dif = Math.abs((Math.max(TargetRadian, tMouse) - 3.14) - (Math.min(TargetRadian, tMouse) + 3.14));
           if (dif < 1) {
               stepRad = 0.001;

           } else {
               stepRad = 0.3;
           }
       }
   }

    public static void main(String[] args) {
        new test();
    }
}
