package org.example.gpu.render;

import org.example.gpu.io.Input;
import org.lwjgl.glfw.*;
import org.lwjgl.openal.*;
import org.lwjgl.opengl.GL;

import java.awt.geom.Point2D;
import java.io.File;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.opengl.GL11.*;

public class Window {
    private long window;
    public int width, height;

    public int windowPosY;
    public int windowPosX;
    private long audioContext;
    private long audiodevice;
    private boolean fullscreen;
    private boolean hasResized;
    private boolean posChanged;
    private boolean focus;
    private Point2D mouse = new Point2D.Float();

    private GLFWWindowSizeCallback windowSizeCallback;
    private GLFWWindowPosCallback windowPosCallback;
    private GLFWWindowFocusCallback windowFocusCallback;
    private GLFWCursorPosCallback cursorPosCallback;
    private Input input;


    public   void setLocalCallBacks(){
        windowSizeCallback = new GLFWWindowSizeCallback() {
            @Override
            public void invoke(long windowArg, int widthArg, int heightArg) {
                width = widthArg;
                height = heightArg;
                hasResized = true;
            }
        };
        glfwSetWindowSizeCallback(window,windowSizeCallback);
        windowPosCallback = new GLFWWindowPosCallback() {
            @Override
            public void invoke(long window, int xpos, int ypos) {
            windowPosX = xpos;
            windowPosY = ypos;
            posChanged = true;
            }
        };
        glfwSetWindowPosCallback(window,windowPosCallback);
        windowFocusCallback = new GLFWWindowFocusCallback() {
            @Override
            public void invoke(long window, boolean focused) {
                focus = focused;
//                System.out.println(focus);
            }
        };
        glfwSetWindowFocusCallback(window,windowFocusCallback);
        cursorPosCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
             mouse.setLocation(xpos-width/2f,-(ypos-height/2f));
            }
        };
        glfwSetCursorPosCallback(window,cursorPosCallback);
    }

    public Window(int width, int height) {

        setSize(width, height);
        setFullscreen(false);
        hasResized = false;
        posChanged = false;
        focus = true;

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
        glfwSetInputMode(window,GLFW_CURSOR,GLFW_CURSOR_DISABLED);

        glfwShowWindow(window);
        glfwMakeContextCurrent(window);

        setLocalCallBacks();

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
public void cleanUp(){
        windowSizeCallback.close();
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
    public Point2D getMouse(){
        return mouse;
    }

    public void update() {
        hasResized = false;
        posChanged = false;
        input.uptade();
        glfwPollEvents();
    }

    public long getWindow() {
        return window;
    }
    public boolean hasResized(){
        return hasResized;
    }
    public boolean posChanged(){
        return posChanged;
    }
    public boolean isFocus(){
        return  focus;
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }

    public Input getInput() {
        return input;
    }
}
