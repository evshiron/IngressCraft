package info.evshiron.ingresscraft.client.effects;

import info.evshiron.ingresscraft.IngressCraft;
import info.evshiron.ingresscraft.utils.RendererUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Created by evshiron on 8/3/15.
 */
public class EffectLink extends EntityFX {

    public static final ResourceLocation LinkTextureLocation = new ResourceLocation(IngressCraft.MODID, "textures/effects/portalLinkTexture.png");

    public static final int VERTEX_SET_COUNT = 9;
    public static final int VERTEX_COUNT = VERTEX_SET_COUNT * 6;
    public static final int VERTEX_FLOAT_COUNT = 12;

    public static final Vector4f COLOR_BASE = new Vector4f(0.46f, 0.18f, 0.18f, 1.0f);

    float[] mC = new float[VERTEX_SET_COUNT];
    float[] mD = new float[VERTEX_SET_COUNT];
    float[] mE = new float[VERTEX_SET_COUNT];

    Vector3f mStart;
    Vector3f mEnd;
    Vector4f mTeamColor;

    // Each vertex has 12 floats.
    // positionX, positionY, positionZ, w?, u, v, normalX, normalZ, colorR, colorG, colorB, colorA.
    FloatBuffer mVertices;
    ShortBuffer mIndices;

    int mShaderProgram;
    int mVertexBufferObjectId;
    int mIndexBufferObjectId;

    public EffectLink(World world, Vector3f start, Vector3f end, Vector4f teamColor) {

        super(world, start.x, start.y, start.z, 0, 0, 0);

        setSize(0.02f, 0.02f);

        for(int i = 0; i < VERTEX_SET_COUNT; i++)
        {
            double f = i / 8.0;
            mC[i] = (float) f;
            mE[i] = (float) (3.0 + (-1.5 * Math.pow(clampedSin(2.0 * Math.abs(f - 0.5)), 4)));
            mD[i] = (float) clampedSin(1.0 - 2.0 * Math.abs(f - 0.5));
        }

        mStart = start;
        mEnd = end;
        mTeamColor = teamColor;

        float[] vertices = generateVertices();
        mVertices = BufferUtils.createFloatBuffer(vertices.length);
        mVertices.put(vertices);
        mVertices.flip();

        short[] indices = generateIndices();
        mIndices = BufferUtils.createShortBuffer(indices.length);
        mIndices.put(indices);
        mIndices.flip();

    }

    @Override
    public void onUpdate() {
        //super.onUpdate();

        particleMaxAge = 100;
        particleAge = 50;

    }

    void fillChunk(float[] vertices, int index, float positionX, float positionY, float positionZ, float u, float v, Vector3f normal, float f6, Vector4f color) {

        int offset = index * VERTEX_FLOAT_COUNT;
        vertices[offset + 0] = positionX;
        vertices[offset + 1] = positionY;
        vertices[offset + 2] = positionZ;
        vertices[offset + 3] = f6;
        vertices[offset + 4] = u;
        vertices[offset + 5] = v;
        vertices[offset + 6] = normal.x;
        vertices[offset + 7] = normal.z;
        vertices[offset + 8] = color.x;
        vertices[offset + 9] = color.y;
        vertices[offset + 10] = color.z;
        vertices[offset + 11] = color.w;

    }

    double clampedSin(double radius) {

        return Math.sin(Math.PI * Math.max(Math.min(1.0, radius), 0) / 2);

    }

    float[] generateVertices() {

        // FIXME: Still some position problems when converting from 2D to 3D.

        float[] vertices = new float[VERTEX_COUNT * VERTEX_FLOAT_COUNT];

        float length = (float) Math.sqrt(Math.pow(mStart.x - mEnd.x, 2) + Math.pow(mStart.y - mEnd.y, 2) + Math.pow(mStart.z - mEnd.z, 2));

        float yMin = 0;
        float yMax = (float) (yMin + Math.min(8, 0.02 * length));
        float percent = 1;
        float f6 = 0.01f * length;
        float f7 = 0.1f + percent * 0.3f;

        Vector3f vec = new Vector3f();
        Vector3f.sub(new Vector3f(mEnd.x, 0, mEnd.z), new Vector3f(mStart.x, 0, mStart.z), vec);
        Vector3f right = new Vector3f();
        Vector3f up = new Vector3f(0, 1, 0);
        Vector3f.cross(vec, up, right);
        right.normalise(right);

        int step = VERTEX_SET_COUNT * 2;

        for(int i = 0; i < VERTEX_SET_COUNT; i++) {

            float f8 = mC[i];
            float f9 = percent + f8 * (percent - percent);
            float f10 = 0.6f + 0.35f * f9;
            float f12 = f8 * f6;
            float f13 = mStart.x + f8 * vec.x;
            float f14 = mStart.z + f8 * vec.z;
            float f15 = yMin + mD[i] * (yMax - yMin);
            float f16 = mE[i];
            Vector4f color = COLOR_BASE;
            color.w = f10;

            fillChunk(vertices, (i * 2), f13 + f16 * right.x, f15, f14 + f16 * right.z, 0, f12, up, f7, color);
            fillChunk(vertices, (i * 2) + 1, f13 - f16 * right.x, f15, f14 - f16 * right.z, 0.5f, f12, up, f7, color);
            fillChunk(vertices, step + (i * 2), f13, f15 + f16, f14, 0, f12, right, f7, color);
            fillChunk(vertices, step + (i * 2) + 1, f13, f15 - f16, f14, 0.5f, f12, right, f7, color);
            fillChunk(vertices, 2 * step + (i * 2), f13, f15 - f16, f14, 0.5f, f12, right, f7, color);
            fillChunk(vertices, 2 * step + (i * 2) + 1, f13, 0, f14, 1.0f, f12, right, f7, color);

        }

        return vertices;

    }

    short[] generateIndices() {

        short[] indices = new short[144];

        short vertexOffset = 0;
        int indexOffset = 0;

        for(int i = 0; i < 3; i++) {

            for(int j = 0; j < VERTEX_SET_COUNT - 1; j++) {

                indices[indexOffset + 0] = (short) (vertexOffset + 1);
                indices[indexOffset + 1] = (short) (vertexOffset + 0);
                indices[indexOffset + 2] = (short) (vertexOffset + 2);
                indices[indexOffset + 3] = (short) (vertexOffset + 1);
                indices[indexOffset + 4] = (short) (vertexOffset + 2);
                indices[indexOffset + 5] = (short) (vertexOffset + 3);
                vertexOffset += 2;
                indexOffset += 6;

            }

            vertexOffset += 2;

        }

        return indices;

    }

    double getElapsedTime() {

        return (((double) Minecraft.getSystemTime() / 1000.0) % 300.0) * 0.1;

    }

    @Override
    public void renderParticle(Tessellator tessellator, float partialTicks, float p_70539_3_, float p_70539_4_, float p_70539_5_, float p_70539_6_, float p_70539_7_) {
        //super.renderParticle(p_70539_1_, p_70539_2_, p_70539_3_, p_70539_4_, p_70539_5_, p_70539_6_, p_70539_7_);

        if(mShaderProgram == 0) {

            int shaderProgram = GL20.glCreateProgram();
            int vertexShader = RendererUtil.CreateShader(GL20.GL_VERTEX_SHADER, getClass().getResourceAsStream("/assets/ingresscraft/shaders/raw_portal_link.glsl.vert"));
            int fragmentShader = RendererUtil.CreateShader(GL20.GL_FRAGMENT_SHADER, getClass().getResourceAsStream("/assets/ingresscraft/shaders/raw_portal_link.glsl.frag"));

            GL20.glAttachShader(shaderProgram, vertexShader);
            GL20.glAttachShader(shaderProgram, fragmentShader);

            GL20.glLinkProgram(shaderProgram);

            System.out.println(GL20.glGetProgramInfoLog(shaderProgram, GL20.GL_LINK_STATUS));

            mShaderProgram = shaderProgram;

            int vertexBufferObjectId = GL15.glGenBuffers();

            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vertexBufferObjectId);
            GL15.glBufferData(GL15.GL_ARRAY_BUFFER, mVertices, GL15.GL_STATIC_DRAW);

            GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

            int indexBufferObjectId = GL15.glGenBuffers();

            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, indexBufferObjectId);
            GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, mIndices, GL15.GL_STATIC_DRAW);

            GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

            mVertexBufferObjectId = vertexBufferObjectId;
            mIndexBufferObjectId = indexBufferObjectId;

        }

        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);

        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        //GL11.glDepthMask(false);
        //GL11.glBlendEquation(GL11.GL_FUNC_ADD);
        //GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        //GL11.glBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glPushMatrix();

        // Fix the position.
        float xx = (float)(this.lastTickPosX + (this.posX - this.lastTickPosX) * partialTicks - interpPosX);
        float yy = (float)(this.lastTickPosY + (this.posY - this.lastTickPosY) * partialTicks - interpPosY);
        float zz = (float)(this.lastTickPosZ + (this.posZ - this.lastTickPosZ) * partialTicks - interpPosZ);
        GL11.glTranslated(xx, yy, zz);

        GL20.glUseProgram(mShaderProgram);

        GL20.glUniform3f(GL20.glGetUniformLocation(mShaderProgram, "u_cameraFwd"), 0, 0, -1);
        GL20.glUniform1f(GL20.glGetUniformLocation(mShaderProgram, "u_elapsedTime"), (float) getElapsedTime());

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        Minecraft.getMinecraft().renderEngine.bindTexture(LinkTextureLocation);
        GL20.glUniform1i(GL20.glGetUniformLocation(mShaderProgram, "u_texture"), 0);

        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, mVertexBufferObjectId);
        GL11.glVertexPointer(4, GL11.GL_FLOAT, 48, 0);

        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, mVertexBufferObjectId);
        GL11.glTexCoordPointer(4, GL11.GL_FLOAT, 48, 16);

        GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, mVertexBufferObjectId);
        GL11.glColorPointer(4, GL11.GL_FLOAT, 48, 32);

        //GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, mIndexBufferObjectId);
        GL11.glDrawElements(GL11.GL_TRIANGLES, mIndices);
        //ResonatorRingModel.renderAll();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);

        GL20.glUseProgram(0);

        GL11.glPopMatrix();

        GL11.glPopAttrib();

    }
}
