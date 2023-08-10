#version 120


//uniform int green;
uniform vec2 u_resolution;
uniform float time;
uniform sampler2D sampler;
uniform vec4 rgb;

varying vec2 coords;


float modulo(float mainF, float dev) {
    if (mainF > 0) {
        return (mainF / dev) - floor(mainF / dev);
    } else {
        return (mainF / dev) + floor(mainF / dev);
    }


}

void main() {
    vec2 ss = gl_FragCoord.xy / u_resolution.xy;
    float dif = u_resolution.y / u_resolution.x;
    float t = time;
    vec4 selfColor = rgb;
    ss -= 0.5;
    ss.x /= dif;
    ss *= 100;

    float s;
    float s2;
    float s3;
    float s4;

    float distRing1;
    float distRing2;

    vec2 uv = coords - 0.5;
    vec2 uv2 = coords - 0.5;
    if(t<=-1){
//    s3 = step(length(uv), length(abs(sin(1.0))) / 2);
s4 = step(length(uv),length(abs(1.0))/2);

    }else{

//        s3 = step(length(uv),length(abs(t))/2);
        s4 = step(length(uv),length(abs(t))/2);
    }
//    s = step(length(uv), length(abs(sin(time + 0.5))) / 2);
//    s2 = step(length(uv), length(abs(sin(time + 0.5))) / 2 + .03);

    distRing1 = step(modulo(length(uv)-t*0.5, 0.11), length(1.0) / 2) * length(uv);
//    distRing2 = step(modulo(length(uv), 0.1), length(abs(t)) / 2) * length(uv);

    float visibility = (s4 / length(uv)*0.5 - s4);

    vec2 dr = vec2(length(uv),atan(uv.x,uv.y))/2;
    float ring = ((length(uv) == distRing1/** && length(uv) <= distRing2*/)) ? 1 : 0;
    ring*=abs(sin((dr.y-(t))*10+(abs(dr.x)*54)));
    ring = ring;

//    ring*=visibility;

    vec4 ringColor = vec4(ring, ring, ring, visibility);
//    ringColor*=selfColor;


    vec4 color = vec4(visibility,visibility, visibility, visibility);
    vec4 color3 = vec4((40 - length(ss)) * 0.015, (40 - length(ss)) * 0.015, (40 - length(ss)) * 0.015, 1);
//    color *= selfColor;

    //    vec2 dr = vec2(length(uv),atan(uv.x,uv.y));
    //
    //    float rings = step(abs(mod(dr.x*4+t,1-dr.x*dr.x*.009)-.1),.1);
    //    rings+=sin((dr.y+t)*10+(dr.x*109));
    //    rings = rings*(1-dr.x);
    //    rings = step (.9,rings);
    //  vec4  outColor = vec4(rings,rings*dr.x*2,rings*(dr.y*.3+.90)*.4,1);


    gl_FragColor = ringColor * color3*color*selfColor;

    if (t <= 0 && t >= -1) {
        t = -t;
        gl_FragColor *= t;
    }
}