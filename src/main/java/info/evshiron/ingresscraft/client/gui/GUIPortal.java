package info.evshiron.ingresscraft.client.gui;

import cpw.mods.fml.client.GuiScrollingList;
import info.evshiron.ingresscraft.CommonProxy;
import info.evshiron.ingresscraft.Constants;
import info.evshiron.ingresscraft.IngressCraft;
import info.evshiron.ingresscraft.entities.EntityPortal;
import info.evshiron.ingresscraft.entities.EntityResonator;
import info.evshiron.ingresscraft.items.ItemPortalKey;
import info.evshiron.ingresscraft.messages.MessageHandler;
import info.evshiron.ingresscraft.messages.MessageSyncPortal;
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
public class GUIPortal extends GuiScreen {

    public static class GuiLinkablePortalList extends GuiScrollingList {

        public static class Item {

            public EntityPortal Portal;

            public Item(EntityPortal portal) {

                Portal = portal;

            }

        }

        public ArrayList<Item> Items;

        GUIPortal mParent;
        int mSelectedItemIndex;

        public GuiLinkablePortalList(GUIPortal parent, int left, int top, int width, int height, int entryHeight) {

            super(parent.mc, width, parent.height, top, height, left, entryHeight);

            mParent = parent;

            Items = new ArrayList<Item>();

        }

        @Override
        protected int getSize() {
            return Items.size();
        }

        @Override
        protected void elementClicked(int index, boolean doubleClick) {

            if(!doubleClick) {

                mSelectedItemIndex = index;
            }
            else {

                // TODO: Time to link!

            }

        }

        @Override
        protected boolean isSelected(int index) {
            return mSelectedItemIndex == index;
        }

        @Override
        protected void drawBackground() {

        }

        @Override
        protected void drawSlot(int index, int var2, int top, int var4, Tessellator tessellator) {

            Item item = Items.get(index);

            // 32x32 icon with a border of 4.
            mParent.renderIcon(new ItemStack(IngressCraft.PortalKeyItem), left + 4, top + 4, 32, 32);

            mParent.drawCenteredString(mParent.fontRendererObj, item.Portal.Name, left + (40 + listWidth) / 2, top + 24 - mParent.fontRendererObj.FONT_HEIGHT, 0xffffff);

        }

    }

    public static final int ID = 10000;

    public static final int ID_BACK_BUTTON = 0;
    public static final int ID_EDIT_BUTTON = 1;
    public static final int ID_LINK_BUTTON = 2;

    EntityPlayer mPlayer;
    EntityPortal mPortal;
    List<EntityResonator> mResonators;
    List<ItemStack> mPortalKeys;

    boolean mIsEditing = false;
    boolean mIsLinking = false;

    GuiTextField mPortalNameField;

    GuiLinkablePortalList mLinkablePortalList;

    GuiButton mBackButton;
    GuiButton mEditButton;
    GuiButton mLinkButton;

    public GUIPortal(EntityPlayer player, EntityPortal portal) {

        mPlayer = player;
        mPortal = portal;
        mResonators = IngressHelper.GetEntitiesAround(portal.worldObj, EntityResonator.class, portal, IngressCraft.CONFIG_PORTAL_RANGE);

        mPortalKeys = new ArrayList<ItemStack>();

        for(int i = 0; i < mPlayer.inventory.mainInventory.length; i++) {

            ItemStack itemStack = mPlayer.inventory.mainInventory[i];

            if(itemStack != null && itemStack.getItem() instanceof ItemPortalKey) {

                mPortalKeys.add(itemStack);

            }

        }

    }

    @Override
    public void initGui() {

        mPortalNameField = new GuiTextField(fontRendererObj, (width - 200) / 2, 20, 200, 20);

        mPortalNameField.setText(mPortal.Name);

        mLinkablePortalList = new GuiLinkablePortalList(this, (width - 200) / 2, 20 + 20 + 10, 200, 190, 40);

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

                    mPortalNameField.setFocused(false);

                    mIsEditing = false;

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

                    mLinkablePortalList.Items.clear();

                    for(int i = 0; i < mPortalKeys.size(); i++) {

                        ItemStack portalKey = mPortalKeys.get(i);

                        String uuid;
                        if(portalKey.hasTagCompound() && (uuid = portalKey.getTagCompound().getString("portalUuid")) != null) {

                            EntityPortal portal = IngressHelper.GetPortalByUuid(mPlayer.worldObj, uuid);
                            if(portal != null && portal != mPortal) {

                                // TODO: And not blocked by links.
                                mLinkablePortalList.Items.add(new GuiLinkablePortalList.Item(portal));

                            }

                        }

                    }

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

            mLinkablePortalList.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);

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

    void renderIcon(ItemStack itemStack, int x, int y, int width, int height) {

        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);

        GL11.glColor4d(1.0, 1.0, 1.0, 1.0);

        IIcon icon = itemStack.getIconIndex();
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        textureManager.bindTexture(textureManager.getResourceLocation(itemStack.getItemSpriteNumber()));

        itemRender.renderIcon(x, y, icon, width, height);

        GL11.glPopAttrib();

    }

    void renderHealth(ItemStack itemStack, int x, int y, int width, int height) {

        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);

        //GL11.glDisable(GL11.GL_DEPTH_TEST);
        //GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_TEXTURE_2D);

        double health = itemStack.getItem().getDurabilityForDisplay(itemStack);
        int k = (int) Math.round(255.0D - health * 255.0D);
        Tessellator tessellator = Tessellator.instance;
        int l = 255 - k << 16 | k << 8;
        int i1 = (255 - k) / 4 << 16 | 16128;

        renderQuad(tessellator, x, y, width + 1, height + 1, 0);
        renderQuad(tessellator, x, y, width, height, i1);
        renderQuad(tessellator, x, y, (int) (width * (1.0 - health)), height, l);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        //GL11.glEnable(GL11.GL_ALPHA_TEST);
        //GL11.glEnable(GL11.GL_DEPTH_TEST);

        GL11.glPopAttrib();

    }

    void drawResonators() {

        GL11.glPushMatrix();

        for(int i = 0; i < mResonators.size(); i++) {

            EntityResonator resonator = mResonators.get(i);

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

            //RenderHelper.enableGUIStandardItemLighting();

            renderIcon(itemStack, x1, y1, 32, 32);

            renderHealth(itemStack, x1 + 1, y1 + 32 + 2, 30, 1);

            drawCenteredString(fontRendererObj, resonator.Owner, x1 + 16, y1 + 32 + 2 + 2, resonator.Faction == Constants.Faction.RESISTANCE ? 0x5555ff : 0x55ff55);

            //RenderHelper.enableGUIStandardItemLighting();

        }

        GL11.glPopMatrix();

    }

    void drawMods() {

    }

    @Override
    public void onGuiClosed() {

        CommonProxy.CurrentScreenId = 0;

        super.onGuiClosed();

    }

    void write() {

        mPortal.SetName(mPortalNameField.getText());

        MessageHandler.Wrapper.sendToServer(new MessageSyncPortal(mPortal));

    }

}
