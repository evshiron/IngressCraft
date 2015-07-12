package info.evshiron.ingresscraft.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * Created by evshiron on 5/31/15.
 */
public class EntityIngressBase extends EntityLiving {

    public EntityIngressBase(World world) {

        super(world);

    }

    /*
    @Override
    protected void entityInit() {

    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbt) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbt) {

    }
    */

    // Use to block knockBack.
    @Override
    public void knockBack(Entity p_70653_1_, float p_70653_2_, double p_70653_3_, double p_70653_5_) {

    }

    // Use to block movement.
    @Override
    public void onLivingUpdate() {

    }

}
