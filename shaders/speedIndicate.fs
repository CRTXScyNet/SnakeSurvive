#version 120

uniform vec2 u_resolution;
uniform float time;
uniform sampler2D sampler;
uniform vec4 rgb;
uniform float speedScale;
uniform float curSpeed;

varying vec2 coords;


float modulo(float mainF,float dev){

return (mainF/dev)-floor(mainF/dev);
}



void main(){
vec4 selfColor = rgb;
float t = time;
vec2 uv = coords - 0.5;
float speed = curSpeed;

uv.y -= 0.3;


float c = length(uv)<0.5 && uv.y< -0.2? length(uv) :0;  // radius obrezania
float r = modulo((-speed+abs((uv.y+0.5)*uv.x)),0.05 )<= 0.3? 1:0;  // uzor

vec4 color = vec4(r*c+c*10,c*(((length(uv))*r))*2,0,length(vec2(uv.x,(uv.y-0.3))*4)-2.6);


//
gl_FragColor = color*(speedScale);
}


