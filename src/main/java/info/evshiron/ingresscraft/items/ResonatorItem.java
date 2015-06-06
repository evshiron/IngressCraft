package info.evshiron.ingresscraft.items;

import info.evshiron.ingresscraft.IngressCraft;
import info.evshiron.ingresscraft.entities.ResonatorEntity;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * Created by evshiron on 5/25/15.
 */
public class ResonatorItem extends Item {

    public static final String NAME = "resonator";

    public ResonatorItem() {

        super();

        setUnlocalizedName(NAME);
        setCreativeTab(CreativeTabs.tabInventory);
        setTextureName(IngressCraft.MODID + ":" + NAME);

    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int targetSide, float targetX, float targetY, float targetZ) {

        if(world.isRemote) {

            return false;

        }

        ResonatorEntity entity = new ResonatorEntity(world);

        ItemStack scanner = player.getCurrentArmor(3);

        if(scanner == null) {

            return false;

        }

        if(!(scanner.getItem() instanceof ScannerItem)) {

            return false;

        }

        if(!player.capabilities.isCreativeMode) {

            itemStack.stackSize--;

        }

        NBTTagCompound nbt = scanner.getTagCompound();

        entity.SetFaction(nbt.getInteger("faction"));
        entity.SetOwner(nbt.getString("codename"));

        entity.setPosition(x + targetX, y + targetY, z + targetZ);

        world.spawnEntityInWorld(entity);

        return true;

    }

}
