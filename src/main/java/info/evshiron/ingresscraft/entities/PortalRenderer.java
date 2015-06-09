package info.evshiron.ingresscraft.entities;

import info.evshiron.ingresscraft.Constants;
import info.evshiron.ingresscraft.IngressCraft;
import info.evshiron.ingresscraft.utils.IngressDeserializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector4f;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by evshiron on 5/28/15.
 */
public class PortalRenderer extends RenderEntity {

    public static final ResourceLocation PortalTextureLocation = new ResourceLocation(IngressCraft.MODID, "textures/entities/portalTexture.png");

    IngressDeserializer.IngressModel mPortalModel;

    int mShaderProgram = 0;

    int mVertexBufferObjectId = 0;
    int mIndexBufferObjectId = 0;

    public PortalRenderer() {

        mPortalModel = IngressDeserializer.Deserialize(getClass().getResourceAsStream("/assets/ingresscraft/models/entities/texturedPortal.obj"));

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

    void applyTeamColorByConstant(Vector4f color) {

        GL20.glUniform4f(GL20.glGetUniformLocation(mShaderProgram, "u_baseColor"), color.x, color.y, color.z, color.w);

    }

    void applyBaseColorByFaction(int faction) {

        switch(faction) {
            case Constants.Faction.RESISTANCE:
                applyTeamColorByConstant(Constants.TeamColor.RESISTANCE);
                break;
            case Constants.Faction.ENLIGHTENED:
                applyTeamColorByConstant(Constants.TeamColor.ENLIGHTENED);
                break;
            default:
                applyTeamColorByConstant(Constants.TeamColor.NEUTRAL);
                break;
        }

    }

    double getRotation() {

        return ((double) Minecraft.getSystemTime() / 5000.0) % 360.0;

    }

    double getRampTarget() {

        double i = ((double) Minecraft.getSystemTime() / 5000.0);

        return Math.sin(Math.PI / 2 * (i - Math.floor(i)));

    }

    double getAlpha() {

        double i = ((double) Minecraft.getSystemTime() / 5000.0);

        return Math.sin(i) * 0.05 + 0.75;

    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float param5, float param6) {

        doRender((PortalEntity) entity, x, y, z, param5, param6);

    }

    void doRender(PortalEntity entity, double x, double y, double z, float param5, float param6) {

        if(mShaderProgram == 0) {

            int shaderProgram = GL20.glCreateProgram();
            int vertexShader = createShader(GL20.GL_VERTEX_SHADER, getClass().getResourceAsStream("/assets/ingresscraft/shaders/portal_scanner.glsl.vert"));
            int fragmentShader = createShader(GL20.GL_FRAGMENT_SHADER, getClass().getResourceAsStream("/assets/ingresscraft/shaders/portal_scanner.glsl.frag"));

            GL20.glAttachShader(shaderProgram, vertexShader);
            GL20.glAttachShader(shaderProgram, fragmentShader);

            GL20.glLinkProgram(shaderProgram);

            System.out.println(GL20.glGetProgramInfoLog(shaderProgram, GL20.GL_LINK_STATUS));

            mShaderProgram = shaderProgram;

            int ringVertexBufferObjectId = GL15.glGenBuffers();

            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, ringVertexBufferObjectId);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, mPortalModel.Vertices, GL15.GL_STATIC_DRAW);

            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

            int ringIndexBufferObjectId = GL15.glGenBuffers();

            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ringIndexBufferObjectId);
            GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, mPortalModel.Indices, GL15.GL_STATIC_DRAW);

            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

            mVertexBufferObjectId = ringVertexBufferObjectId;
            mIndexBufferObjectId = ringIndexBufferObjectId;

        }

        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);

        GL11.glDisable(GL11.GL_CULL_FACE);
        //GL11.glDepthMask(false);
        //GL11.glBlendEquation(GL11.GL_FUNC_ADD);
        //GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        //GL11.glBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glPushMatrix();

        GL11.glTranslated(x, y + 0.5, z);

        GL20.glUseProgram(mShaderProgram);

        applyBaseColorByFaction(entity.Faction);

        GL20.glUniform1f(GL20.glGetUniformLocation(mShaderProgram, "u_rotation"), (float) getRotation());
        GL20.glUniform1f(GL20.glGetUniformLocation(mShaderProgram, "u_rampTarget"), (float) getRampTarget());
        GL20.glUniform1f(GL20.glGetUniformLocation(mShaderProgram, "u_alpha"), (float) getAlpha());

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        bindTexture(PortalTextureLocation);
        GL20.glUniform1i(GL20.glGetUniformLocation(mShaderProgram, "u_texture"), 0);

        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, mVertexBufferObjectId);
        GL11.glVertexPointer(4, GL11.GL_FLOAT, 24, 0);

        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, mVertexBufferObjectId);
        GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 24, 16);

        //GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, mIndexBufferObjectId);
        GL11.glDrawElements(GL11.GL_TRIANGLES, mPortalModel.Indices);
        //ResonatorRingModel.renderAll();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

        GL20.glUseProgram(0);

        GL11.glPopMatrix();

        GL11.glPopAttrib();

    }

}
