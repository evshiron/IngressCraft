package info.evshiron.ingresscraft;

import info.evshiron.ingresscraft.entities.PortalEntity;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.IGuiHandler;
import info.evshiron.ingresscraft.client.gui.PortalGUI;
import info.evshiron.ingresscraft.client.gui.ScannerGUI;
import info.evshiron.ingresscraft.items.ScannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by evshiron on 5/27/15.
 */
public class CommonProxy implements IGuiHandler {

    public static int CurrentScreenId = 0;

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

                    if(helmet.getTagCompound().getInteger("faction") == Constants.Faction.NEUTRAL) {

                        CurrentScreenId = id;
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

                // FIXME: Find a clean way to fetch the interacting Portal.
                int portalId = z;

                CurrentScreenId = id;
                return new PortalGUI(player, (PortalEntity) world.getEntityByID(portalId));

            default:

                return null;

        }

    }
}
