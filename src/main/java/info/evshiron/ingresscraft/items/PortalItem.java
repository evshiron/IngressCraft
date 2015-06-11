package info.evshiron.ingresscraft.items;

import info.evshiron.ingresscraft.IngressCraft;
import info.evshiron.ingresscraft.entities.PortalEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by evshiron on 6/12/15.
 */
public class PortalItem extends Item {

    public static final String NAME = "portal";

    public PortalItem() {

        super();

        setUnlocalizedName(NAME);
        setCreativeTab(IngressCraft.CreativeTab);
        setTextureName(IngressCraft.MODID + ":" + NAME);

    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int targetSide, float targetX, float targetY, float targetZ) {

        PortalEntity entity = new PortalEntity(world);

        entity.setPosition(x + targetX, y + targetY, z + targetZ);

        if(!world.isRemote) world.spawnEntityInWorld(entity);

        return true;

    }

}
