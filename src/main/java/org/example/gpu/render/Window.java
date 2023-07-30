package org.example.gpu.render;

import org.example.gpu.io.Input;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.openal.*;
import org.lwjgl.opengl.GL;

import java.io.File;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.opengl.GL11.*;

public class Window {
    private long window;
    public int width, height;
    private boolean fullscreen;
    private boolean hasResized;
    public int windowPosX;
    public int windowPosY;
    private GLFWWindowSizeCallback windowSizeCallback;
    private Input input;
    private long audioContext;
    private long audiodevice;

    public Window(int width, int height) {

        setSize(width, height);
        setFullscreen(false);
        hasResized = false;
    }

    public void createWindow(String tittle) {
        GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        assert videoMode != null;
        if (fullscreen) {
            setSize(videoMode.width(), videoMode.height());
        }
        window = glfwCreateWindow(
                width,
                height,
                tittle,
                fullscreen ? glfwGetPrimaryMonitor() : 0,
                0);

        if (window == 0) {
            throw new IllegalStateException("Failed to create a window");
        }
        if (!fullscreen) {

            windowPosX = (videoMode.width() - width) / 2;
            windowPosY = (videoMode.height() - height) / 2;
            glfwSetWindowPos(window, windowPosX, windowPosY);


        }
        glfwShowWindow(window);
        glfwMakeContextCurrent(window);

        input = new Input(window);

        String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);

        audiodevice = alcOpenDevice(defaultDeviceName);

        int[] attributes = {0};
        audioContext = alcCreateContext(audiodevice, attributes);
        alcMakeContextCurrent(audioContext);

        ALCCapabilities alcCapabilities = ALC.createCapabilities(audiodevice);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

         if(!alCapabilities.OpenAL11){              //TODO
             assert false : "Audio library not supported";
         }
        GL.createCapabilities();

        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE);
    }
    public void destroyWindow(){
        alcDestroyContext(audioContext);
        alcCloseDevice(audiodevice);

        glfwDestroyWindow(window);

        glfwTerminate();
        System.exit(0);
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void swapBuffer() {
        glfwSwapBuffers(window);
    }

    public void setFullscreen(boolean isFullscreen) {
        this.fullscreen = isFullscreen;
    }

    public void update() {
        input.uptade();
        glfwPollEvents();
    }

    public long getWindow() {
        return window;
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    public Input getInput() {
        return input;
    }
}
