package info.evshiron.ingresscraft.items;

import info.evshiron.ingresscraft.Constants;
import info.evshiron.ingresscraft.IngressCraft;
import info.evshiron.ingresscraft.client.gui.ScannerGUI;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;

import java.util.List;
import java.util.Scanner;

/**
 * Created by evshiron on 5/25/15.
 */
public class ScannerItem extends ItemArmor {

    public static final String NAME = "scanner";

    public static int GUIDisabler = 0;

    public ScannerItem() {

        super(EnumHelper.addArmorMaterial("XM", 33, new int[]{ 1, 3, 2, 1 }, 0), 0, 0);

        setUnlocalizedName(NAME);
        setCreativeTab(IngressCraft.CreativeTab);
        setTextureName("chainmail_helmet");

    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List lines, boolean p_77624_4_) {

        if(!itemStack.hasTagCompound()) {

            return;

        }

        NBTTagCompound nbt = itemStack.getTagCompound();

        String codename = "";

        switch(nbt.getInteger("faction")) {

            case Constants.Faction.NEUTRAL:

                codename += EnumChatFormatting.GRAY;

                break;

            case Constants.Faction.RESISTANCE:

                codename += EnumChatFormatting.BLUE;

                break;

            case Constants.Faction.ENLIGHTENED:

                codename += EnumChatFormatting.GREEN;

                break;

        }

        codename += nbt.getString("codename");

        lines.add(codename);

        lines.add(String.format("Level: %d", nbt.getInteger("level")));
        lines.add(String.format("AP: %d", nbt.getInteger("ap")));
        lines.add(String.format("XM: %d", nbt.getInteger("xm")));

    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int p_77663_4_, boolean p_77663_5_) {

        if(!itemStack.hasTagCompound()) {

            NBTTagCompound nbt = new NBTTagCompound();

            nbt.setString("codename", "-UNKNOWN-");
            nbt.setInteger("faction", Constants.Faction.NEUTRAL);
            nbt.setInteger("level", 0);
            nbt.setInteger("ap", 0);
            nbt.setInteger("xm", 0);

            itemStack.setTagCompound(nbt);

        }

    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {

        //System.out.println("tick");

        if(GUIDisabler > 0 && GUIDisabler < 32) GUIDisabler++;
        else if(GUIDisabler >= 32) GUIDisabler = 0;

        if(itemStack.getTagCompound().getInteger("faction") == Constants.Faction.NEUTRAL) {

            if(world.isRemote) {

                if(GUIDisabler == 0) {

                    player.openGui(IngressCraft.Instance, ScannerGUI.ID, world, (int) player.posX, (int) player.posY, (int) player.posZ);
                    GUIDisabler = 1;

                }
                else {

                    // A trap for the second.
                    player.addChatMessage(new ChatComponentText("Wait."));

                }

            }

        }
        else {

            NBTTagCompound nbt = itemStack.getTagCompound();

            if(nbt.getInteger("level") == 1 && nbt.getInteger("ap") >= 2500) {
                nbt.setInteger("level", 2);
            }
            else if(nbt.getInteger("level") == 2 && nbt.getInteger("ap") >= 20000) {
                nbt.setInteger("level", 3);
            }
            else if(nbt.getInteger("level") == 3 && nbt.getInteger("ap") >= 70000) {
                nbt.setInteger("level", 4);
            }
            else if(nbt.getInteger("level") == 4 && nbt.getInteger("ap") >= 150000) {
                nbt.setInteger("level", 5);
            }
            else if(nbt.getInteger("level") == 5 && nbt.getInteger("ap") >= 300000) {
                nbt.setInteger("level", 6);
            }
            else if(nbt.getInteger("level") == 6 && nbt.getInteger("ap") >= 600000) {
                nbt.setInteger("level", 7);
            }
            else if(nbt.getInteger("level") == 7 && nbt.getInteger("ap") >= 1200000) {
                nbt.setInteger("level", 8);
            }

        }

    }

}
