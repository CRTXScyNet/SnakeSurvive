#version 120


//uniform int green;
uniform vec2 u_resolution;
uniform float time;
uniform sampler2D sampler;
uniform vec4 rgb;

varying vec2 coords;

void main(){
vec2  ss = gl_FragCoord.xy/u_resolution.xy;
float dif =u_resolution.y/u_resolution.x;
float t = time;
vec4 selfColor = rgb;
ss -= 0.5;
ss.x /=dif;
ss*=100;

float s ;
float s2;
float s3;
float s4;


vec2 uv = coords-0.5;
vec2 uv2 = coords-0.5;

s  = step(length(uv),length(abs(sin(time+5)))/50);
s2 = step(length(uv),length(abs(sin(time+5)))/50+.004);
s3 = step(length(uv),length(abs(sin(time)))/50);
s4 = step(length(uv),length(abs(sin(time)))/50+.004);
vec4 ringColor = vec4(0, ((length(uv) >= s3 && length(uv) <= s4)||(length(uv) >= s && length(uv) <= s2))? 1 : 0 , 0,0);

vec4 color = vec4(abs(sin(uv.x*t/uv.y)*sin(uv.y*t/uv.x)) , abs(sin(uv.x*(t+1)/uv.y)*sin(uv.y*(t+1)/uv.x)) , abs(sin(uv.x*(t+2)/uv.y)*sin(uv.y*(t+2)/uv.x)),(1-length(uv)*20));
vec4 color3 = vec4((40-length(ss))*0.015,(40-length(ss))*0.015,(40-length(ss))*0.015,1);

if(t <0){
    t *= -1;
        color.a *= sin(t);
    ringColor.a *= sin(t);
}



gl_FragColor = (color * selfColor*color3) +ringColor*color3;

}