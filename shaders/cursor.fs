#version 120

uniform vec2 u_resolution;
uniform float time;
uniform sampler2D sampler;
uniform vec4 rgb;
uniform vec2 mousePos;

varying vec2 coords;



void main(){
    vec2 uv = coords-0.5;
    float dif =u_resolution.y/u_resolution.x;
    float t = time;
    vec4 selfColor = rgb;
vec2 mouse = mousePos/2;
    mouse.x /= dif;

vec2 cursor = (mouse.xy/u_resolution.xy);


    float color = max( (1/length(cursor+uv)*0.001)*(1-length(cursor+uv)*50),0);



    gl_FragColor =rgb*color;
}