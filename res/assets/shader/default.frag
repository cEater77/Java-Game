#version 430

layout (binding=0) uniform sampler2D textureAtlas;

uniform vec3 camPos;
uniform float fogEnd;
uniform bool useFog;

in vec2 tex;
in float alpha;
in float isHighlighted;
in float darknessValue;
in vec3 fragPosition;

out vec4 fragColor;

void main()
{
    vec4 objectColor = texture(textureAtlas, tex) * vec4(1.0f, 1.0f, 1.0f, alpha);
    vec3 finalColor = objectColor.rgb;
    if(useFog)
    {
       vec3 uFogColor = vec3(0.0f,0.0f,0.0f);
       float distance = length(fragPosition + camPos);
       float fogFactor = clamp(distance / fogEnd, 0.0, 1.0);
       finalColor = mix(objectColor.rgb, uFogColor, fogFactor);
    }

    fragColor = mix(vec4(finalColor, objectColor.w), vec4(1.0f, 1.0f, 1.0f, objectColor.w), isHighlighted / 2.0f);
}