package org.example.gpu.render;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.ref.Cleaner;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;


//Класс описыващий шейдер для модели.
public class Shader implements Cleaner.Cleanable {
    private int program;
    private int vs;
    private int fs;


    public Shader(String filename) {
        program = glCreateProgram();

        vs = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vs, readFile(filename + ".vs"));
        glCompileShader(vs);
        if (glGetShaderi(vs, GL_COMPILE_STATUS) != 1) {
            System.err.println(glGetShaderInfoLog(vs));
            System.exit(1);
        }

        fs = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fs, readFile(filename + ".fs"));
        glCompileShader(fs);
        if (glGetShaderi(fs, GL_COMPILE_STATUS) != 1) {
            System.err.println(glGetShaderInfoLog(fs));
            System.exit(1);
        }

        glAttachShader(program, vs);
        glAttachShader(program, fs);

        glBindAttribLocation(program, 0, "vertices");
        glBindAttribLocation(program, 1, "shaderVertices");

//        glBindAttribLocation(program,1,"u_resolution");

        glLinkProgram(program);
        if (glGetProgrami(program, GL_LINK_STATUS) == 0) {
            System.err.println(glGetProgramInfoLog(program));
            System.exit(1);
        }
        glValidateProgram(program);
        if (glGetProgrami(program, GL_VALIDATE_STATUS) == 0) {
            System.err.println(glGetProgramInfoLog(program));
            System.exit(1);
        }
    }

    public void setUniform(String name, float r, float g, float b) {
        int location = glGetUniformLocation(program, name);
        if (location != -1) {
            glUniform4f(location, r, g, b, 1);
        }
    }

    public void setUniform(String name, float x, float y) {
        int location = glGetUniformLocation(program, name);
        if (location != -1) {
            glUniform2f(location, x, y);
        }
    }

    public void setUniform(String name, float time) {
        int location = glGetUniformLocation(program, name);
        if (location != -1) {
            glUniform1f(location, time);
        }


    }

    public void setUniform(String name, int sampler) {
        int location = glGetUniformLocation(program, name);
        if (location != -1) {
            glUniform1i(location, sampler);
        }


    }

    public void setUniform(String name, Matrix4f value) {
        int location = glGetUniformLocation(program, name);
        if (location != -1) {
            FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
            value.get(buffer);
//        buffer.flip();
            glUniformMatrix4fv(location, false, buffer);
        }


    }


    public void bind() {
        glUseProgram(program);
    }

    private String readFile(String filename) {
        StringBuilder string = new StringBuilder();
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader("./shaders/" + filename));
            String line;
            while ((line = br.readLine()) != null) {
                string.append(line).append("\n");

            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string.toString();
    }

    @Override
    public void clean() {
        glDetachShader(program, vs);
        glDetachShader(program, fs);
        glDeleteShader(vs);
        glDeleteShader(fs);
        glDeleteProgram(program);
    }
}
