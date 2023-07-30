package org.example.Sound;

import java.util.ArrayList;
import java.util.HashMap;

public class MainSoundsController {
    public static boolean empty_space_bool = false;
    public static boolean white_snakes_bool = false;
    public static boolean white_snakes_hunting_bool = false;
    public static boolean purple_snakes_hunting_bool = false;
    public static boolean glue_part_bool = false;
    private HashMap<Long,Boolean> states = new HashMap<>();
    private boolean isBegin  = true;
    private boolean isEnd  = true;



    private static boolean pitchChange = false;
    private static float pitch = 1f;

private ArrayList<MainSound> sounds = new ArrayList<>();
    public MainSoundsController() {
        stateUpdate();

        MainSound empty_space = new MainSound("./sounds/mainSound/empty_space.ogg",true,0);
empty_space.setFadeTime(5);
        sounds.add(empty_space);
        MainSound white_snakes = new MainSound("./sounds/mainSound/white_snakes.ogg",true,1);

        sounds.add(white_snakes);
        MainSound white_snakes_hunting = new MainSound("./sounds/mainSound/white_snakes_hunting_1.ogg",true,2);
        sounds.add(white_snakes_hunting);
        MainSound white_snakes_hunting2 = new MainSound("./sounds/mainSound/white_snakes_hunting_2.ogg",true,2);
        sounds.add(white_snakes_hunting2);
        MainSound white_snakes_hunting3 = new MainSound("./sounds/mainSound/white_snakes_hunting_3.ogg",true,2);

        sounds.add(white_snakes_hunting3);
        MainSound purple_snake_hunting = new MainSound("./sounds/mainSound/purple_snake_hunting.ogg",true,3);
        sounds.add(purple_snake_hunting);

        MainSound glue_part_hunting = new MainSound("./sounds/mainSound/glue_part_hunting_1.ogg",false,4);
        sounds.add(glue_part_hunting);
        glue_part_hunting.addSample("./sounds/mainSound/glue_part_hunting_2.ogg",false);
        glue_part_hunting.addSample("./sounds/mainSound/glue_part_hunting_3.ogg",false);
        glue_part_hunting.addSample("./sounds/mainSound/glue_part_hunting_4.ogg",false);
        glue_part_hunting.addSample("./sounds/mainSound/glue_part_hunting_5.ogg",false);
        glue_part_hunting.setRandom(true);
        glue_part_hunting.setFadeTime(1);
        setVolume(0.2f);
        white_snakes_hunting.setVolume(0.4f);
        white_snakes_hunting2.setVolume(0.4f);
        white_snakes_hunting3.setVolume(0.4f);
//        MainSound purple_snakes_hunting = new MainSound("./sounds/mainSound/white_snakes_hunting_3.ogg",true);

    }

    private void setVolume(float vol){
        for(MainSound sound : sounds){
            sound.setVolume(vol);
        }
    }
    public void stateUpdate(){
        states.put(MainSound.empty_space,empty_space_bool);
        states.put( MainSound.white_snakes,white_snakes_bool);
        states.put(MainSound.white_snakes_hunting, white_snakes_hunting_bool);
        states.put(MainSound.purple_snakes_hunting, purple_snakes_hunting_bool);
        states.put(MainSound.glue_part, glue_part_bool);
    }
    public void update(){
        if(!pitchChange){
            pitch=1;
        }
        calcState();

        for (MainSound sound : sounds){
            sound.update();
        }
        resetBol();
        for(MainSound sound : sounds){
            sound.setPitch(pitch);
        }
        pitchChange = false;
    }
    public static void setPitchChange(boolean pitchChange) {
        MainSoundsController.pitchChange = pitchChange;
    }
    public static void setPitch(float pitch){
        MainSoundsController.pitch = pitch;

    }
    private void resetBol(){
        purple_snakes_hunting_bool = false;
        white_snakes_bool = false;
        white_snakes_hunting_bool = false;
        empty_space_bool = false;
        glue_part_bool = false;
    }
    private void calcState(){
        if(!white_snakes_hunting_bool){
            empty_space_bool = true;

        }else{
            empty_space_bool = false;
        }
        stateUpdate();
        for(long b : states.keySet()){
            if(states.get(b)){
                setAppearState(b);
            }else {
                setFadeState(b);
            }
        }

    }
private void setAppearState(long state){
        for (MainSound sound : sounds){
            if(sound.getSoundType() == state){
                if(!sound.isAppeared()){
                    sound.setAppearIn(true);
                }
            }
        }
}
    private void setFadeState(long state){
        for (MainSound sound : sounds){
            if(sound.getSoundType() == state){
                if(!sound.isFaded()){
                    sound.setFadeOut(true);
                }
            }
        }
    }
    public void reset(){
        pitch = 1;
        resetBol();

        for(MainSound sound : sounds){
            sound.resetVolume();
        }
    }
    public void play(){
        for(MainSound sound : sounds){
            sound.play();
        }
    }
    public void stop(){
        for(MainSound sound : sounds){
            sound.stop();
        }
    }
    public void pause(){
        for(MainSound sound : sounds){
            sound.pause();
        }
    }
    public void delete(){
        for(MainSound sound : sounds){
            sound.delete();
        }
    }
}
