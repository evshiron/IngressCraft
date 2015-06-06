package info.evshiron.ingresscraft;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.IGuiHandler;
import info.evshiron.ingresscraft.client.gui.PortalGUI;
import info.evshiron.ingresscraft.client.gui.ScannerGUI;
import info.evshiron.ingresscraft.items.ScannerItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * Created by evshiron on 5/27/15.
 */
public class CommonProxy implements IGuiHandler {

    public void RegisterRenderers() {

    }

    public void setPlayer(EntityPlayer player){

    }

    public EntityPlayer getPlayer(){
        return null;
    }

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {

        switch(id) {

            case ScannerGUI.ID:

                // HELMET = 3.
                ItemStack helmet = player.getCurrentArmor(3);

                if(helmet.getItem() instanceof ScannerItem) {

                    if(!helmet.hasTagCompound()) {

                        NBTTagCompound nbt = new NBTTagCompound();
                        nbt.setString("codename", "");
                        nbt.setInteger("faction", Constants.Faction.NEUTRAL);

                        helmet.setTagCompound(nbt);

                        return new ScannerGUI(player, helmet);

                    }
                    else if(helmet.getTagCompound().getString("codename").contentEquals("")) {

                        return new ScannerGUI(player, helmet);

                    }
                    else {

                        return null;

                    }

                }
                else {

                    return null;

                }

            case PortalGUI.ID:

                return new PortalGUI();

            default:

                return null;

        }

    }
}
