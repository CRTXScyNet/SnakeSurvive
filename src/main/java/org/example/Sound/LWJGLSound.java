package org.example.Sound;


import org.lwjgl.openal.AL11;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.libc.LibCStdlib;

import java.io.File;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;


import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename;
import static org.lwjgl.system.MemoryStack.*;


public class LWJGLSound {
    private int bufferId;
    private int sourceId;
    private float x = 0;
    private float y = 0;
    private String filePath;
    private boolean isPlaying = false;
    private boolean ready = false;
    private boolean isLoop = false;
    private float volume = 0;
public static ArrayList<LWJGLSound> sounds = new ArrayList<>();
    public void setCastomVolume(float castomVolume) {
        this.castomVolume = castomVolume;
        AL11.alSourcef(sourceId, AL11.AL_GAIN, castomVolume);
    }

    private float castomVolume = 0.2f;


    public LWJGLSound(String filePath, boolean isLoop) {
        this.filePath = filePath;
        File file = new File(filePath);


        bufferId = AL11.alGenBuffers();


        IntBuffer channelsBuffer = stackGet().ints(1);
        stackPush();
        IntBuffer sampleRateBuffer = stackGet().ints(1);

        stackPush();

//try(STBVorbisInfo info = STBVorbisInfo.malloc()){
//    ShortBuffer pcm = readVorbis(filePath,32*1024,info);
//    int format = -1;
//        if (info.channels() == 1) {
//            format = AL_FORMAT_MONO16;
//        } else if (info.channels() == 2) {
//            format = AL_FORMAT_STEREO16;
//        }
//    alBufferData(bufferId, format, pcm, info.sample_rate());
//}


        int bufferSize = 32*1024;
        MemoryUtil.memAllocShort(bufferSize);
        ShortBuffer rawAudioBuffer = null;
        try {
            rawAudioBuffer = stb_vorbis_decode_filename(filePath, channelsBuffer, sampleRateBuffer);
        }catch (Exception e){
            e.printStackTrace();
        }
        if (rawAudioBuffer == null) {

            System.out.println("Could not load sound file '" + filePath + "'");
            stackPop();
            stackPop();
            return;
        }

        stackPop();
        stackPop();

        int channels = channelsBuffer.get();
        int sampleRate = sampleRateBuffer.get();
        int format = -1;
        if (channels == 1) {
            format = AL_FORMAT_MONO16;
        } else if (channels == 2) {
            format = AL_FORMAT_STEREO16;
        }



        alBufferData(bufferId, format, rawAudioBuffer, sampleRate);


        sourceId = AL11.alGenSources();
        AL11.alSourcei(sourceId, AL11.AL_BUFFER, this.bufferId);
        AL11.alSourcei(sourceId, AL11.AL_LOOPING, isLoop ? 1 : 0);
        AL11.alSourcei(sourceId, AL11.AL_POSITION, 0);
        AL11.alSourcef(sourceId, AL11.AL_GAIN, 0.2f);

        sounds.add(this);
    }

    public void delete() {
        stop();
alDeleteBuffers(bufferId);
alDeleteBuffers(sourceId);
    }

    public void update(float x, float y) {
float dist = (float)Math.sqrt(Math.pow(x,2)+Math.pow(y,2));
        volume = (castomVolume-dist/400*castomVolume)/4;
if(!isAppearIn && !isFadeOut) {
    if (dist > 400) {
        AL11.alSourcef(sourceId, AL11.AL_GAIN, 0);
    } else {
        AL11.alSourcef(sourceId, AL11.AL_GAIN, volume);
    }
}


    }

    public void play() {
        int state = alGetSourcei(sourceId, AL_SOURCE_STATE);
        if (state == AL_STOPPED) {
            isPlaying = false;
            alSourcei(sourceId, AL_POSITION, 0);
        }
        if(!isPlaying){
            alSourcePlay(sourceId);
            isPlaying = true;
        }

    }


    public void stop() {
        if (isPlaying) {
            isPlaying = false;
//            AL11.alSourcef(sourceId, AL11.AL_GAIN, 0);
            alSourceStop(sourceId);
        }
    }

    public void setFadeOut(boolean fadeOut) {
        isFadeOut = fadeOut;
    }

    public void setAppearIn(boolean appearIn) {
        isAppearIn = appearIn;
    }

    boolean isFadeOut = false;
    boolean isAppearIn = false;
    public void fadeOut(float t, float T){
        isFadeOut = true;
float change = volume-t/T*volume;
        AL11.alSourcef(sourceId, AL11.AL_GAIN, change);
    }
    public void appearIn(float t, float T){
        isAppearIn = true;
        float change = t/T*volume;
        AL11.alSourcef(sourceId, AL11.AL_GAIN, change);
    }

    public String filePath() {
        return filePath;
    }

    public boolean isPlaying() {
        int state = alGetSourcei(sourceId, AL_SOURCE_STATE);
        if (state == AL_STOPPED) {
            isPlaying = false;
        }
        return isPlaying;
    }

}
