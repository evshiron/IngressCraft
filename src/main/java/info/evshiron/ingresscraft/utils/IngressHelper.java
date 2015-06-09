package info.evshiron.ingresscraft.utils;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by evshiron on 6/9/15.
 */
public class IngressHelper {

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
