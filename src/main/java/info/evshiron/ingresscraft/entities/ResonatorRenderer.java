package info.evshiron.ingresscraft.entities;

import info.evshiron.ingresscraft.IngressCraft;
import info.evshiron.ingresscraft.utils.IngressDeserializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

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

    String readAllFromStream(InputStream source) throws IOException {

        StringBuilder sb = new StringBuilder();

        BufferedReader br = new BufferedReader(new InputStreamReader(source));

        String line;
        while((line = br.readLine()) != null) {

            sb.append(line).append("\n");

        }

        return sb.toString();

    }

    int createShader(int type, InputStream stream) {

        int shader = GL20.glCreateShader(type);

        try {

            String source = readAllFromStream(stream);
            GL20.glShaderSource(shader, source);
            GL20.glCompileShader(shader);

            System.out.println(GL20.glGetShaderInfoLog(shader, GL20.GL_COMPILE_STATUS));

            return shader;

        } catch (IOException e) {

            e.printStackTrace();
            return 0;

        }

    }

    double getElapsedTime() {

        return (((double) Minecraft.getSystemTime() / 1000.0) % 300.0) * 0.1;

    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float param5, float param6) {

        if(mRingShaderProgram == 0 || mRingShaderProgram == 0) {

            int ringShaderProgram = GL20.glCreateProgram();
            int ringVertexShader = createShader(GL20.GL_VERTEX_SHADER, getClass().getResourceAsStream("/assets/ingresscraft/shaders/bicolor_textured.glsl.vert"));
            int ringFragmentShader = createShader(GL20.GL_FRAGMENT_SHADER, getClass().getResourceAsStream("/assets/ingresscraft/shaders/bicolor_textured.glsl.frag"));

            GL20.glAttachShader(ringShaderProgram, ringVertexShader);
            GL20.glAttachShader(ringShaderProgram, ringFragmentShader);

            GL20.glLinkProgram(ringShaderProgram);

            System.out.println(GL20.glGetProgramInfoLog(ringShaderProgram, GL20.GL_LINK_STATUS));

            int xmShaderProgram = GL20.glCreateProgram();
            int xmVertexShader = createShader(GL20.GL_VERTEX_SHADER, getClass().getResourceAsStream("/assets/ingresscraft/shaders/xm.glsl.vert"));
            int xmFragmentShader = createShader(GL20.GL_FRAGMENT_SHADER, getClass().getResourceAsStream("/assets/ingresscraft/shaders/xm.glsl.frag"));

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

        GL20.glUniform4f(GL20.glGetUniformLocation(mRingShaderProgram, "u_color0"), 0.5882352941176471f, 0.15294117647058825f, 0.9568627450980393f, 1.0f);

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

        GL20.glUniform4f(GL20.glGetUniformLocation(mXMShaderProgram, "u_teamColor"), 0.92f, 0.7f, 0.89f, 1.0f);
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
