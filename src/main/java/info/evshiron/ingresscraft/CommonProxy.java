package info.evshiron.ingresscraft;

import info.evshiron.ingresscraft.client.gui.GUICreatePortal;
import info.evshiron.ingresscraft.client.gui.GUIPortal;
import info.evshiron.ingresscraft.entities.EntityPortal;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.IGuiHandler;
import info.evshiron.ingresscraft.client.gui.GUICreateScanner;
import info.evshiron.ingresscraft.items.ItemScanner;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by evshiron on 5/27/15.
 */
public class CommonProxy implements IGuiHandler {

    public static int CurrentScreenId = 0;

    public void RegisterRenderers() {

    }

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {

        switch(id) {

            case GUICreateScanner.ID:

                // HELMET = 3.
                ItemStack helmet = player.getCurrentArmor(3);

                if(helmet.getItem() instanceof ItemScanner) {

                    if(helmet.getTagCompound().getInteger("faction") == Constants.Faction.NEUTRAL) {

                        CurrentScreenId = id;
                        return new GUICreateScanner(player, helmet);

                    }
                    else {

                        return null;

                    }

                }
                else {

                    return null;

                }

            case GUICreatePortal.ID:

                return new GUICreatePortal(player, x, y, z);

            case GUIPortal.ID:

                // FIXME: Find a clean way to fetch the interacting Portal.
                int portalId = z;

                CurrentScreenId = id;
                return new GUIPortal(player, (EntityPortal) world.getEntityByID(portalId));

            default:

                return null;

        }

    }
}
