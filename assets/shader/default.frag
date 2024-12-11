#version 330

layout (binding=0) uniform sampler2D textureAtlas;
in vec2 tex;

out vec4 fragColor;

void main()
{
    fragColor = texture(textureAtlas, tex);
}