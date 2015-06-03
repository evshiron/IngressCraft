package info.evshiron.ingresscraft.utils;

import info.evshiron.ingresscraft.IngressCraft;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBVertexBufferObject;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.*;
import java.util.ArrayList;

/**
 * Created by evshiron on 6/3/15.
 */
public class IngressDeserializer {

    public static class IngressModel {

        public FloatBuffer Vertices;
        public ShortBuffer Indices;

    }

    public static IngressModel Deserialize(InputStream stream) {

        try {

            IngressModel ingressModel = new IngressModel();

            int count = 0;

            ObjectInputStream ois = new ObjectInputStream(stream);

            while (ois.available() == 0) {

                Object obj = ois.readObject();
                Class cls = obj.getClass();
                String[] arr = new String[Array.getLength(obj)];

                for (int i = 0; i < Array.getLength(obj); i++) {

                    if (cls.getComponentType() == float.class) {
                        arr[i] = String.valueOf(Array.getFloat(obj, i));
                    }
                    else if (cls.getComponentType() == short.class) {
                        arr[i] = String.valueOf(Array.getShort(obj, i));
                    }
                    else {

                        System.out.println(cls.getTypeName());

                    }

                }

                switch(count) {

                    case 0:

                        float[] vertices = new float[arr.length];

                        for (int i = 0; i < arr.length; i++) {

                            vertices[i] = Float.parseFloat(arr[i]);

                        }

                        ingressModel.Vertices = BufferUtils.createFloatBuffer(arr.length);
                        ingressModel.Vertices.put(vertices);
                        ingressModel.Vertices.flip();

                        break;

                    case 1:

                        short[] indices = new short[arr.length];

                        for (int i = 0; i < arr.length; i++) {

                            indices[i] = Short.parseShort(arr[i]);

                        }

                        ingressModel.Indices = BufferUtils.createShortBuffer(arr.length);
                        ingressModel.Indices.put(indices);
                        ingressModel.Indices.flip();

                        break;

                }

                count++;

            }

            ois.close();

            return ingressModel;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;

    }

}
