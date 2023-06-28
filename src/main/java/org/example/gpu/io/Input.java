package org.example.gpu.io;

import org.example.gpu.trest;

import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;

public class Input {
    private boolean[] keys;
    private boolean[] buttons;
    private long window;

    public Input(long window) {
        this.window = window;
        this.keys = new boolean[GLFW_KEY_LAST];
        Arrays.fill(keys, false);

        this.buttons = new boolean[GLFW_MOUSE_BUTTON_LAST];
        Arrays.fill(buttons, false);
    }

    public boolean isKeyDown(int key) {
        return glfwGetKey(window, key) == 1;
    }

    public boolean isMouseButtonDown(int button) {
        return glfwGetMouseButton(window, button) == 1;
    }
    public boolean isMouseButtonPressed(int button) {

        return (isMouseButtonDown(button) && !buttons[button]);
    }
    public boolean isKeyPressed(int key) {
        return (isKeyDown(key) && !keys[key]);
    }

    public void uptade() {
        for (int i = 0; i < GLFW_KEY_LAST; i++) {
            keys[i] = isKeyDown(i);
        }
        for (int i = 0; i < GLFW_MOUSE_BUTTON_LAST; i++) {
            buttons[i] = isMouseButtonDown(i);
        }
    }

}
