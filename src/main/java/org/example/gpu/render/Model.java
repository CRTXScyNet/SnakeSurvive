package org.example.gpu.render;

import org.example.gpu.io.Movement;
import org.lwjgl.BufferUtils;

import java.awt.*;
import java.lang.ref.Cleaner;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;

public class Model implements Cleaner.Cleanable {
    private Shader shader;
    private float[] vertices = {
            0.5f, 0.5f, 0,
            -0.5f, 0.5f, 0,
            -0.5f, -0.5f, 0,
            0.5f, -0.5f, 0,

    };
    private float[] shaderVertices = {
            0, 0,
            1, 0,
            1, 1,
            0, 1,

    };
    private int[] indices = {
            0, 1, 2,
            2, 3, 0
    };
    private int draw_count;
    private int v_id;
    private int s_id;

    private int i_id;
    private float i = 0;
    private Texture texture;

    private Movement movement;


    public int getScale() {
        return scale;
    }

    private int scale = 0;

    public Movement getMovement() {
        return movement;
    }

    private int width;
    private int heigth;

    public Model(org.example.gpu.Window window, int scale) {
        this.width = window.width;
        this.heigth = window.height;

        this.scale = scale;
        movement = new Movement(window.width, window.height);
//        texture = new Texture("./img/2.png");

        draw_count = vertices.length;

        v_id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, v_id);
        glBufferData(GL_ARRAY_BUFFER, createFloatBuffer(vertices), GL_STATIC_DRAW);

        s_id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, s_id);
        glBufferData(GL_ARRAY_BUFFER, createFloatBuffer(shaderVertices), GL_STATIC_DRAW);

        i_id = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, i_id);
        IntBuffer buffer = BufferUtils.createIntBuffer(vertices.length);
        buffer.put(indices);
        buffer.flip();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);


    }

    public void update(Window window) {

//        if (glfwGetKey(window.getWindow(), GLFW_KEY_D) != 0) {
//            movement.getPosition().sub(new Vector3f(-10, 0, 0));
//        }
//        if (glfwGetKey(window.getWindow(), GLFW_KEY_A) != 0) {
//            movement.getPosition().sub(new Vector3f(10, 0, 0));
//        }
//        if (glfwGetKey(window.getWindow(), GLFW_KEY_W) != 0) {
//            movement.getPosition().sub(new Vector3f(0, -10, 0));
//        }
//        if (glfwGetKey(window.getWindow(), GLFW_KEY_S) != 0) {
//            movement.getPosition().sub(new Vector3f(0, 10, 0));
//        }

//        if (glfwGetKey(window.getWindow(), GLFW_KEY_Q) != 0) {
//            Point p = MouseInfo.getPointerInfo().getLocation();
//            p.x = p.x - window.windowPosX-window.width/2;
//            p.y = (p.y-window.windowPosY-window.height/2);
//            movement.setPosition(new Vector3f(p.x, -p.y, 0));
//        }
    }

    public void render() {

//        texture.bind(0);


//        shader.setUniform("sampler", 0);


        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glBindBuffer(GL_ARRAY_BUFFER, v_id);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ARRAY_BUFFER, s_id);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, i_id);
        glDrawElements(GL_TRIANGLES, draw_count, GL_UNSIGNED_INT, 0);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
    }

    private FloatBuffer createFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    @Override
    public void clean() {
//        shader.clean();
//        texture.clean();
    }


}
