package info.evshiron.ingresscraft.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;

/**
 * Created by evshiron on 5/25/15.
 */
public class ScannerItem extends ItemArmor {

    public static final String NAME = "scanner";

    public ScannerItem() {

        super(EnumHelper.addArmorMaterial("XM", 65536, new int[]{ 0, 0, 0, 0 }, 0), 0, 0);

        setUnlocalizedName(NAME);
        setTextureName("chainmail_helmet");

    }

}
