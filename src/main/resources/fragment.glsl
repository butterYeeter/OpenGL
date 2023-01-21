#version 330
out vec4 FragColor;
in vec3 color;
in vec2 tex;

uniform sampler2D tex0;

void main()
{
    gl_FragColor = texture(tex0, tex) * vec4(color, 1.0);
//    gl_FragColor = vec4(color, 1.0);
}