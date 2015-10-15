package info.evshiron.ingresscraft.client.renderers;

import info.evshiron.ingresscraft.Constants;
import info.evshiron.ingresscraft.IngressCraft;
import info.evshiron.ingresscraft.entities.EntityResonator;
import info.evshiron.ingresscraft.utils.IngressDeserializer;
import info.evshiron.ingresscraft.utils.RendererUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector4f;

import java.io.*;

/**
 * Created by evshiron on 5/28/15.
 */
public class ResonatorRenderer extends RenderEntity {

    public static final ResourceLocation ResonatorRingTextureLocation = new ResourceLocation(IngressCraft.MODID, "textures/entities/genericModTexture.png");
    public static final ResourceLocation ResonatorXMTextureLocation = new ResourceLocation(IngressCraft.MODID, "textures/entities/objectXMTexture.png");
    //public static final IModelCustom ResonatorRingModel = AdvancedModelLoader.loadModel(new ResourceLocation(IngressCraft.MODID, "models/entities/out_texturedResonatorRing.obj"));
    //public static final IModelCustom ResonatorXMModel = AdvancedModelLoader.loadModel(new ResourceLocation(IngressCraft.MODID, "models/entities/out_texturedResonatorXM.obj"));

    IngressDeserializer.IngressModel mResonatorRingModel;
    IngressDeserializer.IngressModel mResonatorXMModel;

    int mRingShaderProgram = 0;
    int mXMShaderProgram = 0;

    int mRingVertexBufferObjectId = 0;
    int mRingIndexBufferObjectId = 0;
    int mXMVertexBufferObjectId = 0;
    int mXMIndexBufferObjectId = 0;

    public ResonatorRenderer() {

        mResonatorRingModel = IngressDeserializer.Deserialize(getClass().getResourceAsStream("/assets/ingresscraft/models/entities/texturedResonatorRing.obj"));
        mResonatorXMModel = IngressDeserializer.Deserialize(getClass().getResourceAsStream("/assets/ingresscraft/models/entities/texturedResonatorXM.obj"));

    }

    double getElapsedTime() {

        return (((double) Minecraft.getSystemTime() / 1000.0) % 300.0) * 0.1;

    }

    void applyColorByConstant(Vector4f color) {

        GL20.glUniform4f(GL20.glGetUniformLocation(mRingShaderProgram, "u_color0"), color.x, color.y, color.z, color.w);

    }

    void applyColorByLevel(int level) {

        switch(level) {
            case 1:
                applyTeamColorByConstant(Constants.QualityColor.L1);
                break;
            case 2:
                applyTeamColorByConstant(Constants.QualityColor.L2);
                break;
            case 3:
                applyTeamColorByConstant(Constants.QualityColor.L3);
                break;
            case 4:
                applyTeamColorByConstant(Constants.QualityColor.L4);
                break;
            case 5:
                applyTeamColorByConstant(Constants.QualityColor.L5);
                break;
            case 6:
                applyTeamColorByConstant(Constants.QualityColor.L6);
                break;
            case 7:
                applyTeamColorByConstant(Constants.QualityColor.L7);
                break;
            case 8:
                applyTeamColorByConstant(Constants.QualityColor.L8);
                break;
        }

    }

    void applyTeamColorByConstant(Vector4f color) {

        GL20.glUniform4f(GL20.glGetUniformLocation(mXMShaderProgram, "u_teamColor"), color.x, color.y, color.z, color.w);

    }

    void applyTeamColorByFaction(int faction) {

        switch(faction) {
            case Constants.Faction.RESISTANCE:
                applyTeamColorByConstant(Constants.TeamColor.RESISTANCE);
                break;
            case Constants.Faction.ENLIGHTENED:
                applyTeamColorByConstant(Constants.TeamColor.ENLIGHTENED);
                break;
        }

    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float param5, float param6) {

        doRender((EntityResonator) entity, x, y, z, param5, param6);

    }

    void doRender(EntityResonator entity, double x, double y, double z, float param5, float param6) {

        if(mRingShaderProgram == 0 || mRingShaderProgram == 0) {

            int ringShaderProgram = GL20.glCreateProgram();
            int ringVertexShader = RendererUtil.CreateShader(GL20.GL_VERTEX_SHADER, getClass().getResourceAsStream("/assets/ingresscraft/shaders/bicolor_textured.glsl.vert"));
            int ringFragmentShader = RendererUtil.CreateShader(GL20.GL_FRAGMENT_SHADER, getClass().getResourceAsStream("/assets/ingresscraft/shaders/bicolor_textured.glsl.frag"));

            GL20.glAttachShader(ringShaderProgram, ringVertexShader);
            GL20.glAttachShader(ringShaderProgram, ringFragmentShader);

            GL20.glLinkProgram(ringShaderProgram);

            System.out.println(GL20.glGetProgramInfoLog(ringShaderProgram, GL20.GL_LINK_STATUS));

            int xmShaderProgram = GL20.glCreateProgram();
            int xmVertexShader = RendererUtil.CreateShader(GL20.GL_VERTEX_SHADER, getClass().getResourceAsStream("/assets/ingresscraft/shaders/xm.glsl.vert"));
            int xmFragmentShader = RendererUtil.CreateShader(GL20.GL_FRAGMENT_SHADER, getClass().getResourceAsStream("/assets/ingresscraft/shaders/xm.glsl.frag"));

            GL20.glAttachShader(xmShaderProgram, xmVertexShader);
            GL20.glAttachShader(xmShaderProgram, xmFragmentShader);

            GL20.glLinkProgram(xmShaderProgram);

            System.out.println(GL20.glGetProgramInfoLog(xmShaderProgram, GL20.GL_LINK_STATUS));

            mRingShaderProgram = ringShaderProgram;
            mXMShaderProgram = xmShaderProgram;

            int ringVertexBufferObjectId = GL15.glGenBuffers();

            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, ringVertexBufferObjectId);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, mResonatorRingModel.Vertices, GL15.GL_STATIC_DRAW);

            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

            int ringIndexBufferObjectId = GL15.glGenBuffers();

            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ringIndexBufferObjectId);
            GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, mResonatorRingModel.Indices, GL15.GL_STATIC_DRAW);

            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

            mRingVertexBufferObjectId = ringVertexBufferObjectId;
            mRingIndexBufferObjectId = ringIndexBufferObjectId;

            int xmVertexBufferObjectId = GL15.glGenBuffers();

            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, xmVertexBufferObjectId);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, mResonatorXMModel.Vertices, GL15.GL_STATIC_DRAW);

            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

            int xmIndexBufferObjectId = GL15.glGenBuffers();

            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ringIndexBufferObjectId);
            GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, mResonatorXMModel.Indices, GL15.GL_STATIC_DRAW);

            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

            mXMVertexBufferObjectId = xmVertexBufferObjectId;
            mXMIndexBufferObjectId = xmIndexBufferObjectId;

        }

        GL11.glPushMatrix();

        GL11.glTranslated(x, y + 1, z);

        GL20.glUseProgram(mRingShaderProgram);

        applyColorByLevel(entity.Level);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        bindTexture(ResonatorRingTextureLocation);
        GL20.glUniform1i(GL20.glGetUniformLocation(mRingShaderProgram, "u_texture"), 0);

        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, mRingVertexBufferObjectId);
        GL11.glVertexPointer(3, GL11.GL_FLOAT, 20, 0);

        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, mRingVertexBufferObjectId);
        GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 20, 12);

        //GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, mRingIndexBufferObjectId);
        GL11.glDrawElements(GL11.GL_TRIANGLES, mResonatorRingModel.Indices);
        //ResonatorRingModel.renderAll();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

        GL20.glUseProgram(0);

        GL20.glUseProgram(mXMShaderProgram);

        GL20.glUniform1f(GL20.glGetUniformLocation(mXMShaderProgram, "u_elapsedTime"), (float) getElapsedTime());

        applyTeamColorByFaction(entity.Faction);

        GL20.glUniform4f(GL20.glGetUniformLocation(mXMShaderProgram, "u_altColor"), 0.6f, 0.4f, 0.6f, 0.8f);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        bindTexture(ResonatorXMTextureLocation);
        GL20.glUniform1i(GL20.glGetUniformLocation(mXMShaderProgram, "u_texture"), 0);

        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, mXMVertexBufferObjectId);
        GL11.glVertexPointer(3, GL11.GL_FLOAT, 20, 0);

        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, mXMVertexBufferObjectId);
        GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 20, 12);

        //GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, mXMIndexBufferObjectId);
        GL11.glDrawElements(GL11.GL_TRIANGLES, mResonatorXMModel.Indices);
        //ResonatorXMModel.renderAll();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

        GL20.glUseProgram(0);

        GL11.glPopMatrix();

    }

}
