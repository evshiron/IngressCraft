package info.evshiron.ingresscraft.utils;

import net.minecraft.entity.Entity;
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

        switch(level) {
            case 1:
                return 39;
            case 2:
                return 44;
            case 3:
                return 54;
            case 4:
                return 69;
            case 5:
                return 89;
            case 6:
                return 109;
            case 7:
                return 134;
            case 8:
                return 164;
            default:
                return 0;
        }

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

    public static double GetDistanceBetween(Entity entityA, Entity entityB) {

        return Math.sqrt(Math.pow(entityA.posX - entityB.posX, 2) + Math.pow(entityA.posY - entityB.posY, 2) + Math.pow(entityA.posZ - entityB.posZ, 2));

    }

    public static List GetEntitiesAround(World world, Class cls, Entity entity, double range) {

        List matches = new ArrayList();

        List<Entity> entities = (List<Entity>) world.getEntitiesWithinAABB(cls, entity.boundingBox.expand(range * 2, range * 2, range * 2));

        for(int i = 0; i < entities.size(); i++) {

            Entity entity1 = entities.get(i);

            if(GetDistanceBetween(entity, entity1) <= range) {

                matches.add(entity1);

            }

        }

        return matches;

    }

}
