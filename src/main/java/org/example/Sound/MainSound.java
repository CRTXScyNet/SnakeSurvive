package org.example.Sound;

import org.example.gpu.gameProcess.trest;
import org.lwjgl.openal.AL11;
import org.lwjgl.system.MemoryUtil;

import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename;
import static org.lwjgl.system.MemoryStack.*;

// empty_space_bool = false;
//         white_snakes_bool = false;
//         white_snakes_hunting_bool = fal
//         purple_snakes_hunting_bool = fa
//         isBegin  = true;
//         isEnd  = true;

public class MainSound {
    public static long empty_space = 0;
    public static long white_snakes = 1;
    public static long white_snakes_hunting = 2;
    public static long purple_snakes_hunting = 3;
    public static long glue_part = 4;
    private int bufferId;
    private int sourceId;

    private String filePath;
    private boolean isPlaying = false;
    private boolean ready = false;
    private boolean isLoop = false;
    private boolean start = false;


    private boolean random = false;
    private float volume = 0;



    private  float fadeTime = 3;
    private float fadeStartTime = 0;
    private float fadeStart = fadeTime;
    private long soundType = 0;
    private ArrayList<Integer> bufferIds = new ArrayList<>();
    private HashMap<Integer, Integer> buf_src_id = new HashMap<>();
    private int curBufId;
    private int curSrcId;


    public MainSound(String filePath, boolean isLoop, long soundType) {
        isFaded = true;
        this.soundType = soundType;

        this.filePath = filePath;

        bufferId = AL11.alGenBuffers();
        curBufId = bufferId;


        IntBuffer channelsBuffer = stackGet().ints(1);
        stackPush();
        IntBuffer sampleRateBuffer = stackGet().ints(1);

        stackPush();

        int bufferSize = 32 * 1024;
        MemoryUtil.memAllocShort(bufferSize);
        ShortBuffer rawAudioBuffer = null;
        try {
            rawAudioBuffer = stb_vorbis_decode_filename(filePath, channelsBuffer, sampleRateBuffer);
        } catch (Exception e) {
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
        curSrcId = sourceId;
        AL11.alSourcei(sourceId, AL11.AL_BUFFER, this.bufferId);
        AL11.alSourcei(sourceId, AL11.AL_LOOPING, isLoop ? 1 : 0);
        AL11.alSourcei(sourceId, AL11.AL_POSITION, 0);
        AL11.alSourcef(sourceId, AL11.AL_GAIN, 0.0f);
        AL11.alSourcef(sourceId, AL11.AL_MAX_GAIN, 0.5f);


        bufferIds.add(bufferId);
        buf_src_id.put(bufferId, sourceId);
    }

    public void addSample(String s, boolean isLoop) {
        this.filePath = s;

        bufferId = AL11.alGenBuffers();
        curBufId = bufferId;


        IntBuffer channelsBuffer = stackGet().ints(1);
        stackPush();
        IntBuffer sampleRateBuffer = stackGet().ints(1);

        stackPush();

        int bufferSize = 32 * 1024;
        MemoryUtil.memAllocShort(bufferSize);
        ShortBuffer rawAudioBuffer = null;
        try {
            rawAudioBuffer = stb_vorbis_decode_filename(filePath, channelsBuffer, sampleRateBuffer);
        } catch (Exception e) {
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
        curSrcId = sourceId;
        AL11.alSourcei(sourceId, AL11.AL_BUFFER, this.bufferId);
        AL11.alSourcei(sourceId, AL11.AL_LOOPING, isLoop ? 1 : 0);
        AL11.alSourcei(sourceId, AL11.AL_POSITION, 0);
        AL11.alSourcef(sourceId, AL11.AL_GAIN, 0.0f);
        AL11.alSourcef(sourceId, AL_MAX_GAIN, 0.5f);

        bufferIds.add(bufferId);
        buf_src_id.put(bufferId, sourceId);
    }

    public void setRandom(boolean random) {
        this.random = random;
    }
    public void setFadeTime(float fadeTime) {
        this.fadeTime = fadeTime;
    }
    public void setPitch(float pitch){
        for (int id : bufferIds){
            AL11.alSourcef(buf_src_id.get(id), AL_PITCH,pitch); //TODO

        }

    }

    public void delete() {
        ArrayList<Integer> delete = new ArrayList<>(buf_src_id.keySet());
        for (int id : delete) {
            alDeleteBuffers(id);
            alDeleteBuffers(buf_src_id.remove(id));
        }
    }

    public void update() {
        if (isAppearIn) {
            appearIn();
        }
        if (isFadeOut) {
            fadeOut();
        }


    }

    public long getSoundType() {
        return soundType;
    }

    public void setVolume(float vol) {
        volume = vol;

    }

    public void setTime(float t) {

    }

    private int getId = 0;

    public void play() {
        int state = alGetSourcei(curSrcId, AL_SOURCE_STATE);
        if (state == AL_STOPPED) {
            isPlaying = false;
        }
        if (!isPlaying) {
            if (random) {
                if (getId == 0) {
                    Collections.shuffle(bufferIds);
                }

                curBufId = bufferIds.get(getId);
                getId++;
                if (getId >= bufferIds.size()) {
                    getId = 0;
                }
                curSrcId = buf_src_id.get(curBufId);
            }
            alSourcePlay(curSrcId);
            isPlaying = true;

        }
    }

    public void stop() {
        if (isPlaying) {
            isPlaying = false;
            isAppearIn = true;
            alSourceStop(curSrcId);
        }
    }

    public void pause() {
        if (isPlaying) {
            isPlaying = false;
            alSourcePause(curSrcId);
        }
    }


    public void setFaded(boolean faded) {
        isFaded = faded;
    }

    public boolean isFaded() {
        return isFaded;
    }

    public boolean isAppeared() {
        return isAppeared;
    }

    private boolean isFadeOut = false;
    private boolean isAppearIn = false;
    private boolean isFaded = false;
    private boolean isAppeared = false;

    public void setFadeOut(boolean fadeOut) {
        if (fadeStart >= fadeTime) {
            fadeStartTime = trest.mainTime;
            fadeStart = 0;
        } else {
            fadeStart = fadeTime - fadeStart;
        }
        isFadeOut = fadeOut;
        isAppearIn = !fadeOut;

    }

    public void fadeOut() {
        isAppearIn = false;
        isAppeared = false;
        isFaded = true;
        fadeStart += trest.mainTime - fadeStartTime;
        fadeStartTime = trest.mainTime;
        float change = volume - fadeStart / fadeTime * volume;
        for (int id : buf_src_id.keySet()) {
            AL11.alSourcef(buf_src_id.get(id), AL11.AL_GAIN, change);
        }
        if (fadeStart >= fadeTime) {
            isFadeOut = false;
        }
    }

    public void setAppearIn(boolean appearIn) {
        if (fadeStart >= fadeTime) {
            fadeStartTime = trest.mainTime;
            fadeStart = 0;
        } else {
            fadeStart = fadeTime - fadeStart;
        }
        isAppearIn = appearIn;
        isFadeOut = !appearIn;
    }


    public void appearIn() {
        isFadeOut = false;
        isFaded = false;
        isAppeared = true;
        fadeStart += trest.mainTime - fadeStartTime;
        fadeStartTime = trest.mainTime;
        float change = 0;
        change = fadeStart / fadeTime * volume;
        for (int id : buf_src_id.keySet()) {
            AL11.alSourcef(buf_src_id.get(id), AL11.AL_GAIN, change);
        }

        if (fadeStart >= fadeTime) {
            isAppearIn = false;
        }
    }

    public String filePath() {
        return filePath;
    }

    public boolean isPlaying() {
        int state = alGetSourcei(curSrcId, AL_SOURCE_STATE);
        if (state == AL_STOPPED) {
            isPlaying = false;
        }
        return isPlaying;
    }

    public void resetVolume() {
        for(int id :bufferIds){
            alSourcef(buf_src_id.get(id), AL_GAIN, 0);
        }
        fadeTime = 3;
        fadeStartTime = 0;
        fadeStart = fadeTime;
        isFaded = true;
        isFadeOut = false;
        isAppearIn = false;
        isAppeared = false;
    }
}
