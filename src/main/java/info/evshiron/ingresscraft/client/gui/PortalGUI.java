package info.evshiron.ingresscraft.client.gui;

import info.evshiron.ingresscraft.Constants;
import info.evshiron.ingresscraft.IngressCraft;
import info.evshiron.ingresscraft.entities.PortalEntity;
import info.evshiron.ingresscraft.messages.SyncPortalMessage;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Created by evshiron on 6/6/15.
 */
public class PortalGUI extends GuiScreen {

    public static final int ID = 10000;

    public static final int ID_LINK_BUTTON = 0;
    public static final int ID_DONE_BUTTON = 1;

    EntityPlayer mPlayer;
    PortalEntity mPortal;

    boolean mIsEditing = false;

    GuiTextField mPortalNameField;

    GuiButton mLinkButton;
    GuiButton mDoneButton;

    public PortalGUI(EntityPlayer player, PortalEntity portal) {

        mPlayer = player;
        mPortal = portal;

        mIsEditing = mPlayer.capabilities.isCreativeMode || mPortal.Name.contentEquals("");

    }

    @Override
    public void initGui() {

        mPortalNameField = new GuiTextField(fontRendererObj, (width - 200) / 2, 20, 200, 20);

        if(mIsEditing) mPortalNameField.setFocused(true);

        mPortalNameField.setText(mPortal.Name);

        mLinkButton = new GuiButton(ID_LINK_BUTTON, (width - 200) / 2, height - 60 - 10, 200, 20, "LINK");
        mDoneButton = new GuiButton(ID_DONE_BUTTON, (width - 200) / 2, height - 40, 200, 20, "Done");

        buttonList.add(mLinkButton);

        if(mIsEditing) buttonList.add(mDoneButton);

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

            case ID_LINK_BUTTON:

                break;

            case ID_DONE_BUTTON:

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

    void write() {

        mPortal.SetName(mPortalNameField.getText());

        IngressCraft.SyncPortalChannel.sendToServer(new SyncPortalMessage(mPortal));

    }

}
