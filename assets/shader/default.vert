#version 430

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 offset;
layout (location = 2) in vec2 aTex;
layout (location = 3) in float aAlpha;
layout (location = 4) in float aIsHighlighted;

uniform mat4 projection;
uniform mat3 isometricView;
uniform vec2 worldOrigin;
uniform vec3 camPos;

out vec2 tex;
out float alpha;
out float isHighlighted;

void main()
{
    gl_Position = projection * vec4(vec3(offset,0) + isometricView * position + isometricView * camPos + vec3(worldOrigin,0), 1);
    tex = aTex;
    alpha = aAlpha;
    isHighlighted = aIsHighlighted;
}