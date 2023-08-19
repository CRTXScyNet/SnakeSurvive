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



vec2 uv = coords-0.5;
vec2 uv2 = coords-0.5;


s3 = step(length(uv),.5);


float visibility = max( (1 - length(uv)),0);
vec4 color = vec4(0.15/length(uv)*0.1,0.15/length(uv)*0.1,0.15/length(uv)*0.1,0.15/length(uv)*0.1)*visibility;
vec4 color3 = vec4((40-length(ss))*0.015,(40-length(ss))*0.015,(40-length(ss))*0.015,1);

if(t <0){
    t *= -1;
    color /= t*10;


}



gl_FragColor = (color * selfColor*color3) ;

}