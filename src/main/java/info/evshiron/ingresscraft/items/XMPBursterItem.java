package info.evshiron.ingresscraft.items;

import info.evshiron.ingresscraft.IngressCraft;
import info.evshiron.ingresscraft.utils.OExplosion;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * Created by evshiron on 5/25/15
 */
public class XMPBursterItem extends Item {
    public static final String NAME = "xmpBurster";

    public XMPBursterItem(){
        super();
        setUnlocalizedName(NAME);
        setCreativeTab(CreativeTabs.tabInventory);
        setTextureName(IngressCraft.MODID + ":" + NAME);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {

        OExplosion explosion = new OExplosion(world, player, player.posX, player.posY, player.posZ, 10.0F);
        explosion.doExplosionA();
        explosion.doExplosionB(true);

        player.inventory.consumeInventoryItem(player.getCurrentEquippedItem().getItem());

        return itemStack;

    }

}
