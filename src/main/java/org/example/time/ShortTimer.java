package org.example.time;

public class ShortTimer {
    private float startTime = 0;
    private float period;
    private float curPeriod = 0;
    private boolean increase = true;
    private boolean started = false;
    private boolean stopped = false;
    public ShortTimer(float period) {
        this.period = period;
    }
    public void start(boolean increase,float startTime){

        if(increase && !isIncrease() && isStopped()){
            reset();
        }else if(!increase && isIncrease() && isStopped()){
            reset();
        }
        if (!started) {
            started = true;
            this.increase = increase;
            this.startTime = startTime;
//            System.out.println("START!");
        }

    }
    public float update(float curTime){
        if(stopped){
            if(increase){
                curPeriod =1;
                return 1;
            }else {
                curPeriod =0;
                return 0;
            }

        }
        float result = 0;
        if(increase) {
            if (curPeriod == 1) {
                result = 1;
            } else{
                result = (curTime - startTime) / period;
        }
//            System.out.println("Increase " + result);
            if(result>=1){
                stopped = true;

            }
        }else {
            if (curPeriod ==0) {
                result = 0;
            }else {
                result = 1 - (curTime - startTime) / period;
            }
//            System.out.println("Decrease " + result);
            if(result<=0){
                stopped = true;
            }
        }

curPeriod = result;
        return result;
    }
    public void reset(){
        stopped=false;
        started = false;
    }
    public boolean isStopped() {
        return stopped;
    }
    public boolean isIncrease() {
        return increase;
    }
}
