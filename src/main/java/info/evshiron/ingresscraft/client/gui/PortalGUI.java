package info.evshiron.ingresscraft.client.gui;

import cpw.mods.fml.common.registry.GameRegistry;
import info.evshiron.ingresscraft.CommonProxy;
import info.evshiron.ingresscraft.Constants;
import info.evshiron.ingresscraft.IngressCraft;
import info.evshiron.ingresscraft.entities.PortalEntity;
import info.evshiron.ingresscraft.entities.ResonatorEntity;
import info.evshiron.ingresscraft.messages.SyncPortalMessage;
import info.evshiron.ingresscraft.utils.IngressHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

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

    void renderQuad(Tessellator tessellator, int x, int y, int width, int height, int color)
    {

        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(color);
        tessellator.addVertex((double) (x + 0), (double) (y + 0), itemRender.zLevel);
        tessellator.addVertex((double) (x + 0), (double) (y + height), itemRender.zLevel);
        tessellator.addVertex((double) (x + width), (double) (y + height), itemRender.zLevel);
        tessellator.addVertex((double) (x + width), (double) (y + 0), itemRender.zLevel);
        tessellator.draw();

    }

    void drawResonators() {

        GL11.glPushMatrix();

        for(int i = 0; i < mResonators.size(); i++) {

            ResonatorEntity resonator = mResonators.get(i);

            int column = i % 4;
            int row = i / 4;

            int left = (width / 2) - 100;
            int top = 50;
            // Each grid sizes 32x32 and 50x50 with and without borders.
            int x1 = left + 9 + 50 * column;
            int y1 = top + 9 + 50 * row;
            int x2 = x1 + 32;
            int y2 = y1 + 32;

            //drawRect(x1, y1, x2, y2, 0xffffffff);

            int damage = (int) (resonator.getMaxHealth() - resonator.getHealth());

            ItemStack itemStack = new ItemStack(IngressCraft.GetResonatorItem(resonator.Level), 1, damage);

            {

                //RenderHelper.enableGUIStandardItemLighting();
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_TEXTURE_2D);

                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

                IIcon icon = itemStack.getIconIndex();
                TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
                textureManager.bindTexture(textureManager.getResourceLocation(itemStack.getItemSpriteNumber()));

                itemRender.renderIcon(x1, y1, icon, 32, 32);

            }

            {

                //GL11.glDisable(GL11.GL_DEPTH_TEST);
                //GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glDisable(GL11.GL_TEXTURE_2D);

                double health = itemStack.getItem().getDurabilityForDisplay(itemStack);
                int k = (int) Math.round(255.0D - health * 255.0D);
                Tessellator tessellator = Tessellator.instance;
                int l = 255 - k << 16 | k << 8;
                int i1 = (255 - k) / 4 << 16 | 16128;

                renderQuad(tessellator, x1 + 2, y1 + 32 + 2, 31, 2, 0);
                renderQuad(tessellator, x1 + 2, y1 + 32 + 2, 30, 1, i1);
                renderQuad(tessellator, x1 + 2, y1 + 32 + 2, (int) (30 * (1.0 - health)), 1, l);

                GL11.glEnable(GL11.GL_TEXTURE_2D);
                //GL11.glEnable(GL11.GL_ALPHA_TEST);
                //GL11.glEnable(GL11.GL_DEPTH_TEST);

            }

            {

                drawCenteredString(fontRendererObj, resonator.Owner, x1 + 16, y1 + 32 + 2 + 2, resonator.Faction == Constants.Faction.RESISTANCE ? 0x5555ff : 0x55ff55);

            }

            //RenderHelper.enableGUIStandardItemLighting();

        }

        GL11.glPopMatrix();

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
