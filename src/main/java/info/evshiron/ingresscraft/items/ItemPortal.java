package info.evshiron.ingresscraft.items;

import info.evshiron.ingresscraft.IngressCraft;
import info.evshiron.ingresscraft.entities.EntityPortal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by evshiron on 6/12/15.
 */
public class ItemPortal extends Item {

    public static final String NAME = "portal";

    public ItemPortal() {

        super();

        setUnlocalizedName(NAME);
        setCreativeTab(IngressCraft.CreativeTab);
        setTextureName(IngressCraft.MODID + ":" + NAME);

    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int targetSide, float targetX, float targetY, float targetZ) {

        EntityPortal entity = new EntityPortal(world);

        entity.setPosition(x + targetX, y + targetY, z + targetZ);

        if(!world.isRemote) world.spawnEntityInWorld(entity);

        return true;

    }

}
