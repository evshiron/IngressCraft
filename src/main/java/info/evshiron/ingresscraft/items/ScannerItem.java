package info.evshiron.ingresscraft.items;

import info.evshiron.ingresscraft.IngressCraft;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;

/**
 * Created by evshiron on 5/25/15.
 */
public class ScannerItem extends ItemArmor {

    public static final String NAME = "scanner";

    public ScannerItem() {

        super(EnumHelper.addArmorMaterial("XM", 33, new int[]{ 1, 3, 2, 1 }, 0), 0, 0);

        setUnlocalizedName(NAME);
        setTextureName("chainmail_helmet");

    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        IngressCraft.Proxy.setPlayer(player);
    }
}
