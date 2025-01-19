#version 430

layout (binding=0) uniform sampler2D textureAtlas;

in vec2 tex;
in float alpha;
in float isHighlighted;

out vec4 fragColor;

void main()
{
    float darkness = 0.5f;
    vec4 objectColor = texture(textureAtlas, tex) * vec4(1.0f, 1.0f, 1.0f, alpha) * vec4(darkness, darkness, darkness, 1.0f);
    fragColor = mix(objectColor, vec4(1.0f, 1.0f, 1.0f, objectColor.w), isHighlighted / 2.0f);
}