package info.evshiron.ingresscraft.entities;

import info.evshiron.ingresscraft.Constants;
import info.evshiron.ingresscraft.IngressCraft;
import info.evshiron.ingresscraft.client.gui.PortalGUI;
import info.evshiron.ingresscraft.client.gui.ScannerGUI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Created by evshiron on 5/31/15.
 */
public class ResonatorEntity extends IngressEntityBase {

    public static String NAME = "resonator";

    int mFaction = Constants.Faction.NEUTRAL;

    String mOwner;

    public ResonatorEntity(World world) {

        super(world);

    }

    public void SetFaction(int faction) {

        mFaction = faction;

    }

    public void SetOwner(String owner) {

        mOwner = owner;

    }

    @Override
    protected boolean interact(EntityPlayer player) {

        player.openGui(IngressCraft.Instance, ScannerGUI.ID, worldObj, 0, 0, 0);

        return true;

    }
}
