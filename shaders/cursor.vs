#version 120


attribute vec3 vertices;
attribute vec2 shaderVertices;
attribute vec2 mousePos;

varying vec2 coords;
varying vec2 mouse;

uniform mat4 projection;

void main(){
    coords = shaderVertices;
    mouse = mousePos;
    gl_Position = projection * vec4(vertices, 1);

}