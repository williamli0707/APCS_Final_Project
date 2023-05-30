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
uniform mat4 u_projTrans;

attribute vec4 a_position;
attribute vec2 a_texCoord0;
attribute vec4 a_color;

varying vec4 v_color;
varying vec2 v_texCoord;

uniform vec2 u_viewportInverse;

void main() {
    gl_Position = u_projTrans * a_position;
    v_texCoord = a_texCoord0;
    v_color = a_color;
}