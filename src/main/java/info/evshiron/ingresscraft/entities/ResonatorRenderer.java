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

    String getInfoLogARB(int handle) {

        return ARBShaderObjects.glGetInfoLogARB(handle, ARBShaderObjects.glGetObjectParameteriARB(handle, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));

    }

    int createShader(int type, InputStream stream) {

        int shader = ARBShaderObjects.glCreateShaderObjectARB(type);

        try {

            String source = readAllFromStream(stream);
            ARBShaderObjects.glShaderSourceARB(shader, source);
            ARBShaderObjects.glCompileShaderARB(shader);

            if(ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE) {

                System.out.println(source);
                System.out.println(getInfoLogARB(shader));
                return 0;

            }
            else {

                return shader;

            }

        } catch (IOException e) {

            e.printStackTrace();
            return 0;

        }


    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float param5, float param6) {

        if(mRingShaderProgram == 0 || mRingShaderProgram == 0) {

            int ringShaderProgram = ARBShaderObjects.glCreateProgramObjectARB();
            int ringVertexShader = createShader(ARBVertexShader.GL_VERTEX_SHADER_ARB, getClass().getResourceAsStream("/assets/ingresscraft/shaders/bicolor_textured.glsl.vert"));
            int ringFragmentShader = createShader(ARBFragmentShader.GL_FRAGMENT_SHADER_ARB, getClass().getResourceAsStream("/assets/ingresscraft/shaders/bicolor_textured.glsl.frag"));

            ARBShaderObjects.glAttachObjectARB(ringShaderProgram, ringVertexShader);
            ARBShaderObjects.glAttachObjectARB(ringShaderProgram, ringFragmentShader);

            ARBShaderObjects.glLinkProgramARB(ringShaderProgram);

            ARBShaderObjects.glValidateProgramARB(ringShaderProgram);

            if(ARBShaderObjects.glGetObjectParameteriARB(ringShaderProgram, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) {

                System.out.println(getInfoLogARB(ringShaderProgram));

            }
            else {

                System.out.println("RING_SHADER_PROGRAM_VALIDATED");

            }

            int xmShaderProgram = ARBShaderObjects.glCreateProgramObjectARB();
            int xmVertexShader = createShader(ARBVertexShader.GL_VERTEX_SHADER_ARB, getClass().getResourceAsStream("/assets/ingresscraft/shaders/xm.glsl.vert"));
            int xmFragmentShader = createShader(ARBFragmentShader.GL_FRAGMENT_SHADER_ARB, getClass().getResourceAsStream("/assets/ingresscraft/shaders/xm.glsl.frag"));

            ARBShaderObjects.glAttachObjectARB(ringShaderProgram, xmVertexShader);
            ARBShaderObjects.glAttachObjectARB(ringShaderProgram, xmFragmentShader);

            ARBShaderObjects.glLinkProgramARB(xmShaderProgram);

            ARBShaderObjects.glValidateProgramARB(xmShaderProgram);

            if(ARBShaderObjects.glGetObjectParameteriARB(xmShaderProgram, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) {

                System.out.println(getInfoLogARB(xmShaderProgram));

            }
            else {

                System.out.println("XM_SHADER_PROGRAM_VALIDATED");

            }

            mRingShaderProgram = ringShaderProgram;
            mXMShaderProgram = xmShaderProgram;

            int ringVertexBufferObjectId = ARBVertexBufferObject.glGenBuffersARB();

            ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, ringVertexBufferObjectId);
            ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, mResonatorRingModel.Vertices, ARBVertexBufferObject.GL_STATIC_DRAW_ARB);

            ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, 0);

            int ringIndexBufferObjectId = ARBVertexBufferObject.glGenBuffersARB();

            ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB, ringIndexBufferObjectId);
            ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB, mResonatorRingModel.Indices, ARBVertexBufferObject.GL_STATIC_DRAW_ARB);

            ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB, 0);

            mRingVertexBufferObjectId = ringVertexBufferObjectId;
            mRingIndexBufferObjectId = ringIndexBufferObjectId;

            int xmVertexBufferObjectId = ARBVertexBufferObject.glGenBuffersARB();

            ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, xmVertexBufferObjectId);
            ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, mResonatorXMModel.Vertices, ARBVertexBufferObject.GL_STATIC_DRAW_ARB);

            ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, 0);

            int xmIndexBufferObjectId = ARBVertexBufferObject.glGenBuffersARB();

            ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB, ringIndexBufferObjectId);
            ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB, mResonatorXMModel.Indices, ARBVertexBufferObject.GL_STATIC_DRAW_ARB);

            ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ELEMENT_ARRAY_BUFFER_ARB, 0);

            mXMVertexBufferObjectId = xmVertexBufferObjectId;
            mXMIndexBufferObjectId = xmIndexBufferObjectId;

        }

        GL11.glPushMatrix();

        GL11.glTranslated(x, y + 1, z);

        ARBShaderObjects.glUseProgramObjectARB(mRingShaderProgram);

        ARBShaderObjects.glUniform4fARB(ARBShaderObjects.glGetUniformLocationARB(mRingShaderProgram, "u_color0"), 0.5882352941176471f, 0.15294117647058825f, 0.9568627450980393f, 1.0f);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        bindTexture(ResonatorRingTextureLocation);
        ARBShaderObjects.glUniform1iARB(ARBShaderObjects.glGetUniformLocationARB(mRingShaderProgram, "u_texture"), 0);

        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, mRingVertexBufferObjectId);
        GL11.glVertexPointer(3, GL11.GL_FLOAT, 20, 0);

        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, mRingVertexBufferObjectId);
        GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 20, 12);

        //ARBVertexBufferObject.glBindBufferARB(GL15.GL_ELEMENT_ARRAY_BUFFER, mRingIndexBufferObjectId);
        GL11.glDrawElements(GL11.GL_TRIANGLES, mResonatorRingModel.Indices);
        //ResonatorRingModel.renderAll();

        ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, 0);
        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

        ARBShaderObjects.glUseProgramObjectARB(0);

        ARBShaderObjects.glUseProgramObjectARB(mXMShaderProgram);

        ARBShaderObjects.glUniform1fARB(ARBShaderObjects.glGetUniformLocationARB(mXMShaderProgram, "u_elapsedTime"), (float) Minecraft.getSystemTime());

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        bindTexture(ResonatorXMTextureLocation);
        ARBShaderObjects.glUniform1iARB(ARBShaderObjects.glGetUniformLocationARB(mXMShaderProgram, "u_texture"), 0);

        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, mXMVertexBufferObjectId);
        GL11.glVertexPointer(3, GL11.GL_FLOAT, 20, 0);

        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, mXMVertexBufferObjectId);
        GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 20, 12);

        //ARBVertexBufferObject.glBindBufferARB(GL15.GL_ELEMENT_ARRAY_BUFFER, mXMIndexBufferObjectId);
        GL11.glDrawElements(GL11.GL_TRIANGLES, mResonatorXMModel.Indices);
        //ResonatorXMModel.renderAll();

        ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, 0);
        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

        ARBShaderObjects.glUseProgramObjectARB(0);

        GL11.glPopMatrix();

    }
}
