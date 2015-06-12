package info.evshiron.ingresscraft.items;

import info.evshiron.ingresscraft.IngressCraft;
import info.evshiron.ingresscraft.entities.PortalEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import javax.sound.sampled.Port;
import java.util.List;

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
            int portalId = nbt.getInteger("portalId");
            Entity entity = player.worldObj.getEntityByID(portalId);
            if(entity instanceof PortalEntity) {

                PortalEntity portal = (PortalEntity) entity;

                lines.add(String.format("Name: %s", portal.Name));

            }

        }

    }

    @Override
    public void onUpdate(ItemStack itemStack, World world, Entity entity, int p_77663_4_, boolean p_77663_5_) {

        if(!itemStack.hasTagCompound()) {

            NBTTagCompound nbt = new NBTTagCompound();

            nbt.setInteger("portalId", 0);

            itemStack.setTagCompound(nbt);

        }

    }

}