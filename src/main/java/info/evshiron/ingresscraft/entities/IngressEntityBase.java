package info.evshiron.ingresscraft.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * Created by evshiron on 5/31/15.
 */
public class IngressEntityBase extends EntityLiving {


    public IngressEntityBase(World world) {

        super(world);

    }

    /**
     * Used to block entity's back movement
     */
    @Override
    public void knockBack(net.minecraft.entity.Entity p_70653_1_, float p_70653_2_, double p_70653_3_, double p_70653_5_) {
        System.err.println("Yo");
    }

    @Override
    public void onLivingUpdate() {

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

}
