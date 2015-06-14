package info.evshiron.ingresscraft.client.gui;

import cpw.mods.fml.common.registry.GameRegistry;
import info.evshiron.ingresscraft.CommonProxy;
import info.evshiron.ingresscraft.Constants;
import info.evshiron.ingresscraft.IngressCraft;
import info.evshiron.ingresscraft.entities.PortalEntity;
import info.evshiron.ingresscraft.entities.ResonatorEntity;
import info.evshiron.ingresscraft.messages.SyncPortalMessage;
import info.evshiron.ingresscraft.utils.IngressHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import javax.sound.sampled.Port;
import java.util.List;

/**
 * Created by evshiron on 6/6/15.
 */
public class PortalGUI extends GuiScreen {

    public static final int ID = 10000;

    public static final int ID_BACK_BUTTON = 0;
    public static final int ID_EDIT_BUTTON = 1;
    public static final int ID_LINK_BUTTON = 2;

    EntityPlayer mPlayer;
    PortalEntity mPortal;
    List<ResonatorEntity> mResonators;

    boolean mIsEditing = false;
    boolean mIsLinking = false;

    GuiTextField mPortalNameField;

    GuiButton mBackButton;
    GuiButton mEditButton;
    GuiButton mLinkButton;
    GuiButton mDoneButton;

    public PortalGUI(EntityPlayer player, PortalEntity portal) {

        mPlayer = player;
        mPortal = portal;
        mResonators = IngressHelper.GetEntitiesAround(portal.worldObj, ResonatorEntity.class, portal, IngressCraft.CONFIG_PORTAL_RANGE);

    }

    @Override
    public void initGui() {

        mPortalNameField = new GuiTextField(fontRendererObj, (width - 200) / 2, 20, 200, 20);

        mPortalNameField.setText(mPortal.Name);

        mBackButton = new GuiButton(ID_BACK_BUTTON, 0, 0, 40, 20, " < ");
        mEditButton = new GuiButton(ID_EDIT_BUTTON, 0, 20 + 10, 80, 20, "Edit");
        mLinkButton = new GuiButton(ID_LINK_BUTTON, (width - 200) / 2, height - 40, 200, 20, "LINK");

        buttonList.add(mBackButton);
        buttonList.add(mEditButton);
        buttonList.add(mLinkButton);

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

                if(mIsEditing) {

                    mIsLinking = false;

                }
                else if(mIsLinking) {

                    mIsLinking = false;

                }
                else {

                    mc.displayGuiScreen(null);
                    mc.setIngameFocus();

                }

                break;

            case ID_EDIT_BUTTON:

                if(!mIsEditing) {

                    mIsEditing = true;

                    mPortalNameField.setFocused(true);

                }
                else {

                    mPortalNameField.setFocused(false);

                    write();

                    mIsEditing = false;

                }

                break;

            case ID_LINK_BUTTON:

                if(!mIsLinking) {

                    mIsLinking = true;

                }
                else {

                    mIsLinking = false;

                }

                break;

        }

        //super.actionPerformed(p_146284_1_);
    }

    @Override
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
        //super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);

        if(!mIsEditing) {

            if(mPlayer.capabilities.isCreativeMode) {

                mEditButton.displayString = "Edit";

                mEditButton.visible = true;
                mEditButton.enabled = true;

            }

        }
        else {

            mEditButton.displayString = "Done";

            mEditButton.visible = true;
            mEditButton.enabled = true;

        }

        drawDefaultBackground();

        mPortalNameField.drawTextBox();

        if(!mIsLinking) {

            drawResonators();
            drawMods();

        }
        else {

            drawLinkablePortals();

        }

        for(int i = 0; i < buttonList.size(); i++) {

            GuiButton button = (GuiButton) buttonList.get(i);

            button.drawButton(mc, p_73863_1_, p_73863_2_);

        }

    }

    void drawResonators() {

    }

    void drawMods() {

    }

    void drawLinkablePortals() {

    }

    @Override
    public void onGuiClosed() {

        CommonProxy.CurrentScreenId = 0;

        super.onGuiClosed();

    }

    void write() {

        mPortal.SetName(mPortalNameField.getText());

        IngressCraft.SyncPortalChannel.sendToServer(new SyncPortalMessage(mPortal));

    }

}
