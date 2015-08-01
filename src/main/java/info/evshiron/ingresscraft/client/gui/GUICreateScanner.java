package info.evshiron.ingresscraft.client.gui;

import info.evshiron.ingresscraft.CommonProxy;
import info.evshiron.ingresscraft.Constants;
import info.evshiron.ingresscraft.IngressCraft;
import info.evshiron.ingresscraft.items.ItemScanner;
import info.evshiron.ingresscraft.messages.MessageCreateScanner;
import info.evshiron.ingresscraft.messages.MessageHandler;
import info.evshiron.ingresscraft.messages.MessageSyncScanner;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by evshiron on 6/6/15.
 */
public class GUICreateScanner extends GuiScreen {

    public static final int ID = 10001;

    public static final int ID_RESISTANCE_BUTTON = 0;
    public static final int ID_ENLIGHTENED_BUTTON = 1;
    public static final int ID_CANCEL_BUTTON = 2;

    EntityPlayer mPlayer;
    ItemStack mScanner;

    GuiTextField mCodenameField;

    int mFaction;

    GuiButton mResistanceButton;
    GuiButton mEnlightenedButton;

    GuiButton mCancelButton;

    public GUICreateScanner(EntityPlayer player, ItemStack scanner) {
        super();

        mPlayer = player;
        mScanner = scanner;

        allowUserInput = true;

    }

    @Override
    public void initGui() {

        // new GuiTextField(FontRenderer, x, y, width, height).
        mCodenameField = new GuiTextField(this.fontRendererObj, (width - 200) / 2, 20, 200, 20);
        mCodenameField.setFocused(true);
        mCodenameField.setText("");

        // new GuiButton(id, x, y, (width, height, )String).
        mResistanceButton = new GuiButton(ID_RESISTANCE_BUTTON, (width - 200) / 2, height - 80 - 20, 200, 20, "RESISTANCE");
        mEnlightenedButton = new GuiButton(ID_ENLIGHTENED_BUTTON, (width - 200) / 2, height - 60 - 10, 200, 20, "ENLIGHTENED");
        mCancelButton = new GuiButton(ID_CANCEL_BUTTON, (width - 200) / 2, height - 40, 200, 20, "#你怎么还在玩这个游戏");

        buttonList.add(mResistanceButton);
        buttonList.add(mEnlightenedButton);
        buttonList.add(mCancelButton);

    }

    @Override
    protected void keyTyped(char p_73869_1_, int p_73869_2_) {
        //super.keyTyped(p_73869_1_, p_73869_2_);

        if(mCodenameField.textboxKeyTyped(p_73869_1_, p_73869_2_)) {

        }
        else {

            super.keyTyped(p_73869_1_, p_73869_2_);

        }

    }

    @Override
    protected void actionPerformed(GuiButton button) {

        switch(button.id) {

            case ID_RESISTANCE_BUTTON:

                mFaction = Constants.Faction.RESISTANCE;

                write();

                mc.displayGuiScreen(null);
                mc.setIngameFocus();

                break;

            case ID_ENLIGHTENED_BUTTON:

                mFaction = Constants.Faction.ENLIGHTENED;

                write();

                mc.displayGuiScreen(null);
                mc.setIngameFocus();

                break;

            case ID_CANCEL_BUTTON:

                mc.displayGuiScreen(null);
                mc.setIngameFocus();

                break;

        }

        //super.actionPerformed(p_146284_1_);
    }

    @Override
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
        //super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);

        ItemScanner.GUIDisabler = 1;

        drawDefaultBackground();

        mCodenameField.drawTextBox();

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

        NBTTagCompound nbt = mScanner.getTagCompound();

        nbt.setString("codename", mCodenameField.getText());
        nbt.setInteger("faction", mFaction);
        nbt.setInteger("level", 1);
        nbt.setInteger("ap", 0);
        nbt.setInteger("xm", 1000);

        mScanner.setTagCompound(nbt);

        MessageHandler.Wrapper.sendToServer(new MessageCreateScanner(mPlayer, mCodenameField.getText(), mFaction));

    }

}
