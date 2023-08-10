package org.example.gpu.io;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Movement {
    private Vector3f position;
    private static Matrix4f projection;
    private float rotation = 0;

 public Movement(int width,int height){
     position = new Vector3f(0,0,0);
     setProjection(width,height);
 }

    public void setPosition(Vector3f position) {
        this.position = position;
    }
    public void addPosition(Vector3f position){
     this.position.add(position);
    }

    public static void setProjection(/*Matrix4f projection*/float width, float height) {
        projection = new Matrix4f().setOrtho2D(-width/2,width/2,-height/2,height/2);
    }
    public void setRotation(float r){
     this.rotation = r;
    }

    public Matrix4f projection() {
     Matrix4f target = new Matrix4f();
     Matrix4f pos = new Matrix4f().setTranslation(position).rotate(rotation,new Vector3f(0,0,1));
     target = projection.mul(pos,target);
        return target;
    }
    public Vector3f getPosition() {
        return position;
    }
}
