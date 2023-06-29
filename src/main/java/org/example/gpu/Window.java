package org.example.gpu;

import org.example.gpu.io.Input;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowSizeCallback;

import static org.lwjgl.glfw.GLFW.*;

public class Window {
    private long window;
    public int width, height;
    private boolean fullscreen;
    private boolean hasResized;
    public int windowPosX;
    public int windowPosY;
    private GLFWWindowSizeCallback windowSizeCallback;
    private Input input;

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
    }


    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;

    }
    public void swapBuffer(){
        glfwSwapBuffers(window);
    }

    public void setFullscreen(boolean isFullscreen) {
        this.fullscreen = isFullscreen;
    }

    public void update() {
        input.uptade();
        glfwPollEvents();
    }
    public long getWindow(){
        return window;
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(window);
    }
    public Input getInput() {
        return input;
    }
}
