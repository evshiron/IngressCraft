package info.evshiron.ingresscraft.client.gui;

import cpw.mods.fml.client.GuiScrollingList;
import info.evshiron.ingresscraft.CommonProxy;
import info.evshiron.ingresscraft.Constants;
import info.evshiron.ingresscraft.IngressCraft;
import info.evshiron.ingresscraft.entities.EntityPortal;
import info.evshiron.ingresscraft.entities.EntityResonator;
import info.evshiron.ingresscraft.items.ItemPortalKey;
import info.evshiron.ingresscraft.messages.MessageCreatePortal;
import info.evshiron.ingresscraft.messages.MessageHandler;
import info.evshiron.ingresscraft.utils.IngressHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by evshiron on 6/6/15.
 */
public class GUICreatePortal extends GuiScreen {

    public static final int ID = 10002;

    public static final int ID_BACK_BUTTON = 0;
    public static final int ID_ABORT_BUTTON = 1;
    public static final int ID_SUBMIT_BUTTON = 2;

    EntityPlayer mPlayer;
    double mX;
    double mY;
    double mZ;

    GuiTextField mPortalNameField;

    GuiButton mBackButton;
    GuiButton mAbortButton;
    GuiButton mSubmitButton;

    public GUICreatePortal(EntityPlayer player, double x, double y, double z) {

        mPlayer = player;
        mX = x;
        mY = y;
        mZ = z;

    }

    @Override
    public void initGui() {

        mPortalNameField = new GuiTextField(fontRendererObj, (width - 200) / 2, 20, 200, 20);

        mPortalNameField.setText("-UNKNOWN-");
        mPortalNameField.setFocused(true);

        mBackButton = new GuiButton(ID_BACK_BUTTON, 0, 0, 40, 20, " < ");

        mAbortButton = new GuiButton(ID_ABORT_BUTTON, (width - 200) / 2, height - 40, 90, 20, "ABORT");
        mSubmitButton = new GuiButton(ID_SUBMIT_BUTTON, (width - 200) / 2 + 110, height - 40, 90, 20, "SUBMIT");

        buttonList.add(mBackButton);
        buttonList.add(mAbortButton);
        buttonList.add(mSubmitButton);

    }

    @Override
    protected void keyTyped(char p_73869_1_, int p_73869_2_) {
        //super.keyTyped(p_73869_1_, p_73869_2_);

        if(mPortalNameField.textboxKeyTyped(p_73869_1_, p_73869_2_)) {

        }
        else {

            super.keyTyped(p_73869_1_, p_73869_2_);

        }

    }

    @Override
    protected void actionPerformed(GuiButton button) {

        switch(button.id) {

            case ID_BACK_BUTTON:
            case ID_ABORT_BUTTON:

                mc.displayGuiScreen(null);
                mc.setIngameFocus();

                break;

            case ID_SUBMIT_BUTTON:

                write();

                mc.displayGuiScreen(null);
                mc.setIngameFocus();

                break;

        }

        //super.actionPerformed(p_146284_1_);
    }

    @Override
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
        //super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);

        drawDefaultBackground();

        mPortalNameField.drawTextBox();

        for(int i = 0; i < buttonList.size(); i++) {

            GuiButton button = (GuiButton) buttonList.get(i);

            button.drawButton(mc, p_73863_1_, p_73863_2_);

        }

    }

    @Override
    public void onGuiClosed() {

        CommonProxy.CurrentScreenId = 0;

        super.onGuiClosed();

    }

    void write() {

        MessageHandler.Wrapper.sendToServer(new MessageCreatePortal(mPlayer, mPortalNameField.getText(), mX, mY, mZ));

    }

}
