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
        setTextureName(IngressCraft.MODID+":"+NAME);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {

        OExplosion explosion = new OExplosion(p_77659_2_,p_77659_3_,p_77659_3_.posX,p_77659_3_.posY,p_77659_3_.posZ,10.0F);
        explosion.doExplosionA();
        explosion.doExplosionB(true);
        p_77659_3_.inventory.consumeInventoryItem(p_77659_3_.getCurrentEquippedItem().getItem());
        return super.onItemRightClick(p_77659_1_, p_77659_2_, p_77659_3_);
    }


}
