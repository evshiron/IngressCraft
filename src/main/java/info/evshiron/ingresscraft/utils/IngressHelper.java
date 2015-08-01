package info.evshiron.ingresscraft.utils;

import info.evshiron.ingresscraft.IngressCraft;
import info.evshiron.ingresscraft.entities.EntityPortal;
import info.evshiron.ingresscraft.items.ItemPortalKey;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by evshiron on 6/9/15.
 */
public class IngressHelper {

    public static double GetResonatorMaxXM(int level) {

        switch(level) {
            case 1:
                return 1000;
            case 2:
                return 1500;
            case 3:
                return 2000;
            case 4:
                return 2500;
            case 5:
                return 3000;
            case 6:
                return 4000;
            case 7:
                return 5000;
            case 8:
                return 6000;
            default:
                return 0;
        }

    }

    public static double GetXMPBursterRange(int level) {

        double range;

        switch(level) {
            case 1:
                range = 39;
                break;
            case 2:
                range = 44;
                break;
            case 3:
                range = 54;
                break;
            case 4:
                range = 69;
                break;
            case 5:
                range = 89;
                break;
            case 6:
                range = 109;
                break;
            case 7:
                range = 134;
                break;
            case 8:
                range = 164;
                break;
            default:
                range = 0;
                break;
        }

        return range * IngressCraft.CONFIG_RANGE_FACTOR;

    }

    public static double GetXMPBursterDamage(int level) {

        switch(level) {
            case 1:
                return 125;
            case 2:
                return 250;
            case 3:
                return 417;
            case 4:
                return 750;
            case 5:
                return 1000;
            case 6:
                return 1250;
            case 7:
                return 1500;
            case 8:
                return 2250;
            default:
                return 0;
        }

    }

    public static double GetCalculatedDamage(double range, double distance, double damage) {

        // Simple linear formula.
        return (range - distance) / range * damage;

    }

    public static EntityPortal GetPortalByUuid(World world, String uuid) {

        List<Entity> entities = world.getLoadedEntityList();

        for(int i = 0; i < entities.size(); i++) {

            Entity entity = entities.get(i);

            if(entity instanceof EntityPortal) {

                EntityPortal portal = (EntityPortal) entity;

                if(portal.UUID.contentEquals(uuid)) {

                    return portal;

                }

            }

        }

        return null;

    }

    public static boolean HasPortalKey(EntityPlayer player, ItemStack portalKey) {

        for(int i = 0; i < player.inventory.mainInventory.length; i++) {

            ItemStack itemStack = player.inventory.mainInventory[i];

            if(itemStack != null && itemStack.getItem() instanceof ItemPortalKey) {

                if(itemStack.getTagCompound().getString("uuid").contentEquals(portalKey.getTagCompound().getString("uuid"))) {

                    return true;

                }

            }

        }

        return false;

    }

    public static List GetEntitiesAround(World world, Class cls, Entity entity, double range) {

        List matches = new ArrayList();

        List<Entity> entities = (List<Entity>) world.getEntitiesWithinAABB(cls, entity.boundingBox.expand(range * 2, range * 2, range * 2));

        for(int i = 0; i < entities.size(); i++) {

            Entity entity1 = entities.get(i);

            if(entity.getDistanceToEntity(entity1) <= range) {

                matches.add(entity1);

            }

        }

        return matches;

    }

}
