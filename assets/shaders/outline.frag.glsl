#define positionFlag
#define tangentFlag
#define normalFlag
#define lightingFlag
#define ambientCubemapFlag
#define numPointLights 5
#define numSpotLights 0
#define texCoord0Flag
#define diffuseTextureFlag
#define diffuseTextureCoord texCoord0
#define normalTextureFlag
#define normalTextureCoord texCoord0
#define baseColorFactorFlag
#define metallicRoughnessTextureFlag
#define diffuseSpecularEnvSeparateFlag
#define USE_IBL
#define brdfLUTTexture
#define ambientLightFlag
#define MANUAL_SRGB
#define GAMMA_CORRECTION 2.2
#define TS_MANUAL_SRGB
#define MS_MANUAL_SRGB
#define v_diffuseUV v_texCoord0
#define v_normalUV v_texCoord0
#define v_metallicRoughnessUV v_texCoord0
#define textureFlag
#ifdef GL_ES
precision mediump float;
precision mediump int;
#endif

uniform sampler2D u_texture;

// The inverse of the viewport dimensions along X and Y
uniform vec2 u_viewportInverse;

// Color of the outline
uniform vec3 u_color;

// Thickness of the outline
uniform float u_offset;

// Step to check for neighbors
uniform float u_step;

varying vec4 v_color;
varying vec2 v_texCoord;

#define ALPHA_VALUE_BORDER 0.5

void main() {
    vec2 T = v_texCoord.xy;

    float alpha = 0.0;
    bool allin = true;
    for( float ix = -u_offset; ix < u_offset; ix += u_step )
    {
        for( float iy = -u_offset; iy < u_offset; iy += u_step )
        {
            float newAlpha = texture2D(u_texture, T + vec2(ix, iy) * u_viewportInverse).a;
            allin = allin && newAlpha > ALPHA_VALUE_BORDER;
            if (newAlpha > ALPHA_VALUE_BORDER && newAlpha >= alpha)
            {
                alpha = newAlpha;
            }
        }
    }
    if (allin)
    {
        alpha = 0.0;
    }

    gl_FragColor = vec4(u_color,alpha);
}