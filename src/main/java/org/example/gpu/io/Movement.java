package org.example.gpu.io;

import org.joml.Matrix4f;
import org.joml.Vector3f;

//Класс для перемещения объектов на экране.
public class Movement {
    private Vector3f position;
    private static float width = 0;
    private static float height = 0;
    private static Matrix4f projection;
    private static Matrix4f interfaceProjection;
    private boolean isInterface = false;

    private float rotation = 0;
    private static float scale = 3;
    private static float curScale = 3;

    public Movement(int width, int height, boolean isInterface) {
        Movement.width = width;
        Movement.height = height;
        position = new Vector3f(0, 0, 0);

        this.isInterface = isInterface;
        if (isInterface) {
            setInterfaceProjection(width, height);
        } else {
            setProjection(width, height);
        }
    }

    public static void setZoom(float s) {
        if (s < 0) {
            return;
        }
        scale = 1 + s;
//        System.out.println("scale: " + scale);
    }

    public static void increaseScale() {
        if (scale < 2) {
            scale += 0.01;
        }
    }

    public static float getScale() {
        return curScale;
    }

    public static void decreaseScale() {
        if (scale > 1) {
            scale -= 0.01;
        }
    }

    public static void zoomCheck() {
        if (curScale != scale) {
            if (curScale > scale) {
                curScale -= 0.001;
            } else {
                curScale += 0.001;
            }
            refreshProjection();

        }
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public void addPosition(Vector3f position) {
        this.position.add(position);
    }

    public static void setProjection(/*Matrix4f projection*/float width, float height) {

        projection = new Matrix4f().setOrtho2D(-width / curScale, width / curScale, -height / curScale, height / curScale);
    }

    public static void setInterfaceProjection(/*Matrix4f projection*/float width, float height) {

        interfaceProjection = new Matrix4f().setOrtho2D(-width / 2, width / 2, -height / 2, height / 2);
    }

    public static void refreshProjection() {
        projection = new Matrix4f().setOrtho2D(-width / curScale, width / curScale, -height / curScale, height / curScale);
    }

    public void setRotation(float r) {
        this.rotation = r;
    }

    public Matrix4f projection() {
        Matrix4f target = new Matrix4f();
        Matrix4f pos = new Matrix4f().setTranslation(position).rotate(rotation, new Vector3f(0, 0, 1));
        if (isInterface) {
            target = interfaceProjection.mul(pos, target);
            return target;
        } else {
            target = projection.mul(pos, target);
            return target;
        }

    }

    public Vector3f getPosition() {
        return position;
    }
}
