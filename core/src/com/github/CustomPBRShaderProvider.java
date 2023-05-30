package com.github;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.utils.GdxRuntimeException;
import net.mgsx.gltf.scene3d.attributes.*;
import net.mgsx.gltf.scene3d.shaders.PBRCommon;
import net.mgsx.gltf.scene3d.shaders.PBRShader;
import net.mgsx.gltf.scene3d.shaders.PBRShaderConfig;
import net.mgsx.gltf.scene3d.shaders.PBRShaderProvider;
import net.mgsx.gltf.scene3d.utils.LightUtils;

public class CustomPBRShaderProvider extends PBRShaderProvider {
    private SinglePlayerGame game;
    private static final LightUtils.LightsInfo lightsInfo = new LightUtils.LightsInfo();
    public CustomPBRShaderProvider(SinglePlayerGame game) {
        super(buildPBRShaderConfig());
//        super(PBRShaderProvider.createDefaultConfig());
        this.game = game;
    }

    @Override
    protected Shader createShader(Renderable renderable) {
//        if (renderable.userData == game.getPlayer())
//            return createGreenOutlineShader(renderable);
//        else return new DefaultShader(renderable, config);
//        return createDefaultShader(renderable);
        return createGreenOutlineShader(renderable);
    }

    private Shader createGreenOutlineShader(Renderable renderable) {
        PBRShader shader = new PBRShader(renderable, config, DefaultShader.createPrefix(renderable, config));
//        shader.init(new ShaderProgram(Gdx.files.internal("shaders/outline.vert.glsl"), Gdx.files.internal("shaders/outline.frag.glsl")), renderable);
        return shader;
//        return new DefaultShader(renderable, config);
    }

    private Shader createDefaultShader(Renderable renderable) {
//        return new WaterShader(renderable, config);
//        return new DefaultShader(renderable, config);
        PBRShader shader = createShader(renderable, (PBRShaderConfig) config, genPrefix(renderable));
        checkShaderCompilation(shader.program);

        // prevent infinite loop (TODO remove this for libgdx 1.9.12+)
        if(!shader.canRender(renderable)){
            throw new GdxRuntimeException("cannot render with this shader");
        }
        return shader;
    }

    public String genPrefix(Renderable renderable) {
        PBRShaderConfig config = (PBRShaderConfig)this.config;

        String prefix = createPrefixBase(renderable, config);

        // Morph targets
        prefix += morphTargetsPrefix(renderable);

        // optional base color factor
        if(renderable.material.has(PBRColorAttribute.BaseColorFactor)){
            prefix += "#define baseColorFactorFlag\n";
        }

        // Lighting
        int primitiveType = renderable.meshPart.primitiveType;
        boolean isLineOrPoint = primitiveType == GL20.GL_POINTS || primitiveType == GL20.GL_LINES || primitiveType == GL20.GL_LINE_LOOP || primitiveType == GL20.GL_LINE_STRIP;
        boolean unlit = isLineOrPoint || renderable.material.has(PBRFlagAttribute.Unlit) || renderable.meshPart.mesh.getVertexAttribute(VertexAttributes.Usage.Normal) == null;

        if(unlit){

            prefix += "#define unlitFlag\n";

        }else{

            if(renderable.material.has(PBRTextureAttribute.MetallicRoughnessTexture)){
                prefix += "#define metallicRoughnessTextureFlag\n";
            }
            if(renderable.material.has(PBRTextureAttribute.OcclusionTexture)){
                prefix += "#define occlusionTextureFlag\n";
            }
            if(renderable.material.has(PBRFloatAttribute.TransmissionFactor)){
                prefix += "#define transmissionFlag\n";
            }
            if(renderable.material.has(PBRTextureAttribute.TransmissionTexture)){
                prefix += "#define transmissionTextureFlag\n";
            }
            if(renderable.material.has(PBRVolumeAttribute.Type)){
                prefix += "#define volumeFlag\n";
            }
            if(renderable.material.has(PBRTextureAttribute.ThicknessTexture)){
                prefix += "#define thicknessTextureFlag\n";
            }
            if(renderable.material.has(PBRFloatAttribute.IOR)){
                prefix += "#define iorFlag\n";
            }

            // Material specular
            boolean hasSpecular = false;
            if(renderable.material.has(PBRFloatAttribute.SpecularFactor)){
                prefix += "#define specularFactorFlag\n";
                hasSpecular = true;
            }
            if(renderable.material.has(PBRHDRColorAttribute.Specular)){
                hasSpecular = true;
                prefix += "#define specularColorFlag\n";
            }
            if(renderable.material.has(PBRTextureAttribute.SpecularFactorTexture)){
                prefix += "#define specularFactorTextureFlag\n";
                hasSpecular = true;
            }
            if(renderable.material.has(PBRTextureAttribute.SpecularColorTexture)){
                prefix += "#define specularColorTextureFlag\n";
                hasSpecular = true;
            }
            if(hasSpecular){
                prefix += "#define specularFlag\n";
            }

            // Material Iridescence
            if(renderable.material.has(PBRIridescenceAttribute.Type)){
                prefix += "#define iridescenceFlag\n";
            }
            if(renderable.material.has(PBRTextureAttribute.IridescenceTexture)){
                prefix += "#define iridescenceTextureFlag\n";
            }
            if(renderable.material.has(PBRTextureAttribute.IridescenceThicknessTexture)){
                prefix += "#define iridescenceThicknessTextureFlag\n";
            }
            if(renderable.environment.has(ClippingPlaneAttribute.Type)){
                prefix += "#define clippingPlaneFlag\n";
            }
            CascadeShadowMapAttribute csm = renderable.environment.get(CascadeShadowMapAttribute.class, CascadeShadowMapAttribute.Type);
            if(csm != null){
                prefix += "#define numCSM " + csm.cascadeShadowMap.lights.size + "\n";
            }

            // IBL options
            PBRCubemapAttribute specualarCubemapAttribute = null;
            MirrorAttribute specularMirrorAttribute = null;
            if(renderable.environment != null) {
                if (renderable.environment.has(PBRTextureAttribute.TransmissionSourceTexture)) {
                    prefix += "#define transmissionSourceFlag\n";
                }
                if (renderable.environment.has(PBRCubemapAttribute.SpecularEnv)) {
                    prefix += "#define diffuseSpecularEnvSeparateFlag\n";
                    specualarCubemapAttribute = renderable.environment.get(PBRCubemapAttribute.class, PBRCubemapAttribute.SpecularEnv);
                } else if (renderable.environment.has(PBRCubemapAttribute.DiffuseEnv)) {
                    specualarCubemapAttribute = renderable.environment.get(PBRCubemapAttribute.class, PBRCubemapAttribute.DiffuseEnv);
                } else if (renderable.environment.has(PBRCubemapAttribute.EnvironmentMap)) {
                    specualarCubemapAttribute = renderable.environment.get(PBRCubemapAttribute.class, PBRCubemapAttribute.EnvironmentMap);
                }

                if (renderable.environment.has(MirrorSourceAttribute.Type) && renderable.material.has(MirrorAttribute.Specular)) {
                    specularMirrorAttribute = renderable.environment.get(MirrorAttribute.class, MirrorAttribute.Specular);
                    prefix += "#define mirrorSpecularFlag\n";
                }

                if (specualarCubemapAttribute != null || specularMirrorAttribute != null) {
                    prefix += "#define USE_IBL\n";

                    boolean textureLodSupported;
                    if (isGL3()) {
                        textureLodSupported = true;
                    } else if (Gdx.graphics.supportsExtension("EXT_shader_texture_lod")) {
                        prefix += "#define USE_TEXTURE_LOD_EXT\n";
                        textureLodSupported = true;
                    } else {
                        textureLodSupported = false;
                    }

                    if (specualarCubemapAttribute != null) {
                        Texture.TextureFilter textureFilter = specualarCubemapAttribute.textureDescription.minFilter != null ? specualarCubemapAttribute.textureDescription.minFilter : specualarCubemapAttribute.textureDescription.texture.getMinFilter();
                        if (textureLodSupported && textureFilter.equals(Texture.TextureFilter.MipMap)) {
                            prefix += "#define USE_TEX_LOD\n";
                        }
                    }

                    if (renderable.environment.has(PBRTextureAttribute.BRDFLUTTexture)) {
                        prefix += "#define brdfLUTTexture\n";
                    }
                }
                // TODO check GLSL extension 'OES_standard_derivatives' for WebGL

                if (renderable.environment.has(ColorAttribute.AmbientLight)) {
                    prefix += "#define ambientLightFlag\n";
                }

                if (renderable.environment.has(PBRMatrixAttribute.EnvRotation)) {
                    prefix += "#define ENV_ROTATION\n";
                }
            }

        }

        // SRGB
        prefix += createPrefixSRGB(renderable, config);


        // multi UVs
        int maxUVIndex = -1;

        {
            TextureAttribute attribute = renderable.material.get(TextureAttribute.class, TextureAttribute.Diffuse);
            if(attribute != null){
                prefix += "#define v_diffuseUV v_texCoord" + attribute.uvIndex + "\n";
                maxUVIndex = Math.max(maxUVIndex, attribute.uvIndex);
            }
        }
        {
            TextureAttribute attribute = renderable.material.get(TextureAttribute.class, TextureAttribute.Emissive);
            if(attribute != null){
                prefix += "#define v_emissiveUV v_texCoord" + attribute.uvIndex + "\n";
                maxUVIndex = Math.max(maxUVIndex, attribute.uvIndex);
            }
        }
        {
            TextureAttribute attribute = renderable.material.get(TextureAttribute.class, TextureAttribute.Normal);
            if(attribute != null){
                prefix += "#define v_normalUV v_texCoord" + attribute.uvIndex + "\n";
                maxUVIndex = Math.max(maxUVIndex, attribute.uvIndex);
            }
        }
        {
            TextureAttribute attribute = renderable.material.get(TextureAttribute.class, PBRTextureAttribute.MetallicRoughnessTexture);
            if(attribute != null){
                prefix += "#define v_metallicRoughnessUV v_texCoord" + attribute.uvIndex + "\n";
                maxUVIndex = Math.max(maxUVIndex, attribute.uvIndex);
            }
        }
        {
            TextureAttribute attribute = renderable.material.get(TextureAttribute.class, PBRTextureAttribute.OcclusionTexture);
            if(attribute != null){
                prefix += "#define v_occlusionUV v_texCoord" + attribute.uvIndex + "\n";
                maxUVIndex = Math.max(maxUVIndex, attribute.uvIndex);
            }
        }
        {
            TextureAttribute attribute = renderable.material.get(TextureAttribute.class, PBRTextureAttribute.TransmissionTexture);
            if(attribute != null){
                prefix += "#define v_transmissionUV v_texCoord" + attribute.uvIndex + "\n";
                maxUVIndex = Math.max(maxUVIndex, attribute.uvIndex);
            }
        }
        {
            TextureAttribute attribute = renderable.material.get(TextureAttribute.class, PBRTextureAttribute.ThicknessTexture);
            if(attribute != null){
                prefix += "#define v_thicknessUV v_texCoord" + attribute.uvIndex + "\n";
                maxUVIndex = Math.max(maxUVIndex, attribute.uvIndex);
            }
        }
        {
            TextureAttribute attribute = renderable.material.get(TextureAttribute.class, PBRTextureAttribute.SpecularFactorTexture);
            if(attribute != null){
                prefix += "#define v_specularFactorUV v_texCoord" + attribute.uvIndex + "\n";
                maxUVIndex = Math.max(maxUVIndex, attribute.uvIndex);
            }
        }
        {
            TextureAttribute attribute = renderable.material.get(TextureAttribute.class, PBRTextureAttribute.Specular);
            if(attribute != null){
                prefix += "#define v_specularColorUV v_texCoord" + attribute.uvIndex + "\n";
                maxUVIndex = Math.max(maxUVIndex, attribute.uvIndex);
            }
        }
        {
            TextureAttribute attribute = renderable.material.get(TextureAttribute.class, PBRTextureAttribute.IridescenceTexture);
            if(attribute != null){
                prefix += "#define v_iridescenceUV v_texCoord" + attribute.uvIndex + "\n";
                maxUVIndex = Math.max(maxUVIndex, attribute.uvIndex);
            }
        }
        {
            TextureAttribute attribute = renderable.material.get(TextureAttribute.class, PBRTextureAttribute.IridescenceThicknessTexture);
            if(attribute != null){
                prefix += "#define v_iridescenceThicknessUV v_texCoord" + attribute.uvIndex + "\n";
                maxUVIndex = Math.max(maxUVIndex, attribute.uvIndex);
            }
        }

        if(maxUVIndex >= 0){
            prefix += "#define textureFlag\n";
        }
        if(maxUVIndex == 1){
            prefix += "#define textureCoord1Flag\n";
        }else if(maxUVIndex > 1){
            throw new GdxRuntimeException("more than 2 texture coordinates attribute not supported");
        }

        // Fog

        if(renderable.environment != null && renderable.environment.has(FogAttribute.FogEquation)){
            prefix += "#define fogEquationFlag\n";
        }


        // colors
        for(VertexAttribute attribute : renderable.meshPart.mesh.getVertexAttributes()){
            if(attribute.usage == VertexAttributes.Usage.ColorUnpacked){
                prefix += "#define color" + attribute.unit + "Flag\n";
            }
        }

        //

        int numBoneInfluence = 0;
        int numMorphTarget = 0;
        int numColor = 0;

        for(VertexAttribute attribute : renderable.meshPart.mesh.getVertexAttributes()){
            if(attribute.usage == VertexAttributes.Usage.ColorPacked){
                throw new GdxRuntimeException("color packed attribute not supported");
            }else if(attribute.usage == VertexAttributes.Usage.ColorUnpacked){
                numColor = Math.max(numColor, attribute.unit+1);
            }else if(attribute.usage == PBRVertexAttributes.Usage.PositionTarget && attribute.unit >= PBRCommon.MAX_MORPH_TARGETS ||
                    attribute.usage == PBRVertexAttributes.Usage.NormalTarget && attribute.unit >= PBRCommon.MAX_MORPH_TARGETS ||
                    attribute.usage == PBRVertexAttributes.Usage.TangentTarget && attribute.unit >= PBRCommon.MAX_MORPH_TARGETS ){
                numMorphTarget = Math.max(numMorphTarget, attribute.unit+1);
            }else if(attribute.usage == VertexAttributes.Usage.BoneWeight){
                numBoneInfluence = Math.max(numBoneInfluence, attribute.unit+1);
            }
        }


        PBRCommon.checkVertexAttributes(renderable);

        if(numBoneInfluence > 8){
            Gdx.app.error(TAG, "more than 8 bones influence attributes not supported: " + numBoneInfluence + " found.");
        }
        if(numMorphTarget > PBRCommon.MAX_MORPH_TARGETS){
            Gdx.app.error(TAG, "more than 8 morph target attributes not supported: " + numMorphTarget + " found.");
        }
        if(numColor > config.numVertexColors){
            Gdx.app.error(TAG, "more than " + config.numVertexColors + " color attributes not supported: " + numColor + " found.");
        }

        if(renderable.environment != null){
            LightUtils.getLightsInfo(lightsInfo, renderable.environment);
            if(lightsInfo.dirLights > config.numDirectionalLights){
                Gdx.app.error(TAG, "too many directional lights detected: " + lightsInfo.dirLights + "/" + config.numDirectionalLights);
            }
            if(lightsInfo.pointLights > config.numPointLights){
                Gdx.app.error(TAG, "too many point lights detected: " + lightsInfo.pointLights + "/" + config.numPointLights);
            }
            if(lightsInfo.spotLights > config.numSpotLights){
                Gdx.app.error(TAG, "too many spot lights detected: " + lightsInfo.spotLights + "/" + config.numSpotLights);
            }
            if(lightsInfo.miscLights > 0){
                Gdx.app.error(TAG, "unknow type lights not supported.");
            }
        }
        return prefix;

    }

    public static PBRShaderConfig buildPBRShaderConfig() {
        // Create and initialize PBR config
        PBRShaderConfig config = new PBRShaderConfig();
//        config.vertexShader = ShaderParser.parse(Gdx.files.internal("shaders/outline.vert.glsl"));
//        config.fragmentShader = ShaderParser.parse(Gdx.files.internal("shaders/outline.frag.glsl"));
        config.vertexShader = getDefaultVertexShader();
        config.fragmentShader = getDefaultFragmentShader();
        return config;
    }
}
