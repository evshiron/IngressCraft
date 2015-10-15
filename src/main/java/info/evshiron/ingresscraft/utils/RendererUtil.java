package info.evshiron.ingresscraft.utils;

import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by evshiron on 8/14/15.
 */
public class RendererUtil {


    public static String ReadAllFromStream(InputStream source) throws IOException {

        StringBuilder sb = new StringBuilder();

        BufferedReader br = new BufferedReader(new InputStreamReader(source));

        String line;
        while((line = br.readLine()) != null) {

            sb.append(line).append("\n");

        }

        return sb.toString();

    }

    public static int CreateShader(int type, InputStream stream) {

        int shader = GL20.glCreateShader(type);

        try {

            String source = ReadAllFromStream(stream);
            GL20.glShaderSource(shader, source);
            GL20.glCompileShader(shader);

            System.out.println(GL20.glGetShaderInfoLog(shader, GL20.GL_COMPILE_STATUS));

            return shader;

        } catch (IOException e) {

            e.printStackTrace();
            return 0;

        }

    }


}
