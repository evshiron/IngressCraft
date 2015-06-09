package info.evshiron.ingresscraft.items;

import info.evshiron.ingresscraft.Constants;
import info.evshiron.ingresscraft.IngressCraft;
import info.evshiron.ingresscraft.client.gui.ScannerGUI;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;

import java.util.List;

/**
 * Created by evshiron on 5/25/15.
 */
public class ScannerItem extends ItemArmor {

    public static final String NAME = "scanner";

    private int mTriggerCounter;

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

        if(world.isRemote){

            if((!itemStack.hasTagCompound()) || (itemStack.getTagCompound().getInteger("faction") == Constants.Faction.NEUTRAL)) {

                if(mTriggerCounter == 0){

                    player.openGui(IngressCraft.Instance, ScannerGUI.ID, world, (int) player.posX, (int) player.posY, (int) player.posZ);

                }

                mTriggerCounter++;

            }else{

                mTriggerCounter = 0;

            }

        }

    }

}
