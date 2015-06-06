package info.evshiron.ingresscraft;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.IGuiHandler;
import info.evshiron.ingresscraft.client.gui.PortalGUI;
import net.minecraft.entity.player.EntityPlayer;
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

            case PortalGUI.ID:

                return new PortalGUI();

            default:

                return null;

        }

    }
}
