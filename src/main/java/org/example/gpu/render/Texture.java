package org.example.gpu.render;

import org.lwjgl.BufferUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.ref.Cleaner;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

//Класс позволяющий использовать текстуру для моделей.
public class Texture implements Cleaner.Cleanable {
    private int id;
    private int width;
    private int height;

    public Texture(String filename) {
        BufferedImage bi;
        try {
            bi = ImageIO.read(new File(filename));
            width = bi.getWidth();
            height = bi.getHeight();
            int[] pixels = new int[width * height * 4];
            pixels = bi.getRGB(0, 0, width, height, null, 0, width);
            ByteBuffer bb = BufferUtils.createByteBuffer(width * height * 4);

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    try {
                        int pixel = pixels[i * width + j];
                        bb.put((byte) ((pixel >> 16) & 0xFF));        //RED
                        bb.put((byte) ((pixel >> 8) & 0xFF));         //GREEN
                        bb.put((byte) ((pixel) & 0xFF));            //BLUE
                        bb.put((byte) ((pixel >> 24) & 0xFF));        //ALPHA
                    } catch (Exception e) {

                    }
                }
            }
            bb.flip();


            id = glGenTextures();
            glBindTexture(GL_TEXTURE_2D, id);

            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, bb);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void bind(int sampler) {
        if (sampler >= 0 && sampler <= 31) {
            glActiveTexture(GL_TEXTURE0 + sampler);
            glBindTexture(GL_TEXTURE_2D, id);
        }

    }

    @Override
    public void clean() {
        glDeleteTextures(id);
    }
}
