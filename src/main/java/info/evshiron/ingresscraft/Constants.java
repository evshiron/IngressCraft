package info.evshiron.ingresscraft;

import org.lwjgl.util.vector.Vector4f;

/**
 * Created by evshiron on 6/4/15.
 */
public class Constants {

    public static class Faction {

        public static final int NEUTRAL = 0;
        public static final int RESISTANCE = 1;
        public static final int ENLIGHTENED = 2;

    }

    public static class TeamColor {

        public static final Vector4f NEUTRAL = new Vector4f(0.9764705882352941f, 0.9764705882352941f, 0.9764705882352941f, 1f);
        public static final Vector4f RESISTANCE = new Vector4f(0f, 0.7607843137254902f, 1f, 1f);
        public static final Vector4f ENLIGHTENED = new Vector4f(0.1568627450980392f, 0.9568627450980393f, 0.1568627450980392f, 1f);

    }

    public static class QualityColor {

        public static final Vector4f L1 = new Vector4f(0.996078431372549f, 0.807843137254902f, 0.35294117647058826f, 1f);
        public static final Vector4f L2 = new Vector4f(1f, 0.6509803921568628f, 0.18823529411764706f, 1f);
        public static final Vector4f L3 = new Vector4f(1f, 0.45098039215686275f, 0.08235294117647059f, 1f);
        public static final Vector4f L4 = new Vector4f(0.8941176470588236f, 0f, 0f, 1f);
        public static final Vector4f L5 = new Vector4f(0.9921568627450981f, 0.1607843137254902f, 0.5725490196078431f, 1f);
        public static final Vector4f L6 = new Vector4f(0.9215686274509803f, 0.14901960784313725f, 0.803921568627451f, 1f);
        public static final Vector4f L7 = new Vector4f(0.7568627450980392f, 0.1411764705882353f, 0.8784313725490196f, 1f);
        public static final Vector4f L8 = new Vector4f(0.5882352941176471f, 0.15294117647058825f, 0.9568627450980393f, 1f);

    }

    public static class XMColor {

        public static Vector4f CoreGlow = new Vector4f(0.92f, 0.7f, 0.89f, 1f);
        public static Vector4f CoreGlowAlt = new Vector4f(0.6f, 0.4f, 0.6f, 0.8f);

    }

}
