package info.evshiron.ingresscraft.client.gui;

import net.minecraft.client.gui.GuiScreen;

/**
 * Created by evshiron on 6/6/15.
 */
public class PortalGUI extends GuiScreen {

    public static final int ID = 10000;

    @Override
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {

        //super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);

        drawDefaultBackground();

        fontRendererObj.drawString("这不是节操po", 8, 8, 0xff0000);

    }
}
