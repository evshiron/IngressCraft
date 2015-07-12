package info.evshiron.ingresscraft.items;

import info.evshiron.ingresscraft.IngressCraft;
import info.evshiron.ingresscraft.entities.EntityPortal;
import info.evshiron.ingresscraft.utils.IngressHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.List;
import java.util.UUID;

/**
 * Created by evshiron on 6/12/15.
 */
public class ItemPortalKey extends Item {

    public static final String NAME = "portalKey";

    public ItemPortalKey() {

        super();

        setUnlocalizedName(NAME);
        setCreativeTab(IngressCraft.CreativeTab);
        setTextureName(IngressCraft.MODID + ":" + NAME);

    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List lines, boolean p_77624_4_) {

        if(itemStack.hasTagCompound()) {

            NBTTagCompound nbt = itemStack.getTagCompound();
            String uuid = nbt.getString("uuid");

            EntityPortal portal = IngressHelper.GetPortalByUuid(player.worldObj, uuid);
            if(portal != null) {

                nbt.setString("name", portal.Name);
                nbt.setInteger("level", portal.GetLevel());
                nbt.setDouble("x", portal.posX);
                nbt.setDouble("y", portal.posY);
                nbt.setDouble("z", portal.posZ);

                lines.add("Found");

            }
            else {

                lines.add("Cached");

            }

            String name = nbt.getString("name");
            int level = nbt.getInteger("level");
            double x = nbt.getDouble("x");
            double y = nbt.getDouble("y");
            double z = nbt.getDouble("z");

            lines.add(String.format("Name: %s", name));
            lines.add(String.format("Level: %d", level));
            lines.add(String.format("Position: %.2f, %.2f, %.2f", x, y, z));
            lines.add(String.format("Distance: %.2f", Math.sqrt(Math.pow(x - player.posX, 2) + Math.pow(y - player.posY, 2) + Math.pow(z - player.posZ, 2))));

        }

    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int p_77663_4_, boolean p_77663_5_) {

        if(!itemStack.hasTagCompound()) {

            NBTTagCompound nbt = new NBTTagCompound();

            nbt.setString("uuid", UUID.randomUUID().toString());
            nbt.setString("name", "-UNKNOWN-");
            nbt.setInteger("level", 0);
            nbt.setDouble("x", 0);
            nbt.setDouble("y", 0);
            nbt.setDouble("z", 0);

            itemStack.setTagCompound(nbt);

        }

    }

}
