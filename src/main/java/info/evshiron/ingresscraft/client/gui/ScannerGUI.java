package info.evshiron.ingresscraft.client.gui;

import info.evshiron.ingresscraft.Constants;
import info.evshiron.ingresscraft.IngressCraft;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.World;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * Created by evshiron on 6/6/15.
 */
public class ScannerGUI extends GuiScreen {

    public static final int ID = 10001;

    public static final int ID_RESISTANCE_BUTTON = 10000;
    public static final int ID_ENLIGHTENED_BUTTON = 10001;
    public static final int ID_CANCEL_BUTTON = 10002;

    EntityPlayer mPlayer;
    ItemStack mScanner;

    GuiTextField mCodenameInput;

    int mFaction;

    GuiButton mResistanceButton;
    GuiButton mEnlightenedButton;

    GuiButton mCancelButton;

    public ScannerGUI(EntityPlayer player, ItemStack scanner) {
        super();

        mPlayer = player;
        mScanner = scanner;

        allowUserInput = true;

    }

    @Override
    public void initGui() {

        // new GuiTextField(FontRenderer, x, y, width, height).
        mCodenameInput = new GuiTextField(this.fontRendererObj, (width - 200) / 2, (height - 20) / 2 - 20 - 20, 200, 20);
        mCodenameInput.setFocused(true);
        mCodenameInput.setText(mScanner.getTagCompound().getString("codename"));

        // new GuiButton(id, x, y, (width, height, )String).
        mResistanceButton = new GuiButton(ID_RESISTANCE_BUTTON, (width - 200) / 2, (height - 20) / 2, 200, 20, "RESISTANCE");
        mEnlightenedButton = new GuiButton(ID_ENLIGHTENED_BUTTON, (width - 200) / 2, (height - 20) / 2 + 20, 200, 20, "ENLIGHTENED");
        mCancelButton = new GuiButton(ID_CANCEL_BUTTON, (width - 200) / 2, (height - 20) / 2 + 40, 200, 20, "#你怎么还在玩这个游戏");

        buttonList.add(mResistanceButton);
        buttonList.add(mEnlightenedButton);
        buttonList.add(mCancelButton);

    }

    @Override
    protected void keyTyped(char p_73869_1_, int p_73869_2_) {
        //super.keyTyped(p_73869_1_, p_73869_2_);

        System.out.println(p_73869_1_);

        if(mCodenameInput.textboxKeyTyped(p_73869_1_, p_73869_2_)) {

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

                break;

            case ID_ENLIGHTENED_BUTTON:

                mFaction = Constants.Faction.ENLIGHTENED;

                write();

                mc.displayGuiScreen(null);

                break;

            case ID_CANCEL_BUTTON:

                mc.displayGuiScreen(null);

                //mc.setIngameFocus();

                break;

        }

        //super.actionPerformed(p_146284_1_);
    }

    @Override
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_) {
        //super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);

        drawDefaultBackground();

        mCodenameInput.drawTextBox();

        for(int i = 0; i < buttonList.size(); i++) {

            GuiButton button = (GuiButton) buttonList.get(i);

            button.drawButton(mc, p_73863_1_, p_73863_2_);

        }

    }

    public void write() {

        NBTTagCompound nbt = mScanner.getTagCompound();

        nbt.setString("codename", mCodenameInput.getText());
        nbt.setInteger("faction", mFaction);

        mScanner.setTagCompound(nbt);

        ByteBuf bytes = Unpooled.buffer();

        try {

            // Use this to send data back to the server.
            // FIXME: But it gets lost when restarting.
            (new PacketBuffer(bytes)).writeItemStackToBuffer(mScanner);

            IngressCraft.LoginScannerChannel.sendToServer(new IngressCraft.LoginScannerMessage(mCodenameInput.getText(), mFaction));

            String broadcast = String.format("%s has joined the %s.", nbt.getString("codename"), nbt.getInteger("faction") == 1 ? "Resistance" : "Enlightened");

            mc.getIntegratedServer().getConfigurationManager().sendChatMsg(new ChatComponentText(broadcast));

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            bytes.release();
        }

    }

}