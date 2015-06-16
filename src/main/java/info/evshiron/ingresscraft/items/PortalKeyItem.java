package info.evshiron.ingresscraft.items;

import info.evshiron.ingresscraft.IngressCraft;
import info.evshiron.ingresscraft.entities.PortalEntity;
import info.evshiron.ingresscraft.utils.IngressHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import javax.sound.sampled.Port;
import java.util.List;
import java.util.UUID;

/**
 * Created by evshiron on 6/12/15.
 */
public class PortalKeyItem extends Item {

    public static final String NAME = "portalKey";

    public PortalKeyItem() {

        super();

        setUnlocalizedName(NAME);
        setCreativeTab(IngressCraft.CreativeTab);
        setTextureName(IngressCraft.MODID + ":" + NAME);

    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List lines, boolean p_77624_4_) {

        if(itemStack.hasTagCompound()) {

            NBTTagCompound nbt = itemStack.getTagCompound();
            String portalUuid = nbt.getString("portalUuid");
            String portalName = nbt.getString("portalName");

            Entity entity = IngressHelper.GetPortalByUuid(player.worldObj, portalUuid);
            if(entity instanceof PortalEntity) {

                PortalEntity portal = (PortalEntity) entity;

                lines.add(String.format("Name: %s", portal.Name));
                lines.add(String.format("Level: %d", portal.GetLevel()));
                lines.add(String.format("Position: %.2f, %.2f, %.2f", portal.posX, portal.posY, portal.posZ));
                lines.add(String.format("Distance: %.2f", player.getDistanceToEntity(portal)));

            }
            else if(!portalName.contentEquals("-UNKNOWN-")) {

                lines.add(String.format("Name: %s", portalName));
                lines.add(String.format("Action failed"));

            }
            else {

                lines.add(String.format(Long.toHexString(UUID.fromString(portalUuid).getMostSignificantBits())));

            }

        }

    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int p_77663_4_, boolean p_77663_5_) {

        if(!itemStack.hasTagCompound()) {

            NBTTagCompound nbt = new NBTTagCompound();

            nbt.setString("portalUuid", UUID.randomUUID().toString());
            nbt.setString("portalName", "-UNKNOWN-");

            itemStack.setTagCompound(nbt);

        }

    }

}
