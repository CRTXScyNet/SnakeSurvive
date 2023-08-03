#version 120


//uniform int green;
uniform vec2 u_resolution;
uniform float time;
uniform sampler2D sampler;
uniform vec4 rgb;
uniform float dist;

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
vec2 uv2 = vec2(coords.x-0.5,coords.y-1.4);

//s = step(length(uv),0.4);
//s2 = step(length(uv),0.95);
s3 = step(length(uv2),.6);

//uv.x += (sin(time) + cos(time*2.8))/5;
//uv.y += (cos(time) + sin(time*5.1))/5;
//uv2.x -= (cos(time) + sin(time*3.5))/5;
//uv2.y -= (sin(time) + cos(time*4.5))/5;

//if(s >= length(uv)){
//vec4 color = vec4((0.55-length(uv))*7,(0.55-length(uv))*7,(0.55-length(uv))*7,(0.5-length(uv))*7);
vec4 color = vec4(0.5/length(vec2(uv.x,(uv.y-0.15)))*0.2,1,1,pow((abs(uv.y+0.5))/1.5 ,2) / abs(uv.x)*0.9);
vec4 color2 = vec4(1,1,1,(pow((abs(uv.y+0.5))/1.5 ,2) >= abs(uv.x) && (length(uv2)>s3) ? 1:0));
//}else{
//color = vec4(0,1,0,0);
//}
if(t <= 0){
    t = -t;
    color.a *= t;
    //color = vec4(0.5/length(uv)*0.1/(t*10),0.5/length(uv)*0.1/(t*10),0.5/length(uv)*0.1/(t*10),(0.1/length(uv)*0.1));
}else{

color*=abs(sin(t*5))+0.1;
}



gl_FragColor = (color * selfColor)*color2 ;

}