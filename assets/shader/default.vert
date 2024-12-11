#version 330

layout (location = 0) in vec2 pos;
layout (location = 1) in vec2 aTex;

uniform mat4 projection;

out vec2 tex;

void main()
{
    gl_Position = projection * vec4(pos,1.0,1.0);
    tex = aTex;
}