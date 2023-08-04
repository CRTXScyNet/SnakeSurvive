#version 120


attribute vec3 vertices;
attribute vec2 shaderVertices;

varying vec2 coords;

uniform mat4 projection;

void main(){
    coords = shaderVertices;
    gl_Position = projection * vec4(vertices, 1);

}