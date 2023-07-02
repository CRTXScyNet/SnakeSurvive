package org.example.gpu.io;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Movement {
    private Vector3f position;
    private Matrix4f projection;

 public Movement(int width,int height){
     position = new Vector3f(0,0,0);
     projection = new Matrix4f().setOrtho2D(-width/2,width/2,-height/2,height/2);
 }

    public void setPosition(Vector3f position) {
        this.position = position;
    }
    public void addPosition(Vector3f position){
     this.position.add(position);
    }

    public void setProjection(Matrix4f projection) {
        this.projection = projection;
    }

    public Matrix4f projection() {
     Matrix4f target = new Matrix4f();
     Matrix4f pos = new Matrix4f().setTranslation(position);
     target = projection.mul(pos,target);
        return target;
    }
    public Vector3f getPosition() {
        return position;
    }
}
